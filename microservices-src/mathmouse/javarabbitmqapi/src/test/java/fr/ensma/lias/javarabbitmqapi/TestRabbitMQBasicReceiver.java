package fr.ensma.lias.javarabbitmqapi;

import java.io.IOException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;

public class TestRabbitMQBasicReceiver {
    private static final String   QUEUE_NAME  = "queue";

    @Mock
    private Channel               channel;
    @Mock
    private Connection            connection;
    @Mock
    private ConnectionFactory     connectionFactory;
    @Mock
    private Consumer              consumer;

    @Rule
    public MockitoRule            mockitoRule = MockitoJUnit.rule();

    private RabbitMQBasicReceiver receiver;

    @Before
    public void initialize() {
        receiver = Mockito.mock( RabbitMQBasicReceiver.class, Mockito.CALLS_REAL_METHODS );
        receiver.setChannel( channel );
        receiver.setConnection( connection );
        receiver.setFactory( connectionFactory );
    }

    @Test
    public void testPublishWithByteArray() {

        try {
            Mockito.when( connection.isOpen() ).thenReturn( true );
            Mockito.when( channel.isOpen() ).thenReturn( true );

            receiver.declareConsumer( QUEUE_NAME, consumer );

            Mockito.verify( channel ).basicConsume( QUEUE_NAME, false, consumer );
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }
}
