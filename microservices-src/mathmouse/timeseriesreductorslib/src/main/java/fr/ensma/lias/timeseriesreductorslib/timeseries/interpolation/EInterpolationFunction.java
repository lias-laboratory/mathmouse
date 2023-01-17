package fr.ensma.lias.timeseriesreductorslib.timeseries.interpolation;

public enum EInterpolationFunction {
    LINEAR_INTERPOLATION( "linear" ), STEP_INTERPOLATION( "none" ), EXPONENTIAL_INTERPOLATION(
            "exponentialSpline" ), CUBIC_INTERPOLATION( "cubicSpline" );

    private String name = "";

    EInterpolationFunction( String name ) {
        this.name = name;
    }

    public String value() {
        return this.name;
    }

    @Override
    public String toString() {
        return name;
    }

    public static EInterpolationFunction getInterpolationFunction( String role ) {
        EInterpolationFunction[] values = values();

        EInterpolationFunction result = null;

        for ( int i = 0; i < values.length; i++ ) {
            if ( values[i].value().equals( role ) )
                result = values[i];
        }

        return result;
    }
}
