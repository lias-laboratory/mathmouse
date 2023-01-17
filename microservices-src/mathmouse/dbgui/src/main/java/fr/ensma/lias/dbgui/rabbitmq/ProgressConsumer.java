package fr.ensma.lias.dbgui.rabbitmq;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Envelope;

import fr.ensma.lias.dbgui.gui.DbUserInterfaceMain;
import fr.ensma.lias.javarabbitmqapi.RabbitMQBasicReceiver;
import fr.ensma.lias.javarabbitmqapi.RabbitMQConsumer;
import fr.ensma.lias.javarabbitmqapi.enumerations.ESpecialCharacter;

public class ProgressConsumer extends RabbitMQConsumer {
    private Logger logger;

    public ProgressConsumer( RabbitMQBasicReceiver receiver ) {
        super( receiver );
        logger = LoggerFactory.getLogger( getClass() );
    }

    @Override
    public void handleDelivery( String consumerTag, Envelope envelope, BasicProperties properties, byte[] body )
            throws IOException {
        String message = new String( body );
        message = message.replace( "\r", "" );
        String[] data = message.split( ESpecialCharacter.SEPARATOR.value() );
        logger.debug( "received job progress update..." );
        DbUserInterfaceMain.getStartedJobManager().updateJob( data );
        logger.debug( "received job progress update... done." );
        logger.debug( "sending acknowledgement..." );
        getChannel().basicAck( envelope.getDeliveryTag(), false );
        logger.debug( "sending acknowledgement... done." );
    }
}
