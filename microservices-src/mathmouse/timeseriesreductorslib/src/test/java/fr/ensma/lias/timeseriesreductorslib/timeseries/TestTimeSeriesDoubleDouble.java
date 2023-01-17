package fr.ensma.lias.timeseriesreductorslib.timeseries;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import fr.ensma.lias.timeseriesreductorslib.timeseries.interpolation.EInterpolationFunction;

/**
 * 
 * @author cyrille ponchateau (cyrille.ponchateau@ensma.fr)
 *
 */
public class TestTimeSeriesDoubleDouble {
    private static final String    FILE_NAME = "src/test/resources/timeseries-sources/Data2.txt";
    private TimeSeriesDoubleDouble series;

    @BeforeClass
    public static void initialize() {
        String log4j = "src/test/resources/log4j.xml";
        DOMConfigurator.configure( log4j );
    }

    @Before
    public void constructorTest() {
        series = TimeSeriesDoubleDoubleIO.readFromFile( FILE_NAME );
        Logger.getLogger( getClass() ).debug( "series: " + series );
        assertTrue( series.size() == 1000 );
    }

    @Test
    public void testGet() {
        TimeSeriesDoubleDouble aux = series.get( series.higherKey( series.firstKey() ),
                series.lowerKey( series.lastKey() ) );
        assertTrue( aux.size() == 998 );
        assertTrue( aux.firstKey().equals( series.higherKey( series.firstKey() ) ) );
        assertTrue( aux.lastKey().equals( series.lowerKey( series.lastKey() ) ) );
        Logger.getLogger( getClass() ).debug( "aux: " + aux );
    }

    @Test
    public void testClone() {
        TimeSeriesDoubleDouble clone = series.clone();
        assertFalse( clone == series );
        assertTrue( clone.size() == series.size() );
        Logger.getLogger( getClass() ).debug( "clone: " + clone );
    }

    @Test
    public void testTimeSeriesInterpolationMethodNameConstructor() {
        TimeSeriesDoubleDouble series = new TimeSeriesDoubleDouble( EInterpolationFunction.STEP_INTERPOLATION );
        assertTrue( series.getInterpolationFunction().getInterpolationFunctionName()
                .equals( EInterpolationFunction.STEP_INTERPOLATION ) );
    }

}
