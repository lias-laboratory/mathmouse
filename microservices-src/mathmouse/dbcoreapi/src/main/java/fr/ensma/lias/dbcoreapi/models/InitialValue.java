package fr.ensma.lias.dbcoreapi.models;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.postgresql.util.PSQLException;

import fr.ensma.lias.dbcoreapi.models.enumerations.EAttributeName;

/**
 * 
 * @author Cyrille Ponchateau (cyrille.ponchateau@ensma.fr)
 *
 */
public class InitialValue extends EquationElement {
    private long    id;
    private String  name;
    private int     deriv;
    private Double  initialValue;
    private boolean exist;

    public InitialValue() {
        exist = false;
    }

    public InitialValue( long id, String name, int deriv, double initialValue ) {
        this.id = id;
        this.name = name;
        this.deriv = deriv;
        this.initialValue = initialValue;
        exist = false;
    }

    public InitialValue( long id, String name, int deriv, Double initialValue, boolean exist ) {
        super();
        this.id = id;
        this.name = name;
        this.deriv = deriv;
        this.initialValue = initialValue;
        this.exist = exist;
    }

    public InitialValue( ResultSet tuple ) {
        try {
            try {
                id = tuple.getLong( EAttributeName.ID_ATTRIBUTE.value() );
            } catch ( PSQLException e ) {
                id = tuple.getLong( EAttributeName.ID_INITIAL_ATTRIBUTE.value() );
            }
            name = tuple.getString( EAttributeName.NAME_ATTRIBUTE.value() ).replaceAll( "\\s+", "" );
            deriv = tuple.getInt( EAttributeName.DERIV_ATTRIBUTE.value() );
            initialValue = tuple.getDouble( EAttributeName.INITIAL_VALUE_ATTRIBUTE.value() );
            exist = false;
        } catch ( SQLException e ) {
            e.printStackTrace();
        }
    }

    public long getId() {
        return id;
    }

    public void setId( long id ) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public int getDeriv() {
        return deriv;
    }

    public void setDeriv( int deriv ) {
        this.deriv = deriv;
    }

    public double getInitialValue() {
        return initialValue;
    }

    public void setInitialValue( double initialValue ) {
        this.initialValue = initialValue;
    }

    @Override
    public String toString() {
        return "{id=" + id + ";name=" + name + ";deriv=" + deriv + ";initialValue=" + initialValue + "}";
    }

    @Override
    public boolean equals( Object obj ) {
        if ( obj == this )
            return true;
        if ( obj == null )
            return false;
        if ( obj instanceof InitialValue ) {
            InitialValue iv = (InitialValue) obj;
            if ( !name.equals( iv.name ) )
                return false;
            if ( deriv != iv.deriv )
                return false;
            if ( !initialValue.equals( initialValue ) )
                return false;
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int prime = 67;
        int result = 1;
        result = prime * result + ( name == null ? 0 : name.hashCode() );
        result = prime * result + deriv;
        result = prime * result + ( initialValue == null ? 0 : initialValue.hashCode() );
        return result;
    }
}
