package fr.ensma.lias.timeseriesreductorslib.comparison;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import fr.ensma.lias.timeseriesreductorslib.Experiment;
import fr.ensma.lias.timeseriesreductorslib.Memory;
import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.DifferentialEquation;
import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.numericalmethod.NumericalMethod;
import fr.ensma.lias.timeseriesreductorslib.timeseries.TimeSeriesDoubleDouble;

public class TestComparisonModelToSeries {
    public static final String            STATIC_LOGGER           = "STATIC_LOGGER:TestComparisonModelToSeries";

    // path to the equations
    public static final String            EQUATION_1_PATH         = "src/test/resources/equation/equation1_2.xml";
    public static final String            EQUATION_2_PATH         = "src/test/resources/equation/equation1_2.xml";
    public static final String            EQUATION_3_PATH         = "src/test/resources/equation/equation3.xml";

    private static final int              EQUATION_1_SYSTEM_INDEX = 0;
    private static final int              EQUATION_2_SYSTEM_INDEX = 1;
    private static final int              EQUATION_3_SYSTEM_INDEX = 0;

    // path to the raw series data
    private static final String           SERIES_1_PATH           = "src/test/resources/timeseries-sources/Data1.txt";
    private static final String           SERIES_2_PATH           = "src/test/resources/timeseries-sources/Data2.txt";
    private static final String           SERIES_3_PATH           = "src/test/resources/timeseries-sources/Data3.txt";

    private static final int              SERIES_1_INDEX          = 1;
    private static final int              SERIES_2_INDEX          = 1;
    private static final int              SERIES_3_INDEX          = 1;

    private static final String           SEPARATOR               = "   ";
    private static final String           DELIMITER               = "";

    private static DifferentialEquation   equation1               = null;
    private static DifferentialEquation   equation2               = null;
    private static DifferentialEquation   equation3               = null;

    private static NumericalMethod        numericalMethod1        = null;
    private static NumericalMethod        numericalMethod2        = null;
    private static NumericalMethod        numericalMethod3        = null;

    private static TimeSeriesDoubleDouble modelSeries1;
    private static TimeSeriesDoubleDouble modelSeries2;
    private static TimeSeriesDoubleDouble modelSeries3;

    private static TimeSeriesDoubleDouble rawSeries1              = null;
    private static TimeSeriesDoubleDouble rawSeries2              = null;
    private static TimeSeriesDoubleDouble rawSeries3              = null;

    private static long                   time;
    private static long                   intermediateTime;

    private static final long             N                       = 1;

    private static Experiment             experiement1;
    private static Experiment             experiement2;
    private static Experiment             experiement3;

