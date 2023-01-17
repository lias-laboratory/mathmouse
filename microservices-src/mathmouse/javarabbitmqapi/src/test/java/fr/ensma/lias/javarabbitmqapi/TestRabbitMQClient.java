package fr.ensma.lias.javarabbitmqapi;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

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

public class TestRabbitMQClient {

    @Mock
    private Channel           channel;
    @Mock
    private Connection        connection;
    @Mock
    private ConnectionFactory connectionFactory;

    @Rule
    public MockitoRule        mockitoRule = MockitoJUnit.rule();

    private RabbitMQClient    client;

    @Before
    public void initialize() {

        client = Mockito.mock( RabbitMQClient.class, Mockito.CALLS_REAL_METHODS );
        client.setChannel( channel );
        client.setConnection( connection );
        client.setFactory( connectionFactory );
    }

    @Test
    public void testDeclareQueueWhenConectionAndChannelAreOpen() {
        String queueName = "queue";
        try {
            Mockito.when( connection.isOpen() ).thenReturn( true );
            Mockito.when( channel.isOpen() ).thenReturn( true );
            Mockito.when( channel.queueDeclare( queueName, true, false, false, null ) ).thenReturn( null );

            client.declareQueue( queueName );

            Mockito.verify( connection ).isOpen();
            Mockito.verify( channel ).isOpen();
            Mockito.verify( channel ).queueDeclare( queueName, true, false, false, null );
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDeconnect() {
        try {
            client.deconnect();
            Mockito.verify( channel ).close();
            Mockito.verify( connection ).close();
        } catch ( IOException | TimeoutException e ) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCheckConnectionWhenConnectionWereClosed() {
        try {
            Mockito.when( connection.isOpen() ).thenReturn( false );
            Mockito.when( connectionFactory.newConnection() ).thenReturn( connection );

            client.checkConnection();

            Mockito.verify( connection ).isOpen();
            Mockito.verify( connectionFactory ).newConnection();
        } catch ( IOException | TimeoutException e ) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCheckChannelWhenChannelWereClosed() {
        try {
            Mockito.when( channel.isOpen() ).thenReturn( false );
            Mockito.when( connection.createChannel() ).thenReturn( channel );

            client.checkChannel();

            Mockito.verify( channel ).isOpen();
            Mockito.verify( connection ).createChannel();
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }

}
