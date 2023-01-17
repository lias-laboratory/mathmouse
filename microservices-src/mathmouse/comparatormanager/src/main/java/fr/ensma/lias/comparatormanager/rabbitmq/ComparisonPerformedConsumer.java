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
 * Handles message coming from the comparisonPerformed RabbitMQ Queue. Those message happens when a
 * comparator just finished to perform a comparison.
 * 
 * @author Cyrille Ponchateau (cyrille.ponchateau@ensma.fr)
 *
 */
public class ComparisonPerformedConsumer extends RabbitMQConsumer {
    private Logger logger;

    public ComparisonPerformedConsumer( RabbitMQBasicReceiver receiver ) {
        super( receiver );
        logger = LoggerFactory.getLogger( getClass() );
    }

    @Override
    public void handleDelivery( String consumerTag, Envelope envelope, BasicProperties properties, byte[] body )
            throws IOException {
        logger.debug( "received comparison progress acknowledgement..." );
        Actions.getInstance().checkComparisonState( new String( body ) );
        // logs the end of the operation
        logger.debug( "received comparison progress acknowledgement... done." );
        // notifies the server about operation over
        logger.debug( "sending acknowledgement..." );
        getChannel().basicAck( envelope.getDeliveryTag(), false );
        logger.debug( "sending acknowledgement... done." );
    }

}
