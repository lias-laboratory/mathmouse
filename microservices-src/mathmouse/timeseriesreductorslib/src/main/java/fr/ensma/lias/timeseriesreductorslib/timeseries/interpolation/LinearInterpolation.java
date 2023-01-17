package fr.ensma.lias.timeseriesreductorslib.timeseries.interpolation;

import fr.ensma.lias.timeseriesreductorslib.timeseries.TimeSeriesDoubleDouble;

public class LinearInterpolation implements IInterpolationFunction {

    public LinearInterpolation() {

    }

    @Override
    public double interpolate( TimeSeriesDoubleDouble series, Double t1, Double t2, Double t ) {
        double a = ( series.get( t2 ) - series.get( t1 ) )
                / ( t2 - t1 );
        double b = series.get( t1 ) - a * t1;
        double value = a * t + b;
        return value;
    }

    @Override
    public EInterpolationFunction getInterpolationFunctionName() {
        return EInterpolationFunction.LINEAR_INTERPOLATION;
    }

}
