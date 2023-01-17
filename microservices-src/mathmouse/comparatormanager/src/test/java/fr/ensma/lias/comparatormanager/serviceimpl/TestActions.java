package fr.ensma.lias.comparatormanager.serviceimpl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.log4j.BasicConfigurator;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import fr.ensma.lias.javarabbitmqapi.RabbitMQBasicSender;
import fr.ensma.lias.javarabbitmqapi.enumerations.EQueueName;
import fr.ensma.lias.javarabbitmqapi.enumerations.ESpecialCharacter;

public class TestActions {
    private static final String PUT_SERIES_INPUT_FILE_PATH = "src/test/resources/txts/putseriesinput.txt";
    private static final String DISPATCH_INPUT_FILE_PATH   = "src/test/resources/txts/dispatchinput.txt";
    private static final String DISPATCH_OUTPUT_FILE_PATH  = "src/test/resources/txts/dispatchoutput.txt";
    private static final String DISPATCH_INITIAL_FILE_PATH = "src/test/resources/txts/dispatchinitial.txt";

    @Mock
    private RabbitMQBasicSender sender;

    @Rule
    public MockitoRule          mockitoRule                = MockitoJUnit.rule();

    @Before
    public void initialize() {
        BasicConfigurator.configure();

    }

    @Test
    public void testPutNewSeries() {
        RabbitMQBasicSender sender = Mockito.mock( RabbitMQBasicSender.class );
        Actions.initialize( sender );
        String message = readFile( PUT_SERIES_INPUT_FILE_PATH );
        String hashString = message.substring( 0, message.indexOf( ESpecialCharacter.NEWLINE.value() ) );

        Actions.getInstance().putNewSeries( message );

        Mockito.verify( sender ).publish( EQueueName.GENERATE_REQUEST_QUEUE_NAME.value(), hashString );
    }

    @Test
    public void testDispatchComparisonData() {
        RabbitMQBasicSender sender = Mockito.mock( RabbitMQBasicSender.class );
        Actions.initialize( sender );
        Actions.getInstance().putNewSeries( readFile( DISPATCH_INITIAL_FILE_PATH ) );
        String inputMessage = readFile( DISPATCH_INPUT_FILE_PATH );

        Actions.getInstance().dispatchComparisonData( inputMessage );

        String outputMessage = readFile( DISPATCH_OUTPUT_FILE_PATH );
        // Mockito.verify( sender ).publish(
        // EQueueName.COMPARISON_DISPATCH.value(), outputMessage );
    }

    private String readFile( String path ) {
        try {
            File file = new File( path );
            BufferedReader bf = new BufferedReader( new FileReader( file ) );

            String result = "";
            String line;

            while ( ( line = bf.readLine() ) != null )
                result = result + line + ESpecialCharacter.NEWLINE.value();

            return result;
        } catch ( IOException e ) {
            e.printStackTrace();
            return null;
        }
    }
}
