package fr.ensma.lias.comparatormanager.serviceimpl.models;

import org.apache.log4j.BasicConfigurator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import fr.ensma.lias.timeseriesreductorslib.timeseries.TimeSeriesDoubleDouble;

public class TestComparisonContextData {
    private TimeSeriesDoubleDouble series;

    private ComparisonContextData  contextData;

    @Before
    public void initialize() {
        BasicConfigurator.configure();

        contextData = new ComparisonContextData( series );
        contextData.setExpectedComparisons( 2 );
    }

    @Test
    public void test() {
        // since both the expected number value and performed comparison value
        // are purposely not accessible from the outside, this test will be a
        // behavior test, more than a unit test
        // The scenario is : expected is set to two (see initialization)
        // therefore, performed comparisons is supposed to be automatically
        // initialized to zero, meaning that if it is incremented twice, the
        // method operationOver of the ComparisonContextData class should return
        // true
        // Initially the method should return false
        Assert.assertFalse( contextData.operationOver() );
        // first increment, operationOver should still return false
        contextData.incremente();
        Assert.assertFalse( contextData.operationOver() );
        // second increment, operationOver should return true
        contextData.incremente();
        Assert.assertTrue( contextData.operationOver() );
        // third, operationOver should go back to false
        contextData.incremente();
        Assert.assertFalse( contextData.operationOver() );
    }

}
