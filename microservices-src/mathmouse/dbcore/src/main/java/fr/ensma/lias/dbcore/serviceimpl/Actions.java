package fr.ensma.lias.dbcore.serviceimpl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import fr.ensma.lias.dbcoreapi.interfaces.DBService;
import fr.ensma.lias.dbcoreapi.io.CSVFileFactory;
import fr.ensma.lias.dbcoreapi.io.XMLFileFactory;
import fr.ensma.lias.dbcoreapi.io.tools.IdGenerator;
import fr.ensma.lias.dbcoreapi.models.FlatDifferentialEquation;
import fr.ensma.lias.dbcoreapi.models.InitialValue;
import fr.ensma.lias.dbcoreapi.models.Input;
import fr.ensma.lias.dbcoreapi.models.Node;
import fr.ensma.lias.dbcoreapi.models.Variable;
import fr.ensma.lias.dbcoreapi.models.enumerations.EAttributeName;
import fr.ensma.lias.dbcoreapi.models.enumerations.ETableName;
import fr.ensma.lias.dockermanagerapi.enumerations.EServiceName;
import fr.ensma.lias.javarabbitmqapi.RabbitMQBasicSender;
import fr.ensma.lias.javarabbitmqapi.enumerations.EQueueName;
import fr.ensma.lias.javarabbitmqapi.enumerations.ESpecialCharacter;

/**
 * Contains all the actions (interactions with the database) available through the API.
 * 
 * @author cyrille ponchateau (ponchateau@ensma.fr)
 *
 */
public class Actions implements DBService {

    private static String       RETRIEVAL_QUERIES_FILE_PATH;
    /**
     * XML schema file for differential equation. When the API receives an equation in an XML file,
     * it is parsed into a DOM tree and its conformity with the XML schema specification is checked.
     */
    private static String       XSD_FILE_PATH;
    private static String       RESOURCES;
    private static final String CSV_FILE_FORMAT             = ".csv";

    private static final int    NODES                       = 0;
    private static final int    VARIABLES                   = 1;
    private static final int    INPUTS                      = 2;
    private static final int    INITIALS                    = 3;
    private static final int    EQUATIONS_MEASURES          = 4;
    private static final int    EQUATIONS_ID_AND_NAME       = 5;
    private static final int    GROUPS                      = 6;
    private static final String MATHEMATICAL_OBJECT_CAST    = "::MATHEMATICAL_OBJECT_TYPE";
    private static final String INTERPOLATION_FUNCTION_CAST = "::INTERPOLATION_FUNCTION_TYPE";

    /**
     * Link to the database
     */
    private Connection          connection;

    /**
     * Link to rabbitmq
     */
    private RabbitMQBasicSender sender;

    private static Actions      actions;
    private Logger              logger;

    public static void initialize( Connection connection, RabbitMQBasicSender sender ) {
        actions = new Actions( connection, sender );

        RESOURCES = System.getenv( "RESOURCES" );
        if ( RESOURCES == null ) {
            RESOURCES = "src/main/resources/";
        }
        LoggerFactory.getLogger( "STATIC_ACTIONS" ).debug( "RESOURCES path has been set to " + RESOURCES );

        XSD_FILE_PATH = System.getenv( "XSD_FILE_PATH" );
        if ( XSD_FILE_PATH == null ) {
            XSD_FILE_PATH = "src/main/resources/differential-equation-2.0.xsd";
        }
        LoggerFactory.getLogger( "STATIC_ACTIONS" ).debug( "XSD_FILE_PATH has been set to " + XSD_FILE_PATH );

        RETRIEVAL_QUERIES_FILE_PATH = System.getenv( "RETRIEVAL_QUERIES_FILE_PATH" );
        if ( RETRIEVAL_QUERIES_FILE_PATH == null ) {
            RETRIEVAL_QUERIES_FILE_PATH = "src/main/resources/queries/retrieval.sql";
        }
        LoggerFactory.getLogger( "STATIC_ACTIONS" )
                .debug( "RETRIEVAL_QUERIES_FILE_PATH has been set to " + RETRIEVAL_QUERIES_FILE_PATH );
    }

    public static Actions getInstance() {
        return actions;
    }

    /**
     * Constructor using fields
     * 
     * @param connection
     * @param sender
     */
    private Actions( Connection connection, RabbitMQBasicSender sender ) {
        this.connection = connection;
        this.sender = sender;
        logger = LoggerFactory.getLogger( getClass() );
    }

    /**
     * Retrieves all the equations contained in the database and sends them all in multiple XML
     * strings.
     */
    @Override
    public void sendEquations( Long id ) {
        ArrayList<String> fails = new ArrayList<String>();
        try {
            if ( id == null ) {
                // getting the equations
                Map<Long, FlatDifferentialEquation> differentialEquations = getAllEquations();

                // writing into text file and sending
                XMLFileFactory factory = XMLFileFactory.getInstance( XSD_FILE_PATH );

                logger.debug( "writing equations in xml strings..." );

                if ( differentialEquations.isEmpty() ) {
                    sender.publish( EQueueName.GET_EQUATIONS_RESPONSE_QUEUE_NAME,
                            "database is currently empty." );
                } else {
                    for ( Long idkey : differentialEquations.keySet() ) {
                        // writing
                        String eqxml = factory.XMLWrite( differentialEquations.get( idkey ) );
                        if ( eqxml == null )
                            fails.add( eqxml );
                        logger.debug( eqxml );
                        // sending
                        sender.publish( EQueueName.GET_EQUATIONS_RESPONSE_QUEUE_NAME, eqxml );
                        logger.debug( "new xml sent..." );
                    }
                }
                logger.debug( "writing equations in xml strings... done" );
            } else {
                // getting the equations
                FlatDifferentialEquation differentialEquation = getEquation( id );

                // writing into text file and sending
                XMLFileFactory factory = XMLFileFactory.getInstance( XSD_FILE_PATH );

                if ( differentialEquation != null ) {
                    // writing
                    String eqxml = factory.XMLWrite( differentialEquation );
                    logger.debug( eqxml );
                    // sending
                    sender.publish( EQueueName.GET_EQUATIONS_RESPONSE_QUEUE_NAME, eqxml );
                    logger.debug( "new xml sent..." );
                }
            }
        } catch ( SQLException | NullPointerException | IOException e ) {
            e.printStackTrace();
        }

        LoggerFactory.getLogger( getClass() ).debug( "following models failed..." );
        for ( String eq : fails ) {
            LoggerFactory.getLogger( getClass() ).debug( eq );
        }
    }

