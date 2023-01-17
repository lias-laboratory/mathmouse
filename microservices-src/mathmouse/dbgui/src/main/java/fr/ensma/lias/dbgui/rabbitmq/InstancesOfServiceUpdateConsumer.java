package fr.ensma.lias.dbgui.rabbitmq;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Envelope;

import fr.ensma.lias.dbgui.kernel.models.ServicesList;
import fr.ensma.lias.javarabbitmqapi.RabbitMQBasicReceiver;
import fr.ensma.lias.javarabbitmqapi.RabbitMQConsumer;
import fr.ensma.lias.javarabbitmqapi.enumerations.ESpecialCharacter;

public class InstancesOfServiceUpdateConsumer extends RabbitMQConsumer {
    private static final int SERVICE_NAME_INDEX = 0;
    private static final int INSTANCES_INDEX    = 1;

    private Logger           logger;

    public InstancesOfServiceUpdateConsumer( RabbitMQBasicReceiver receiver ) {
        super( receiver );
        logger = LoggerFactory.getLogger( getClass() );
    }

    @Override
    public void handleDelivery( String consumerTag, Envelope envelope, BasicProperties properties, byte[] body )
            throws IOException {
        logger.debug( "received service instances update..." );
        readMessage( new String( body ) );
        logger.debug( "received service instances update... done." );
        logger.debug( "sending acknowledgement..." );
        getChannel().basicAck( envelope.getDeliveryTag(), false );
        logger.debug( "sending acknowledgement... done." );
    }

    private void readMessage( String message ) {
        message = message.replace( "\r", "" );
        String[] data = message.split( ESpecialCharacter.SEPARATOR.value() );
        ServicesList.getInstances().setServicesInstances( data[SERVICE_NAME_INDEX],
                Integer.parseInt( data[INSTANCES_INDEX] ) );
    }
}
