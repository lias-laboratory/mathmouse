package fr.ensma.lias.populator.rabbitmq;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Envelope;

import fr.ensma.lias.javarabbitmqapi.RabbitMQBasicReceiver;
import fr.ensma.lias.javarabbitmqapi.RabbitMQConsumer;
import fr.ensma.lias.populator.service.Actions;

public class EquationsIDAndNamesResponseConsumer extends RabbitMQConsumer {
    private Logger logger;

    public EquationsIDAndNamesResponseConsumer( RabbitMQBasicReceiver receiver ) {
        super( receiver );
        logger = LoggerFactory.getLogger( getClass() );
    }

    @Override
    public void handleDelivery( String consumerTag, Envelope envelope, BasicProperties properties,
            byte[] body )
            throws IOException {
        logger.debug( "received new equations ids and names..." );
        Actions.getInstance().logMessage( new String( body ) );
        logger.debug( "received new equations ids and names... done." );
        logger.debug( "sending acknowledgement..." );
        getChannel().basicAck( envelope.getDeliveryTag(), false );
        logger.debug( "sending acknowledgement... done." );
    }

}
