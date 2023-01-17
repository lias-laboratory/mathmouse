package fr.ensma.lias.dbcoreapi.models;

import java.sql.ResultSet;
import java.sql.SQLException;

import fr.ensma.lias.dbcoreapi.models.enumerations.EAttributeName;

/**
 * 
 * @author Cyrille Ponchateau (cyrille.ponchateau@ensma.fr)
 *
 */
public class InitialValues extends EquationElements<Long, InitialValue> {
    private boolean allExist;

    public InitialValues() {
        allExist = false;
    }

    public InitialValues( ResultSet initialValuesTable ) {
        try {
            while ( initialValuesTable.next() )
                this.put( initialValuesTable.getLong( EAttributeName.ID_ATTRIBUTE.value() ),
                        new InitialValue( initialValuesTable ) );
            allExist = false;
        } catch ( SQLException e ) {
            e.printStackTrace();
        }
    }

}
