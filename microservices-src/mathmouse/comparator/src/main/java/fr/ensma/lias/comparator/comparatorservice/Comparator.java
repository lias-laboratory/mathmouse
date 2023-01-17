package fr.ensma.lias.comparator.comparatorservice;

import org.apache.log4j.BasicConfigurator;

import fr.ensma.lias.comparator.rabbitmq.CompareDispatchConsumer;
import fr.ensma.lias.javarabbitmqapi.RabbitMQBasicReceiver;
import fr.ensma.lias.javarabbitmqapi.RabbitMQBasicSender;
import fr.ensma.lias.javarabbitmqapi.enumerations.EQueueName;

/**
 * Main class of the service.
 * 
 * @author Cyrille Ponchateau (cyrille.ponchateau@ensma.fr)
 *
 */
public class Comparator {

    public static void main( String[] args ) {
    	// configuration of log4j, the log library used throughout the
        // application
        BasicConfigurator.configure();

        String rabbitmqHost;

        // host name of rabbitmq can be given as an argument in command line
        if ( args.length > 0 ) {
            rabbitmqHost = args[0];
        } else {
            // if no host is given, we take localhost by default
            rabbitmqHost = "localhost";
        }

        // setting rabbitmq sender and receiver
        RabbitMQBasicSender sender = null;
        RabbitMQBasicReceiver receiver = null;
        sender = new RabbitMQBasicSender( rabbitmqHost, "comparatorsender", "comparatorsender" );
        receiver = new RabbitMQBasicReceiver( rabbitmqHost, "comparatorreceiver",
                "comparatorreceiver" );
        Actions.initialize( sender );// initializing service
        // declaring consumer (listener) on COMPARISON_DISPATCH channel
        receiver.declareConsumer( EQueueName.COMPARISON_DISPATCH.value(),
                new CompareDispatchConsumer( receiver ) );
    }

}
