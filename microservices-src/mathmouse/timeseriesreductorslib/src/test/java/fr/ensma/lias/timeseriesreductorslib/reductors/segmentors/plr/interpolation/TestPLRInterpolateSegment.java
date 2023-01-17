package fr.ensma.lias.timeseriesreductorslib.reductors.segmentors.plr.interpolation;

import static org.junit.Assert.assertTrue;

import org.apache.log4j.xml.DOMConfigurator;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * 
 * @author cyrille ponchateau (cyrille.ponchateau@ensma.fr)
 *
 */
public class TestPLRInterpolateSegment {
    private static final double          LEFT_BOUND  = 14523.25;
    private static final double          RIGHT_BOUND = 1965463.57;
    private static final double          LEFT_VALUE  = 456.123;
    private static final double          RIGHT_VALUE = 542.87;

    private static PLRInterpolateSegment segment1;
    private static PLRInterpolateSegment segment2;

    @BeforeClass
    public static void initialize() {
        String log4j = "src/test/resources/log4j.xml";
        DOMConfigurator.configure( log4j );
        segment1 = new PLRInterpolateSegment( LEFT_BOUND, RIGHT_BOUND, LEFT_VALUE, RIGHT_VALUE );
        segment2 = new PLRInterpolateSegment( LEFT_BOUND, RIGHT_BOUND, LEFT_VALUE, RIGHT_VALUE );
    }

    @Test
    public void testEqual() {
        assertTrue( segment1.equals( segment2 ) );
    }

}
