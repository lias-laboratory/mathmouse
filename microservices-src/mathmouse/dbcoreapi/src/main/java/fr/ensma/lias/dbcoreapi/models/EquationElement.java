package fr.ensma.lias.dbcoreapi.models;

/**
 * 
 * 
 * @author Cyrille Ponchateau (cyrille.ponchateau@ensma.fr)
 *
 */
public abstract class EquationElement {
    protected boolean exist;

    public EquationElement() {
        exist = false;
    }

    public boolean exist() {
        return exist;
    }

    public void setExist( boolean exist ) {
        this.exist = exist;
    }

}
