package fr.ensma.lias.dbcoreapi.io;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import fr.ensma.lias.dbcoreapi.io.tools.IdGenerator;
import fr.ensma.lias.dbcoreapi.models.FlatDifferentialEquation;
import fr.ensma.lias.dbcoreapi.models.Formula;
import fr.ensma.lias.dbcoreapi.models.InitialValue;
import fr.ensma.lias.dbcoreapi.models.Input;
import fr.ensma.lias.dbcoreapi.models.Node;
import fr.ensma.lias.dbcoreapi.models.Variable;
import fr.ensma.lias.dbcoreapi.models.enumerations.EMathObject;

/**
 * The class is made to implement any file writing function. Currently, it only implement a function
 * able to write an equation into an XML file. The XML is confirmed to an XML Schema, see
 * resources/differential-equation.xsd. This XML Schema where made to be as similar as the PMML XML
 * Schema, hoping it will help produce an extended PMML XML Schema, that can support this
 * differential-equation representation.
 * 
 * @author cyrille ponchateau (cyrille.ponchateau@ensma.fr)
 *
 */
public class XMLFileFactory extends FileIOFactory {
    /**
     * XML tag names
     */
    protected static final String ROOT_TAG_NAME                       = "differential-equation";
    protected static final String FORMULA_TAG_NAME                    = "formula";
    protected static final String BINARY_OPERATOR_TAG_NAME            = "binary-operator";
    protected static final String UNARY_OPERATOR_TAG_NAME             = "unary-operator";
    protected static final String FUNCTION_TAG_NAME                   = "function";
    protected static final String NUMBER_TAG_NAME                     = "number";
    protected static final String VARIABLE_TAG_NAME                   = "variable";
    protected static final String PARAMETERS_SETS_TAG_NAME            = "parameters-sets";
    protected static final String PARAMETERS_SET_TAG_NAME             = "parameters-set";
    protected static final String INITIAL_VALUE_TAG_NAME              = "initial-value";
    protected static final String INITIAL_VALUES_TAG_NAME             = "initial-values";
    protected static final String VARIABLES_VALUES_TAG_NAME           = "variables-values";
    protected static final String VARIABLE_VALUE_TAG_NAME             = "variable-value";
    protected static final String INPUT_FUNCTIONS_TAG_NAME            = "input-functions";
    protected static final String INPUT_FUNCTION_TAG_NAME             = "input-function";
    protected static final String TIME_SERIES_TAG_NAME                = "time-series";
    protected static final String TIME_VALUE_TAG_NAME                 = "time-value";

    /**
     * XML atrributes names
     */
    protected static final String NUMBER_OF_VARIABLES_ATTRIBUTE       = "numberOfVariables";
    protected static final String NUMBER_OF_INPUT_FUNCTIONS_ATTRIBUTE = "numberOfInputFunctions";
    protected static final String NUMBER_OF_INITIAL_VALUES_ATTRIBUTE  = "numberOfInitialValues";
    protected static final String OUTPUT_NAME_ATTRIBUTE               = "outputName";
    protected static final String NUMBER_OF_PARAMETERS_SETS_ATTRIBUTE = "numberOfParametersSets";
    protected static final String VALUE_ATTRIBUTE                     = "value";
    protected static final String DERIV_ATTRIBUTE                     = "deriv";
    protected static final String ROLE_ATTRIBUTE                      = "role";
    protected static final String NAME_ATTRIBUTE                      = "name";
    protected static final String PATH_ATTRIBUTE                      = "path";
    protected static final String TIME_ATTRIBUTE                      = "time";
    protected static final String INTERPOLATION_METHOD_ATTRIBUTE      = "interpolationMethod";
    protected static final String GROUP_ATTRIBUTE                     = "group";

    private Logger                logger;

    /**
     * some other need variable
     */
    private List<String>          variablesList;
    private List<String>          inputFunctionsList;
    private String                outputName;
    private int                   numberOfParametersSets;

    /**
     * Static instance
     */
    private static XMLFileFactory factory;

    /**
     * XML Schema
     */
    protected Schema              schema;

    /**
     * Sets the XML Schema from the path of the .xsd file.
     * 
     * @param XSDFilePath
     */
    public void setSchema( String XSDFilePath ) {
        SchemaFactory factory = SchemaFactory.newInstance( XMLConstants.W3C_XML_SCHEMA_NS_URI );
        try {
            schema = factory.newSchema( new File( XSDFilePath ) );
        } catch ( SAXException e ) {
            e.printStackTrace();
        }
    }

