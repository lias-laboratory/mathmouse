package fr.ensma.lias.javarabbitmqapi;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.Consumer;

import fr.ensma.lias.javarabbitmqapi.enumerations.EQueueName;

/**
 * Classe abstraite héritant de RabbitMQClient, permettant de créer un objet qui écoutera le serveur
 * sur une ou plusieurs queues.
 * 
 * @author Cyrille Ponchateau (cyrille.ponchateau@ensma.fr)
 *
 */
public class RabbitMQBasicReceiver extends RabbitMQClient {
    private Logger logger;

    public RabbitMQBasicReceiver( String host ) {
        super( host );
        logger = LoggerFactory.getLogger( getClass() );
    }

    public RabbitMQBasicReceiver( String host, String username, String password ) {
        super( host, username, password );
        logger = LoggerFactory.getLogger( getClass() );
    }

    public void declareConsumer( String queueName, Consumer consumer ) {
        try {
            declareQueue( queueName );
            channel.basicConsume( queueName, false, (com.rabbitmq.client.Consumer) consumer );
            logger.debug( "started a new consumer on " + queueName + "." );
        } catch ( IOException | SecurityException | IllegalArgumentException e ) {
            logger.debug( "error ocurred by instantiating a new consumer" );
            e.printStackTrace();
        }
    }

    public void declareConsumer( EQueueName queueName, Consumer consumer ) {
        declareConsumer( queueName.value(), consumer );
    }

}
