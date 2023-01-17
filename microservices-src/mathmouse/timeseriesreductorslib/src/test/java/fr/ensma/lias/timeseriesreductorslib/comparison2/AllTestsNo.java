package fr.ensma.lias.timeseriesreductorslib.comparison2;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith( Suite.class )
@SuiteClasses( value = { TestComparisonModelToSeries.class, TestComparisonSeriesToPLRInterpolateBU.class,
        TestComparisonSeriesToPLRInterpolateSW.class, TestComparisonSeriesToPLRInterpolateTD.class,
        TestComparisonSeriesToPLRRegressionBU.class, TestComparisonSeriesToPLRInterpolateTD.class,
        TestComparisonSeriesToModelWithPLRInterpolateInputs.class,
        TestComparisonSeriesToModelWithPLRRegressionInputs.class } )
public class AllTestsNo {

}