    /**
     * Retrieves the equation of the database for a given ID.
     * 
     * @param id
     * @return
     * @throws IOException
     * @throws SQLException
     */
    public FlatDifferentialEquation getEquation( Long id ) throws IOException, SQLException {
        // the following string must be added to the queries
        // ("schema_mmw.differential_equation.id = id")
        String clause = ETableName.DIFFERENTIAL_EQUATION_TABLE.value() + "." + EAttributeName.ID_ATTRIBUTE.value()
                + " = " + id;

        // reads the request necessary for equation retrieval in the
        // src/main/resources/queries/retrieval.sql file
        File file = new File( RETRIEVAL_QUERIES_FILE_PATH );
        FileReader fileReader = new FileReader( file );
        BufferedReader bufferedReader = new BufferedReader( fileReader );
        ArrayList<String> queries = new ArrayList<String>();
        String line;
        while ( ( line = bufferedReader.readLine() ) != null ) {
            queries.add( line );
        }
        logger.debug( queries.toString() );

        // requests
        // equations measurements
        PreparedStatement statement = connection
                .prepareStatement( insertWhereClause( queries.get( EQUATIONS_MEASURES ), clause ) );
        logger.debug( statement.toString() );
        sendLogMessage( statement.toString() );
        ResultSet measurements = statement.executeQuery();

        // nodes table request
        statement = connection.prepareStatement( insertWhereClause( queries.get( NODES ), clause ) );
        logger.debug( statement.toString() );
        sendLogMessage( statement.toString() );
        ResultSet nodesTable = statement.executeQuery();

        // variables request
        statement = connection.prepareStatement( insertWhereClause( queries.get( VARIABLES ), clause ) );
        logger.debug( statement.toString() );
        sendLogMessage( statement.toString() );
        ResultSet variablesTable = statement.executeQuery();

        // inputs request
        statement = connection.prepareStatement( insertWhereClause( queries.get( INPUTS ), clause ) );
        logger.debug( statement.toString() );
        sendLogMessage( statement.toString() );
        ResultSet inputsTable = statement.executeQuery();

        // initial values request
        statement = connection.prepareStatement( insertWhereClause( queries.get( INITIALS ), clause ) );
        logger.debug( statement.toString() );
        sendLogMessage( statement.toString() );
        ResultSet initialValuesTable = statement.executeQuery();

        // equations groups
        statement = connection.prepareStatement( insertWhereClause( queries.get( GROUPS ), clause ) );
        logger.debug( statement.toString() );
        sendLogMessage( statement.toString() );
        ResultSet groupsTable = statement.executeQuery();
        // logger.debug( Serializer.serialize(
        // groupsTable ) );

        return processResults( measurements, nodesTable, variablesTable, inputsTable, initialValuesTable, groupsTable )
                .get( id );
    }

    /**
     * Inserts an additional WHERE clause to a query.
     * 
     * @param query
     * @param clause
     * @return
     */
    private String insertWhereClause( String query, String clause ) {
        String[] parts = query.split( "WHERE" );
        if ( parts.length > 1 )
            return parts[0] + "WHERE " + clause + " AND" + parts[1];
        else {
            parts[0] = parts[0].substring( 0, parts[0].length() - 1 );
            return parts[0] + " WHERE " + clause;
        }
    }

