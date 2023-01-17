package fr.ensma.lias.timeseriesreductorslib.comparison2;

import static org.junit.Assert.assertTrue;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fr.ensma.lias.timeseriesreductorslib.Experiment;
import fr.ensma.lias.timeseriesreductorslib.Memory;
import fr.ensma.lias.timeseriesreductorslib.comparison.CompareAlgorithm;
import fr.ensma.lias.timeseriesreductorslib.reductors.segmentors.plr.interpolation.segalgs.PLRInterpolateSW;
import fr.ensma.lias.timeseriesreductorslib.timeseries.TimeSeriesDoubleDouble;

public class TestComparisonSeriesToPLRInterpolateSW {
    public static final String                 STATIC_LOGGER  = "STATIC_LOGGER:TestComparisonSeriesToPLRInterpolateSW";

    private static final String                SERIES_1_PATH  = "src/test/resources/timeseries-sources/Data1.txt";
    private static final String                SERIES_2_PATH  = "src/test/resources/timeseries-sources/Data2.txt";
    private static final String                SERIES_3_PATH  = "src/test/resources/timeseries-sources/Data3.txt";

    private static final int                   SERIES_1_INDEX = 1;
    private static final int                   SERIES_2_INDEX = 1;
    private static final int                   SERIES_3_INDEX = 1;

    private static final String                SEPARATOR      = "   ";
    private static final String                DELIMITER      = "";

    private static final double                MAX_ERROR      = 0.5;

    private static TimeSeriesDoubleDouble      rawSeries1     = null;
    private static TimeSeriesDoubleDouble      rawSeries2     = null;
    private static TimeSeriesDoubleDouble      rawSeries3     = null;

    private static PLRInterpolateSW segmentsSW1;
    private static PLRInterpolateSW segmentsSW2;
    private static PLRInterpolateSW segmentsSW3;

    private static TimeSeriesDoubleDouble      series1;
    private static TimeSeriesDoubleDouble      series2;
    private static TimeSeriesDoubleDouble      series3;

    private static long                        time;
    private static long                        intermediateTime;

    private static final long                  N              = 1;

    private static Experiment                  experiement1;
    private static Experiment                  experiement2;
    private static Experiment                  experiement3;

