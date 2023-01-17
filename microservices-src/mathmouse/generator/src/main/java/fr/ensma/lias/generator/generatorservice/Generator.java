package fr.ensma.lias.generator.generatorservice;

import org.apache.log4j.BasicConfigurator;

import fr.ensma.lias.generator.rabbitmq.GeneratePullResponseConsumer;
import fr.ensma.lias.generator.rabbitmq.GenerateRequestConsumer;
import fr.ensma.lias.javarabbitmqapi.RabbitMQBasicReceiver;
import fr.ensma.lias.javarabbitmqapi.RabbitMQBasicSender;
import fr.ensma.lias.javarabbitmqapi.enumerations.EQueueName;

/**
 * 
 * Main class of the generation service. The service is totally stateless, it has no context data
 * and it response only depends on the incoming data. This design allows to allow the scaling of the
 * service, with no data migration to make.
 * 
 * @author Cyrille Ponchateau (cyrille.ponchateau@ensma.fr)
 *
 */
public class Generator {

    public static void main( String[] args ) {
        // configuration of log4j, the log library used throughout the
        // application
        BasicConfigurator.configure();

        String rabbitmqHost;

        if ( args.length > 0 ) {
            // if the rabbitmq host name were given on command line, it is set
            // to the given argument value
            rabbitmqHost = args[0];
        } else {
            // otherwise, the rabbitmq host is considered to be localhost by
            // default
            rabbitmqHost = "localhost";
        }

        RabbitMQBasicSender sender = null;
        RabbitMQBasicReceiver receiver = null;
        // initializes the sender used by the application to send messages via
        // rabbitmq
        sender = new RabbitMQBasicSender( rabbitmqHost, "generatorsender", "generatorsender" );
        // initializes the receiver, that handles the listeners of rabbitmq
        // messages
        receiver = new RabbitMQBasicReceiver( rabbitmqHost, "generatorreceiver", "generatorreceiver" );
        // initializes the Actions class, that contains the different
        // functionalities available in the service
        Actions.initialize( sender );
        // declares the necessary listeners
        receiver.declareConsumer( EQueueName.GENERATE_REQUEST_QUEUE_NAME.value(),
                new GenerateRequestConsumer( receiver ) );
        receiver.declareConsumer( EQueueName.GENERATE_PULL_RESPONSE_QUEUE_NAME.value(),
                new GeneratePullResponseConsumer( receiver ) );
    }

}