    /**
     * Maps the data of result sets into a list of FlatDifferentialEquation object.
     * 
     * @see fr.ensma.lias.dbcoreapi.models.FlatDifferentialEquation
     * @param measurements
     * @param nodesTable
     * @param variablesTable
     * @param inputsTable
     * @param initialValuesTable
     * @param groupsTable
     * @return
     * @throws IOException
     * @throws SQLException
     */
    private Map<Long, FlatDifferentialEquation> processResults( ResultSet measurements, ResultSet nodesTable,
            ResultSet variablesTable, ResultSet inputsTable, ResultSet initialValuesTable, ResultSet groupsTable )
            throws IOException, SQLException {
        Map<Long, FlatDifferentialEquation> differentialEquations = new HashMap<Long, FlatDifferentialEquation>();
        Long currentId;

        // sorts out the nodes then variables then inputs functions and then
        // initial values into a list of FlatDifferentialEquation object
        // (one per equation)
        // reading measurements
        while ( measurements.next() ) {
            currentId = measurements.getLong( EAttributeName.ID_ATTRIBUTE.value() );
            differentialEquations.put( currentId, new FlatDifferentialEquation() );
            differentialEquations.get( currentId ).setName(
                    measurements.getString( EAttributeName.NAME_ATTRIBUTE.value() ).replaceAll( "\\s+", "" ) );
        }

        // reading the nodes table
        while ( nodesTable.next() ) {
            // reads the id of the equation of the current node
            currentId = nodesTable.getLong( EAttributeName.ID_EQUATION_ATTRIBUTE.value() );
            // the nodes informations are added to the corresponding
            // equation object.
            if ( differentialEquations.containsKey( currentId ) )
                differentialEquations.get( currentId ).getFormula().put(
                        nodesTable.getLong( EAttributeName.ID_NODE_ATTRIBUTE.value() ),
                        new Node( nodesTable ) );
        }

        for ( Long id : differentialEquations.keySet() ) {
            logger.debug( differentialEquations.get( id ).getFormula().toString() );
        }

        // reading the variables table
        while ( variablesTable.next() ) {
            currentId = variablesTable.getLong( EAttributeName.ID_EQUATION_ATTRIBUTE.value() );
            // if the variable is related to an equation that is in the
            // equations list, the said equation is fed with the variable
            // data.
            if ( differentialEquations.containsKey( currentId ) )
                differentialEquations.get( currentId ).getVariables()
                        .put( variablesTable.getLong( EAttributeName.ID_VARIABLE_ATTRIBUTE.value() ),
                                new Variable( variablesTable ) );
        }

        for ( Long id : differentialEquations.keySet() ) {
            logger.debug( differentialEquations.get( id ).getVariables().toString() );
        }

        // the inputs are stored into CSV files, the path to the file is
        // stored into the database. In order to retrieve the inputs value,
        // the file path is read in the query result table and used to
        // access the correct input file.
        CSVFileFactory csvfactory = CSVFileFactory.getInstance();
        while ( inputsTable.next() ) {
            currentId = inputsTable.getLong( EAttributeName.ID_EQUATION_ATTRIBUTE.value() );
            // if the input is related to an equation in the list, the
            // equation is fed with the input data
            if ( differentialEquations.containsKey( currentId ) ) {
                // creates a new input
                Input input = new Input( inputsTable );
                // fill it with the values read in the csv file
                input.setSeries( csvfactory.readSeriesFromFile(
                        RESOURCES + inputsTable.getString( EAttributeName.SERIAL_KEY_ATTRIBUTE.value() )
                                + CSV_FILE_FORMAT ) );
                // add it to the corresponding equation
                differentialEquations.get( currentId ).getInputs()
                        .put( inputsTable.getLong( EAttributeName.ID_INPUT_FUNCTION_ATTRIBUTE.value() ), input );
            }
        }

        for ( Long id : differentialEquations.keySet() ) {
            logger.debug( differentialEquations.get( id ).getInputs().toString() );
        }

        // reading the initial values table
        while ( initialValuesTable.next() ) {
            currentId = initialValuesTable.getLong( EAttributeName.ID_EQUATION_ATTRIBUTE.value() );
            if ( differentialEquations.containsKey( currentId ) )
                differentialEquations.get( currentId ).getInitialValues().put(
                        initialValuesTable.getLong( EAttributeName.ID_INITIAL_ATTRIBUTE.value() ),
                        new InitialValue( initialValuesTable ) );
        }

        for ( Long id : differentialEquations.keySet() ) {
            logger.debug( differentialEquations.get( id ).getInitialValues().toString() );
        }

        // reading the groups table values
        while ( groupsTable.next() ) {
            currentId = groupsTable.getLong( EAttributeName.ID_EQUATION_ATTRIBUTE.value() );
            if ( differentialEquations.containsKey( currentId ) ) {
                differentialEquations.get( currentId )
                        .setGroup( groupsTable.getString( EAttributeName.GROUP_ATTRIBUTE.value() ).trim() );
            }
        }

        for ( Long id : differentialEquations.keySet() ) {
            logger.debug( differentialEquations.get( id ).getGroup() );
        }

        logger.debug( differentialEquations.toString() );

        return differentialEquations;
    }

    /**
     * Retrieves all the equations contained in the database
     * 
     * @return
     * @throws IOException
     * @throws SQLException
     */
    private Map<Long, FlatDifferentialEquation> getAllEquations() throws IOException, SQLException {
        // reads the request necessary for equation retrieval in the
        // src/main/resources/queries/retrieval.sql file
        File file = new File( RETRIEVAL_QUERIES_FILE_PATH );
        FileReader fileReader = new FileReader( file );
        BufferedReader bufferedReader = new BufferedReader( fileReader );
        ArrayList<String> queries = new ArrayList<String>();
        String line;
        while ( ( line = bufferedReader.readLine() ) != null ) {
            queries.add( line );
        }
        logger.debug( queries.toString() );

        // requests
        // equations measurements
        PreparedStatement statement = connection.prepareStatement( queries.get( EQUATIONS_MEASURES ) );
        logger.debug( statement.toString() );
        sendLogMessage( statement.toString() );
        ResultSet measurements = statement.executeQuery();

        // nodes table request
        statement = connection.prepareStatement( queries.get( NODES ) );
        logger.debug( statement.toString() );
        sendLogMessage( statement.toString() );
        ResultSet nodesTable = statement.executeQuery();

        // variables request
        statement = connection.prepareStatement( queries.get( VARIABLES ) );
        logger.debug( statement.toString() );
        sendLogMessage( statement.toString() );
        ResultSet variablesTable = statement.executeQuery();

        // inputs request
        statement = connection.prepareStatement( queries.get( INPUTS ) );
        logger.debug( statement.toString() );
        sendLogMessage( statement.toString() );
        ResultSet inputsTable = statement.executeQuery();

        // initial values request
        statement = connection.prepareStatement( queries.get( INITIALS ) );
        logger.debug( statement.toString() );
        sendLogMessage( statement.toString() );
        ResultSet initialValuesTable = statement.executeQuery();

        // equations groups
        statement = connection.prepareStatement( queries.get( GROUPS ) );
        logger.debug( statement.toString() );
        sendLogMessage( statement.toString() );
        ResultSet groupsTable = statement.executeQuery();
        // logger.debug( Serializer.serialize(
        // groupsTable ) );

        // process results
        return processResults( measurements, nodesTable, variablesTable, inputsTable, initialValuesTable, groupsTable );
    }

