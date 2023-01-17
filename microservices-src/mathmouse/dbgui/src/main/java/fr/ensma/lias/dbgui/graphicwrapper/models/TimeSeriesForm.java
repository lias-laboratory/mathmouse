package fr.ensma.lias.dbgui.graphicwrapper.models;

import java.util.HashMap;
import java.util.Map;

import fr.ensma.lias.timeseriesreductorslib.timeseries.TimeSeriesDoubleDouble;

public class TimeSeriesForm {
    private String              filePath;
    private String              interpolationMethod;
    private Map<String, String> values;

    public TimeSeriesForm( String path, TimeSeriesDoubleDouble series ) {
        filePath = path;
        interpolationMethod = series.getInterpolationFunction().getInterpolationFunctionName().value();
        values = new HashMap<String, String>();
        for ( Double key : series.keySet() ) {
            values.put( String.valueOf( key ), String.valueOf( series.get( key ) ) );
        }
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath( String filePath ) {
        this.filePath = filePath;
    }

    public String getInterpolationMethod() {
        return interpolationMethod;
    }

    public void setInterpolationMethod( String interpolationMethod ) {
        this.interpolationMethod = interpolationMethod;
    }

    public Map<String, String> getValues() {
        return values;
    }

    public void setValues( Map<String, String> values ) {
        this.values = values;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( filePath == null ) ? 0 : filePath.hashCode() );
        result = prime * result + ( ( interpolationMethod == null ) ? 0 : interpolationMethod.hashCode() );
        result = prime * result + ( ( values == null ) ? 0 : values.hashCode() );
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
        TimeSeriesForm other = (TimeSeriesForm) obj;
        if ( filePath == null ) {
            if ( other.filePath != null )
                return false;
        } else if ( !filePath.equals( other.filePath ) )
            return false;
        if ( interpolationMethod == null ) {
            if ( other.interpolationMethod != null )
                return false;
        } else if ( !interpolationMethod.equals( other.interpolationMethod ) )
            return false;
        if ( values == null ) {
            if ( other.values != null )
                return false;
        } else if ( !values.equals( other.values ) )
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "TimeSeriesForm [filePath=" + filePath + ", interpolationMethod=" + interpolationMethod + ", values="
                + values + "]";
    }

}