    /**
     * Private constructor, for singleton pattern purposes
     */
    private XMLFileFactory() {
        logger = LoggerFactory.getLogger( getClass() );
    }

    /**
     * Static accessors
     */
    public static XMLFileFactory getInstance() {
        if ( factory == null )
            factory = new XMLFileFactory();
        return factory;
    }

    public static XMLFileFactory getInstance( String XSDFilePath ) {
        if ( factory == null )
            factory = new XMLFileFactory();
        factory.setSchema( XSDFilePath );
        return factory;
    }

    /**
     * Writes an equation into an XML file.
     * 
     * @param destinationXMLFile,
     *            where to write the XML file
     * @param eqdiff,
     *            the equation to write
     * @param XSDFilePath,
     *            the XML Schema description
     */
    public String XMLWrite( FlatDifferentialEquation eqdiff ) {
        numberOfParametersSets = 1;
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder;
        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();
            // creating root
            Element rootElement = document.createElement( ROOT_TAG_NAME );
            rootElement.setAttribute( NAME_ATTRIBUTE, eqdiff.getName() );
            rootElement.setAttribute( GROUP_ATTRIBUTE, eqdiff.getGroup() );
            document.appendChild( rootElement );
            // creating formula tag
            Element formula = document.createElement( FORMULA_TAG_NAME );
            // recursive creation of the elements of the equation
            formula.appendChild(
                    createElement( eqdiff.getFormula(), document, findRootNodeId( eqdiff.getFormula() ) ) );
            // settings attributes of formula
            formula.setAttribute( NUMBER_OF_INPUT_FUNCTIONS_ATTRIBUTE, String.valueOf( eqdiff.getInputs().size() ) );
            formula.setAttribute( NUMBER_OF_VARIABLES_ATTRIBUTE, String.valueOf( eqdiff.getVariables().size() ) );
            formula.setAttribute( OUTPUT_NAME_ATTRIBUTE, findOutputName( eqdiff ) );
            // appending formula to root
            rootElement.appendChild( formula );
            // creating parameters sets tag
            Element parametersSetsElement = document.createElement( PARAMETERS_SETS_TAG_NAME );
            parametersSetsElement.setAttribute( NUMBER_OF_PARAMETERS_SETS_ATTRIBUTE,
                    String.valueOf( numberOfParametersSets ) );
            parametersSetsElement.appendChild( createElement( eqdiff, document ) );
            rootElement.appendChild( parametersSetsElement );
            // writing into string
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty( OutputKeys.ENCODING, "utf-8" );
            DOMSource source = new DOMSource( document );
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult( writer );
            transformer.transform( source, result );
            // validates the files
            Validator validator = schema.newValidator();
            validator.validate( new DOMSource( document ) );
            return writer.getBuffer().toString().replaceAll( "\n|\r", "" );
        } catch ( Exception e ) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * Creates the element structure for a parameters set (system).
     * 
     * @param parametersSet
     * @param document
     * @return
     */
    private Element createElement( FlatDifferentialEquation eqdiff, Document document ) {
        // create new parameters set element
        Element parametersSetElement = document.createElement( PARAMETERS_SET_TAG_NAME );
        // create initial element
        Element initialValuesElement = document.createElement( INITIAL_VALUES_TAG_NAME );
        initialValuesElement.setAttribute( NUMBER_OF_INITIAL_VALUES_ATTRIBUTE,
                String.valueOf( eqdiff.getInitialValues().size() ) );
        // going through the initial value list of the parameters set
        for ( Long id : eqdiff.getInitialValues().keySet() ) {
            // create a variable value element
            Element initialValueElement = document.createElement( INITIAL_VALUE_TAG_NAME );
            initialValueElement.setAttribute( NAME_ATTRIBUTE, eqdiff.getInitialValues().get( id ).getName() );
            initialValueElement.setAttribute( VALUE_ATTRIBUTE,
                    Double.toString( eqdiff.getInitialValues().get( id ).getInitialValue() ) );
            initialValueElement.setAttribute( DERIV_ATTRIBUTE,
                    Integer.toString( eqdiff.getInitialValues().get( id ).getDeriv() ) );
            initialValuesElement.appendChild( initialValueElement );
        }
        parametersSetElement.appendChild( initialValuesElement );
        // create variables value element
        Element variablesValuesElement = document.createElement( VARIABLES_VALUES_TAG_NAME );
        variablesValuesElement.setAttribute( NUMBER_OF_VARIABLES_ATTRIBUTE,
                Integer.toString( eqdiff.getVariables().size() ) );
        // going through the variable list of the parameters set
        for ( Long id : eqdiff.getVariables().keySet() ) {
            // create a variable value element
            Element variableValueElement = document.createElement( VARIABLE_VALUE_TAG_NAME );
            variableValueElement.setAttribute( NAME_ATTRIBUTE, eqdiff.getVariables().get( id ).getName() );
            variableValueElement.setAttribute( VALUE_ATTRIBUTE,
                    Double.toString( eqdiff.getVariables().get( id ).getValue() ) );
            variablesValuesElement.appendChild( variableValueElement );
        }
        // appending variables values element to parameters set element
        parametersSetElement.appendChild( variablesValuesElement );
        // create input functions element
        Element inputFunctionsElement = document.createElement( INPUT_FUNCTIONS_TAG_NAME );
        inputFunctionsElement.setAttribute( NUMBER_OF_INPUT_FUNCTIONS_ATTRIBUTE,
                Integer.toString( eqdiff.getInputs().size() ) );
        // going through the input functions list of the parameters set
        for ( Long id : eqdiff.getInputs().keySet() ) {
            // create input function element
            Element inputFunctionElement = document.createElement( INPUT_FUNCTION_TAG_NAME );
            inputFunctionElement.setAttribute( NAME_ATTRIBUTE, eqdiff.getInputs().get( id ).getName() );
            inputFunctionElement.setAttribute( DERIV_ATTRIBUTE,
                    Integer.toString( eqdiff.getInputs().get( id ).getDeriv() ) );
            // create time series element
            Element timeSeriesElement = document.createElement( TIME_SERIES_TAG_NAME );
            timeSeriesElement.setAttribute( INTERPOLATION_METHOD_ATTRIBUTE,
                    eqdiff.getInputs().get( id ).getInterpolationFunction().value() );
            for ( Double timeKey : eqdiff.getInputs().get( id ).getSeries().keySet() ) {
                Element timeValueElement = document.createElement( TIME_VALUE_TAG_NAME );
                timeValueElement.setAttribute( TIME_ATTRIBUTE, String.valueOf( timeKey ) );
                timeValueElement.setAttribute( VALUE_ATTRIBUTE,
                        String.valueOf( eqdiff.getInputs().get( id ).getSeries().get( timeKey ) ) );
                timeSeriesElement.appendChild( timeValueElement );
            }
            inputFunctionElement.appendChild( timeSeriesElement );
            inputFunctionsElement.appendChild( inputFunctionElement );
        }
        // appending input functions element to parameters set element
        parametersSetElement.appendChild( inputFunctionsElement );
        return parametersSetElement;
    }

