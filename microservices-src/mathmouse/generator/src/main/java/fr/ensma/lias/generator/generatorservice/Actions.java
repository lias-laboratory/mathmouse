package fr.ensma.lias.generator.generatorservice;

import org.slf4j.LoggerFactory;

import fr.ensma.lias.dockermanagerapi.enumerations.EServiceName;
import fr.ensma.lias.javarabbitmqapi.RabbitMQBasicSender;
import fr.ensma.lias.javarabbitmqapi.enumerations.EQueueName;
import fr.ensma.lias.javarabbitmqapi.enumerations.ESpecialCharacter;
import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.DifferentialEquation2;
import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.DifferentialEquation2IO;
import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.numericalmethod.NumericalMethod2;
import fr.ensma.lias.timeseriesreductorslib.timeseries.TimeSeriesDoubleDouble;
import fr.ensma.lias.timeseriesreductorslib.timeseries.TimeSeriesDoubleDoubleIO;
import fr.ensma.lias.timeseriesreductorslib.timeseries.interpolation.EInterpolationFunction;

/**
 * 
 * Implementations of all the actions allowed by the generator service.
 * 
 * @author cyrille ponchateau (ponchateau@ensma.fr)
 *
 */
public class Actions {
    public static String        XSD_FILE_PATH       = "/resources/differential-equation-2.0.xsd";
    public static final int     METADATA            = 0;
    public static final int     XML_DATA            = 1;
    public static final int     HASH                = 0;
    public static final int     EQUATION_ID         = 1;
    public static final int     NUMBER_OF_EQUATIONS = 2;
    public static final String  NEWLINE             = "\n";

    private RabbitMQBasicSender sender;

    /**
     * Static instance of the class (Pattern Singleton)
     */
    private static Actions      actions;

    /**
     * 
     * Private constructor (Pattern Singleton)
     * 
     * @param sender
     */
    private Actions( RabbitMQBasicSender sender ) {
        this.sender = sender;
    }

    public static void initialize( RabbitMQBasicSender sender ) {
        actions = new Actions( sender );

        XSD_FILE_PATH = System.getenv( "XSD_FILE_PATH" );
        if ( XSD_FILE_PATH == null ) {
            XSD_FILE_PATH = "src/main/resources/differential-equation-2.0.xsd";
        }
    }

    /**
     * 
     * Returns the private static instance of the class (Pattern Singleton)
     * 
     * @return
     */
    public static Actions getInstance() {
        return actions;
    }

    /**
     * 
     * Generate a time series from the equation xml, stored in string and sends it via rabbitmq.
     * 
     * @param string
     */
    public void generateAndSend( String string ) {
        LoggerFactory.getLogger( getClass() ).debug( string );

        // removes unnecessary tabulations
        string = string.replace( "\r", "" );
        // data is : hashcode\nxml. The string is split in two containing
        // hashcode and xml separately
        String[] lines = string.split( NEWLINE );
        // initialize a differential equation from the xml string
        LoggerFactory.getLogger( getClass() ).debug( lines[XML_DATA] );
        DifferentialEquation2 differentialEquation = DifferentialEquation2IO.XMLRead20FromString( XSD_FILE_PATH,
                lines[XML_DATA] );
        try {
            // build a solver for the differential equation
            NumericalMethod2 numericalMethod = new NumericalMethod2( differentialEquation );
            // solves the equations and returns a time series
            TimeSeriesDoubleDouble timeSeries = numericalMethod.equationToTimeseriesRK4();
            // since the series has been generated, its interpolation method
            // will be set to linear by default
            timeSeries.setInterpolationFunction( EInterpolationFunction.LINEAR_INTERPOLATION );
            // the series is sent via rabbitmq, along with the received hashcode
            sender.publish( EQueueName.GENERATE_RESPONSE_QUEUE_NAME.value(),
                    lines[METADATA] + ";" + differentialEquation.getName() + ";"
                            + differentialEquation.getEquation().toString() + NEWLINE
                            + TimeSeriesDoubleDoubleIO.write( timeSeries ) );
            String[] data = lines[METADATA].split( ESpecialCharacter.SEPARATOR.value() );
            sender.publish( EQueueName.PROGRESS,
                    EServiceName.GENERATOR.value() + ESpecialCharacter.SEPARATOR.value()
                            + data[HASH] + ESpecialCharacter.SEPARATOR.value()
                            + data[NUMBER_OF_EQUATIONS] + ESpecialCharacter.SEPARATOR.value()
                            + data[EQUATION_ID] );
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    /**
     * 
     * Sends a pull request to the database. The request message contains the name of the series
     * received by the generator with the comparison request. This name is pushed to the database
     * and back to the comparator, so when the comparator receives the requested data, it is still
     * able to associate it the right time series.
     * 
     * @param string
     */
    public void sendPullRequest( String string ) {
        sender.publish( EQueueName.GENERATE_PULL_REQUEST_QUEUE_NAME.value(), string );
        sender.publish( EQueueName.PROGRESS,
                EServiceName.GENERATOR.value() + ESpecialCharacter.SEPARATOR.value() + string );
    }

}
