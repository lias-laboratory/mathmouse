package fr.ensma.lias.dbcore.serviceimpl;

import java.sql.SQLException;

import org.slf4j.LoggerFactory;

import fr.ensma.lias.dbcore.rabbitmq.EquationsRequestConsumer;
import fr.ensma.lias.dbcore.rabbitmq.GeneratePullRequestConsumer;
import fr.ensma.lias.dbcore.rabbitmq.GetEquationsIDAndNamesRequest;
import fr.ensma.lias.dbcore.rabbitmq.PutEquationRequestConsumer;
import fr.ensma.lias.dbcore.rabbitmq.SQLQueryRequestConsumer;
import fr.ensma.lias.dbcoreapi.jdbc.JDBCClient;
import fr.ensma.lias.dockermanagerapi.enumerations.EServiceName;
import fr.ensma.lias.javarabbitmqapi.RabbitMQBasicReceiver;
import fr.ensma.lias.javarabbitmqapi.RabbitMQBasicSender;
import fr.ensma.lias.javarabbitmqapi.enumerations.EQueueName;

/**
 * Sets the service (db connection and rabbitmq connection)
 * 
 * @author cyrille ponchateau (ponchateau@ensma.fr)
 *
 */
public class Services {

    /**
     * 
     * @param args
     */
    public static void main( String[] args ) {

        String rabbitmqHost;
        String jdbcHost;
        int port;
        String databasename;
        String username;
        String password;

        if ( args.length > 0 ) {
            rabbitmqHost = args[0];
            jdbcHost = args[1];
            port = Integer.parseInt( args[2] );
            databasename = args[3];
            username = args[4];
            password = args[5];
        } else {
            rabbitmqHost = "localhost";
            jdbcHost = "localhost";
            port = 5432;
            databasename = "db_mmw";
            username = "db_admin";
            password = "password";
        }

        JDBCClient jdbc = null;
        RabbitMQBasicSender sender = null;
        RabbitMQBasicReceiver receiver = null;
        try {
            jdbc = new JDBCClient( "postgresql", jdbcHost, port, databasename, username, password );
            sender = new RabbitMQBasicSender( rabbitmqHost, "dbcoresender", "dbcoresender" );
            receiver = new RabbitMQBasicReceiver( rabbitmqHost, "dbcorereceiver", "dbcorereceiver" );
            Actions.initialize( jdbc.getConnection(), sender );
            receiver.declareConsumer( EQueueName.GET_EQUATIONS_REQUEST_QUEUE_NAME.value(),
                    new EquationsRequestConsumer( receiver ) );
            receiver.declareConsumer( EQueueName.PUT_EQUATION_REQUEST_QUEUE_NAME.value(),
                    new PutEquationRequestConsumer( receiver ) );
            receiver.declareConsumer( EQueueName.SQL_QUERY_REQUEST_QUEUE_NAME.value(),
                    new SQLQueryRequestConsumer( receiver ) );
            receiver.declareConsumer( EQueueName.GENERATE_PULL_REQUEST_QUEUE_NAME.value(),
                    new GeneratePullRequestConsumer( receiver ) );
            receiver.declareConsumer( EQueueName.GET_EQUATIONS_ID_AND_NAMES_REQUEST_QUEUE_NAME.value(),
                    new GetEquationsIDAndNamesRequest( receiver ) );
        } catch ( SQLException | ClassNotFoundException e ) {
            e.printStackTrace();
            try {
                if ( jdbc != null )
                    jdbc.getConnection().close();
                LoggerFactory.getLogger( EServiceName.DB_CORE.value() ).debug( "connection with database closed." );
            } catch ( SQLException e1 ) {
                e1.printStackTrace();
            }
        }

    }

}
