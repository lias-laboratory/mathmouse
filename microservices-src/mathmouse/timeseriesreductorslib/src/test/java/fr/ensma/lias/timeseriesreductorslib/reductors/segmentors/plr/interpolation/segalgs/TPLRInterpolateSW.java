package fr.ensma.lias.timeseriesreductorslib.reductors.segmentors.plr.interpolation.segalgs;

import static org.junit.Assert.assertTrue;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import fr.ensma.lias.timeseriesreductorslib.reductors.segmentors.plr.interpolation.PLRInterpolateSegment;
import fr.ensma.lias.timeseriesreductorslib.reductors.segmentors.plr.interpolation.PLRInterpolateSegments;
import fr.ensma.lias.timeseriesreductorslib.timeseries.TimeSeriesDoubleDouble;

/**
 * 
 * @author cyrille ponchateau (cyrille.ponchateau@ensma.fr)
 *
 */
public class TPLRInterpolateSW {
    private static final String                FILE_NAME      = "src/test/resources/timeseries-sources/testPLRSW.csv";
    private static final double                MAX_ERROR      = 0.5;

    private static TimeSeriesDoubleDouble      series;
    private static PLRInterpolateSegments      segments;
    private static PLRInterpolateSegments      expectedSegments;

    private static PLRInterpolateSW PLRSWseries;

    private static final String                SERIES_1_PATH  = "src/test/resources/timeseries-sources/Data1.txt";
    private static final String                SERIES_2_PATH  = "src/test/resources/timeseries-sources/Data2.txt";
    private static final String                SERIES_3_PATH  = "src/test/resources/timeseries-sources/Data3.txt";

    private static final int                   SERIES_1_INDEX = 1;
    private static final int                   SERIES_2_INDEX = 1;
    private static final int                   SERIES_3_INDEX = 1;

    private static final String                SEPARATOR      = "   ";
    private static final String                DELIMITER      = "";

    private static TimeSeriesDoubleDouble      rawSeries1     = null;
    private static TimeSeriesDoubleDouble      rawSeries2     = null;
    private static TimeSeriesDoubleDouble      rawSeries3     = null;

    private static PLRInterpolateSW segmentsSW1;
    private static PLRInterpolateSW segmentsSW2;
    private static PLRInterpolateSW segmentsSW3;

    @BeforeClass
    public static void staticInitialize() {
        series = new TimeSeriesDoubleDouble( FILE_NAME, "   ", "" );
        expectedSegments = new PLRInterpolateSegments();
        expectedSegments.put( new Double( 0.0 ),
                new PLRInterpolateSegment( new Double( 0.0 ), new Double( 3.0 ), new Double( -1.0 ),
                        new Double( 2.0 ) ) );
        expectedSegments.put( new Double( 4.0 ),
                new PLRInterpolateSegment( new Double( 4.0 ), new Double( 9.0 ), new Double( 0.0 ),
                        new Double( 4.9 ) ) );
        expectedSegments.put( new Double( 10.0 ),
                new PLRInterpolateSegment( new Double( 10.0 ), new Double( 11.0 ), new Double( 3.0 ),
                        new Double( 2.0 ) ) );
        Logger.getLogger( "STATIC_LOGGER - TestPLRSlidingWindow" ).debug( expectedSegments );
        PLRSWseries = new PLRInterpolateSW( series );

    }

    @Before
    public void initialize() {
        rawSeries1 = new TimeSeriesDoubleDouble( SERIES_1_PATH, SEPARATOR, DELIMITER, SERIES_1_INDEX );
        Logger.getLogger( getClass() ).debug( "raw series 1:" + rawSeries1 );
        rawSeries2 = new TimeSeriesDoubleDouble( SERIES_2_PATH, SEPARATOR, DELIMITER, SERIES_2_INDEX );
        Logger.getLogger( getClass() ).debug( "raw series 2:" + rawSeries2 );
        rawSeries3 = new TimeSeriesDoubleDouble( SERIES_3_PATH, SEPARATOR, DELIMITER, SERIES_3_INDEX );
        Logger.getLogger( getClass() ).debug( "raw series 3:" + rawSeries3 );

        segmentsSW1 = new PLRInterpolateSW( rawSeries1 );
        segmentsSW1.process( MAX_ERROR );
        Logger.getLogger( getClass() ).debug( "segments 1:" + segmentsSW1.getInterpolateSegments() );
        segmentsSW2 = new PLRInterpolateSW( rawSeries2 );
        segmentsSW2.process( MAX_ERROR );
        Logger.getLogger( getClass() ).debug( "segments 2:" + segmentsSW2.getInterpolateSegments() );
        segmentsSW3 = new PLRInterpolateSW( rawSeries3 );
        segmentsSW3.process( MAX_ERROR );
        Logger.getLogger( getClass() ).debug( "segments 3:" + segmentsSW3.getInterpolateSegments() );
    }

    @Test
    public void dimensionCheck() {
        Logger.getLogger( getClass() ).debug( "dim raw series 1: " + rawSeries1.size() );
        assertTrue( rawSeries1.size() == 1000 );
        Logger.getLogger( getClass() ).debug( "dim raw series 2: " + rawSeries2.size() );
        // assertTrue( rawSeries2.size() == 1001 );
        Logger.getLogger( getClass() ).debug( "dim raw series 3: " + rawSeries3.size() );
        assertTrue( rawSeries3.size() == 2000 );

    }

    @Test
    public void test() {
        PLRSWseries.process( MAX_ERROR );
        segments = PLRSWseries.getInterpolateSegments();
        Logger.getLogger( "STATIC_LOGGER - TestPLRSlidingWindow" ).debug( segments );
        assertTrue( segments.equals( expectedSegments ) );
    }

}
