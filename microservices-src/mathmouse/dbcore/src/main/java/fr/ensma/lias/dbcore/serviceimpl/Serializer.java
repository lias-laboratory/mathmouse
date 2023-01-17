package fr.ensma.lias.dbcore.serviceimpl;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.slf4j.LoggerFactory;

/**
 * 
 * This class is made to serialize the data contained in a result set object.
 * 
 * @author Cyrille Ponchateau (ponchateau@ensma.fr)
 *
 */
public class Serializer {
    private static final String SEPARATOR = ";";
    private static final String NEWLINE   = "\n";

    /**
     * Transforms the data contained in a result set object into a csv formatted string
     * 
     * @param result
     * @return
     */
    public static String serialize( ResultSet result ) {
        String text = new String();

        try {
            // extracts the names of the columns of the table contained in the
            // result set and writes into the string
            ResultSetMetaData resultMetaData = result.getMetaData();
            for ( int i = 1; i <= resultMetaData.getColumnCount(); i++ ) {
                text = text + resultMetaData.getColumnLabel( i )
                        + ( i == resultMetaData.getColumnCount() ? NEWLINE : SEPARATOR );
            }

            // reads the content to copy it in the string
            while ( result.next() ) {
                for ( int i = 1; i <= resultMetaData.getColumnCount(); i++ ) {
                    text = text + result.getObject( i )
                            + ( i == resultMetaData.getColumnCount() ? NEWLINE : SEPARATOR );
                    text = text.replaceAll( " ", "" );
                }
            }
        } catch ( SQLException e ) {
            e.printStackTrace();
        }
        LoggerFactory.getLogger( "static_logger" ).debug( text );
        return text;
    }

    public static String serialize( Long id, String name, String group ) {
        String text = new String();

        text = "id" + SEPARATOR + "name" + SEPARATOR + "group_name" + NEWLINE;
        text = text + id + SEPARATOR + name + SEPARATOR + group + NEWLINE;

        return text;
    }

}
