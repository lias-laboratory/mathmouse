package fr.ensma.lias.timeseriesreductorslib.timeseries;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import fr.ensma.lias.timeseriesreductorslib.timeseries.interpolation.EInterpolationFunction;
import fr.ensma.lias.timeseriesreductorslib.timeseries.interpolation.IInterpolationFunction;

/**
 * An abstract class defining a TimeSeries<Key,Value> object as a TreeMap
 * <Key,Value>. The TreeMap structure is sensitive to the order relation of the
 * Key type.
 * 
 * @author cyrille ponchateau (cyrille.ponchateau@ensma.fr)
 *
 * @param <K>
 * @param <V>
 */
public abstract class AbstractTimeSeries<K, V> extends TreeMap<K, V> {
    protected static final String                        INTERPOLATION_PACKAGE = "fr.ensma.lias.timeseriesreductorslib.timeseries.interpolation.";
    protected static Map<EInterpolationFunction, String> CLASS_NAMES;

    static {
        CLASS_NAMES = new HashMap<EInterpolationFunction, String>();
        CLASS_NAMES.put( EInterpolationFunction.STEP_INTERPOLATION, "StepInterpolation" );
        CLASS_NAMES.put( EInterpolationFunction.LINEAR_INTERPOLATION, "LinearInterpolation" );
    }

    /**
     * defines the function to use, to interpolate the values of the series
     */
    protected IInterpolationFunction interpolationFunction;

    public abstract void printSeries();

    public AbstractTimeSeries() {
        super();
    }

    public AbstractTimeSeries( IInterpolationFunction interpolationFunction ) {
        super();
        this.interpolationFunction = interpolationFunction;
    }

    public AbstractTimeSeries( EInterpolationFunction interpolationFunctionName ) {
        super();
        setInterpolationFunction( interpolationFunctionName );
    }

    @Override
    public V put( K key, V value ) {
        V old = super.put( key, value );
        return old;
    }

    public IInterpolationFunction getInterpolationFunction() {
        return interpolationFunction;
    }

    public void setInterpolationFunction( IInterpolationFunction interpolationFunction ) {
        this.interpolationFunction = interpolationFunction;
    }

    public void setInterpolationFunction( EInterpolationFunction interpolationFunctionName ) {
        try {
            interpolationFunction = (IInterpolationFunction) Class
                    .forName( INTERPOLATION_PACKAGE + CLASS_NAMES.get( interpolationFunctionName ) ).newInstance();
        } catch ( InstantiationException | IllegalAccessException | ClassNotFoundException e ) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean equals( Object arg0 ) {
        if ( arg0 == null ) {
            return false;
        } else if ( arg0 instanceof AbstractTimeSeries<?, ?> ) {
            AbstractTimeSeries<K, V> series = (AbstractTimeSeries<K, V>) arg0;
            return series.interpolationFunction.getInterpolationFunctionName()
                    .equals( ( (AbstractTimeSeries) arg0 ).interpolationFunction.getInterpolationFunctionName() )
                    && super.equals( arg0 );
        }
        return false;
    }

    @Override
    public int hashCode() {
        int prime = 1163;
        int result = 1;
        result = prime * result + interpolationFunction.getInterpolationFunctionName().hashCode();

        String concatKeys = "";
        String concatValues = "";

        for ( K key : this.keySet() ) {
            concatKeys = concatKeys + key;
            concatValues = concatValues + this.get( key );
        }

        result = prime * result + concatKeys.hashCode();
        result = prime * result + concatValues.hashCode();

        return result;
    }
}
