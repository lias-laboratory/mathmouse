package fr.ensma.lias.comparatormanager.serviceimpl;

import fr.ensma.lias.comparatormanager.rabbitmq.ComparisonPerformedConsumer;
import fr.ensma.lias.comparatormanager.rabbitmq.ComparisonResquestConsumer;
import fr.ensma.lias.comparatormanager.rabbitmq.GenerateResponseConsumer;
import fr.ensma.lias.comparatormanager.rabbitmq.JobOverConsumer;
import fr.ensma.lias.javarabbitmqapi.RabbitMQBasicReceiver;
import fr.ensma.lias.javarabbitmqapi.RabbitMQBasicSender;
import fr.ensma.lias.javarabbitmqapi.enumerations.EQueueName;

/**
 * Main class of the comparator manager service
 * 
 * @author Cyrille Ponchateau (cyrille.ponchateau@ensma.fr)
 *
 */
public class ComparatorManager {

    public static void main( String[] args ) {

        String rabbitmqHost;

        if ( args.length > 0 ) {
            rabbitmqHost = args[0];
        } else {
            rabbitmqHost = "localhost";
        }

        RabbitMQBasicSender sender;
        RabbitMQBasicReceiver receiver;
        sender = new RabbitMQBasicSender( rabbitmqHost, "dockermanagersender", "dockermanagersender" );
        receiver = new RabbitMQBasicReceiver( rabbitmqHost, "dockermanagerreceiver", "dockermanagerreceiver" );
        Actions.initialize( sender );
        receiver.declareConsumer( EQueueName.COMPARISON_PERFORMED, new ComparisonPerformedConsumer( receiver ) );
        receiver.declareConsumer( EQueueName.COMPARISON_REQUEST_QUEUE_NAME,
                new ComparisonResquestConsumer( receiver ) );
        receiver.declareConsumer( EQueueName.GENERATE_RESPONSE_QUEUE_NAME, new GenerateResponseConsumer( receiver ) );
        receiver.declareConsumer( EQueueName.JOB_OVER, new JobOverConsumer( receiver ) );
    }

}
