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
 * Handles messages from the getEquationsIDAndNames queue. Happens when the GUI needs main
 * information about the equations in the database.
 * 
 * @author Cyrille Ponchateau (cyrille.ponchateau@ensma.fr)
 *
 */
public class GetEquationsIDAndNamesRequest extends RabbitMQConsumer {
    private Logger logger;

    public GetEquationsIDAndNamesRequest( RabbitMQBasicReceiver receiver ) {
        super( receiver );
        logger = LoggerFactory.getLogger( getClass() );
    }

    @Override
    public void handleDelivery( String consumerTag, Envelope envelope, BasicProperties properties, byte[] body )
            throws IOException {
        // when data are requested, the operation is logged and the
        // corresponding operation is called from the Actions object
        logger.debug( "received get equations ids and names request..." );
        Actions.getInstance().getAllEquationsID( new String( body ) );
        // logs the end of the operation
        logger.debug( "received get equations ids and names request... done." );
        // notifies the server about operation over
        logger.debug( "sending acknowledgement..." );
        getChannel().basicAck( envelope.getDeliveryTag(), false );
        logger.debug( "sending acknowledgement... done." );
    }

}
