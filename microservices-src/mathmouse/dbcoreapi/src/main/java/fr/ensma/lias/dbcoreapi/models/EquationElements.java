package fr.ensma.lias.dbcoreapi.models;

import java.util.HashMap;

/**
 * 
 * @author Cyrille Ponchateau (cyrille.ponchateau@ensma.fr)
 *
 * @param <Long>
 * @param <K>
 */
public class EquationElements<Long, K extends EquationElement> extends HashMap<Long, K> {
    protected boolean allExist;

    public EquationElements() {
        // TODO Auto-generated constructor stub
    }

    public void updateExist() {
        boolean existAll = true;

        for ( Long id : keySet() ) {
            existAll = existAll && get( id ).exist();
        }
        allExist = existAll;
    }

    public boolean allExist() {
        return allExist;
    }

    public void setAllExist( boolean allExist ) {
        this.allExist = allExist;
    }

}
