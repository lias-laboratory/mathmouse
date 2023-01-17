package fr.ensma.lias.timeseriesreductorslib.comparison2;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.junit.BeforeClass;
import org.junit.Test;

import fr.ensma.lias.timeseriesreductorslib.comparison.CompareAlgorithm;
import fr.ensma.lias.timeseriesreductorslib.comparison.ENotification;
import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.DifferentialEquation2;
import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.DifferentialEquation2IO;
import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.numericalmethod.NumericalMethod2;
import fr.ensma.lias.timeseriesreductorslib.timeseries.TimeSeriesDoubleDouble;
import fr.ensma.lias.timeseriesreductorslib.timeseries.TimeSeriesDoubleDoubleIO;

public class TestCompareAlgorithm {
    private static Map<String, TimeSeriesDoubleDouble> rawSeries;
   // private static Map<String, TimeSeriesDoubleDouble> inputSeries;
    private static Map<String, TimeSeriesDoubleDouble> modelSeries;

    private static List<DifferentialEquation2>         equations;
    private static NumericalMethod2                    numericalMethod;

    @BeforeClass
    public static void initialize() {
        String log4j = "src/test/resources/log4j.xml";
        DOMConfigurator.configure( log4j );
        Logger.getLogger("").debug("Initializing test…");

        rawSeries = new HashMap<String, TimeSeriesDoubleDouble>();
        //inputSeries = new HashMap<String, TimeSeriesDoubleDouble>();
        equations = new ArrayList<DifferentialEquation2>();
        modelSeries = new HashMap<String, TimeSeriesDoubleDouble>();

       /* inputSeries.put( "input1", TimeSeriesDoubleDoubleIO.readFromFile( "src/test/resources/input/outDiffEq.txt" ) );
        Logger.getLogger( "" ).debug( rawSeries.get( "input1" ) );
        inputSeries.put( "input2", TimeSeriesDoubleDoubleIO.readFromFile( "src/test/resources/input/outDiffEq2.txt" ) );
        Logger.getLogger( "" ).debug( rawSeries.get( "input2" ) );
        inputSeries.put( "input3", TimeSeriesDoubleDoubleIO.readFromFile( "src/test/resources/input/outDiffEq3.txt" ) );
        Logger.getLogger( "" ).debug( rawSeries.get( "input3" ) );*/
        rawSeries.put( "data1",
                TimeSeriesDoubleDoubleIO.readFromFile( "src/test/resources/timeseries-sources/Data1.csv" ) );
        Logger.getLogger( "" ).debug( rawSeries.get( "data1" ) );
        rawSeries.put( "data2",
                TimeSeriesDoubleDoubleIO.readFromFile( "src/test/resources/timeseries-sources/Data2.csv" ) );
        Logger.getLogger( "" ).debug( rawSeries.get( "data2" ) );
        rawSeries.put( "data3",
                TimeSeriesDoubleDoubleIO.readFromFile( "src/test/resources/timeseries-sources/Data3.csv" ) );
        Logger.getLogger( "" ).debug( rawSeries.get( "data3" ) );

        equations.add( DifferentialEquation2IO.XMLRead20FromFile( "src/test/resources/differential-equation-2.0.xsd",
                "src/test/resources/xmls2.0/equation1.xml" ) );
        Logger.getLogger( "" ).debug( equations );
        equations.add( DifferentialEquation2IO.XMLRead20FromFile( "src/test/resources/differential-equation-2.0.xsd",
                "src/test/resources/xmls2.0/equation2.xml" ) );
        Logger.getLogger( "" ).debug( equations );
        equations.add( DifferentialEquation2IO.XMLRead20FromFile( "src/test/resources/differential-equation-2.0.xsd",
                "src/test/resources/xmls2.0/equation3.xml" ) );
        Logger.getLogger( "" ).debug( equations );

        for ( int i = 0; i < equations.size(); i++ ) {
            try {
                numericalMethod = new NumericalMethod2( equations.get( i ) );
                modelSeries.put( "model" + ( i + 1 ), numericalMethod.equationToTimeseriesRK4() );
                Logger.getLogger( "" ).debug( modelSeries.get( "model" + ( i + 1 ) ) );
            } catch ( Exception e ) {
                e.printStackTrace();
            }
        }
        
        Logger.getLogger("").debug("tests initialized.");
    }

    @Test
    public void testStatistics() {
    	Logger.getLogger("").debug("testing statistics…");
    	double meanErrorMax = 10e-1;
    	double meanErrorMin = 10e-4;
    	double sdMax = 10e-1;
    	double sdMin = 10e-4;
    	CompareAlgorithm.set();
        try {
            for ( String key1 : rawSeries.keySet() ) {
                for ( String key2 : modelSeries.keySet() ) {
                    Logger.getLogger( getClass() ).debug( key1 + " : " + key2 );
                    CompareAlgorithm.statistics( modelSeries.get( key2 ), rawSeries.get( key1 ) );
                    CompareAlgorithm.printResults();
                    Logger.getLogger( getClass() ).debug( CompareAlgorithm.notify(meanErrorMax, sdMax, meanErrorMin, sdMin ) );
                    for ( int i = 1; i <= 3; i++ ) {
                        if ( key2.equals( "model" + i ) ) {
                            if ( key1.equals( "data" + i ) ) {
                                assertTrue( CompareAlgorithm.notify( meanErrorMax, sdMax, meanErrorMin, sdMin )
                                        .equals( ENotification.MODEL_ACCEPTED ) );
                            } else {
                                assertTrue( CompareAlgorithm.notify( meanErrorMax, sdMax, meanErrorMin, sdMin )
                                        .equals( ENotification.MODEL_REJECTED ) );
                            }
                        }
                    }
                }
            }
        } catch ( Exception e ) {
            e.printStackTrace();
        } finally {
        	Logger.getLogger("").debug("testing statistics… done.");
        }
    }
    
    @Test
    public void testLeastSquaredStatistics() {
    	Logger.getLogger("").debug("testing least squared statistics…");
    	double meanErrorMax = 10e-1;
    	double meanErrorMin = 10e-3;
    	double sdMax = 10e-1;
    	double sdMin = 10e-3;
    	CompareAlgorithm.set();
        try {
            for ( String key1 : rawSeries.keySet() ) {
                for ( String key2 : modelSeries.keySet() ) {
                    Logger.getLogger( getClass() ).debug( key1 + " : " + key2 );
                    CompareAlgorithm.leastSquareStatistics( modelSeries.get( key2 ), rawSeries.get( key1 ) );
                    CompareAlgorithm.printResults();
                    Logger.getLogger( getClass() ).debug( CompareAlgorithm.notify( meanErrorMax, sdMax, meanErrorMin, sdMin) );
                    for ( int i = 1; i <= 3; i++ ) {
                        if ( key2.equals( "model" + i ) ) {
                            if ( key1.equals( "data" + i ) ) {
                                assertTrue( CompareAlgorithm.notify( meanErrorMax, sdMax, meanErrorMin, sdMin )
                                        .equals( ENotification.MODEL_ACCEPTED ) );
                            } else {
                                assertTrue( CompareAlgorithm.notify(meanErrorMax, sdMax, meanErrorMin, sdMin )
                                        .equals( ENotification.MODEL_REJECTED ) );
                            }
                        }
                    }
                }
            }
        } catch ( Exception e ) {
            e.printStackTrace();
        } finally {
        	Logger.getLogger("").debug("testing least squared statistics… done.");
        }
    }

}