    /**
     * Insert into a new equation into the warehouse, read from an XML string
     * 
     * @param xmlString
     */
    @Override
    public void putEquation( String xmlString ) {
        // get the list of unavailable ids from database
        ArrayList<Long> differentialEquationsUnavailableIds = getUnavailableIds(
                "SELECT " + ETableName.DIFFERENTIAL_EQUATION_TABLE.value() + "." + EAttributeName.ID_ATTRIBUTE.value()
                        + " FROM " + ETableName.DIFFERENTIAL_EQUATION_TABLE.value() + " ORDER BY "
                        + EAttributeName.ID_ATTRIBUTE.value() + ";" );
        ArrayList<Long> nodesUnavailableIds = getUnavailableIds(
                "SELECT " + ETableName.NODE_TABLE.value() + "." + EAttributeName.ID_ATTRIBUTE.value() + " FROM "
                        + ETableName.NODE_TABLE.value() + " ORDER BY " + EAttributeName.ID_ATTRIBUTE.value() + ";" );
        ArrayList<Long> inputFunctionsUnavailbleIds = getUnavailableIds( "SELECT "
                + ETableName.INPUT_FUNCTION_TABLE.value() + "." + EAttributeName.ID_ATTRIBUTE.value() + " FROM "
                + ETableName.INPUT_FUNCTION_TABLE.value() + " ORDER BY " + EAttributeName.ID_ATTRIBUTE.value() + ";" );
        ArrayList<Long> initialValuesUnavailableIds = getUnavailableIds( "SELECT "
                + ETableName.INITIAL_VALUE_TABLE.value() + "." + EAttributeName.ID_ATTRIBUTE.value() + " FROM "
                + ETableName.INITIAL_VALUE_TABLE.value() + " ORDER BY " + EAttributeName.ID_ATTRIBUTE.value() + ";" );
        ArrayList<Long> variableValuesUnavailableIds = getUnavailableIds( "SELECT "
                + ETableName.VARIABLE_VALUE_TABLE.value() + "." + EAttributeName.ID_ATTRIBUTE.value() + " FROM "
                + ETableName.VARIABLE_VALUE_TABLE.value() + " ORDER BY " + EAttributeName.ID_ATTRIBUTE.value() + ";" );
        ArrayList<Long> groupsUnavailableIds = getUnavailableIds(
                "SELECT " + ETableName.GROUP_TABLE.value() + "." + EAttributeName.ID_ATTRIBUTE.value() + " FROM "
                        + ETableName.GROUP_TABLE.value() + " ORDER BY " + EAttributeName.ID_ATTRIBUTE.value() + ";" );

        // mapping XML attributes into FlatDifferentialEquation object
        XMLFileFactory factory = XMLFileFactory.getInstance( XSD_FILE_PATH );
        try {
            FlatDifferentialEquation flateqdiff = factory.XMLread(
                    xmlString.replaceAll( "[\\t\\n\\r]", "" ),
                    differentialEquationsUnavailableIds, nodesUnavailableIds, initialValuesUnavailableIds,
                    inputFunctionsUnavailbleIds, variableValuesUnavailableIds, groupsUnavailableIds );

            // checking if newly read elements does not already exist in the
            // database
            PreparedStatement statement;

            // variables checking
            HashMap<Long, Variable> replaceVariables = new HashMap<Long, Variable>();
            for ( Long id : flateqdiff.getVariables().keySet() ) {
                statement = connection.prepareStatement(
                        "SELECT * FROM " + ETableName.VARIABLE_VALUE_TABLE.value() + " WHERE "
                                + EAttributeName.NAME_ATTRIBUTE.value()
                                + " = ? AND " + EAttributeName.VARIABLE_VALUE_ATTRIBUTE.value() + " = ?;" );
                statement.setString( 1, flateqdiff.getVariables().get( id ).getName() );
                statement.setDouble( 2, flateqdiff.getVariables().get( id ).getValue() );
                logger.debug( statement.toString() );
                ResultSet result = statement.executeQuery();

                if ( result.next() ) {
                    logger.debug( result.getLong( 1 ) + " " + result.getString( 2 ) + " " + result.getDouble( 3 ) );
                    replaceVariables.put( id, new Variable( result ) );
                }
            }

            for ( Long id : replaceVariables.keySet() ) {
                flateqdiff.getVariables().put( replaceVariables.get( id ).getId(),
                        replaceVariables.get( id ) );
                flateqdiff.getVariables().remove( id );
                flateqdiff.getVariables().get( replaceVariables.get( id ).getId() ).setExist( true );
            }

            logger.debug( flateqdiff.getVariables().toString() );

            // initial values checking
            Iterator<Entry<Long, InitialValue>> iterator = flateqdiff.getInitialValues().entrySet().iterator();
            HashMap<Long, InitialValue> replaceInitialValues = new HashMap<Long, InitialValue>();
            while ( iterator.hasNext() ) {
                Entry<Long, InitialValue> entry = iterator.next();
                Long id = entry.getKey();
                statement = connection.prepareStatement(
                        "SELECT * FROM " + ETableName.INITIAL_VALUE_TABLE.value() + " WHERE "
                                + EAttributeName.NAME_ATTRIBUTE.value()
                                + " = ? AND " + EAttributeName.DERIV_ATTRIBUTE.value() + " = ? AND "
                                + EAttributeName.INITIAL_VALUE_ATTRIBUTE.value() + " = ?;" );
                statement.setString( 1, flateqdiff.getInitialValues().get( id ).getName() );
                statement.setInt( 2, flateqdiff.getInitialValues().get( id ).getDeriv() );
                statement.setDouble( 3, flateqdiff.getInitialValues().get( id ).getInitialValue() );
                logger.debug( statement.toString() );
                ResultSet result = statement.executeQuery();

                if ( result.next() ) {
                    logger.debug( result.getLong( 1 ) + " " + result.getString( 2 ) + " " + result.getInt( 3 ) + " "
                            + result.getDouble( 4 ) );
                    replaceInitialValues.put( id, new InitialValue( result ) );
                }
            }

            for ( Long id : replaceInitialValues.keySet() ) {
                flateqdiff.getInitialValues().put( replaceInitialValues.get( id ).getId(),
                        replaceInitialValues.get( id ) );
                flateqdiff.getInitialValues().remove( id );
                flateqdiff.getInitialValues().get( replaceInitialValues.get( id ).getId() ).setExist( true );
            }

            logger.debug( flateqdiff.getInitialValues().toString() );

            IdGenerator nodeContentIdGenerator = new IdGenerator( getUnavailableIds(
                    "SELECT " + ETableName.NODE_CONTENT_TABLE.value() + "." + EAttributeName.ID_ATTRIBUTE.value()
                            + " FROM " + ETableName.NODE_CONTENT_TABLE.value() + ";" ) );

            // nodes content
            for ( Long id : flateqdiff.getFormula().keySet() ) {
                statement = connection.prepareStatement( "SELECT * FROM " + ETableName.NODE_CONTENT_TABLE.value()
                        + " WHERE " + EAttributeName.MATH_OBJECT_ATTRIBUTE.value() + " = ?" + MATHEMATICAL_OBJECT_CAST
                        + " AND " + EAttributeName.DERIV_ATTRIBUTE.value() + " = ? AND "
                        + EAttributeName.NAME_ATTRIBUTE.value() + " = ?;" );
                statement.setString( 1, flateqdiff.getFormula().get( id ).getMathObject().value() );
                statement.setInt( 2, flateqdiff.getFormula().get( id ).getDeriv() );
                statement.setString( 3, flateqdiff.getFormula().get( id ).getName() );
                logger.debug( statement.toString() );
                ResultSet result = statement.executeQuery();

                if ( result.next() ) {
                    logger.debug( result.getLong( 1 ) + " " + result.getString( 2 ) + " " + result.getString( 3 )
                            + " " + result.getInt( 4 ) );
                    flateqdiff.getFormula().get( id ).setIdContent( result.getLong( 1 ) );
                    flateqdiff.getFormula().get( id ).setExist( true );
                } else {
                    flateqdiff.getFormula().get( id ).setIdContent( nodeContentIdGenerator.getValidId() );
                }
            }

            // group checking
            statement = connection.prepareStatement( "SELECT * FROM " + ETableName.GROUP_TABLE.value() + " WHERE "
                    + EAttributeName.GROUP_ATTRIBUTE.value() + " = ?;" );
            statement.setString( 1, flateqdiff.getGroup() );
            logger.debug( statement.toString() );
            ResultSet tuples = statement.executeQuery();

            if ( tuples.next() ) {
                logger.debug( "( " + tuples.getLong( 1 ) + ", " + tuples.getString( 2 ) + ")" );
                flateqdiff.setIdGroup( tuples.getLong( 1 ) );
                flateqdiff.setGroupExist( true );
            }

            logger.debug( flateqdiff.getFormula().toString() );

            flateqdiff.updateAllExist();
            // checkIdsValidity( flateqdiff );

            if ( !flateqdiff.allExist() ) {
                // insertions

                // potential new group insertion
                if ( !flateqdiff.groupExist() ) {
                    statement = connection
                            .prepareStatement( "INSERT INTO " + ETableName.GROUP_TABLE.value() + " VALUES (?,?);" );
                    statement.setLong( 1, flateqdiff.getIdGroup() );
                    statement.setString( 2, flateqdiff.getGroup() );
                    logger.debug( statement.toString() );
                    // sendLogMessage( statement.toString() );
                    statement.executeUpdate();
                }

                // equation ID
                statement = connection.prepareStatement(
                        "INSERT INTO " + ETableName.DIFFERENTIAL_EQUATION_TABLE.value() + " VALUES (?,?,?,?);" );
                statement.setLong( 1, flateqdiff.getId() );
                statement.setInt( 2, flateqdiff.getOrder() );
                statement.setString( 3, flateqdiff.getName() );
                statement.setLong( 4, flateqdiff.getIdGroup() );
                logger.debug( statement.toString() );
                // sendLogMessage( statement.toString() );
                statement.executeUpdate();

                // insertions of the new node contents and nodes
                for ( Long id : flateqdiff.getFormula().keySet() ) {
                    if ( !flateqdiff.getFormula().get( id ).exist() ) {
                        statement = connection.prepareStatement( "INSERT INTO " + ETableName.NODE_CONTENT_TABLE.value()
                                + " VALUES (?,?" + MATHEMATICAL_OBJECT_CAST + ",?,?);" );
                        statement.setLong( 1, flateqdiff.getFormula().get( id ).getIdContent() );
                        statement.setString( 2, flateqdiff.getFormula().get( id ).getMathObject().value() );
                        statement.setString( 3, flateqdiff.getFormula().get( id ).getName() );
                        statement.setInt( 4, flateqdiff.getFormula().get( id ).getDeriv() );
                        logger.debug( statement.toString() );
                        // sendLogMessage( statement.toString() );
                        statement.executeUpdate();
                    }

                    statement = connection.prepareStatement(
                            "INSERT INTO " + ETableName.NODE_TABLE.value() + " VALUES (?,?);" );
                    statement.setLong( 1, id );
                    statement.setLong( 2, flateqdiff.getFormula().get( id ).getIdContent() );
                    logger.debug( statement.toString() );
                    // sendLogMessage( statement.toString() );
                    statement.executeUpdate();
                }

                IdGenerator nodeConnectionIdGenerator = new IdGenerator(
                        getUnavailableIds( "SELECT " + ETableName.NODE_CONNECTION_TABLE.value() + "."
                                + EAttributeName.ID_ATTRIBUTE.value() + " FROM "
                                + ETableName.NODE_CONNECTION_TABLE.value() ) );

                // insertions of the new node connections
                for ( Long id : flateqdiff.getFormula().keySet() ) {
                    statement = connection.prepareStatement(
                            "INSERT INTO " + ETableName.NODE_CONNECTION_TABLE.value() + " VALUES (?,?,?,?,?,?,?);" );
                    statement.setLong( 1, nodeConnectionIdGenerator.getValidId() );
                    statement.setLong( 2, flateqdiff.getId() );
                    statement.setLong( 3, id );
                    if ( flateqdiff.getFormula().get( id ).getParent() == 0 )
                        statement.setNull( 4, Types.INTEGER );
                    else
                        statement.setLong( 4, flateqdiff.getFormula().get( id ).getParent() );
                    if ( flateqdiff.getFormula().get( id ).getLeft() == 0 )
                        statement.setNull( 5, Types.INTEGER );
                    else
                        statement.setLong( 5, flateqdiff.getFormula().get( id ).getLeft() );
                    if ( flateqdiff.getFormula().get( id ).getRight() == 0 )
                        statement.setNull( 6, Types.INTEGER );
                    else
                        statement.setLong( 6, flateqdiff.getFormula().get( id ).getRight() );
                    statement.setInt( 7, flateqdiff.getFormula().get( id ).getDepth() );
                    logger.debug( statement.toString() );
                    sendLogMessage( statement.toString() );
                    statement.executeUpdate();
                }

                IdGenerator equationVariablesIdGenerator = new IdGenerator(
                        getUnavailableIds( "SELECT " + ETableName.EQUATION_VARIABLE_VALUE_TABLE.value() + "."
                                + EAttributeName.ID_ATTRIBUTE.value() + " FROM "
                                + ETableName.EQUATION_VARIABLE_VALUE_TABLE.value() ) );

                // insertions of the new variables and links to the equation
                for ( Long id : flateqdiff.getVariables().keySet() ) {
                    if ( !flateqdiff.getVariables().get( id ).exist() ) {
                        statement = connection.prepareStatement(
                                "INSERT INTO " + ETableName.VARIABLE_VALUE_TABLE.value() + " VALUES (?,?,?);" );
                        statement.setLong( 1, id );
                        statement.setString( 2, flateqdiff.getVariables().get( id ).getName() );
                        statement.setDouble( 3, flateqdiff.getVariables().get( id ).getValue() );
                        logger.debug( statement.toString() );
                        // sendLogMessage( statement.toString() );
                        statement.executeUpdate();
                    }

                    statement = connection.prepareStatement(
                            "INSERT INTO " + ETableName.EQUATION_VARIABLE_VALUE_TABLE.value() + " VALUES (?,?,?);" );
                    statement.setLong( 1, equationVariablesIdGenerator.getValidId() );
                    statement.setLong( 2, flateqdiff.getId() );
                    statement.setLong( 3, id );
                    logger.debug( statement.toString() );
                    // sendLogMessage( statement.toString() );
                    statement.executeUpdate();
                }

                IdGenerator equationInputsIdGenerator = new IdGenerator( getUnavailableIds(
                        "SELECT " + ETableName.EQUATION_INPUT_TABLE.value() + "." + EAttributeName.ID_ATTRIBUTE.value()
                                + " FROM " + ETableName.EQUATION_INPUT_TABLE.value() ) );

                CSVFileFactory csvFactory = CSVFileFactory.getInstance();

                // insertion of the input functions and links to the equation
                for ( Long id : flateqdiff.getInputs().keySet() ) {
                    statement = connection
                            .prepareStatement( "INSERT INTO " + ETableName.INPUT_FUNCTION_TABLE.value()
                                    + " VALUES (?,?,?,?,?" + INTERPOLATION_FUNCTION_CAST + ");" );
                    statement.setLong( 1, id );
                    statement.setString( 2, flateqdiff.getInputs().get( id ).getName() );
                    statement.setInt( 3, flateqdiff.getInputs().get( id ).getDeriv() );
                    statement.setString( 4, flateqdiff.getInputs().get( id ).getSerialKey() );
                    statement.setString( 5, flateqdiff.getInputs().get( id ).getInterpolationFunction().value() );
                    logger.debug( statement.toString() );
                    // sendLogMessage( statement.toString() );
                    statement.executeUpdate();

                    logger.debug( "writing in file..." );
                    csvFactory.writeSeriesInFile( flateqdiff.getInputs().get( id ).getSeries(),
                            RESOURCES + flateqdiff.getInputs().get( id ).getSerialKey() + CSV_FILE_FORMAT );
                    logger.debug( "writing in file... done." );

                    statement = connection.prepareStatement(
                            "INSERT INTO " + ETableName.EQUATION_INPUT_TABLE.value() + " VALUES (?,?,?);" );
                    statement.setLong( 1, equationInputsIdGenerator.getValidId() );
                    statement.setLong( 2, flateqdiff.getId() );
                    statement.setLong( 3, id );
                    logger.debug( statement.toString() );
                    // sendLogMessage( statement.toString() );
                    statement.executeUpdate();
                }

                IdGenerator equationInitialValuesIdGenerator = new IdGenerator( getUnavailableIds(
                        "SELECT " + ETableName.EQUATION_INITIAL_VALUE_TABLE.value() + "."
                                + EAttributeName.ID_ATTRIBUTE.value() + " FROM "
                                + ETableName.EQUATION_INITIAL_VALUE_TABLE.value() ) );

                // insertion of the initial values and links to the equation
                for ( Long id : flateqdiff.getInitialValues().keySet() ) {
                    if ( !flateqdiff.getInitialValues().get( id ).exist() ) {
                        statement = connection.prepareStatement(
                                "INSERT INTO " + ETableName.INITIAL_VALUE_TABLE.value() + " VALUES (?,?,?,?);" );
                        statement.setLong( 1, id );
                        statement.setString( 2, flateqdiff.getInitialValues().get( id ).getName() );
                        statement.setInt( 3, flateqdiff.getInitialValues().get( id ).getDeriv() );
                        statement.setDouble( 4, flateqdiff.getInitialValues().get( id ).getInitialValue() );
                        logger.debug( statement.toString() );
                        // sendLogMessage( statement.toString() );
                        statement.executeUpdate();
                    }

                    statement = connection.prepareStatement(
                            "INSERT INTO " + ETableName.EQUATION_INITIAL_VALUE_TABLE.value() + " VALUES (?,?,?);" );
                    statement.setLong( 1, equationInitialValuesIdGenerator.getValidId() );
                    statement.setLong( 2, flateqdiff.getId() );
                    statement.setLong( 3, id );
                    logger.debug( statement.toString() );
                    // sendLogMessage( statement.toString() );
                    statement.executeUpdate();
                }

                // send new equation ID and Name data to notify clients about
                // the new entry
                logger.debug( "new insertion notification..." );
                sender.publish( EQueueName.GET_EQUATIONS_ID_AND_NAMES_RESPONSE_QUEUE_NAME.value(),
                        Serializer.serialize( flateqdiff.getId(), flateqdiff.getName(), flateqdiff.getGroup() ) );

                Thread.sleep( 3000 );
            }
        } catch ( ParserConfigurationException | SAXException | IOException | SQLException | InterruptedException e ) {
            e.printStackTrace();
        }
    }

