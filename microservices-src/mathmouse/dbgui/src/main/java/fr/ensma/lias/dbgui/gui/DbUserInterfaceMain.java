package fr.ensma.lias.dbgui.gui;

import fr.ensma.lias.dbgui.graphicwrapper.observers.DifferentialEquationListObserver;
import fr.ensma.lias.dbgui.graphicwrapper.observers.EquationIDDataListObserver;
import fr.ensma.lias.dbgui.graphicwrapper.observers.JobResultObserver;
import fr.ensma.lias.dbgui.kernel.models.JobCreator;
import fr.ensma.lias.dbgui.kernel.models.StartedJobManager;
import fr.ensma.lias.dbgui.rabbitmq.ComparisonResponseConsumer;
import fr.ensma.lias.dbgui.rabbitmq.EquationsIDAndNamesResponseConsumer;
import fr.ensma.lias.dbgui.rabbitmq.GetEquationsResponseConsumer;
import fr.ensma.lias.dbgui.rabbitmq.InstancesOfServiceUpdateConsumer;
import fr.ensma.lias.dbgui.rabbitmq.ProgressConsumer;
import fr.ensma.lias.dockermanagerapi.enumerations.EServiceName;
import fr.ensma.lias.javarabbitmqapi.RabbitMQBasicReceiver;
import fr.ensma.lias.javarabbitmqapi.RabbitMQBasicSender;
import fr.ensma.lias.javarabbitmqapi.enumerations.EQueueName;

public class DbUserInterfaceMain {
    private static RabbitMQBasicSender sender            = null;
    private static JobCreator          jobCreator        = null;
    private static StartedJobManager   startedJobManager = null;
    public static long                 start;
    public static long                 end;

    public static void main( String[] args ) {

        String rabbitmqHost;

        if ( args.length > 0 ) {
            rabbitmqHost = args[0];
        } else {
            rabbitmqHost = "localhost";
        }

        jobCreator = new JobCreator();
        startedJobManager = new StartedJobManager();
        sender = new RabbitMQBasicSender( rabbitmqHost, "dbguisender", "dbguisender" );
        RabbitMQBasicReceiver receiver = new RabbitMQBasicReceiver( rabbitmqHost, "dbguireceiver", "dbguireceiver" );
        EquationsIDAndNamesResponseConsumer equationsIDAndNamesConsumer = new EquationsIDAndNamesResponseConsumer(
                receiver );
        receiver.declareConsumer( EQueueName.GET_EQUATIONS_ID_AND_NAMES_RESPONSE_QUEUE_NAME,
                equationsIDAndNamesConsumer );
        GetEquationsResponseConsumer getEquationsResponseConsumer = new GetEquationsResponseConsumer( receiver );
        receiver.declareConsumer( EQueueName.GET_EQUATIONS_RESPONSE_QUEUE_NAME, getEquationsResponseConsumer );
        ComparisonResponseConsumer comparisonResponseConsumer = new ComparisonResponseConsumer( receiver );
        receiver.declareConsumer( EQueueName.COMPARISON_RESPONSE_QUEUE_NAME, comparisonResponseConsumer );
        receiver.declareConsumer( EQueueName.INSTANCES_OF_SERVICE_UPDATE,
                new InstancesOfServiceUpdateConsumer( receiver ) );
        receiver.declareConsumer( EQueueName.PROGRESS, new ProgressConsumer( receiver ) );
        MainWindow window = new MainWindow();
        equationsIDAndNamesConsumer
                .addObserver( new EquationIDDataListObserver( window.getEquationsPane().getEquationsListView() ) );
        getEquationsResponseConsumer.addObserver(
                new DifferentialEquationListObserver( window.getEquationsPane().getEquationTabbedView() ) );
        comparisonResponseConsumer.addObserver( new JobResultObserver() );
        sender.publish( EQueueName.GET_EQUATIONS_ID_AND_NAMES_REQUEST_QUEUE_NAME );
        sender.publish( EQueueName.INSTANCES_OF_SERVICE_REQUEST, EServiceName.COMPARATOR.value() );
        sender.publish( EQueueName.INSTANCES_OF_SERVICE_REQUEST, EServiceName.GENERATOR.value() );
        jobCreator.addObserver( window.getCreateJobsPane().getCreateJobsTableView() );
        startedJobManager.addObserver( window.getJobsManagerPane().getJobsManagerView() );
    }

    public static RabbitMQBasicSender getSender() throws Exception {
        if ( sender == null )
            throw new Exception( "Sender is not initialized." );
        return sender;
    }

    public static JobCreator getJobCreator() {
        return jobCreator;
    }

    public static StartedJobManager getStartedJobManager() {
        return startedJobManager;
    }

}
