package fr.ensma.lias.dbcoreapi.models;

import java.sql.ResultSet;
import java.sql.SQLException;

import fr.ensma.lias.dbcoreapi.models.enumerations.EAttributeName;
import fr.ensma.lias.dbcoreapi.models.enumerations.EMathObject;

/**
 * 
 * @author Cyrille Ponchateau (cyrille.ponchateau@ensma.fr)
 *
 */
public class Formula extends EquationElements<Long, Node> {
    private boolean allExist;

    public Formula() {
        allExist = false;
    }

    public Formula( ResultSet formulaTable ) {
        try {
            while ( formulaTable.next() ) {
                Node node = new Node( formulaTable.getLong( EAttributeName.ID_NODE_ATTRIBUTE.value() ),
                        EMathObject.fromValue( formulaTable.getString( EAttributeName.MATH_OBJECT_ATTRIBUTE.value() )
                                .replaceAll( "\\s+", "" ).toLowerCase() ),
                        formulaTable.getString( EAttributeName.NAME_ATTRIBUTE.value() ).replaceAll( "\\s+", "" ),
                        formulaTable.getInt( EAttributeName.DERIV_ATTRIBUTE.value() ),
                        formulaTable.getInt( EAttributeName.NODE_DEPTH_ATTRIBUTE.value() ),
                        formulaTable.getLong( EAttributeName.ID_PARENT_ATTRIBUTE.value() ),
                        formulaTable.getLong( EAttributeName.ID_LEFT_ATTRIBUTE.value() ),
                        formulaTable.getLong( EAttributeName.ID_RIGHT_ATTRIBUTE.value() ),
                        formulaTable.getLong( EAttributeName.ID_EQUATION_ATTRIBUTE.value() ) );
                this.put( node.getId(), node );
            }
            allExist = false;
        } catch ( SQLException | IllegalArgumentException e ) {
            e.printStackTrace();
        }
    }

}
