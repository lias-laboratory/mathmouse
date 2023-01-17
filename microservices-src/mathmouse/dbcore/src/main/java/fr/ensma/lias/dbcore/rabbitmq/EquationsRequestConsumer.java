package fr.ensma.lias.dbcore.rabbitmq;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Envelope;

import fr.ensma.lias.dbcore.serviceimpl.Actions;
import fr.ensma.lias.javarabbitmqapi.RabbitMQBasicReceiver;
import fr.ensma.lias.javarabbitmqapi.RabbitMQConsumer;
import fr.ensma.lias.javarabbitmqapi.RabbitMQObjectManager;

/**
 * Handles data retrieval requests (select operations)
 * 
 * @author Cyrille Ponchateau (ponchateau@ensma.fr)
 *
 */
public class EquationsRequestConsumer extends RabbitMQConsumer {
    private RabbitMQObjectManager<String> stringObjectManager;
    private Logger                        logger;

    /**
     * Constructor using fields
     * 
     * @param receiver,
     *            the rabbitmq receiver client
     * @param actions,
     *            the class containing the different operations available in the API
     */
    public EquationsRequestConsumer( RabbitMQBasicReceiver receiver ) {
        super( receiver );
        stringObjectManager = new RabbitMQObjectManager<String>();
        logger = LoggerFactory.getLogger( getClass() );
    }

    @Override
    public void handleDelivery( String consumerTag, Envelope envelope, BasicProperties properties, byte[] body )
            throws IOException {
        // when data are requested, the operation is logged and the
        // corresponding operation is called from the Actions object
        logger.debug( "received all equations request..." );
        String data = new String( body );
        if ( data == null || data.equals( "" ) )
            Actions.getInstance().sendEquations( null );
        else {
            Long id = Long.parseLong( data );
            Actions.getInstance().sendEquations( id );
        }
        // logs the end of the operation
        logger.debug( "received all equations request... done." );
        // notifies the server about operation over
        logger.debug( "sending acknowledgement..." );
        getChannel().basicAck( envelope.getDeliveryTag(), false );
        logger.debug( "sending acknowledgement... done." );
    }

}
