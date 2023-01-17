package fr.ensma.lias.dockermanager.serviceimpl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.yaml.snakeyaml.Yaml;

import fr.ensma.lias.dockermanager.serviceimpl.models.YamlConstructor;
import fr.ensma.lias.dockermanagerapi.DockerCommandsManager;
import fr.ensma.lias.dockermanagerapi.enumerations.EServiceName;
import fr.ensma.lias.javarabbitmqapi.RabbitMQBasicSender;
import fr.ensma.lias.javarabbitmqapi.enumerations.EQueueName;
import fr.ensma.lias.javarabbitmqapi.enumerations.ESpecialCharacter;

/**
 * Implements the actions available from the service.
 * 
 * @author cyrille ponchateau (cyrille.ponchateau@ensma.fr)
 *
 */
public class Actions implements DockerCommandsManager
{
    public static final int                MIN_SERVICE_INSTANCES = 1;
    public static final int                MAX_SERVICE_INSTANCES = 5;

    private static Actions                 actions;

    private RabbitMQBasicSender            sender;
    private HashMap<EServiceName, Integer> counts;

    public static Actions getInstance()
    {
        if ( actions == null )
            actions = new Actions();
        return actions;
    }

    private Actions()
    {
        counts = new HashMap<EServiceName, Integer>();
    }

    public void setSender( RabbitMQBasicSender sender )
    {
        this.sender = sender;
    }

    /**
     * Starts the services of the application, contained in the docker-compose file.
     */
    public void startAllServices()
    {
        try
        {
            String[] commands;
            String output;
            String[] lines;

            // reading the yaml file to anticipate the services-to-be
            Yaml yaml = new Yaml( new YamlConstructor() );
            FileReader is;
            is = new FileReader( new File( "docker-compose.yml" ) );
            Map<String, Object> obj = (Map<String, Object>) yaml.load( is );

            // starting the services
            commands = new String[] { "sudo", "docker-compose", "up", "-d" };
            output = executeCommand( commands );
            lines = output.split( "\n" );
            for ( String line : lines )
            {
                Logger.getLogger( getClass() ).debug( line );
            }

            Thread.sleep( 5000 );
            // setting database service
            commands = new String[] { "sudo", "docker", "exec", "-i", "mathmouse_db", "bash", "init-db.sh" };
            output = executeCommand( commands );
            lines = output.split( "\n" );
            for ( String line : lines )
            {
                Logger.getLogger( getClass() ).debug( line );
            }

            // once up, each service has one instance
            for ( String key : obj.keySet() )
            {
                counts.put( EServiceName.fromValue( key ), 1 );
                Logger.getLogger( getClass() ).debug( "service " + key + " is up and has "
                        + counts.get( EServiceName.fromValue( key ) ) + " running instances." );
            }

            Thread.sleep( 2000 );
            // setting rabbitmq
            commands = new String[] { "sudo", "docker", "exec", "-i", "rabbitmq", "bash", "init.sh" };
            output = executeCommand( commands );
            lines = output.split( "\n" );
            for ( String line : lines )
            {
                Logger.getLogger( getClass() ).debug( line );
            }

        } catch ( FileNotFoundException | InterruptedException e )
        {
            e.printStackTrace();
        }
    }

    public void incrementeInstanceService( String message )
    {
        incrementeInstanceService( EServiceName.fromValue( message ) );
    }

    @Override
    public void incrementeInstanceService( EServiceName serviceName )
    {
        if ( counts.containsKey( serviceName ) )
        {
            int newCount = counts.get( serviceName ) + 1;
            if ( newCount > MAX_SERVICE_INSTANCES )
            {
                newCount = MAX_SERVICE_INSTANCES;
            }
            scale( serviceName, newCount );
            sender.publish( EQueueName.INSTANCES_OF_SERVICE_UPDATE.value(),
                    serviceName.value() + ESpecialCharacter.SEPARATOR.value() + counts.get( serviceName ) );
        }
    }