    @BeforeClass
    public static void initialize() {
        String log4j = "resources/log4j.xml";
        DOMConfigurator.configure( log4j );

        experiement1 = new Experiment( "ODE1" );
        experiement2 = new Experiment( "ODE2" );
        experiement3 = new Experiment( "ODE3" );

        try {
            equation1 = new DifferentialEquation( EQUATION_1_PATH, EQUATION_1_SYSTEM_INDEX, true );
            Logger.getLogger( STATIC_LOGGER ).debug( "Equation 1: " + equation1 );

            equation2 = new DifferentialEquation( EQUATION_2_PATH, EQUATION_2_SYSTEM_INDEX, true );
            Logger.getLogger( STATIC_LOGGER ).debug( "Equation 2: " + equation2 );
            equation3 = new DifferentialEquation( EQUATION_3_PATH, EQUATION_3_SYSTEM_INDEX, true );
            Logger.getLogger( STATIC_LOGGER ).debug( "Equation 3: " + equation3 );

            numericalMethod1 = new NumericalMethod( equation1 );
            numericalMethod2 = new NumericalMethod( equation2 );
            numericalMethod3 = new NumericalMethod( equation3 );

            rawSeries1 = new TimeSeriesDoubleDouble( SERIES_1_PATH, SEPARATOR, DELIMITER, SERIES_1_INDEX );
            Logger.getLogger( STATIC_LOGGER ).debug( "raw series 1: " + rawSeries1 );
            experiement1.putOriginalMemorySize( (double) Memory.printSeriesSize( rawSeries1 ) );
            experiement1.putNumberOfElements( 0.0 );
            rawSeries2 = new TimeSeriesDoubleDouble( SERIES_2_PATH, SEPARATOR, DELIMITER, SERIES_2_INDEX );
            Logger.getLogger( STATIC_LOGGER ).debug( "raw series 2: " + rawSeries2 );
            experiement2.putOriginalMemorySize( (double) Memory.printSeriesSize( rawSeries2 ) );
            experiement2.putNumberOfElements( 0.0 );
            rawSeries3 = new TimeSeriesDoubleDouble( SERIES_3_PATH, SEPARATOR, DELIMITER, SERIES_3_INDEX );
            Logger.getLogger( STATIC_LOGGER ).debug( "raw series 3: " + rawSeries3 );
            experiement3.putOriginalMemorySize( (double) Memory.printSeriesSize( rawSeries3 ) );
            experiement3.putNumberOfElements( 0.0 );

            Logger.getLogger( STATIC_LOGGER ).debug( "modelSeries 1..." );
            time = 0;
            for ( int i = 0; i < N; i++ ) {
                intermediateTime = System.nanoTime();
                modelSeries1 = numericalMethod1.equationToTimeseriesRK4( rawSeries1.size() );
                intermediateTime = System.nanoTime() - intermediateTime;
                time = time + intermediateTime;
                if ( i != N - 1 )
                    modelSeries1.clear();
            }
            Logger.getLogger( STATIC_LOGGER ).debug( "model series 1: " + modelSeries1 );

            // Logger.getLogger( getClass() ).debug( "model series 1:" +
            // modelSeries1 );
            Logger.getLogger( STATIC_LOGGER ).debug( "Number of generated values: " + modelSeries1.size() );
            Logger.getLogger( STATIC_LOGGER )
                    .debug( "Time to generate: " + ( (double) time / (double) N ) / 1000000.0 + " ms" );
            experiement1.putTime( ( (double) time / (double) N ) / 1000000.0 );
            Logger.getLogger( STATIC_LOGGER )
                    .debug( "Time by value: "
                            + ( ( (double) time / (double) N ) / (double) modelSeries1.size() ) / 1000000.0 + " ms" );

            Logger.getLogger( STATIC_LOGGER ).debug( "modelSeries 2..." );
            time = 0;
            for ( int i = 0; i < N; i++ ) {
                intermediateTime = System.nanoTime();
                modelSeries2 = numericalMethod2.equationToTimeseriesRK4( rawSeries2.size() );
                intermediateTime = System.nanoTime() - intermediateTime;
                time = time + intermediateTime;
                if ( i != N - 1 )
                    modelSeries2.clear();
            }
            Logger.getLogger( STATIC_LOGGER ).debug( "model series 2: " + modelSeries2 );

            Logger.getLogger( STATIC_LOGGER ).debug( "Number of generated values: " + modelSeries2.size() );
            Logger.getLogger( STATIC_LOGGER )
                    .debug( "Time to generate: " + ( (double) time / (double) N ) / 1000000.0 + " ms" );
            experiement2.putTime( ( (double) time / (double) N ) / 1000000.0 );
            Logger.getLogger( STATIC_LOGGER )
                    .debug( "Time by value: "
                            + ( ( (double) time / (double) N ) / (double) modelSeries2.size() ) / 1000000.0 + " ms" );

            Logger.getLogger( STATIC_LOGGER ).debug( "modelSeries 3..." );
            time = 0;
            for ( int i = 0; i < N; i++ ) {
                intermediateTime = System.nanoTime();
                modelSeries3 = numericalMethod3.equationToTimeseriesRK4( rawSeries3.size() );
                intermediateTime = System.nanoTime() - intermediateTime;
                time = time + intermediateTime;
                if ( i != N - 1 )
                    modelSeries3.clear();
            }
            Logger.getLogger( STATIC_LOGGER ).debug( "model series 3: " + modelSeries3 );

            Logger.getLogger( STATIC_LOGGER ).debug( "Number of generated values: " + modelSeries3.size() );
            Logger.getLogger( STATIC_LOGGER )
                    .debug( "Time to generate: " + ( (double) time / (double) N ) / 1000000.0 + " ms" );
            experiement3.putTime( ( (double) time / (double) N ) / 1000000.0 );
            Logger.getLogger( STATIC_LOGGER )
                    .debug( "Time by value: "
                            + ( ( (double) time / (double) N ) / (double) modelSeries3.size() ) / 1000000.0 + " ms" );
        } catch ( Exception e ) {
            e.printStackTrace();
        }

    }

