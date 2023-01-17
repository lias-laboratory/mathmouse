package fr.ensma.lias.dbcoreapi.interfaces;

/**
 * 
 * Operations contract for a dbms service
 * 
 * @author cyrille ponchateau + (ponchateau@ensma.fr)
 *
 */
public interface DBService {

    /**
     * 
     * @param id
     */
    void sendEquations( Long id );

    /**
     * 
     * @param xmlString
     */
    void putEquation( String xmlString );

    /**
     * 
     * @param query
     */
    void executeSQLQuery( String query );

    /**
     * 
     * @param message
     */
    void doPull( String message );

    /**
     * 
     * @param message
     */
    void sendLogMessage( String message );

    /**
     * 
     * @param body
     */
    void getAllEquationsID( String body );
}