    /**
     * Recursive building of the element structure corresponding to the equation.
     * 
     * @param node
     * @param document
     * @return
     */
    private Element createElement( Formula formula, Document document, Long id ) {
        Element element = null;
        Node currentNode = formula.get( id );
        if ( currentNode.getMathObject().equals( EMathObject.BINARY_OPERATOR ) ) {
            // creating binary operator element
            element = document.createElement( BINARY_OPERATOR_TAG_NAME );
            // setting value attribute
            Attr value = document.createAttribute( VALUE_ATTRIBUTE );
            value.setValue( currentNode.getName() );
            element.setAttributeNode( value );
            // creating left child element
            element.appendChild( createElement( formula, document, currentNode.getLeft() ) );
            // creating right child element
            element.appendChild( createElement( formula, document, currentNode.getRight() ) );
        } else if ( currentNode.getMathObject().equals( EMathObject.UNARY_OPERATOR ) ) {
            // creating unary operator element
            element = document.createElement( UNARY_OPERATOR_TAG_NAME );
            // setting value attribute
            element.setAttribute( VALUE_ATTRIBUTE, currentNode.getName() );
            // creating child
            element.appendChild( createElement( formula, document, currentNode.getLeft() ) );
        } else if ( currentNode.getMathObject().equals( EMathObject.FUNCTION ) ) {
            // creating function element
            element = document.createElement( FUNCTION_TAG_NAME );
            // setting attributes
            element.setAttribute( VALUE_ATTRIBUTE, currentNode.getName() );
            element.setAttribute( DERIV_ATTRIBUTE, String.valueOf( currentNode.getDeriv() ) );
            // check if the function has a child
            if ( currentNode.getLeft() > 0 )
                element.appendChild( createElement( formula, document, currentNode.getLeft() ) );
        } else if ( currentNode.getMathObject().equals( EMathObject.NUMBER ) ) {
            // creating number element
            element = document.createElement( NUMBER_TAG_NAME );
            // setting attribute
            element.setAttribute( VALUE_ATTRIBUTE, currentNode.getName() );
        } else if ( currentNode.getMathObject().equals( EMathObject.VARIABLE ) ) {
            // creating variable element
            element = document.createElement( VARIABLE_TAG_NAME );
            // setting attribute
            element.setAttribute( VALUE_ATTRIBUTE, currentNode.getName() );
        }
        return element;
    }

