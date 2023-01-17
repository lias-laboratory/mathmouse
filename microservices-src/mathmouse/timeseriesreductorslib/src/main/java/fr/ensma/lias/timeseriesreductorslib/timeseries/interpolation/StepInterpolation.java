package fr.ensma.lias.timeseriesreductorslib.timeseries.interpolation;

import fr.ensma.lias.timeseriesreductorslib.timeseries.TimeSeriesDoubleDouble;

public class StepInterpolation implements IInterpolationFunction {

    public StepInterpolation() {
    }

    @Override
    public double interpolate( TimeSeriesDoubleDouble series, Double t1, Double t2, Double t ) {
        return series.get( t1 );
    }

    @Override
    public EInterpolationFunction getInterpolationFunctionName() {
        return EInterpolationFunction.STEP_INTERPOLATION;
    }

}
