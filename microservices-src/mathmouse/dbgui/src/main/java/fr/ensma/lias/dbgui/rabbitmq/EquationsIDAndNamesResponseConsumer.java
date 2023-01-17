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

import fr.ensma.lias.dbgui.graphicwrapper.models.EquationIDData;
import fr.ensma.lias.javarabbitmqapi.RabbitMQBasicReceiver;
import fr.ensma.lias.javarabbitmqapi.RabbitMQConsumer;
import rx.Observable;
import rx.Observer;

/**
 * Receives equations names and ids list
 * 
 * @author Cyrille Ponchateau (ponchateau@ensma.fr)
 *
 */
public class EquationsIDAndNamesResponseConsumer extends RabbitMQConsumer {
    private static final String            SEPARATOR              = ";";
    private static final String            NEWLINE                = "\n";
    private static final String            ID_HEADER_ATTRIBUTE    = "id";
    private static final String            NAME_HEADER_ATTRIBUTE  = "name";
    private static final String            GROUP_HEADER_ATTRIBUTE = "group_name";

    /**
     * list of objects to be listened by other objects
     */
    private static List<EquationIDData>    equationsDataList      = Collections
            .synchronizedList( new Vector<EquationIDData>() );
    /**
     * observable object for RxJava
     */
    private Observable<EquationIDData>     observableDataList;
    /**
     * list of observers
     */
    private List<Observer<EquationIDData>> observers;
    private Logger                         logger;

    /**
     * Constructor
     * 
     * @param receiver
     */
    public EquationsIDAndNamesResponseConsumer( RabbitMQBasicReceiver receiver ) {
        super( receiver );
        observableDataList = Observable.from( equationsDataList );
        observers = new ArrayList<Observer<EquationIDData>>();
        logger = LoggerFactory.getLogger( getClass() );
    }

    /**
     * Add a new observer
     * 
     * @param newObserver
     */
    public void addObserver( Observer<EquationIDData> newObserver ) {
        observers.add( newObserver );
    }

    @Override
    public void handleDelivery( String consumerTag, Envelope envelope, BasicProperties properties,
            byte[] body )
            throws IOException {
        logger.debug( "received new equations ids and names..." );
        createAndAddEquationData( new String( body ) );
        logger.debug( "received new equations ids and names... done." );
        logger.debug( "sending acknowledgement..." );
        getChannel().basicAck( envelope.getDeliveryTag(), false );
        logger.debug( "sending acknowledgement... done." );
    }

    /**
     * Create an EquationIDData
     * 
     * @see fr.ensma.lias.dbgui.graphicwrapper.models.EquationIDData
     * @param body
     */
    private void createAndAddEquationData( String body ) {
        body = body.replaceAll( "\r", "" );
        logger.debug( "received : " + body );
        String[] lines = body.split( NEWLINE );

        // first line is metadata, message needs to be at least two lines
        if ( lines.length > 1 ) {
            String[] metadata = lines[0].split( SEPARATOR );
            if ( metadata.length == 3 && metadata[0].equals( ID_HEADER_ATTRIBUTE )
                    && metadata[1].equals( NAME_HEADER_ATTRIBUTE ) && metadata[2].equals( GROUP_HEADER_ATTRIBUTE ) ) {
                for ( int i = 1; i < lines.length; i++ ) {
                    // lines needs to be splitted (csv formatted)
                    String[] data = lines[i].split( SEPARATOR );
                    // builds object with the new data
                    EquationIDData eqiddata = new EquationIDData( Long.parseLong( data[0] ), data[1], data[2] );
                    logger.debug( "Adding element : " + eqiddata.getId() + ", " + eqiddata.getName() );
                    // add to the list and forward to observers
                    equationsDataList.add( eqiddata );
                    for ( Observer<EquationIDData> obs : observers ) {
                        observableDataList.subscribe( obs );
                    }
                    equationsDataList.clear();
                }
            }
        }
    }
}
