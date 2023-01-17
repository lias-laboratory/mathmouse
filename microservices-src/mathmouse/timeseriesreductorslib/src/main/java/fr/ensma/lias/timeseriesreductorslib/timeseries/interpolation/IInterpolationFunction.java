package fr.ensma.lias.timeseriesreductorslib.timeseries.interpolation;

import fr.ensma.lias.timeseriesreductorslib.timeseries.TimeSeriesDoubleDouble;

/**
 * Interface to implement a class that needs to make continuous approximation of
 * a time series
 * 
 * @author cyrille ponchateau (ponchateau@ensma.fr)
 *
 */
public interface IInterpolationFunction {

    /**
     * Returns the approximation of the a time series at time t, between t1 and
     * t2.
     * 
     * @param series
     * @param t1
     * @param t2
     * @param t
     * @return
     */
    double interpolate( TimeSeriesDoubleDouble series, Double t1, Double t2, Double t );

    /**
     * Returns the name of the interpolation function
     * 
     * @return
     */
    EInterpolationFunction getInterpolationFunctionName();

}
