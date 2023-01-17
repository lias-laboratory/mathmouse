package fr.ensma.lias.comparatormanager.serviceimpl.models;

import org.apache.log4j.BasicConfigurator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import fr.ensma.lias.timeseriesreductorslib.timeseries.TimeSeriesDoubleDouble;

public class TestTimeSeriesManager {
    private TimeSeriesDoubleDouble   series1;
    private TimeSeriesDoubleDouble   series2;

    private static TimeSeriesManager tsManager;

    @Before
    public void initialize() {
        BasicConfigurator.configure();

        series1 = new TimeSeriesDoubleDouble();
        series2 = new TimeSeriesDoubleDouble();

        tsManager = TimeSeriesManager.getInstance();
        tsManager.put( 1, series1 );
        tsManager.put( 2, series2 );
        tsManager.setExpectedComparisons( 1, 1 );
        tsManager.setExpectedComparisons( 2, 1 );
    }

    @Test
    public void test() {
        Assert.assertNotNull( tsManager.getSeriesByHash( 1 ) );
        Assert.assertNotNull( tsManager.getSeriesByHash( 2 ) );
        tsManager.newComparisonPerformed( 1 );
        Assert.assertNull( tsManager.getSeriesByHash( 1 ) );
        Assert.assertNotNull( tsManager.getSeriesByHash( 2 ) );
    }

}
