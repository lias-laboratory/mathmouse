package fr.ensma.lias.comparator.rabbitmq;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Envelope;

import fr.ensma.lias.comparator.comparatorservice.Actions;
import fr.ensma.lias.javarabbitmqapi.RabbitMQBasicReceiver;
import fr.ensma.lias.javarabbitmqapi.RabbitMQConsumer;

/**
 * Handles messages from the compareDispatch queue. Happens when the comparator manager sends data
 * containing two time series to compare.
 * 
 * @author Cyrille Ponchateau (cyrille.ponchateau@ensma.fr)
 *
 */
public class CompareDispatchConsumer extends RabbitMQConsumer {

    /**
     * Constructor using fields
     * 
     * @param receiver
     */
    public CompareDispatchConsumer( RabbitMQBasicReceiver receiver ) {
        super( receiver );
    }

    @Override
    public void handleDelivery( String consumerTag, Envelope envelope, BasicProperties properties, byte[] body )
            throws IOException {
        Logger.getLogger(this.getClass()).debug( "received comparison order from manager..." );
        // there to introduce a latency.
        // when chaining several of the service, some inconsistency behaviors appears, introducing
        // those break times, gives time to properly finish some other processes, before starting
        // this one.
        try {
            Thread.sleep( 100 );
        } catch ( InterruptedException e ) {
            e.printStackTrace();
        }
        Actions.getInstance().executeComparison( new String( body ) );
        try {
            Thread.sleep( 100 );
        } catch ( InterruptedException e ) {
            e.printStackTrace();
        }
        // logs the end of the operation
        Logger.getLogger(this.getClass()).debug( "received comparison order from manager... done." );
        // notifies the server about operation over
        Logger.getLogger(this.getClass()).debug( "sending acknowledgement..." );
        getChannel().basicAck( envelope.getDeliveryTag(), false );
        Logger.getLogger(this.getClass()).debug( "sending acknowledgement... done." );
    }

}
