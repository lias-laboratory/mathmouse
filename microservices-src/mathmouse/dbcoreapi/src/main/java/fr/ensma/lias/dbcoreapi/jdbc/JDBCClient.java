package fr.ensma.lias.dbcoreapi.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles the necessary attributes and operations to access a database with JDBC
 * 
 * @author cyrille ponchateau (ponchateau@ensma.fr)
 *
 */
public class JDBCClient {
    private Connection connection;
    private Logger     logger;

    /**
     * 
     * @param dbmsname
     *            ('postgres','mysql',...)
     * @param ip,
     *            IP address of the host of the DBMS
     * @param port,
     *            port of the host to connect to
     * @param database,
     *            name of the database to connect
     * @param username,
     *            access identifiers
     * @param password
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public JDBCClient( String dbmsname, String ip, int port, String database, String username, String password )
            throws SQLException, ClassNotFoundException {
        boolean connected = false;
        Class.forName( "org.postgresql.Driver" );
        logger = LoggerFactory.getLogger( getClass() );

        while ( !connected ) {
            try {
                Thread.sleep( 2000 );
                logger.debug( "attempting connection to URL: jdbc:" + dbmsname + "://" + ip + ":" + port
                        + "/" + database + " with username: " + username );
                connection = (Connection) DriverManager.getConnection(
                        "jdbc:" + dbmsname + "://" + ip + ":" + port + "/" + database, username, password );
                logger.debug( "attempting connection to URL: jdbc:" + dbmsname + "://" + ip + ":" + port
                        + "/" + database + " succeeded." );
                connected = true;
            } catch ( SQLException | InterruptedException e ) {
                e.printStackTrace();
                logger.debug( "connection to database failed." );
                connected = false;
            }
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
