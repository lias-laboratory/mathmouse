package fr.ensma.lias.dbgui.rabbitmq;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Envelope;

import fr.ensma.lias.javarabbitmqapi.RabbitMQBasicReceiver;
import fr.ensma.lias.javarabbitmqapi.RabbitMQConsumer;
import rx.Observable;
import rx.Observer;

public class GetEquationsResponseConsumer extends RabbitMQConsumer {
    private static List<String>    equationsDataList = Collections
            .synchronizedList( new Vector<String>() );

    private Observable<String>     observableList;
    private List<Observer<String>> observers;
    private Logger                 logger;

    public GetEquationsResponseConsumer( RabbitMQBasicReceiver receiver ) {
        super( receiver );
        observableList = Observable.from( equationsDataList );
        observers = new ArrayList<Observer<String>>();
        logger = LoggerFactory.getLogger( getClass() );
    }

    public void addObserver( Observer<String> newObserver ) {
        observers.add( newObserver );
    }

    @Override
    public void handleDelivery( String consumerTag, Envelope envelope, BasicProperties properties, byte[] body )
            throws IOException {
        logger.debug( "received new equations ids and names..." );
        addDifferentialEquation( new String( body ) );
        logger.debug( "received new equations ids and names... done." );
        logger.debug( "sending acknowledgement..." );
        getChannel().basicAck( envelope.getDeliveryTag(), false );
        logger.debug( "sending acknowledgement... done." );
    }

    private void addDifferentialEquation( String data ) {
        equationsDataList.add( data );
        for ( Observer<String> observer : observers ) {
            observableList.subscribe( observer );
        }
        equationsDataList.clear();
    }
}
