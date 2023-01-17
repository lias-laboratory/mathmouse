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

/**
 * Handles comparison results messages. When a new equation has been compared, the result must
 * appear in the details window of the Job Panel view, of the corresponding job line.
 * 
 * @author Cyrille Ponchateau (ponchateau@ensma.fr)
 *
 */
public class ComparisonResponseConsumer extends RabbitMQConsumer {
    /**
     * String stream listened by other objects.
     */
    private static List<String>    resultsList = Collections.synchronizedList( new Vector<String>() );
    /**
     * Observable object, needed for using RxJava library
     */
    private Observable<String>     observableList;
    /**
     * List of the observers
     */
    private List<Observer<String>> observers;
    private Logger                 logger;

    /**
     * Constructor
     * 
     * @param receiver
     */
    public ComparisonResponseConsumer( RabbitMQBasicReceiver receiver ) {
        super( receiver );
        observableList = Observable.from( resultsList );
        observers = new ArrayList<Observer<String>>();
        logger = LoggerFactory.getLogger( getClass() );
    }

    /**
     * Adds a new observer, to the observers list
     * 
     * @param newObserver
     */
    public void addObserver( Observer<String> newObserver ) {
        observers.add( newObserver );
    }

    @Override
    public void handleDelivery( String consumerTag, Envelope envelope, BasicProperties properties, byte[] body )
            throws IOException {
        logger.debug( "received new comparison response..." );
        addResults( new String( body ) );
        logger.debug( "received new comparison response... done." );
        logger.debug( "sending acknowledgement..." );
        getChannel().basicAck( envelope.getDeliveryTag(), false );
        logger.debug( "sending acknowledgement... done." );
    }

    /**
     * Adds a new incoming comparison result to the list stream
     * 
     * @param data
     */
    private void addResults( String data ) {
        resultsList.add( data );
        for ( Observer<String> observer : observers ) {
            observableList.subscribe( observer );
        }
        // once observers updated there data, the list is cleared
        // it should not be necessary to empty the list every time, but RxJava started acting
        // inconsistent. The observers would iterate through the whole list, instead of the last
        // added element, each time a new element was added to the list.
        resultsList.clear();
    }
}
