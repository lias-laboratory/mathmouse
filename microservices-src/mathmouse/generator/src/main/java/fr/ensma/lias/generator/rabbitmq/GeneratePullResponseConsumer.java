package fr.ensma.lias.generator.rabbitmq;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Envelope;

import fr.ensma.lias.generator.generatorservice.Actions;
import fr.ensma.lias.javarabbitmqapi.RabbitMQBasicReceiver;
import fr.ensma.lias.javarabbitmqapi.RabbitMQConsumer;

/**
 * Handles messages from the GeneratePullResponse queue. Happens when the
 * database sent the result of a query previously performed.
 * 
 * @author Cyrille Ponchateau (cyrille.ponchateau@ensma.fr)
 *
 */
public class GeneratePullResponseConsumer extends RabbitMQConsumer {

    public GeneratePullResponseConsumer( RabbitMQBasicReceiver receiver ) {
        super( receiver );
    }

    @Override
    public void handleDelivery( String consumerTag, Envelope envelope, BasicProperties properties, byte[] body )
            throws IOException {
        Logger.getLogger( getClass() ).debug( "received pull response from generator..." );
        // build and start a new job to generate a time series using the
        // information contained in the body of the message just received.
        try {
            Thread.sleep( 100 );
        } catch ( InterruptedException e ) {
            e.printStackTrace();
        }
        Actions.getInstance().generateAndSend( new String( body ) );
        try {
            Thread.sleep( 100 );
        } catch ( InterruptedException e ) {
            e.printStackTrace();
        }
        // logs the end of the operation
        Logger.getLogger( getClass() ).debug( "received pull response from generator... done." );
        // notifies the server about operation over
        Logger.getLogger( getClass() ).debug( "sending acknowledgement..." );
        getChannel().basicAck( envelope.getDeliveryTag(), false );
        Logger.getLogger( getClass() ).debug( "sending acknowledgement... done." );
    }

}
