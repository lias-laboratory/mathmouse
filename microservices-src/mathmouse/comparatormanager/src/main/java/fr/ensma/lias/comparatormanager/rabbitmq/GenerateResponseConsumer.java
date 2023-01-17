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
 * Handles messages coming form generateResponse queue. Happens when a generator service finished a
 * generation operation.
 * 
 * @author Cyrille Ponchateau (cyrille.ponchateau@ensma.fr)
 *
 */
public class GenerateResponseConsumer extends RabbitMQConsumer {
    private Logger logger;

    public GenerateResponseConsumer( RabbitMQBasicReceiver receiver ) {
        super( receiver );
        logger = LoggerFactory.getLogger( getClass() );
    }

    @Override
    public void handleDelivery( String consumerTag, Envelope envelope, BasicProperties properties, byte[] body )
            throws IOException {
        logger.debug( "received generate response from generator..." );
        Actions.getInstance().dispatchComparisonData( new String( body ) );
        // logs the end of the operation
        logger.debug( "received generate response from generator... done." );
        // notifies the server about operation over
        logger.debug( "sending acknowledgement..." );
        getChannel().basicAck( envelope.getDeliveryTag(), false );
        logger.debug( "sending acknowledgement... done." );
    }

}
