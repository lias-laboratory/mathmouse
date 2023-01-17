package fr.ensma.lias.timeseriesreductorslib.reductors.segmentors.plr.regression;

import static org.junit.Assert.assertTrue;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.junit.BeforeClass;
import org.junit.Test;

import fr.ensma.lias.timeseriesreductorslib.timeseries.TimeSeriesDoubleDouble;

/**
 * 
 * @author cyrille ponchateau (cyrille.ponchateau@ensma.fr)
 *
 */
public class TestPLRRegressionSegment {
    private static final String           STATIC_LOGGER = "STATIC_LOGGER:TestPLRRegressionSegment";

    private static final double           MAX_ERROR     = 0.5;

    private static TimeSeriesDoubleDouble series1;
    private static double                 slope1        = 1.0;
    private static double                 yIntersect1   = 1.0;
    private static double[]               keySet1       = { 2.0, 3.0, 4.0, 5.0, 6.0, 7.0 };
    private static TimeSeriesDoubleDouble series2;
    private static double                 slope2        = -2.0;
    private static double                 yIntersect2   = 4.5;
    private static double[]               keySet2       = { 2.0, 2.1, 2.2, 2.3, 2.4, 2.5, 2.6, 2.7, 2.8, 2.9, 3.0 };

    private static PLRRegressionSegment   segment1;
    private static PLRRegressionSegment   segment2;

    @BeforeClass
    public static void initialize() {
        String log4j = "src/test/resources/log4j.xml";
        DOMConfigurator.configure( log4j );

        series1 = createSeries( slope1, yIntersect1, keySet1 );
        series2 = createSeries( slope2, yIntersect2, keySet2 );

    }

    @Test
    public void test() {
        // builds segments from series
        segment1 = new PLRRegressionSegment( series1 );
        Logger.getLogger( getClass() ).debug( "segment 1: " + segment1 );
        segment2 = new PLRRegressionSegment( series2 );
        Logger.getLogger( getClass() ).debug( "segment 2: " + segment2 );

        // check segment build gives expected results
        // left and right bound of the segments MUST be the first and last
        // values of the keySet tabs.
        // slope and yIntersect MUST be equal or close enough from the slope and
        // yIntersect variables formerly defined.
        assertTrue( Math.abs( segment1.getLeftBound() - keySet1[0] ) <= 0.0000000001 );
        assertTrue( Math.abs( segment1.getRightBound() - keySet1[keySet1.length - 1] ) <= 0.0000000001 );
        assertTrue( Math.abs( segment1.getSlope() - slope1 ) <= 0.01 );
        assertTrue( Math.abs( segment1.getyIntersect() - yIntersect1 ) <= 0.01 );

        assertTrue( Math.abs( segment2.getLeftBound() - keySet2[0] ) <= 0.0000000001 );
        assertTrue( Math.abs( segment2.getRightBound() - keySet2[keySet2.length - 1] ) <= 0.0000000001 );
        assertTrue( Math.abs( segment2.getSlope() - slope2 ) <= 0.01 );
        assertTrue( Math.abs( segment2.getyIntersect() - yIntersect2 ) <= 0.01 );

        // test compute value
        assertTrue( Math.abs( segment1.computeValue( 10.3 ) - 11.3 ) <= 0.00000001 );
        assertTrue( Math.abs( segment2.computeValue( 10.3 ) + 16.1 ) <= 0.00000001 );
    }

    /**
     * Creates a time series representing a straight line, given the slope and y
     * intersect values of the line.
     * 
     * @param slope
     * @param yIntersect
     * @param keySet,
     *            the set of real keys to put in the time series and compute
     *            values for.
     * 
     * @return a time series which pairs are (key, slope * key + yIntersect).
     */
    private static TimeSeriesDoubleDouble createSeries( double slope, double yIntersect, double[] keySet ) {
        TimeSeriesDoubleDouble result = new TimeSeriesDoubleDouble();
        for ( double key : keySet ) {
            result.put( key, slope * key + yIntersect );
        }
        Logger.getLogger( STATIC_LOGGER ).debug( "result: " + result );
        return result;
    }
}
