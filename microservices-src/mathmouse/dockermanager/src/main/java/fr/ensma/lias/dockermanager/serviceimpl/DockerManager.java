package fr.ensma.lias.dockermanager.serviceimpl;

import org.apache.log4j.BasicConfigurator;

import fr.ensma.lias.dockermanager.rabbitmq.DecrementeServiceRequest;
import fr.ensma.lias.dockermanager.rabbitmq.IncrementeServiceRequest;
import fr.ensma.lias.dockermanager.rabbitmq.InstancesOfServiceRequest;
import fr.ensma.lias.dockermanager.rabbitmq.ScaleServiceRequest;
import fr.ensma.lias.javarabbitmqapi.RabbitMQBasicReceiver;
import fr.ensma.lias.javarabbitmqapi.RabbitMQBasicSender;
import fr.ensma.lias.javarabbitmqapi.enumerations.EQueueName;

public class DockerManager {

    public static void main( String[] args ) {
        BasicConfigurator.configure();
        Actions.getInstance().startAllServices();

        String rabbitmqHost;

        if ( args.length > 0 ) {
            rabbitmqHost = args[0];
        } else {
            rabbitmqHost = "localhost";
        }

        RabbitMQBasicSender sender = null;
        RabbitMQBasicReceiver receiver = null;
        sender = new RabbitMQBasicSender( rabbitmqHost, "dockermanagersender", "dockermanagersender" );
        receiver = new RabbitMQBasicReceiver( rabbitmqHost, "dockermanagerreceiver", "dockermanagerreceiver" );
        Actions.getInstance().setSender( sender );
        receiver.declareConsumer( EQueueName.INCREMENTE_SERVICE_REQUEST.value(),
                new IncrementeServiceRequest( receiver ) );
        receiver.declareConsumer( EQueueName.DECREMENTE_SERVICE_REQUEST.value(),
                new DecrementeServiceRequest( receiver ) );
        receiver.declareConsumer( EQueueName.SCALE_SERVICE_REQUEST.value(), new ScaleServiceRequest( receiver ) );
        receiver.declareConsumer( EQueueName.INSTANCES_OF_SERVICE_REQUEST.value(),
                new InstancesOfServiceRequest( receiver ) );
    }
}