    @Before
    public void checkInitialize() {
        assertNotNull( equation1 );
        assertNotNull( equation2 );
        assertNotNull( equation3 );

        assertNotNull( numericalMethod1 );
        assertNotNull( numericalMethod2 );
        assertNotNull( numericalMethod3 );

        assertNotNull( rawSeries1 );
        assertNotNull( rawSeries2 );
        assertNotNull( rawSeries3 );
    }

    @Test
    public void dimensionCheck() {
        // Logger.getLogger( getClass() ).debug( "dim raw series 1: " +
        // rawSeries1.getDimension() );
        assertTrue( rawSeries1.size() == 1000 );
        // Logger.getLogger( getClass() ).debug( "dim raw series 2: " +
        // rawSeries2.getDimension() );
        assertTrue( rawSeries2.size() == 1000 );
        // Logger.getLogger( getClass() ).debug( "dim raw series 3: " +
        // rawSeries3.getDimension() );
        assertTrue( rawSeries3.size() == 2000 );

        assertTrue( rawSeries1.size() == modelSeries1.size() );
        assertTrue( rawSeries2.size() == modelSeries2.size() );
        assertTrue( rawSeries3.size() == modelSeries3.size() );
    }

    @Test
    public void test() {
        try {
            Logger.getLogger( getClass() ).debug( "comparaison 1:" );
            CompareAlgorithm.statistics( modelSeries1, rawSeries1 );
            CompareAlgorithm.printResults();
            experiement1.putAvg( CompareAlgorithm.getStatsMap().get( CompareAlgorithm.MEAN_DELTA ) );
            experiement1.putMaxAvg( CompareAlgorithm.getStatsMap().get( CompareAlgorithm.MEAN_DELTA_MAX ) );
            experiement1.putMinAvg( CompareAlgorithm.getStatsMap().get( CompareAlgorithm.MEAN_DELTA_MIN ) );
            experiement1
                    .putStandardDeviation( CompareAlgorithm.getStatsMap().get( CompareAlgorithm.STANDARD_DEVIATION ) );

            Logger.getLogger( getClass() ).debug( "comparaison 2:" );
            CompareAlgorithm.statistics( modelSeries2, rawSeries2 );
            CompareAlgorithm.printResults();
            experiement2.putAvg( CompareAlgorithm.getStatsMap().get( CompareAlgorithm.MEAN_DELTA ) );
            experiement2.putMaxAvg( CompareAlgorithm.getStatsMap().get( CompareAlgorithm.MEAN_DELTA_MAX ) );
            experiement2.putMinAvg( CompareAlgorithm.getStatsMap().get( CompareAlgorithm.MEAN_DELTA_MIN ) );
            experiement2
                    .putStandardDeviation( CompareAlgorithm.getStatsMap().get( CompareAlgorithm.STANDARD_DEVIATION ) );

            Logger.getLogger( getClass() ).debug( "comparaison 3:" );
            CompareAlgorithm.statistics( modelSeries3, rawSeries3 );
            CompareAlgorithm.printResults();
            experiement3.putAvg( CompareAlgorithm.getStatsMap().get( CompareAlgorithm.MEAN_DELTA ) );
            experiement3.putMaxAvg( CompareAlgorithm.getStatsMap().get( CompareAlgorithm.MEAN_DELTA_MAX ) );
            experiement3.putMinAvg( CompareAlgorithm.getStatsMap().get( CompareAlgorithm.MEAN_DELTA_MIN ) );
            experiement3
                    .putStandardDeviation( CompareAlgorithm.getStatsMap().get( CompareAlgorithm.STANDARD_DEVIATION ) );
        } catch ( Exception e ) {
            e.printStackTrace();
        }

        try {
            Logger.getLogger( getClass() ).debug( "equation 1 size" );
            experiement1.putMemorySize( (double) Memory.printDifferentialEquationSize( equation1 ) );

            Logger.getLogger( getClass() ).debug( "equation 2 size" );
            experiement2.putMemorySize( (double) Memory.printDifferentialEquationSize( equation2 ) );

            Logger.getLogger( getClass() ).debug( "equation 3 size" );
            experiement3.putMemorySize( (double) Memory.printDifferentialEquationSize( equation3 ) );
        } catch ( Exception e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @After
    public void writeResults() {
        experiement1.write();
        experiement2.write();
        experiement3.write();
    }

}