    /**
     * Looks for the root node of the equation (root node of the tree that represents the formula of
     * the equation)
     * 
     * @param formula
     * @return
     */
    private static long findRootNodeId( Formula formula ) {
        for ( Long id : formula.keySet() ) {
            // the root has no parent and its depth is 0
            if ( formula.get( id ).getDepth() == 0 && formula.get( id ).getParent() <= 0 ) {
                return id.longValue();
            }
        }
        return 0;
    }

    /**
     * Looks for the output function of the equation.
     * 
     * @param eqdiff
     * @return
     * @throws Exception
     */
    private static String findOutputName( FlatDifferentialEquation eqdiff ) throws Exception {
        List<String> outputs = new ArrayList<String>();
        List<String> inputs = new ArrayList<String>();
        // looks for all functions contained in the formula
        for ( Long id : eqdiff.getFormula().keySet() ) {
            if ( eqdiff.getFormula().get( id ).getMathObject().equals( EMathObject.FUNCTION ) ) {
                outputs.add( eqdiff.getFormula().get( id ).getName() );
            }
        }
        // looks for all the functions listed as inputs
        for ( Long id : eqdiff.getInputs().keySet() ) {
            inputs.add( eqdiff.getInputs().get( id ).getName() );
        }
        // removes from the functions of the formula the input functions found,
        // to keep only the outputs
        outputs.removeAll( inputs );
        if ( outputs.isEmpty() ) {
            throw new Exception( "no output found" );
        }
        return outputs.get( 0 );
    }

