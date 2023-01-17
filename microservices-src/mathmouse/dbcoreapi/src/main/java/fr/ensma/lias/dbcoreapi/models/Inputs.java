package fr.ensma.lias.dbcoreapi.models;

import java.sql.ResultSet;
import java.sql.SQLException;

import fr.ensma.lias.dbcoreapi.models.enumerations.EAttributeName;

/**
 * 
 * @author Cyrille Ponchateau (cyrille.ponchateau@ensma.fr)
 *
 */
public class Inputs extends EquationElements<Long, Input> {
    private boolean allExist;

    public Inputs() {
        allExist = false;
    }

    public Inputs( ResultSet inputsTable ) {
        try {
            while ( inputsTable.next() )
                this.put( inputsTable.getLong( EAttributeName.ID_ATTRIBUTE.value() ), new Input( inputsTable ) );
            allExist = false;
        } catch ( SQLException e ) {
            e.printStackTrace();
        }
    }

    public void setSerialKeys( long equationId ) {
        for ( Long key : this.keySet() ) {
            this.get( key ).setSerialKey( equationId );
        }
    }

}