    @Before
    public void intialize() {
        String log4j = "resources/log4j.xml";
        DOMConfigurator.configure( log4j );

        experiement1 = new Experiment( "PLRInterpolateSW1" );
        experiement2 = new Experiment( "PLRInterpolateSW2" );
        experiement3 = new Experiment( "PLRInterpolateSW3" );

        rawSeries1 = new TimeSeriesDoubleDouble( SERIES_1_PATH, SEPARATOR, DELIMITER, SERIES_1_INDEX );
        // Logger.getLogger( getClass() ).debug( "raw series 1:" + rawSeries1 );
        try {
            Logger.getLogger( getClass() ).debug( "series 1 size" );
            experiement1.putOriginalMemorySize( (double) Memory.printSeriesSize( SERIES_1_PATH, SERIES_1_INDEX ) );
        } catch ( Exception e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        rawSeries2 = new TimeSeriesDoubleDouble( SERIES_2_PATH, SEPARATOR, DELIMITER, SERIES_2_INDEX );
        // Logger.getLogger( getClass() ).debug( "raw series 2:" + rawSeries2 );
        try {
            Logger.getLogger( getClass() ).debug( "series 2 size" );
            experiement2.putOriginalMemorySize( (double) Memory.printSeriesSize( SERIES_2_PATH, SERIES_2_INDEX ) );
        } catch ( Exception e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        rawSeries3 = new TimeSeriesDoubleDouble( SERIES_3_PATH, SEPARATOR, DELIMITER, SERIES_3_INDEX );
        // Logger.getLogger( getClass() ).debug( "raw series 3:" + rawSeries3 );
        try {
            Logger.getLogger( getClass() ).debug( "series 3 size" );
            experiement3.putOriginalMemorySize( (double) Memory.printSeriesSize( SERIES_3_PATH, SERIES_3_INDEX ) );
        } catch ( Exception e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        segmentsSW1 = new PLRInterpolateSW( rawSeries1 );

        segmentsSW1.process( MAX_ERROR );

        Logger.getLogger( getClass() ).debug( "segments 1 size" );
        try {
            experiement1.putMemorySize( (double) Memory.printSegmentSize( segmentsSW1.getInterpolateSegments() ) );
            experiement1.putNumberOfElements( (double) segmentsSW1.getInterpolateSegments().size() );
        } catch ( Exception e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        segmentsSW2 = new PLRInterpolateSW( rawSeries2 );
        segmentsSW2.process( MAX_ERROR );
        // Logger.getLogger( getClass() ).debug( "segments 2:" +
        // segmentsSW2.getInterpolateSegments().size() );

        Logger.getLogger( getClass() ).debug( "segments 2 size" );
        try {
            experiement2.putMemorySize( (double) Memory.printSegmentSize( segmentsSW2.getInterpolateSegments() ) );
            experiement2.putNumberOfElements( (double) segmentsSW2.getInterpolateSegments().size() );
        } catch ( Exception e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        segmentsSW3 = new PLRInterpolateSW( rawSeries3 );
        segmentsSW3.process( MAX_ERROR );
        // Logger.getLogger( getClass() ).debug( "segments 3:" +
        // segmentsSW3.getInterpolateSegments().size() );

        Logger.getLogger( getClass() ).debug( "segments 3 size" );
        try {
            experiement3.putMemorySize( (double) Memory.printSegmentSize( segmentsSW3.getInterpolateSegments() ) );
            experiement3.putNumberOfElements( (double) segmentsSW3.getInterpolateSegments().size() );
        } catch ( Exception e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        series1 = new TimeSeriesDoubleDouble();
        time = 0;
        for ( int i = 0; i < N; i++ ) {
            intermediateTime = System.nanoTime();
            series1.putAll( segmentsSW1.getInterpolateSegments().computeValues( rawSeries1.keySet() ) );
            intermediateTime = System.nanoTime() - intermediateTime;
            time = time + intermediateTime;
            if ( i != N - 1 )
                series1.clear();
        }

        Logger.getLogger( getClass() ).debug( "segments 1:" + segmentsSW1.getInterpolateSegments().size() );

        Logger.getLogger( STATIC_LOGGER ).debug( "Series size: " + series1.size() );
        Logger.getLogger( STATIC_LOGGER )
                .debug( "Time to generate: " + ( (double) time / (double) N ) / 1000000.0 + " ms" );
        Logger.getLogger( getClass() ).debug( "series 1: " + series1 );
        experiement1.putTime( ( (double) time / (double) N ) / 1000000.0 );
        Logger.getLogger( STATIC_LOGGER )
                .debug( "Time by value: "
                        + ( ( (double) time / (double) N ) / (double) series1.size() ) / 1000000.0 + " ms" );

        series2 = new TimeSeriesDoubleDouble();
        time = 0;
        for ( int i = 0; i < N; i++ ) {
            intermediateTime = System.nanoTime();
            series2.putAll( segmentsSW2.getInterpolateSegments().computeValues( rawSeries2.keySet() ) );
            intermediateTime = System.nanoTime() - intermediateTime;
            time = time + intermediateTime;
            if ( i != N - 1 )
                series2.clear();
        }

        Logger.getLogger( getClass() ).debug( "segments 2:" + segmentsSW2.getInterpolateSegments().size() );

        Logger.getLogger( STATIC_LOGGER ).debug( "Series size: " + series2.size() );
        Logger.getLogger( STATIC_LOGGER )
                .debug( "Time to generate: " + ( (double) time / (double) N ) / 1000000.0 + " ms" );
        Logger.getLogger( getClass() ).debug( "series 2: " + series2 );
        experiement2.putTime( ( (double) time / (double) N ) / 1000000.0 );
        Logger.getLogger( STATIC_LOGGER )
                .debug( "Time by value: "
                        + ( ( (double) time / (double) N ) / (double) series2.size() ) / 1000000.0 + " ms" );

        series3 = new TimeSeriesDoubleDouble();
        time = 0;
        for ( int i = 0; i < N; i++ ) {
            intermediateTime = System.nanoTime();
            series3.putAll( segmentsSW3.getInterpolateSegments().computeValues( rawSeries3.keySet() ) );
            intermediateTime = System.nanoTime() - intermediateTime;
            time = time + intermediateTime;
            if ( i != N - 1 )
                series3.clear();
        }

        Logger.getLogger( getClass() ).debug( "segments 3:" + segmentsSW3.getInterpolateSegments().size() );

        Logger.getLogger( STATIC_LOGGER ).debug( "Series size: " + series3.size() );
        Logger.getLogger( STATIC_LOGGER )
                .debug( "Time to generate: " + ( (double) time / (double) N ) / 1000000.0 + " ms" );
        Logger.getLogger( getClass() ).debug( "series 3: " + series3 );
        experiement3.putTime( ( (double) time / (double) N ) / 1000000.0 );
        Logger.getLogger( STATIC_LOGGER )
                .debug( "Time by value: "
                        + ( ( (double) time / (double) N ) / (double) series3.size() ) / 1000000.0 + " ms" );
    }

    @Test
    public void dimensionCheck() {
        // Logger.getLogger( getClass() ).debug( "dim raw series 1: " +
        // rawSeries1.getDimension() );
        assertTrue( rawSeries1.size() == 1000 );
        assertTrue( series1.size() == 1000 );
        // Logger.getLogger( getClass() ).debug( "dim raw series 2: " +
        // rawSeries2.getDimension() );
        assertTrue( rawSeries2.size() == 1000 );
        assertTrue( series2.size() == 1000 );
        // Logger.getLogger( getClass() ).debug( "dim raw series 3: " +
        // rawSeries3.getDimension() );
        assertTrue( rawSeries3.size() == 2000 );
        assertTrue( series3.size() == 2000 );

    }

    @Test
    public void test() {
        try {
            Logger.getLogger( getClass() ).debug( "series1" );
            CompareAlgorithm.statistics( series1, rawSeries1 );
            CompareAlgorithm.printResults();
            experiement1.putAvg( CompareAlgorithm.getStatsMap().get( CompareAlgorithm.MEAN_DELTA ) );
            experiement1.putMaxAvg( CompareAlgorithm.getStatsMap().get( CompareAlgorithm.MEAN_DELTA_MAX ) );
            experiement1.putMinAvg( CompareAlgorithm.getStatsMap().get( CompareAlgorithm.MEAN_DELTA_MIN ) );
            experiement1
                    .putStandardDeviation( CompareAlgorithm.getStatsMap().get( CompareAlgorithm.STANDARD_DEVIATION ) );

            Logger.getLogger( getClass() ).debug( "series2" );
            CompareAlgorithm.statistics( series2, rawSeries2 );
            CompareAlgorithm.printResults();
            experiement2.putAvg( CompareAlgorithm.getStatsMap().get( CompareAlgorithm.MEAN_DELTA ) );
            experiement2.putMaxAvg( CompareAlgorithm.getStatsMap().get( CompareAlgorithm.MEAN_DELTA_MAX ) );
            experiement2.putMinAvg( CompareAlgorithm.getStatsMap().get( CompareAlgorithm.MEAN_DELTA_MIN ) );
            experiement2
                    .putStandardDeviation( CompareAlgorithm.getStatsMap().get( CompareAlgorithm.STANDARD_DEVIATION ) );

            Logger.getLogger( getClass() ).debug( "series3" );
            CompareAlgorithm.statistics( series3, rawSeries3 );
            CompareAlgorithm.printResults();
            experiement3.putAvg( CompareAlgorithm.getStatsMap().get( CompareAlgorithm.MEAN_DELTA ) );
            experiement3.putMaxAvg( CompareAlgorithm.getStatsMap().get( CompareAlgorithm.MEAN_DELTA_MAX ) );
            experiement3.putMinAvg( CompareAlgorithm.getStatsMap().get( CompareAlgorithm.MEAN_DELTA_MIN ) );
            experiement3
                    .putStandardDeviation( CompareAlgorithm.getStatsMap().get( CompareAlgorithm.STANDARD_DEVIATION ) );

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