    /**
     * Reads an equation form an XML string
     * 
     * @param xmlString
     * @param differentialEquationsUnavailableIds
     * @param nodesUnavailableIds
     * @param initialValuesUnavailableIds
     * @param inputFunctionsUnavailbleIds
     * @param variableValuesUnavailableIds
     * @return FlatDifferentialEquation
     * @see fr.ensma.lias.mmwdbapi.pojos.FlatDifferentialEquation
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public FlatDifferentialEquation XMLread( String xmlString, ArrayList<Long> differentialEquationsUnavailableIds,
            ArrayList<Long> nodesUnavailableIds, ArrayList<Long> initialValuesUnavailableIds,
            ArrayList<Long> inputFunctionsUnavailbleIds, ArrayList<Long> variableValuesUnavailableIds,
            ArrayList<Long> groupsUnavailableIds )
            throws ParserConfigurationException, SAXException, IOException {
        logger.debug( xmlString );
        IdGenerator EqDiffIdGenerator = new IdGenerator( differentialEquationsUnavailableIds );
        FlatDifferentialEquation flateqdiff = new FlatDifferentialEquation();
        flateqdiff.setId( EqDiffIdGenerator.getValidId() );

        DocumentBuilder db;
        db = DocumentBuilderFactory.newInstance().newDocumentBuilder();

        InputSource is = new InputSource();
        is.setCharacterStream( new StringReader( xmlString ) );

        // parse the xml string into DOM object
        Document doc = db.parse( is );

        // reads equation name and group
        NodeList rootNodesList = doc.getElementsByTagName( ROOT_TAG_NAME );
        Element rootNode = (Element) rootNodesList.item( 0 );
        flateqdiff.setName( rootNode.getAttribute( NAME_ATTRIBUTE ) );

        IdGenerator groupIdGenerator = new IdGenerator( groupsUnavailableIds );
        Long groupId = groupIdGenerator.getValidId();
        flateqdiff.setIdGroup( groupId );
        flateqdiff.setGroup( rootNode.getAttribute( GROUP_ATTRIBUTE ) );

        IdGenerator nodeIdGenerator = new IdGenerator( nodesUnavailableIds );
        Long rootNodeId = nodeIdGenerator.getValidId();

        logger.debug( "reading equation formula..." );
        readFormula( flateqdiff, doc, (Element) doc.getElementsByTagName( BINARY_OPERATOR_TAG_NAME ).item( 0 ),
                rootNodeId, new Long( 0 ), 0, nodeIdGenerator );
        logger.debug( "reading equation formula... done." );
        logger.debug( "reading parameters set..." );
        readParametersSet( flateqdiff, doc, initialValuesUnavailableIds, inputFunctionsUnavailbleIds,
                variableValuesUnavailableIds );
        logger.debug( "reading parameters set... done." );

        // setting of the order
        flateqdiff.setOrder();

        // trigerring setting of the serial keys of the input functions
        flateqdiff.setInputSerialKeys();

        logger.debug( flateqdiff.toString() );
        return flateqdiff;
    }

    /**
     * Reads the elements of the parameters set of the XML file and maps the information into the
     * appropriate objects of the FlatDifferentialEquation object.
     * 
     * @param flateqdiff
     * @param document
     * @see fr.ensma.lias.mmwdbapi.pojos.FlatDifferentialEquation
     */
    private void readParametersSet( FlatDifferentialEquation flateqdiff, Document document,
            ArrayList<Long> initialValuesUnavailableIds, ArrayList<Long> inputFunctionsUnavailbleIds,
            ArrayList<Long> variableValuesUnavailableIds ) {
        // reading initial values
        // getting initial-value tags list
        NodeList nodesList = document.getElementsByTagName( INITIAL_VALUE_TAG_NAME );
        // preparing generator
        IdGenerator idGenerator = new IdGenerator( initialValuesUnavailableIds );
        Element currentNode = null;
        Long currentId;

        // creating a new InitialValue Object for each tag in nodesList and
        // adding it to initialValues map in FlatDifferentialEquation object
        for ( int i = 0; i < nodesList.getLength(); i++ ) {
            currentNode = (Element) nodesList.item( i );
            currentId = idGenerator.getValidId();
            flateqdiff.getInitialValues().put( currentId,
                    new InitialValue( currentId, currentNode.getAttribute( NAME_ATTRIBUTE ),
                            Integer.parseInt( currentNode.getAttribute( DERIV_ATTRIBUTE ) ),
                            Double.parseDouble(
                                    currentNode.getAttribute( VALUE_ATTRIBUTE ) ) ) );
        }

        // reading variable values
        nodesList = document.getElementsByTagName( VARIABLE_VALUE_TAG_NAME );
        // reset the generator
        idGenerator = new IdGenerator( variableValuesUnavailableIds );

        for ( int i = 0; i < nodesList.getLength(); i++ ) {
            currentNode = (Element) nodesList.item( i );
            currentId = idGenerator.getValidId();
            flateqdiff.getVariables().put( currentId,
                    new Variable( currentId, currentNode.getAttribute( NAME_ATTRIBUTE ),
                            Double.parseDouble( currentNode.getAttribute( VALUE_ATTRIBUTE ) ) ) );
        }

        // reading input functions
        nodesList = document.getElementsByTagName( INPUT_FUNCTION_TAG_NAME );
        idGenerator = new IdGenerator( inputFunctionsUnavailbleIds );

        for ( int i = 0; i < nodesList.getLength(); i++ ) {
            currentNode = (Element) nodesList.item( i );
            currentId = idGenerator.getValidId();
            NodeList timeseriesElements = currentNode.getElementsByTagName( TIME_SERIES_TAG_NAME );
            Element timeseriesElement = (Element) timeseriesElements.item( 0 );
            flateqdiff.getInputs().put( currentId, new Input( currentId, currentNode.getAttribute( NAME_ATTRIBUTE ),
                    Integer.parseInt( currentNode.getAttribute( DERIV_ATTRIBUTE ) ),
                    timeseriesElement.getAttribute( INTERPOLATION_METHOD_ATTRIBUTE ) ) );
            NodeList timeValueElements = currentNode.getElementsByTagName( TIME_VALUE_TAG_NAME );
            for ( int j = 0; j < timeValueElements.getLength(); j++ ) {
                Element timeValueElement = (Element) timeValueElements.item( j );
                flateqdiff.getInputs().get( currentId ).getSeries().put(
                        Double.parseDouble( timeValueElement.getAttribute( TIME_ATTRIBUTE ) ),
                        Double.parseDouble( timeValueElement.getAttribute( VALUE_ATTRIBUTE ) ) );
            }
        }
    }