    private void checkIdsValidity( FlatDifferentialEquation flatEqDiff ) {
        // get the list of unavailable ids from database
        ArrayList<Long> differentialEquationsUnavailableIds = getUnavailableIds(
                "SELECT " + ETableName.DIFFERENTIAL_EQUATION_TABLE.value() + "." + EAttributeName.ID_ATTRIBUTE.value()
                        + " FROM " + ETableName.DIFFERENTIAL_EQUATION_TABLE.value() + " ORDER BY "
                        + EAttributeName.ID_ATTRIBUTE.value() + ";" );
        ArrayList<Long> nodesUnavailableIds = getUnavailableIds(
                "SELECT " + ETableName.NODE_TABLE.value() + "." + EAttributeName.ID_ATTRIBUTE.value() + " FROM "
                        + ETableName.NODE_TABLE.value() + " ORDER BY " + EAttributeName.ID_ATTRIBUTE.value() + ";" );
        ArrayList<Long> inputFunctionsUnavailbleIds = getUnavailableIds( "SELECT "
                + ETableName.INPUT_FUNCTION_TABLE.value() + "." + EAttributeName.ID_ATTRIBUTE.value() + " FROM "
                + ETableName.INPUT_FUNCTION_TABLE.value() + " ORDER BY " + EAttributeName.ID_ATTRIBUTE.value() + ";" );
        ArrayList<Long> initialValuesUnavailableIds = getUnavailableIds( "SELECT "
                + ETableName.INITIAL_VALUE_TABLE.value() + "." + EAttributeName.ID_ATTRIBUTE.value() + " FROM "
                + ETableName.INITIAL_VALUE_TABLE.value() + " ORDER BY " + EAttributeName.ID_ATTRIBUTE.value() + ";" );
        ArrayList<Long> variableValuesUnavailableIds = getUnavailableIds( "SELECT "
                + ETableName.VARIABLE_VALUE_TABLE.value() + "." + EAttributeName.ID_ATTRIBUTE.value() + " FROM "
                + ETableName.VARIABLE_VALUE_TABLE.value() + " ORDER BY " + EAttributeName.ID_ATTRIBUTE.value() + ";" );
        ArrayList<Long> groupsUnavailableIds = getUnavailableIds(
                "SELECT " + ETableName.GROUP_TABLE.value() + "." + EAttributeName.ID_ATTRIBUTE.value() + " FROM "
                        + ETableName.GROUP_TABLE.value() + " ORDER BY " + EAttributeName.ID_ATTRIBUTE.value() + ";" );

        // check equation id
        IdGenerator differentialEquationsIdsGenerator = new IdGenerator( differentialEquationsUnavailableIds );
        if ( !differentialEquationsIdsGenerator.isValid( flatEqDiff.getId() ) ) {
            long newId = differentialEquationsIdsGenerator.getValidId();
            flatEqDiff.updateId( newId );
        }

        // check nodes ids
        IdGenerator nodesIdsGenerator = new IdGenerator( nodesUnavailableIds );
        for ( Long key : flatEqDiff.getFormula().keySet() ) {
            if ( !nodesIdsGenerator.isValid( flatEqDiff.getFormula().get( key ).getId() ) ) {
                long newId = nodesIdsGenerator.getValidId();
                Node node = flatEqDiff.getFormula().get( key );
                flatEqDiff.getFormula().remove( key );
                node.setId( newId );
                flatEqDiff.getFormula().put( newId, node );
            }
        }

        // check inputs ids
        IdGenerator inputFunctionsIdGenerator = new IdGenerator( inputFunctionsUnavailbleIds );
        for ( Long key : flatEqDiff.getInputs().keySet() ) {
            if ( inputFunctionsIdGenerator.isValid( key ) ) {
                long newId = inputFunctionsIdGenerator.getValidId();
                Input input = flatEqDiff.getInputs().get( key );
                flatEqDiff.getInputs().remove( key );
                input.setId( newId );
                flatEqDiff.getInputs().put( newId, input );
            }
        }

        // check initial values
        IdGenerator initialValuesIdGenerator = new IdGenerator( initialValuesUnavailableIds );
        for ( Long key : flatEqDiff.getInitialValues().keySet() ) {
            if ( !initialValuesIdGenerator.isValid( key ) ) {
                long newId = initialValuesIdGenerator.getValidId();
                InitialValue initial = flatEqDiff.getInitialValues().get( key );
                flatEqDiff.getInitialValues().remove( key );
                initial.setId( newId );
                flatEqDiff.getInitialValues().put( newId, initial );
            }
        }

        // check variables ids
        IdGenerator variablesIdGenerator = new IdGenerator( variableValuesUnavailableIds );
        for ( Long key : flatEqDiff.getVariables().keySet() ) {
            if ( !variablesIdGenerator.isValid( key ) ) {
                long newId = variablesIdGenerator.getValidId();
                Variable variable = flatEqDiff.getVariables().get( key );
                flatEqDiff.getVariables().remove( key );
                variable.setId( newId );
                flatEqDiff.getVariables().put( newId, variable );
            }
        }
    }

