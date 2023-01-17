package fr.ensma.lias.dbcore.rabbitmq;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Envelope;

import fr.ensma.lias.dbcore.serviceimpl.Actions;
import fr.ensma.lias.javarabbitmqapi.RabbitMQBasicReceiver;
import fr.ensma.lias.javarabbitmqapi.RabbitMQConsumer;

/**
 * Handles insertion requests (insert into operations). Happens when a service needs to insert new a
 * new equation in the database.
 * 
 * @author Cyrille Ponchateau (ponchateau@ensma.fr)
 *
 */
public class PutEquationRequestConsumer extends RabbitMQConsumer {
    private Logger logger;

    /**
     * Constructor using fields
     * 
     * @param receiver
     * @param actions
     */
    public PutEquationRequestConsumer( RabbitMQBasicReceiver receiver ) {
        super( receiver );
        logger = LoggerFactory.getLogger( getClass() );
    }

    @Override
    public void handleDelivery( String consumerTag, Envelope envelope, BasicProperties properties, byte[] body )
            throws IOException {
        logger.debug( "received put equation request..." );
        Actions.getInstance().putEquation( new String( body ) );
        logger.debug( "received put equation request... done." );
        logger.debug( "sending acknowledgement..." );
        getChannel().basicAck( envelope.getDeliveryTag(), false );
        logger.debug( "sending acknowledgement... done." );
    }

}
