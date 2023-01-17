package fr.ensma.lias.javarabbitmqapi;

import java.io.IOException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.slf4j.Logger;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

public class TestRabbitMQBasicSender {
    private static final String QUEUE_NAME  = "queue";
    private static final byte[] BYTES       = new String( "Hello World !" ).getBytes();

    @Mock
    private Channel             channel;
    @Mock
    private Connection          connection;
    @Mock
    private ConnectionFactory   connectionFactory;
    @Mock
    private Logger              logger;

    @Rule
    public MockitoRule          mockitoRule = MockitoJUnit.rule();

    private RabbitMQBasicSender sender;

    @Before
    public void initialize() {
        sender = Mockito.mock( RabbitMQBasicSender.class, Mockito.CALLS_REAL_METHODS );
        sender.setChannel( channel );
        sender.setConnection( connection );
        sender.setFactory( connectionFactory );
    }

    @Test
    public void testPublishWithByteArray() {

        try {
            Mockito.when( connection.isOpen() ).thenReturn( true );
            Mockito.when( channel.isOpen() ).thenReturn( true );

            sender.publish( QUEUE_NAME, BYTES );

            Mockito.verify( channel ).basicPublish( "", QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, BYTES );
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }
}
