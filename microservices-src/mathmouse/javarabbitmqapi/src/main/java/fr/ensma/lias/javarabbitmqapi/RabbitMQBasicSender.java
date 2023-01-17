package fr.ensma.lias.javarabbitmqapi;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.MessageProperties;

import fr.ensma.lias.javarabbitmqapi.enumerations.EQueueName;

/**
 * Classe abstraite héritant de RabbitMQClient, ajoutant des fonctionnalitées d'envoie de messages
 * sur le serveur.
 * 
 * @author Cyrille Ponchateau (cyrille.ponchateau@ensma.fr)
 *
 */
public class RabbitMQBasicSender extends RabbitMQClient {
    private Logger logger;

    public RabbitMQBasicSender( String host ) {
        super( host );
        logger = LoggerFactory.getLogger( getClass() );
    }

    public RabbitMQBasicSender( String host, String username, String password ) {
        super( host, username, password );
        logger = LoggerFactory.getLogger( getClass() );
    }

    public void publish( String queueName, byte[] bytes ) {
        try {
            //declareQueue( queueName );
            logger.debug( "sending byte format content... " );
            channel.basicPublish( "", queueName, MessageProperties.PERSISTENT_TEXT_PLAIN, bytes );
            logger.debug( "sending byte format content... done." );
        } catch ( IOException e ) {
            logger.debug( "error occured while sending." );
            e.printStackTrace();
        }
    }

    public void publish( String queueName ) {
        publish( queueName, "".getBytes() );
    }

    public void publish( EQueueName queueName ) {
        publish( queueName.value() );
    }

    public void publish( String queueName, String message ) {
        publish( queueName, message.getBytes() );
    }

    public void publish( EQueueName queueName, String message ) {
        publish( queueName.value(), message );
    }

}
