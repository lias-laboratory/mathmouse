package fr.ensma.lias.comparatormanager.rabbitmq;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Envelope;

import fr.ensma.lias.comparatormanager.serviceimpl.Actions;
import fr.ensma.lias.javarabbitmqapi.RabbitMQBasicReceiver;
import fr.ensma.lias.javarabbitmqapi.RabbitMQConsumer;

/**
 * Gets job over acknowledgement message from database, meaning the comparison job (defined by a
 * hash) can be deleted.
 * 
 * @author Cyrille Ponchateau (cyrille.ponchateau@ensma.fr)
 *
 */
public class JobOverConsumer extends RabbitMQConsumer {
    private Logger logger;

    public JobOverConsumer( RabbitMQBasicReceiver receiver ) {
        super( receiver );
        logger = LoggerFactory.getLogger( getClass() );
    }

    @Override
    public void handleDelivery( String consumerTag, Envelope envelope, BasicProperties properties, byte[] body )
            throws IOException {
        logger.debug( "received job over acknowledgement..." );
        Actions.getInstance().deleteJob( new String( body ) );
        // logs the end of the operation
        logger.debug( "received job over acknowledgement... done." );
        // notifies the server about operation over
        logger.debug( "sending acknowledgement..." );
        getChannel().basicAck( envelope.getDeliveryTag(), false );
        logger.debug( "sending acknowledgement... done." );
    }
}
