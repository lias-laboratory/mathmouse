package fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.nummeth;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.junit.BeforeClass;
import org.junit.Test;

import fr.ensma.lias.timeseriesreductorslib.comparison.CompareAlgorithm;
import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.DifferentialEquation2;
import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.DifferentialEquation2IO;
import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.numericalmethod.NumericalMethod2;
import fr.ensma.lias.timeseriesreductorslib.timeseries.TimeSeriesDoubleDouble;
import fr.ensma.lias.timeseriesreductorslib.timeseries.TimeSeriesDoubleDoubleIO;

public class TestNumericalMethod2 {
    public static final String            STATIC_LOGGER      = "BEFORE_CLASS";

    public static final String            XSD_FILE_PATH      = "src/test/resources/differential-equation-2.0.xsd";

    public static final String            EQUATION_1_PATH    = "src/test/resources/xmls2.0/equation1.xml";
    public static final String            EQUATION_2_PATH    = "src/test/resources/xmls2.0/equation2.xml";
    public static final String            EQUATION_3_PATH    = "src/test/resources/xmls2.0/equation3.xml";

    public static final String            SERIES_1_FILE_PATH = "src/test/resources/timeseries-sources/Data1.csv";
    public static final String            SERIES_2_FILE_PATH = "src/test/resources/timeseries-sources/Data2.csv";
    public static final String            SERIES_3_FILE_PATH = "src/test/resources/timeseries-sources/Data3.csv";

    public static final String            SEPARATOR          = "   ";
    public static final String            DELIMITER          = "";
    public static final int               SERIES_INDEX       = 1;

    private static TimeSeriesDoubleDouble series1;
    private static TimeSeriesDoubleDouble series2;
    private static TimeSeriesDoubleDouble series3;

    private static DifferentialEquation2  equation1;
    private static DifferentialEquation2  equation2;
    private static DifferentialEquation2  equation3;

    private static NumericalMethod2       num1;
    private static NumericalMethod2       num2;
    private static NumericalMethod2       num3;

    @BeforeClass
    public static void initialize() {
        String log4j = "src/test/resources/log4j.xml";
        DOMConfigurator.configure( log4j );

        try {
            series1 = TimeSeriesDoubleDoubleIO.readFromFile( SERIES_1_FILE_PATH );
            Logger.getLogger( STATIC_LOGGER ).debug( series1 );
            series2 = TimeSeriesDoubleDoubleIO.readFromFile( SERIES_2_FILE_PATH );
            Logger.getLogger( STATIC_LOGGER ).debug( series2 );
            series3 = TimeSeriesDoubleDoubleIO.readFromFile( SERIES_3_FILE_PATH );
            Logger.getLogger( STATIC_LOGGER ).debug( series3 );

            // test constructor with xml file
            equation1 = DifferentialEquation2IO.XMLRead20FromFile( XSD_FILE_PATH, EQUATION_1_PATH );
            Logger.getLogger( STATIC_LOGGER ).debug( equation1 );
            equation2 = DifferentialEquation2IO.XMLRead20FromFile( XSD_FILE_PATH, EQUATION_2_PATH );
            Logger.getLogger( STATIC_LOGGER ).debug( equation2 );
            equation3 = DifferentialEquation2IO.XMLRead20FromFile( XSD_FILE_PATH, EQUATION_3_PATH );
            Logger.getLogger( STATIC_LOGGER ).debug( equation3 );

            // initializes numerical method
            num1 = new NumericalMethod2( equation1 );
            num2 = new NumericalMethod2( equation2 );
            num3 = new NumericalMethod2( equation3 );
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    @Test
    public void testInitialize() {
        assertNotNull( num1 );
        assertNotNull( num2 );
        assertNotNull( num3 );

        assertTrue( num1.getParametersSet().equals( equation1.getParametersSet() ) );
        assertTrue( num1.getDifferentialEquation().equals( equation1 ) );
        assertTrue( num1.getOrder() == equation1.getOrder() );
        assertNotNull( num1.getInputFunctionsValues() );
        assertTrue( num1.getInputFunctionsValues().equals( equation1.getInputFunctions() ) );
        try {
            assertTrue( num1.getUnkownFunctionName().equals( equation1.getUnknownFunction() ) );
        } catch ( Exception e ) {
            e.printStackTrace();
        }

        assertTrue( num2.getParametersSet().equals( equation2.getParametersSet() ) );
        assertTrue( num2.getDifferentialEquation().equals( equation2 ) );
        assertTrue( num2.getOrder() == equation2.getOrder() );
        assertNotNull( num2.getInputFunctionsValues() );
        assertTrue( num2.getInputFunctionsValues().equals( equation2.getInputFunctions() ) );
        try {
            assertTrue( num2.getUnkownFunctionName().equals( equation2.getUnknownFunction() ) );
        } catch ( Exception e ) {
            e.printStackTrace();
        }

        assertTrue( num3.getParametersSet().equals( equation3.getParametersSet() ) );
        assertTrue( num3.getDifferentialEquation().equals( equation3 ) );
        assertTrue( num3.getOrder() == equation3.getOrder() );
        assertNotNull( num3.getInputFunctionsValues() );
        assertTrue( num3.getInputFunctionsValues().equals( equation3.getInputFunctions() ) );
        try {
            assertTrue( num3.getUnkownFunctionName().equals( equation3.getUnknownFunction() ) );
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    @Test
    public void tests() {
        try {
            TimeSeriesDoubleDouble numSeries1 = num1.equationToTimeseriesRK4();
            Logger.getLogger( STATIC_LOGGER ).debug( numSeries1 );
            CompareAlgorithm.statistics( numSeries1, series1 );
            assertTrue( CompareAlgorithm.getStatsMap().get( CompareAlgorithm.MEAN_DELTA ) < 10e-3 );

            TimeSeriesDoubleDouble numSeries2 = num2.equationToTimeseriesRK4();
            Logger.getLogger( STATIC_LOGGER ).debug( numSeries2 );
            CompareAlgorithm.statistics( numSeries2, series2 );
            assertTrue( CompareAlgorithm.getStatsMap().get( CompareAlgorithm.MEAN_DELTA ) < 10e-3 );

            TimeSeriesDoubleDouble numSeries3 = num3.equationToTimeseriesRK4();
            Logger.getLogger( STATIC_LOGGER ).debug( numSeries3 );
            CompareAlgorithm.statistics( numSeries3, series3 );
            assertTrue( CompareAlgorithm.getStatsMap().get( CompareAlgorithm.MEAN_DELTA ) < 10e-3 );
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

}
