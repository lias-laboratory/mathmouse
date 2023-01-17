package fr.ensma.lias.populator.service;

import org.apache.log4j.BasicConfigurator;

import fr.ensma.lias.javarabbitmqapi.RabbitMQBasicReceiver;
import fr.ensma.lias.javarabbitmqapi.RabbitMQBasicSender;
import fr.ensma.lias.javarabbitmqapi.enumerations.EQueueName;
import fr.ensma.lias.populator.rabbitmq.EquationsIDAndNamesResponseConsumer;

public class Populator
{

    public static void main( String[] args )
    {
        // configuration of log4j, the log library used throughout the
        // application
        BasicConfigurator.configure();

        String rabbitmqHost;

        if ( args.length > 0 )
        {
            // if the rabbitmq host name were given on command line, it is set
            // to the given argument value
            rabbitmqHost = args[0];
        } else
        {
            // otherwise, the rabbitmq host is considered to be localhost by
            // default
            rabbitmqHost = "localhost";
        }

        RabbitMQBasicSender sender = null;
        RabbitMQBasicReceiver receiver = null;
        // initializes the sender used by the application to send messages via
        // rabbitmq
        sender = new RabbitMQBasicSender( rabbitmqHost, "populatorsender", "populatorsender" );
        // initializes the receiver, that handles the listeners of rabbitmq
        // messages
        receiver = new RabbitMQBasicReceiver( rabbitmqHost, "populatorreceiver", "populatorreceiver" );
        // initializes the Actions class, that contains the different
        // functionalities available in the service
        Actions.initialize( sender );
        // declares the necessary listeners
        receiver.declareConsumer( EQueueName.GET_EQUATIONS_ID_AND_NAMES_RESPONSE_QUEUE_NAME.value(),
                new EquationsIDAndNamesResponseConsumer( receiver ) );

        Actions.getInstance().sendAllEquation();
    }

}
