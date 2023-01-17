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
 * 
 * 
 * @author Cyrille Ponchateau (cyrille.ponchateau@ensma.fr)
 *
 */
public class SQLQueryRequestConsumer extends RabbitMQConsumer {
    private RabbitMQBasicReceiver receiver;
    private Logger                logger;

    public SQLQueryRequestConsumer( RabbitMQBasicReceiver receiver ) {
        super( receiver );
        logger = LoggerFactory.getLogger( getClass() );
    }

    @Override
    public void handleDelivery( String consumerTag, Envelope envelope, BasicProperties properties, byte[] body )
            throws IOException {
        logger.debug( "received sql query..." );
        Actions.getInstance().executeSQLQuery( new String( body ) );
        logger.debug( "received sql query... done." );
        logger.debug( "sending acknowledgement..." );
        getChannel().basicAck( envelope.getDeliveryTag(), false );
        logger.debug( "sending acknowledgement... done." );
    }

}