    /**
     * Get a list of already used id's in the database.
     * 
     * @param query
     * @return
     */
    private ArrayList<Long> getUnavailableIds( String query ) {
        ArrayList<Long> ids = new ArrayList<Long>();
        PreparedStatement statement;
        try {
            statement = connection.prepareStatement( query );
            logger.debug( statement.toString() );
            ResultSet result = statement.executeQuery();

            while ( result.next() ) {
                ids.add( result.getLong( EAttributeName.ID_ATTRIBUTE.value() ) );
            }

            Collections.sort( ids );

            logger.debug( "ID's found: " + ids );
        } catch ( SQLException e ) {
            e.printStackTrace();
        }
        return ids;
    }

    @Override
    public void sendLogMessage( String message ) {
        // sender.publish( EQueueName.LOG_MESSAGES_QUEUE_NAME.value(),
        // EServiceName.DB_CORE.value() + ":" + message );
    }

    @Override
    public void executeSQLQuery( String query ) {
        PreparedStatement statement;
        try {
            statement = connection.prepareStatement( query );
            logger.debug( statement.toString() );
            sendLogMessage( statement.toString() );
            ResultSet result = statement.executeQuery();
            String serializedResult = Serializer.serialize( result );
            logger.debug( serializedResult );
            sender.publish( EQueueName.SQL_QUERY_RESPONSE_QUEUE_NAME, serializedResult );
        } catch ( SQLException e ) {
            e.printStackTrace();
        }
    }