    public void decrementeInstanceService( String message )
    {
        decrementeInstanceService( EServiceName.fromValue( message ) );
    }

    @Override
    public void decrementeInstanceService( EServiceName serviceName )
    {
        if ( counts.containsKey( serviceName ) )
        {
            int newCount = counts.get( serviceName ) - 1;
            if ( newCount < MIN_SERVICE_INSTANCES )
            {
                newCount = MIN_SERVICE_INSTANCES;
            }
            scale( serviceName, newCount );
            sender.publish( EQueueName.INSTANCES_OF_SERVICE_UPDATE.value(),
                    serviceName.value() + ESpecialCharacter.SEPARATOR.value() + counts.get( serviceName ) );
        }
    }

    public void scale( String message )
    {
        message = message.replace( "\r", "" );
        String[] params = message.split( ESpecialCharacter.SEPARATOR.value() );
        scale( EServiceName.fromValue( params[0] ), Integer.parseInt( params[1] ) );
    }

    /**
     * Scales a given service. The number of instances of a service is bounded by 1 and 5, if the
     * required number in parameter is lower than, the number of instances will get down to one and
     * if the required number is greater than 5, the number of instances will go up to five).
     * 
     * @param serviceName,
     *            name of the service to scale
     * @param numberOfInstances,
     *            number of instances to create
     */
    @Override
    public void scale( EServiceName serviceName, int numberOfInstances )
    {
        if ( numberOfInstances < MIN_SERVICE_INSTANCES )
            numberOfInstances = MIN_SERVICE_INSTANCES;
        else if ( numberOfInstances > MAX_SERVICE_INSTANCES )
            numberOfInstances = MAX_SERVICE_INSTANCES;
        String[] commands = { "docker-compose", "scale", serviceName.value() + "=" + numberOfInstances };
        String output = executeCommand( commands );

        counts.put( serviceName, numberOfInstances );
        sender.publish( EQueueName.INSTANCES_OF_SERVICE_UPDATE.value(),
                serviceName.value() + ESpecialCharacter.SEPARATOR.value() + counts.get( serviceName ) );
    }

    public void instancesOfService( String message )
    {
        instancesOfService( EServiceName.fromValue( message ) );
    }

    @Override
    public void instancesOfService( EServiceName serviceName )
    {
        if ( counts.containsKey( serviceName ) )
        {
            sender.publish( EQueueName.INSTANCES_OF_SERVICE_UPDATE.value(),
                    serviceName.value() + ESpecialCharacter.SEPARATOR.value() + counts.get( serviceName ) );
        } else
        {
            sender.publish( EQueueName.INSTANCES_OF_SERVICE_UPDATE.value(),
                    serviceName.value() + ESpecialCharacter.SEPARATOR.value() + 0 );
        }
    }

    public int instancesOfService2( EServiceName serviceName )
    {
        if ( counts.containsKey( serviceName ) )
        {
            return counts.get( serviceName );
        } else
        {
            return 0;
        }
    }

    private String executeCommand( String[] commands )
    {
        BufferedReader reader = null;
        String output = "";
        try
        {
            String command = "";
            for ( String item : commands )
            {
                command = command + " " + item;
            }
            Logger.getLogger( getClass() ).debug( "executing : " + command );
            ProcessBuilder pb = new ProcessBuilder( commands );
            pb.inheritIO();
            Process proc = pb.start();

            InputStream is = proc.getInputStream();

            reader = new BufferedReader( new InputStreamReader( is ) );

            String line;
            while ( ( line = reader.readLine() ) != null )
            {
                output = output + line + "\n";
            }

            proc.waitFor();
        } catch ( Exception e )
        {
            e.printStackTrace();
        } finally
        {
            if ( reader != null )
                try
                {
                    reader.close();
                } catch ( IOException e )
                {
                    e.printStackTrace();
                }
            return output;
        }
    }

}
