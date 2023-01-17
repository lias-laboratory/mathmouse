package fr.ensma.lias.dockermanager.rabbitmq;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Envelope;

import fr.ensma.lias.dockermanager.serviceimpl.Actions;
import fr.ensma.lias.javarabbitmqapi.RabbitMQBasicReceiver;
import fr.ensma.lias.javarabbitmqapi.RabbitMQConsumer;

public class IncrementeServiceRequest extends RabbitMQConsumer {

    public IncrementeServiceRequest( RabbitMQBasicReceiver receiver ) {
        super( receiver );
    }

    @Override
    public void handleDelivery( String consumerTag, Envelope envelope, BasicProperties properties, byte[] body )
            throws IOException {
        Logger.getLogger( getClass() ).debug( "received incremente service request..." );
        Actions.getInstance().incrementeInstanceService( new String( body ) );
        // logs the end of the operation
        Logger.getLogger( getClass() ).debug( "received incremente service request... done." );
        // notifies the server about operation over
        Logger.getLogger( getClass() ).debug( "sending acknowledgement..." );
        getChannel().basicAck( envelope.getDeliveryTag(), false );
        Logger.getLogger( getClass() ).debug( "sending acknowledgement... done." );
    }
}
