package fr.ensma.lias.generator.rabbitmq;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Envelope;

import fr.ensma.lias.generator.generatorservice.Actions;
import fr.ensma.lias.javarabbitmqapi.RabbitMQBasicReceiver;
import fr.ensma.lias.javarabbitmqapi.RabbitMQConsumer;

/**
 * Handles messages from the generateResquest queue. Happens when the comparator
 * needs data to compare a series to.
 * 
 * @author Cyrille Ponchateau (cyrille.ponchateau@ensma.fr)
 *
 */
public class GenerateRequestConsumer extends RabbitMQConsumer {

    public GenerateRequestConsumer( RabbitMQBasicReceiver receiver ) {
        super( receiver );
    }

    @Override
    public void handleDelivery( String consumerTag, Envelope envelope, BasicProperties properties, byte[] body )
            throws IOException {
        // when data are requested, the operation is logged and the
        // corresponding operation is called from the Actions object
        Logger.getLogger( getClass() ).debug( "received generate request..." );
        try {
            Thread.sleep( 100 );
        } catch ( InterruptedException e ) {
            e.printStackTrace();
        }
        Actions.getInstance().sendPullRequest( new String( body ) );
        try {
            Thread.sleep( 100 );
        } catch ( InterruptedException e ) {
            e.printStackTrace();
        }
        // logs the end of the operation
        Logger.getLogger( getClass() ).debug( "received generate request... done." );
        // notifies the server about operation over
        Logger.getLogger( getClass() ).debug( "sending acknowledgement..." );
        getChannel().basicAck( envelope.getDeliveryTag(), false );
        Logger.getLogger( getClass() ).debug( "sending acknowledgement... done." );
    }

}