    /**
     * Recursively reads the formula tag of the XML file to map the node in the nodes list of the
     * Formula object of a FlatDifferentialEquation object.
     * 
     * @param flateqdiff
     * @param document
     * @param currentElement,
     *            the current node Element to put in the node list
     * @param currentNodeId,
     *            the id of the current node to put in the nodes map
     * @param parentId,
     *            the id of the parent of the node
     * @param depth,
     *            the depth of the node in the tree formula
     * @param generator,
     *            the id generator
     * @see fr.ensma.lias.mmwdbapi.pojos.FlatDifferentialEquation
     * @see fr.ensma.lias.mmwdbapi.pojos.Formula
     */
    private void readFormula( FlatDifferentialEquation flateqdiff, Document document, Element currentElement,
            Long currentNodeId, Long parentId, int depth, IdGenerator generator ) {
        Long leftId;
        Long rightId;
        int deriv;

        if ( currentElement.getAttribute( DERIV_ATTRIBUTE ).equals( "" ) ) {
            deriv = -1;
        } else {
            deriv = Integer.parseInt( currentElement.getAttribute( DERIV_ATTRIBUTE ) );
        }

        if ( currentElement.getTagName().equals( EMathObject.BINARY_OPERATOR.value() ) ) {
            // the current node is a binary operator, thus has two children
            leftId = generator.getValidId(); // get an id for each of its kids
            rightId = generator.getValidId();
            // fill the formula node with the current node data
            flateqdiff.getFormula().put( currentNodeId,
                    new Node( currentNodeId, EMathObject.fromValue( currentElement.getTagName() ),
                            currentElement.getAttribute( VALUE_ATTRIBUTE ), deriv, depth, parentId, leftId, rightId,
                            flateqdiff.getId() ) );
            // recursively process the left node of the current node
            // which depth is increased by one, parentId is the id of the
            // current node and node id is the leftId calculated above
            try {
                readFormula( flateqdiff, document, (Element) currentElement.getChildNodes().item( 0 ), leftId,
                        currentNodeId, depth + 1, generator );
                // idem for right node
                readFormula( flateqdiff, document, (Element) currentElement.getChildNodes().item( 1 ), rightId,
                        currentNodeId, depth + 1, generator );
            } catch ( ClassCastException e ) {
                e.printStackTrace();
            }
        } else if ( currentElement.getTagName().equals( EMathObject.UNARY_OPERATOR.value() )
                || ( currentElement.getTagName().toUpperCase().equals( EMathObject.FUNCTION.value() )
                        && currentElement.getFirstChild() != null ) ) {
            // the current node is an unary node (has only one child)
            // generates an id for its child
            leftId = generator.getValidId();
            // fills the formula with the node data
            flateqdiff.getFormula().put( currentNodeId,
                    new Node( currentNodeId, EMathObject.fromValue( currentElement.getTagName() ),
                            currentElement.getAttribute( VALUE_ATTRIBUTE ), deriv, depth, parentId, leftId, 0,
                            flateqdiff.getId() ) );
            // processes the child of the node as a left child
            readFormula( flateqdiff, document, (Element) currentElement.getChildNodes().item( 0 ), leftId,
                    currentNodeId, depth + 1, generator );
        } else if ( currentElement.getTagName().equals( EMathObject.VARIABLE.value() )
                || currentElement.getTagName().equals( EMathObject.NUMBER.value() )
                || ( currentElement.getTagName().equals( EMathObject.FUNCTION.value() )
                        && currentElement.getFirstChild() == null ) ) {
            // the current node has no child
            // processes the current node and stops the recursion
            flateqdiff.getFormula().put( currentNodeId,
                    new Node( currentNodeId, EMathObject.fromValue( currentElement.getTagName() ),
                            currentElement.getAttribute( VALUE_ATTRIBUTE ), deriv, depth, parentId, 0, 0,
                            flateqdiff.getId() ) );
        }
    }

}
