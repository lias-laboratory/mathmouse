package fr.ensma.lias.dbcoreapi.models;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.postgresql.util.PSQLException;

import fr.ensma.lias.dbcoreapi.models.enumerations.EAttributeName;
import fr.ensma.lias.timeseriesreductorslib.timeseries.TimeSeriesDoubleDouble;
import fr.ensma.lias.timeseriesreductorslib.timeseries.interpolation.EInterpolationFunction;

/**
 * 
 * @author Cyrille Ponchateau (cyrille.ponchateau@ensma.fr)
 *
 */
public class Input extends EquationElement {
    private long                   id;
    private String                 name;
    private int                    deriv;
    private TimeSeriesDoubleDouble series;
    private String                 serialKey;

    public Input() {
        exist = false;
    }

    public Input( long id, String name, int deriv, String interpolationFunctionName ) {
        super();
        this.id = id;
        this.name = name;
        this.deriv = deriv;
        series = new TimeSeriesDoubleDouble();
        series.setInterpolationFunction( EInterpolationFunction.getInterpolationFunction( interpolationFunctionName ) );
    }

    public Input( long id, String name, int deriv, String interpolationFunctionName, boolean exist ) {
        super();
        this.id = id;
        this.name = name;
        this.deriv = deriv;
        series = new TimeSeriesDoubleDouble();
        series.setInterpolationFunction( EInterpolationFunction.getInterpolationFunction( interpolationFunctionName ) );
        this.exist = exist;
    }

    public Input( String name, int deriv, TimeSeriesDoubleDouble series ) {
        super();
        this.name = name;
        this.deriv = deriv;
        this.series = series;
    }

    public Input( ResultSet tuple ) {
        try {
            try {
                id = tuple.getLong( EAttributeName.ID_ATTRIBUTE.value() );
            } catch ( PSQLException e ) {
                id = tuple.getLong( EAttributeName.ID_INPUT_FUNCTION_ATTRIBUTE.value() );
            }
            name = tuple.getString( EAttributeName.NAME_ATTRIBUTE.value() ).replaceAll( "\\s+", "" );
            deriv = tuple.getInt( EAttributeName.DERIV_ATTRIBUTE.value() );
            series = new TimeSeriesDoubleDouble();
            series.setInterpolationFunction( EInterpolationFunction.getInterpolationFunction( tuple
                    .getString( EAttributeName.INTERPOLATION_FUNCTION_ATTRIBUTE.value() ).replaceAll( "\\s+", "" ) ) );
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

    public String getSerialKey() {
        return serialKey;
    }

    public void setSerialKey( long equationId ) {
        serialKey = id + "-" + equationId + "-" + name + "-" + deriv;
    }

    public int getDeriv() {
        return deriv;
    }

    public void setDeriv( int deriv ) {
        this.deriv = deriv;
    }

    public TimeSeriesDoubleDouble getSeries() {
        return series;
    }

    public void setSeries( TimeSeriesDoubleDouble series ) {
        this.series = series;
    }

    public EInterpolationFunction getInterpolationFunction() {
        return series.getInterpolationFunction().getInterpolationFunctionName();
    }

    public void setInterpolationFunction( EInterpolationFunction interpolationFunction ) {
        series.setInterpolationFunction( interpolationFunction );
    }

    @Override
    public String toString() {
        return "Input [id=" + id + ", name=" + name + ", deriv=" + deriv + ", serial key="
                + serialKey + ", interpolationFunction="
                + series.getInterpolationFunction().getInterpolationFunctionName().value() + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + deriv;
        result = prime * result + (int) ( id ^ ( id >>> 32 ) );
        result = prime * result + ( ( name == null ) ? 0 : name.hashCode() );
        result = prime * result + ( ( serialKey == null ) ? 0 : serialKey.hashCode() );
        result = prime * result + ( ( series == null ) ? 0 : series.hashCode() );
        return result;
    }

    @Override
    public boolean equals( Object obj ) {
        if ( this == obj )
            return true;
        if ( obj == null )
            return false;
        if ( getClass() != obj.getClass() )
            return false;
        Input other = (Input) obj;
        if ( deriv != other.deriv )
            if ( id != other.id )
                return false;
        if ( name == null ) {
            if ( other.name != null )
                return false;
        } else if ( !name.equals( other.name ) )
            return false;
        if ( serialKey == null ) {
            if ( other.serialKey != null )
                return false;
        } else if ( !serialKey.equals( other.serialKey ) )
            return false;
        if ( series == null ) {
            if ( other.series != null )
                return false;
        } else if ( !series.equals( other.series ) )
            return false;
        return true;
    }

}
