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
public class Variable extends EquationElement {
    private long   id;
    private String name;
    private Double value;

    public Variable() {
        exist = false;
    }

    public Variable( long id, String name, double value ) {
        this.id = id;
        this.name = name;
        this.value = value;
        exist = false;
    }

    public Variable( long id, String name, Double value, boolean exist ) {
        super();
        this.id = id;
        this.name = name;
        this.value = value;
        this.exist = exist;
    }

    public Variable( ResultSet tuple ) {
        try {
            try {
                id = tuple.getLong( EAttributeName.ID_VARIABLE_ATTRIBUTE.value() );
            } catch ( PSQLException e ) {
                id = tuple.getLong( EAttributeName.ID_ATTRIBUTE.value() );
            }
            name = tuple.getString( EAttributeName.NAME_ATTRIBUTE.value() ).replaceAll( "\\s+", "" );
            value = tuple.getDouble( EAttributeName.VARIABLE_VALUE_ATTRIBUTE.value() );
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

    public double getValue() {
        return value;
    }

    public void setValue( double value ) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "{var_id=" + id + ";name=" + name + ";value=" + value + "}";
    }

    @Override
    public boolean equals( Object obj ) {
        if ( obj == this )
            return true;
        if ( obj == null )
            return false;
        if ( obj instanceof Variable ) {
            Variable v = (Variable) obj;
            if ( !name.equals( v.name ) ) {
                return false;
            }
            if ( value != v.value ) {
                return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int prime = 73;
        int result = 1;
        result = prime * result + ( name == null ? 0 : name.hashCode() );
        result = prime * result + ( value == null ? 0 : value.hashCode() );
        return result;
    }

}
