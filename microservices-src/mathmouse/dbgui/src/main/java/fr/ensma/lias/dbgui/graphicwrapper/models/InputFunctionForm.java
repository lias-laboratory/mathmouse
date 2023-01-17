package fr.ensma.lias.dbgui.graphicwrapper.models;

import java.util.Map;
import java.util.TreeMap;

import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.FunctionKey;
import fr.ensma.lias.timeseriesreductorslib.timeseries.TimeSeriesDoubleDouble;

public class InputFunctionForm {
    private String              name;
    private String              derivationOrder;
    private String              interpolationMethod;
    private Map<String, String> values;

    public InputFunctionForm() {
    }

    public InputFunctionForm( FunctionKey key, TimeSeriesDoubleDouble timeSeriesDoubleDouble ) {
        name = key.getName();
        derivationOrder = String.valueOf( key.getDeriv() );
        interpolationMethod = timeSeriesDoubleDouble.getInterpolationFunction().getInterpolationFunctionName().value();
        values = new TreeMap<String, String>();
        for ( Double timeKey : timeSeriesDoubleDouble.keySet() ) {
            values.put( String.valueOf( timeKey ), String.valueOf( timeSeriesDoubleDouble.get( timeKey ) ) );
        }
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public String getDerivationOrder() {
        return derivationOrder;
    }

    public void setDerivationOrder( String derivattionOrder ) {
        this.derivationOrder = derivattionOrder;
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
        result = prime * result + ( ( derivationOrder == null ) ? 0 : derivationOrder.hashCode() );
        result = prime * result + ( ( interpolationMethod == null ) ? 0 : interpolationMethod.hashCode() );
        result = prime * result + ( ( name == null ) ? 0 : name.hashCode() );
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
        InputFunctionForm other = (InputFunctionForm) obj;
        if ( derivationOrder == null ) {
            if ( other.derivationOrder != null )
                return false;
        } else if ( !derivationOrder.equals( other.derivationOrder ) )
            return false;
        if ( interpolationMethod == null ) {
            if ( other.interpolationMethod != null )
                return false;
        } else if ( !interpolationMethod.equals( other.interpolationMethod ) )
            return false;
        if ( name == null ) {
            if ( other.name != null )
                return false;
        } else if ( !name.equals( other.name ) )
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
        return "InputFunctionForm [name=" + name + ", derivattionOrder=" + derivationOrder + ", interpolationMethod="
                + interpolationMethod + ", values=" + values + "]";
    }

}
