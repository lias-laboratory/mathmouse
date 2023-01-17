package fr.ensma.lias.populator.service;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.ensma.lias.javarabbitmqapi.RabbitMQBasicSender;
import fr.ensma.lias.javarabbitmqapi.enumerations.EQueueName;
import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.DifferentialEquation2;
import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.DifferentialEquation2IO;

/**
 * 
 * @author cyrille ponchateau (cyrille.ponchateau@ensma.fr)
 *
 */
public class Actions {
    private static String       XSD_FILE_PATH;
    private static String       MODELS_DIRECTORY_PATH;

    /**
     * Pattern singleton, pour fournir un accès global aux fonctionnalités de la
     * classe, sans multipler les instances.
     * 
     */
    private static Actions      actions;

    /**
     * sert à émettre des messages via rabbitmq
     */
    private RabbitMQBasicSender sender;

    private Logger              logger;

    private Actions( RabbitMQBasicSender sender ) {
        this.sender = sender;
        logger = LoggerFactory.getLogger( getClass() );
    }

    /**
     * Initializes the class. The variable:
     * <ul>
     * <li>XSD_FILE_PATH: defines the path to the xsd XML Schema file, necessary
     * to interpret an XML formatted string representing an equation.</li>
     * <li>MODELS_DIRECTORY_PATH: defines the path of a directory containing xml
     * files</li>
     * </ul>
     * They are environment variables and must be defined before running the
     * service: $export VARIABLE_NAME=value
     * 
     * If not defined, the following default values are used:
     * <ul>
     * <li>XSD_FILE_PATH: src/main/resources/differential-equation-2.0.xsd</li>
     * <li>MODELS_DIRECTORY_PATH: src/main/resources/xmls</li>
     * </ul>
     * 
     * @param sender
     */
    public static void initialize( RabbitMQBasicSender sender ) {
        actions = new Actions( sender );

        // looking for environments variables
        XSD_FILE_PATH = System.getenv( "XSD_FILE_PATH" );
        if ( XSD_FILE_PATH == null ) {// using default, if no variables defined
                                      // in environment
            XSD_FILE_PATH = "src/main/resources/differential-equation-2.0.xsd";
        }
        LoggerFactory.getLogger( "STATIC_ACTIONS_INITIALIZE" )
                .debug( "xsd file path has been set to " + XSD_FILE_PATH );

        MODELS_DIRECTORY_PATH = System.getenv( "MODELS_DIRECTORY_PATH" );
        if ( MODELS_DIRECTORY_PATH == null ) {
            MODELS_DIRECTORY_PATH = "src/main/resources/xmls";
        }
        LoggerFactory.getLogger( "STATIC_ACTIONS_INITIALIZE" )
                .debug( "models directory path has been set to " + MODELS_DIRECTORY_PATH );
    }

    public static Actions getInstance() {
        return actions;
    }

    public void logMessage( String string ) {
        string = string.replaceAll( "\r", "" );
        logger.debug( "received : " + string );
    }

    public void sendEquation( File file ) {
        DifferentialEquation2 equation = DifferentialEquation2IO.XMLRead20FromFile( XSD_FILE_PATH,
                file.getAbsolutePath() );
        String xmlEquation = DifferentialEquation2IO.XMLWrite20InString( XSD_FILE_PATH, equation );
        sender.publish( EQueueName.PUT_EQUATION_REQUEST_QUEUE_NAME, xmlEquation );
    }

    public void sendAllEquation() {
        File directory = new File( MODELS_DIRECTORY_PATH );
        logger.debug( "sending all models from directory " + directory.getAbsolutePath() );
        File[] files = directory.listFiles();

        int i = 0;

        for ( File file : files ) {
            logger.debug( "sending models from file " + file.getAbsolutePath() );
            sendEquation( file );
            try {
                Thread.sleep( 3000 );
            } catch ( InterruptedException e ) {
                e.printStackTrace();
            }
            i++;
            logger.debug( i + " models sent." );
        }
        logger.debug( "All models have been sent." );
    }

}
