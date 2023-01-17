package fr.ensma.lias.javarabbitmqapi;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * Classe abstraite encapsulant les opérations necessaires pour se connecter à un serveur rabbitmq.
 * 
 * @author Cyrille Ponchateau (ponchateau@ensma.fr)
 *
 */
public abstract class RabbitMQClient {
    private static final String USER_NAME    = "guest";
    private static final String PASSWORD     = "guest";
    private static final String VIRTUAL_HOST = "/";

    protected ConnectionFactory factory      = null;
    protected Connection        connection   = null;
    protected Channel           channel      = null;

    private Logger              logger;

    /**
     * Constructor with fields.
     * 
     * @param host,
     *            the name of the rabbitmq host server
     */
    public RabbitMQClient( String host ) {
        boolean connected = false;
        logger = LoggerFactory.getLogger( getClass() );

        // setting the connection factory
        factory = new ConnectionFactory();
        factory.setUsername( USER_NAME );
        factory.setPassword( PASSWORD );
        factory.setVirtualHost( VIRTUAL_HOST );
        factory.setHost( host );

        // starts the connection, if the connection cannot be created, waits for
        // a while and tries again
        while ( !connected ) {
            try {
                // waits 2 seconds, before trying to connect
                Thread.sleep( 3000 );
                logger.debug( "attempting connection to rabbitmq server..." );
                // tries to create a new connection
                connection = factory.newConnection();
                logger.debug( "connected to rabbitmq server." );
                logger.debug( "attempting channel declaration..." );
                // create a new channel on the connection
                channel = connection.createChannel();
                logger.debug( "channel declared." );
                int prefetchCount = 1;
                // sets the qos to 1
                channel.basicQos( prefetchCount );
            } catch ( IOException | TimeoutException | InterruptedException e ) {
                e.printStackTrace();
                logger.debug( "connection to rabbitmq failed." );
            }

            if ( connection != null && connection.isOpen() ) {
                // the connection between the service and the broker has
                // successfully been established
                if ( channel != null && channel.isOpen() )
                    connected = true;
            }
        }
    }

    /**
     * Constructor with fields.
     * 
     * @param userName
     * @param password
     * @param host,
     *            the name of the rabbitmq host server
     */
    public RabbitMQClient( String host, String username, String password ) {
        logger = LoggerFactory.getLogger( getClass() );

        // setting the connection factory
        factory = new ConnectionFactory();
        factory.setUsername( username );
        factory.setPassword( password );
        factory.setVirtualHost( VIRTUAL_HOST );
        factory.setHost( host );

        // starts the connection, if the connection cannot be created, waits for
        // a while and tries again
        while ( connection == null || !connection.isOpen() ) {
            try {
                // waits 3 seconds, before trying to connect
                Thread.sleep( 3000 );
                logger.debug( "attempting connection to rabbitmq server..." );
                // tries to create a new connection
                connection = factory.newConnection();
                logger.debug( "connected to rabbitmq server." );
                logger.debug( "attempting channel declaration..." );
            } catch ( IOException | TimeoutException | InterruptedException e ) {
                e.printStackTrace();
                logger.debug( "connection to rabbitmq failed." );
            }
        }

        // creates a channel. While the channel is not created and open, it waits
        // for 3 seconds, before trying again
        while ( channel == null || !channel.isOpen() ) {
            try {
                Thread.sleep( 3000 );
                channel = connection.createChannel();
                logger.debug( "channel declared." );
                int prefetchCount = 1;
                // sets the qos to 1
                channel.basicQos( prefetchCount );
            } catch ( IOException | InterruptedException e ) {
                e.printStackTrace();
                logger.debug( "channel creation failed." );
            }

        }
    }

    public void setFactory( ConnectionFactory factory ) {
        this.factory = factory;
    }

    public void setConnection( Connection connection ) {
        this.connection = connection;
    }

    public void setChannel( Channel channel ) {
        this.channel = channel;
    }

    /**
     * Déclare une nouvelle queue, lorsque celle-ci n'a pas encore été déclarée par l'application.
     * 
     * @param queueName
     */
    public void declareQueue( String queueName ) {
        checkConnection();
        checkChannel();
        try {
            logger.debug( "attempting to declare new queue \"" + queueName + "\"..." );
            channel.queueDeclare( queueName, true, false, false, null );
            logger.debug( "queue \"" + queueName + "\" declared." );
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }

    /**
     * Deconnects from the rabbitmq broker.
     */
    public void deconnect() {
        try {
            logger.debug( "closing channel..." );
            channel.close();
            logger.debug( "channel closed." );
            logger.debug( "closing connection..." );
            connection.close();
            logger.debug( "connection closed." );
        } catch ( IOException | TimeoutException e ) {
            e.printStackTrace();
            logger.debug( "deconnection failed." );
        }
    }

    public Channel getChannel() {
        checkConnection();
        checkChannel();
        return channel;
    }

    /**
     * Check if the connection is still active, recreate it if needed.
     */
    protected void checkConnection() {
        logger.info( "checking connection..." );
        try {
            if ( connection == null || !connection.isOpen() ) {
                // the connection is null are closed (might have been closed)
                connection = factory.newConnection();
                logger.debug( "connection: " + connection + ", " + connection.isOpen() );
            } else {
                logger.debug( "nothing wrong with connection." );
            }

        } catch ( IOException e ) {
            e.printStackTrace();
        } catch ( TimeoutException e ) {
            e.printStackTrace();
        }
        logger.info( "checking connection... done." );
    }

    /**
     * Checks if the channel is still opened and redeclare it if needed
     */
    protected void checkChannel() {
        logger.info( "checking channel..." );
        try {
            if ( channel == null || !channel.isOpen() ) {
                // the channel is null or not open (might have been closed)
                logger.debug( "channel: " + channel + ", " + channel.isOpen() );
                // the channel is recreated
                channel = connection.createChannel();
            } else {
                logger.debug( "nothing wrong with channel." );
            }
        } catch ( IOException e ) {
            e.printStackTrace();
        }
        logger.info( "checking channel... done." );
    }

    /**
     * Assure proprement la déconnection, lorsque l'objet est effacé de la mémoire.
     */
    @Override
    public void finalize() {
        deconnect();
    }

}
