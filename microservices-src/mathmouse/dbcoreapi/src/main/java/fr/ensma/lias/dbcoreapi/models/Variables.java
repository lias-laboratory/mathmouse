package fr.ensma.lias.dbcoreapi.models;

import java.sql.ResultSet;
import java.sql.SQLException;

import fr.ensma.lias.dbcoreapi.models.enumerations.EAttributeName;

/**
 * 
 * @author Cyrille Ponchateau (cyrille.ponchateau@ensma.fr)
 *
 */
public class Variables extends EquationElements<Long, Variable> {
    private boolean allExist;

    public Variables() {
        allExist = false;
    }

    public Variables( ResultSet variablesTable ) {
        try {
            while ( variablesTable.next() ) {
                this.put( variablesTable.getLong( EAttributeName.ID_ATTRIBUTE.value() ),
                        new Variable( variablesTable ) );
            }
            allExist = false;
        } catch ( SQLException e ) {
            e.printStackTrace();
        }
    }

}