    @Override
    public void doPull( String message ) {
        try {
            Map<Long, FlatDifferentialEquation> differentialEquations = getAllEquations();
            // writing into text file and sending
            XMLFileFactory factory = XMLFileFactory.getInstance( XSD_FILE_PATH );
            logger.debug( "writing equations in xml strings..." );
            if ( differentialEquations.isEmpty() ) {
                sender.publish( EQueueName.GET_EQUATIONS_RESPONSE_QUEUE_NAME,
                        "database is currently empty." );
            } else {
                for ( Long id : differentialEquations.keySet() ) {
                    // writing
                    String eqxml = factory.XMLWrite( differentialEquations.get( id ) );
                    logger.debug( eqxml );
                    // sending
                    sender.publish( EQueueName.GENERATE_PULL_RESPONSE_QUEUE_NAME,
                            message + ";" + id + ";" + differentialEquations.size() + "\n" + eqxml );
                    logger.debug( "new xml sent..." );

                }
            }
            logger.debug( "writing equations in xml strings... done" );
            sender.publish( EQueueName.PROGRESS,
                    EServiceName.DB_CORE.value() + ESpecialCharacter.SEPARATOR.value() + message
                            + ESpecialCharacter.SEPARATOR.value() + differentialEquations.size() );
        } catch ( SQLException | NullPointerException | IOException e ) {
            e.printStackTrace();
        }
    }

    @Override
    public void getAllEquationsID( String body ) {
        // reads the query in the retrieval query file
        try {
            File file = new File( RETRIEVAL_QUERIES_FILE_PATH );
            FileReader fileReader = new FileReader( file );
            BufferedReader bufferedReader = new BufferedReader( fileReader );
            ArrayList<String> queries = new ArrayList<String>();
            String line;
            while ( ( line = bufferedReader.readLine() ) != null ) {
                queries.add( line );
            }

            PreparedStatement statement = connection.prepareStatement( queries.get( EQUATIONS_ID_AND_NAME ) );
            logger.debug( statement.toString() );
            ResultSet result = statement.executeQuery();
            logger.debug( "sending ids and names query result..." );
            sender.publish( EQueueName.GET_EQUATIONS_ID_AND_NAMES_RESPONSE_QUEUE_NAME.value(),
                    Serializer.serialize( result ) );
            logger.debug( "sending ids and names query result... done." );
        } catch ( IOException | SQLException e ) {
            e.printStackTrace();
        }
    }
}
