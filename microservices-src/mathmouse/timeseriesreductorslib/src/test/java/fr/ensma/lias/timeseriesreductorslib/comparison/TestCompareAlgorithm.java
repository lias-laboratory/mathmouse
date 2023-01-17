package fr.ensma.lias.timeseriesreductorslib.comparison;

import static org.junit.Assert.assertTrue;

import org.apache.log4j.xml.DOMConfigurator;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import fr.ensma.lias.timeseriesreductorslib.timeseries.TimeSeriesDoubleDouble;

public class TestCompareAlgorithm {
    private static TimeSeriesDoubleDouble modelSeries;
    private static TimeSeriesDoubleDouble rawSeries;

    @BeforeClass
    public static void initialize() {
        String log4j = "src/test/resources/log4j.xml";
        DOMConfigurator.configure( log4j );
    }

    @Before
    public void initializeTest() {
        modelSeries = new TimeSeriesDoubleDouble( "src/test/resources/timeseries/Data1.txt", "   ", "", 0 );
        rawSeries = new TimeSeriesDoubleDouble( "src/test/resources/timeseries/Data1.txt", "   ", "", 1 );
    }

    @Test
    public void testStatistics() {

        try {
            CompareAlgorithm.statistics( modelSeries, rawSeries );
            CompareAlgorithm.printResults();
            assertTrue( CompareAlgorithm.notify( 0.5, 0.5, 0.5, 0.5 ).equals( ENotification.MODEL_REJECTED ) );
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

}
