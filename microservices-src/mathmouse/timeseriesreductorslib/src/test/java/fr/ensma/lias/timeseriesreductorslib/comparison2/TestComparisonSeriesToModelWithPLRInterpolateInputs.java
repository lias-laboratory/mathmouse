package fr.ensma.lias.timeseriesreductorslib.comparison2;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import fr.ensma.lias.timeseriesreductorslib.ExperimentWithInput;
import fr.ensma.lias.timeseriesreductorslib.Memory;
import fr.ensma.lias.timeseriesreductorslib.comparison.CompareAlgorithm;
import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.DifferentialEquation2;
import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.DifferentialEquation2IO;
import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.FunctionKey;
import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.numericalmethod.NumericalMethod2;
import fr.ensma.lias.timeseriesreductorslib.reductors.segmentors.plr.interpolation.segalgs.PLRInterpolateBU;
import fr.ensma.lias.timeseriesreductorslib.reductors.segmentors.plr.interpolation.segalgs.PLRInterpolateSW;
import fr.ensma.lias.timeseriesreductorslib.reductors.segmentors.plr.interpolation.segalgs.PLRInterpolateTD;
import fr.ensma.lias.timeseriesreductorslib.timeseries.TimeSeriesDoubleDouble;
import fr.ensma.lias.timeseriesreductorslib.timeseries.TimeSeriesDoubleDoubleIO;

public class TestComparisonSeriesToModelWithPLRInterpolateInputs {
    private static final String                             STATIC_LOGGER   = "STATIC_LOGGER:TestComparisonSeriesToModelWithPLRInterpolateInputs";

    public static final String                              XSD_FILE_PATH   = "src/test/resources/differential-equation-2.0.xsd";

    // maximum segment error
    private static final double                             MAX_ERROR       = 0.001;

    // path to the input functions values
    private static final String                             INPUT_1_PATH    = "src/test/resources/input/outDiffEq.txt";
    private static final String                             INPUT_2_PATH    = "src/test/resources/input/outDiffEq2.txt";
    private static final String                             INPUT_3_PATH    = "src/test/resources/input/outDiffEq3.txt";

    // path to the series examples
    private static final String                             SERIES_1_PATH   = "src/test/resources/timeseries-sources/Data1.txt";
    private static final String                             SERIES_2_PATH   = "src/test/resources/timeseries-sources/Data2.txt";
    private static final String                             SERIES_3_PATH   = "src/test/resources/timeseries-sources/Data3.txt";

    // path to the equations
    public static final String                              EQUATION_1_PATH = "src/test/resources/xmls2.0/equation1.xml";
    public static final String                              EQUATION_2_PATH = "src/test/resources/xmls2.0/equation2.xml";
    public static final String                              EQUATION_3_PATH = "src/test/resources/xmls2.0/equation3.xml";

    // series examples
    private static TimeSeriesDoubleDouble                   rawSeries1      = null;
    private static TimeSeriesDoubleDouble                   rawSeries2      = null;
    private static TimeSeriesDoubleDouble                   rawSeries3      = null;

    // input series
    private static TimeSeriesDoubleDouble                   inputSeries1    = null;
    private static TimeSeriesDoubleDouble                   inputSeries2    = null;
    private static TimeSeriesDoubleDouble                   inputSeries3    = null;

    // segments
    private static PLRInterpolateBU                   segmentsBU1;
    private static PLRInterpolateBU                   segmentsBU2;
    private static PLRInterpolateBU                   segmentsBU3;

    private static PLRInterpolateTD                    segmentsTD1;
    private static PLRInterpolateTD                    segmentsTD2;
    private static PLRInterpolateTD                    segmentsTD3;

    private static PLRInterpolateSW              segmentsSW1;
    private static PLRInterpolateSW              segmentsSW2;
    private static PLRInterpolateSW              segmentsSW3;

    // rebuilt series
    private static TimeSeriesDoubleDouble                   seriesBU1;
    private static TimeSeriesDoubleDouble                   seriesBU2;
    private static TimeSeriesDoubleDouble                   seriesBU3;

    private static TimeSeriesDoubleDouble                   seriesTD1;
    private static TimeSeriesDoubleDouble                   seriesTD2;
    private static TimeSeriesDoubleDouble                   seriesTD3;

    private static TimeSeriesDoubleDouble                   seriesSW1;
    private static TimeSeriesDoubleDouble                   seriesSW2;
    private static TimeSeriesDoubleDouble                   seriesSW3;

    // equations objects
    private static DifferentialEquation2                    equation1InputPLRInterpolateBU;
    private static DifferentialEquation2                    equation1InputPLRInterpolateTD;
    private static DifferentialEquation2                    equation1InputPLRInterpolateSW;

    private static DifferentialEquation2                    equation2InputPLRInterpolateBU;
    private static DifferentialEquation2                    equation2InputPLRInterpolateTD;
    private static DifferentialEquation2                    equation2InputPLRInterpolateSW;

    private static DifferentialEquation2                    equation3InputPLRInterpolateBU;
    private static DifferentialEquation2                    equation3InputPLRInterpolateTD;
    private static DifferentialEquation2                    equation3InputPLRInterpolateSW;

    private static NumericalMethod2                         numericalMethod1InputPLRInterpolateBU;
    private static NumericalMethod2                         numericalMethod1InputPLRInterpolateTD;
    private static NumericalMethod2                         numericalMethod1InputPLRInterpolateSW;

    private static NumericalMethod2                         numericalMethod2InputPLRInterpolateBU;
    private static NumericalMethod2                         numericalMethod2InputPLRInterpolateTD;
    private static NumericalMethod2                         numericalMethod2InputPLRInterpolateSW;

    private static NumericalMethod2                         numericalMethod3InputPLRInterpolateBU;
    private static NumericalMethod2                         numericalMethod3InputPLRInterpolateTD;
    private static NumericalMethod2                         numericalMethod3InputPLRInterpolateSW;

    private static Map<FunctionKey, TimeSeriesDoubleDouble> inputsBU1;
    private static Map<FunctionKey, TimeSeriesDoubleDouble> inputsBU2;
    private static Map<FunctionKey, TimeSeriesDoubleDouble> inputsBU3;

    private static Map<FunctionKey, TimeSeriesDoubleDouble> inputsTD1;
    private static Map<FunctionKey, TimeSeriesDoubleDouble> inputsTD2;
    private static Map<FunctionKey, TimeSeriesDoubleDouble> inputsTD3;

    private static Map<FunctionKey, TimeSeriesDoubleDouble> inputsSW1;
    private static Map<FunctionKey, TimeSeriesDoubleDouble> inputsSW2;
    private static Map<FunctionKey, TimeSeriesDoubleDouble> inputsSW3;

    // series calculated from equations and rebuilt series
    private static TimeSeriesDoubleDouble                   modelSeries1InputPLRInterpolateBU;
    private static TimeSeriesDoubleDouble                   modelSeries1InputPLRInterpolateTD;
    private static TimeSeriesDoubleDouble                   modelSeries1InputPLRInterpolateSW;

    private static TimeSeriesDoubleDouble                   modelSeries2InputPLRInterpolateBU;
    private static TimeSeriesDoubleDouble                   modelSeries2InputPLRInterpolateTD;
    private static TimeSeriesDoubleDouble                   modelSeries2InputPLRInterpolateSW;

    private static TimeSeriesDoubleDouble                   modelSeries3InputPLRInterpolateBU;
    private static TimeSeriesDoubleDouble                   modelSeries3InputPLRInterpolateTD;
    private static TimeSeriesDoubleDouble                   modelSeries3InputPLRInterpolateSW;

    private static ExperimentWithInput                      experiementBU1;
    private static ExperimentWithInput                      experiementBU2;
    private static ExperimentWithInput                      experiementBU3;

    private static ExperimentWithInput                      experiementTD1;
    private static ExperimentWithInput                      experiementTD2;
    private static ExperimentWithInput                      experiementTD3;

    private static ExperimentWithInput                      experiementSW1;
    private static ExperimentWithInput                      experiementSW2;
    private static ExperimentWithInput                      experiementSW3;

    private static long                                     time;
    private static long                                     intermediateTime;

    @BeforeClass
    public static void staticInitialize() {
        // BasicConfigurator.configure();
        String log4j = "src/test/resources/log4j.xml";
        DOMConfigurator.configure( log4j );

        experiementBU1 = new ExperimentWithInput( "ODEWithInputPLRInterpolateBU_1" );
        experiementBU2 = new ExperimentWithInput( "ODEWithInputPLRInterpolateBU_2" );
        experiementBU3 = new ExperimentWithInput( "ODEWithInputPLRInterpolateBU_3" );

        experiementTD1 = new ExperimentWithInput( "ODEWithInputPLRInterpolateTD_1" );
        experiementTD2 = new ExperimentWithInput( "ODEWithInputPLRInterpolateTD_2" );
        experiementTD3 = new ExperimentWithInput( "ODEWithInputPLRInterpolateTD_3" );

        experiementSW1 = new ExperimentWithInput( "ODEWithInputPLRInterpolateSW_1" );
        experiementSW2 = new ExperimentWithInput( "ODEWithInputPLRInterpolateSW_2" );
        experiementSW3 = new ExperimentWithInput( "ODEWithInputPLRInterpolateSW_3" );

        // loading raw series data
        rawSeries1 = TimeSeriesDoubleDoubleIO.readFromFile( SERIES_1_PATH );
        Logger.getLogger( STATIC_LOGGER ).debug( "raw series 1:" + rawSeries1 );
        try {
            double size = (double) Memory.printSeriesSize( rawSeries1 );
            experiementBU1.putOriginalMemorySize( size );
            experiementSW1.putOriginalMemorySize( size );
            experiementTD1.putOriginalMemorySize( size );
        } catch ( Exception e1 ) {
            e1.printStackTrace();
        }
        rawSeries2 = TimeSeriesDoubleDoubleIO.readFromFile( SERIES_2_PATH );
        Logger.getLogger( STATIC_LOGGER ).debug( "raw series 1:" + rawSeries2 );
        try {
            double size = (double) Memory.printSeriesSize( rawSeries2 );
            experiementBU2.putOriginalMemorySize( size );
            experiementSW2.putOriginalMemorySize( size );
            experiementTD2.putOriginalMemorySize( size );
        } catch ( Exception e1 ) {
            e1.printStackTrace();
        }
        rawSeries3 = TimeSeriesDoubleDoubleIO.readFromFile( SERIES_3_PATH );
        Logger.getLogger( STATIC_LOGGER ).debug( "raw series 1:" + rawSeries3 );
        try {
            double size = (double) Memory.printSeriesSize( rawSeries3 );
            experiementBU3.putOriginalMemorySize( size );
            experiementSW3.putOriginalMemorySize( size );
            experiementTD3.putOriginalMemorySize( size );
        } catch ( Exception e1 ) {
            e1.printStackTrace();
        }

        // loading input series data from files
        inputSeries1 = TimeSeriesDoubleDoubleIO.readFromFile( INPUT_1_PATH );
        Logger.getLogger( STATIC_LOGGER ).debug( "input series 1:" + inputSeries1 );
        assertNotNull( inputSeries1 );
        try {
            Logger.getLogger( STATIC_LOGGER ).debug( "input 1 size" );
            double size = (double) Memory.printSeriesSize( inputSeries1 );
            experiementBU1.putInputOriginalMemorySize( size );
            experiementSW1.putInputOriginalMemorySize( size );
            experiementTD1.putInputOriginalMemorySize( size );
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        inputSeries2 = TimeSeriesDoubleDoubleIO.readFromFile( INPUT_2_PATH );
        Logger.getLogger( STATIC_LOGGER ).debug( "input series 2:" + inputSeries2 );
        try {
            Logger.getLogger( STATIC_LOGGER ).debug( "input 2 size" );
            double size = (double) Memory.printSeriesSize( inputSeries2 );
            experiementBU2.putInputOriginalMemorySize( size );
            experiementSW2.putInputOriginalMemorySize( size );
            experiementTD2.putInputOriginalMemorySize( size );
        } catch ( Exception e ) {
            e.printStackTrace();
        }

        inputSeries3 = TimeSeriesDoubleDoubleIO.readFromFile( INPUT_3_PATH );
        Logger.getLogger( STATIC_LOGGER ).debug( "input series 3:" + inputSeries3 );
        try {
            Logger.getLogger( STATIC_LOGGER ).debug( "input 3 size" );
            double size = (double) Memory.printSeriesSize( inputSeries3 );
            experiementBU3.putInputOriginalMemorySize( size );
            experiementSW3.putInputOriginalMemorySize( size );
            experiementTD3.putInputOriginalMemorySize( size );
        } catch ( Exception e ) {
            e.printStackTrace();
        }

        // segmenting series with PLR Interpolate
        segmentsBU1 = new PLRInterpolateBU( inputSeries1 );
        segmentsBU1.process( MAX_ERROR );
        Logger.getLogger( STATIC_LOGGER ).debug( "segments 1:" + segmentsBU1.getInterpolateSegments() );
        try {
            experiementBU1
                    .putInputMemorySize( (double) Memory.printSegmentSize( segmentsBU1.getInterpolateSegments() ) );
            experiementBU1.putInputNumberOfElements( (double) segmentsBU1.getInterpolateSegments().size() );
        } catch ( Exception e1 ) {
            e1.printStackTrace();
        }

        segmentsBU2 = new PLRInterpolateBU( inputSeries2 );
        segmentsBU2.process( MAX_ERROR );
        Logger.getLogger( STATIC_LOGGER ).debug( "segments 2:" + segmentsBU2.getInterpolateSegments() );
        try {
            experiementBU2
                    .putInputMemorySize( (double) Memory.printSegmentSize( segmentsBU2.getInterpolateSegments() ) );
            experiementBU2.putInputNumberOfElements( (double) segmentsBU2.getInterpolateSegments().size() );
        } catch ( Exception e1 ) {
            e1.printStackTrace();
        }

        segmentsBU3 = new PLRInterpolateBU( inputSeries3 );
        segmentsBU3.process( MAX_ERROR );
        Logger.getLogger( STATIC_LOGGER ).debug( "segments 3:" + segmentsBU3.getInterpolateSegments() );
        try {
            experiementBU3
                    .putInputMemorySize( (double) Memory.printSegmentSize( segmentsBU3.getInterpolateSegments() ) );
            experiementBU3.putInputNumberOfElements( (double) segmentsBU3.getInterpolateSegments().size() );
        } catch ( Exception e1 ) {
            e1.printStackTrace();
        }

        segmentsTD1 = new PLRInterpolateTD( inputSeries1 );
        segmentsTD1.process( MAX_ERROR );
        Logger.getLogger( STATIC_LOGGER ).debug( "segments 1:" + segmentsTD1.getInterpolateSegments() );
        try {
            experiementTD1
                    .putInputMemorySize( (double) Memory.printSegmentSize( segmentsTD1.getInterpolateSegments() ) );
            experiementTD1.putInputNumberOfElements( (double) segmentsTD1.getInterpolateSegments().size() );
        } catch ( Exception e1 ) {
            e1.printStackTrace();
        }

        segmentsTD2 = new PLRInterpolateTD( inputSeries2 );
        segmentsTD2.process( MAX_ERROR );
        Logger.getLogger( STATIC_LOGGER ).debug( "segments 2:" + segmentsTD2.getInterpolateSegments() );
        try {
            experiementTD2
                    .putInputMemorySize( (double) Memory.printSegmentSize( segmentsTD2.getInterpolateSegments() ) );
            experiementTD2.putInputNumberOfElements( (double) segmentsTD2.getInterpolateSegments().size() );
        } catch ( Exception e1 ) {
            e1.printStackTrace();
        }

        segmentsTD3 = new PLRInterpolateTD( inputSeries3 );
        segmentsTD3.process( MAX_ERROR );
        Logger.getLogger( STATIC_LOGGER ).debug( "segments 3:" + segmentsTD3.getInterpolateSegments() );
        try {
            experiementTD3
                    .putInputMemorySize( (double) Memory.printSegmentSize( segmentsTD3.getInterpolateSegments() ) );
            experiementTD3.putInputNumberOfElements( (double) segmentsTD3.getInterpolateSegments().size() );
        } catch ( Exception e1 ) {
            e1.printStackTrace();
        }

        segmentsSW1 = new PLRInterpolateSW( inputSeries1 );
        segmentsSW1.process( MAX_ERROR );
        Logger.getLogger( STATIC_LOGGER ).debug( "segments 1:" + segmentsSW1.getInterpolateSegments() );
        try {
            experiementSW1
                    .putInputMemorySize( (double) Memory.printSegmentSize( segmentsSW1.getInterpolateSegments() ) );
            experiementSW1.putInputNumberOfElements( (double) segmentsSW1.getInterpolateSegments().size() );
        } catch ( Exception e1 ) {
            e1.printStackTrace();
        }

        segmentsSW2 = new PLRInterpolateSW( inputSeries2 );
        segmentsSW2.process( MAX_ERROR );
        Logger.getLogger( STATIC_LOGGER ).debug( "segments 2:" + segmentsSW2.getInterpolateSegments() );
        try {
            experiementSW2
                    .putInputMemorySize( (double) Memory.printSegmentSize( segmentsSW2.getInterpolateSegments() ) );
            experiementSW2.putInputNumberOfElements( (double) segmentsSW2.getInterpolateSegments().size() );
        } catch ( Exception e1 ) {
            e1.printStackTrace();
        }

        segmentsSW3 = new PLRInterpolateSW( inputSeries3 );
        segmentsSW3.process( MAX_ERROR );
        Logger.getLogger( STATIC_LOGGER ).debug( "segments 3:" + segmentsSW3.getInterpolateSegments() );
        try {
            experiementSW3
                    .putInputMemorySize( (double) Memory.printSegmentSize( segmentsSW3.getInterpolateSegments() ) );
            experiementSW3.putInputNumberOfElements( (double) segmentsSW3.getInterpolateSegments().size() );
        } catch ( Exception e1 ) {
            e1.printStackTrace();
        }

        seriesBU1 = new TimeSeriesDoubleDouble();
        seriesBU2 = new TimeSeriesDoubleDouble();
        seriesBU3 = new TimeSeriesDoubleDouble();

        seriesTD1 = new TimeSeriesDoubleDouble();
        seriesTD2 = new TimeSeriesDoubleDouble();
        seriesTD3 = new TimeSeriesDoubleDouble();

        seriesSW1 = new TimeSeriesDoubleDouble();
        seriesSW2 = new TimeSeriesDoubleDouble();
        seriesSW3 = new TimeSeriesDoubleDouble();

        // rebuilding the input series from their PLR Interpolate representation
        seriesBU1.putAll( segmentsBU1.getInterpolateSegments().computeValues( inputSeries1.keySet() ) );
        Logger.getLogger( "" ).debug( "seriesBU1: " + seriesBU1 );
        seriesBU2.putAll( segmentsBU2.getInterpolateSegments().computeValues( inputSeries2.keySet() ) );
        Logger.getLogger( "" ).debug( "seriesBU2: " + seriesBU2 );
        seriesBU3.putAll( segmentsBU3.getInterpolateSegments().computeValues( inputSeries3.keySet() ) );
        Logger.getLogger( "" ).debug( "seriesBU3: " + seriesBU3 );

        seriesTD1.putAll( segmentsTD1.getInterpolateSegments().computeValues( inputSeries1.keySet() ) );
        Logger.getLogger( "" ).debug( "seriesTD1: " + seriesTD1 );
        seriesTD2.putAll( segmentsTD2.getInterpolateSegments().computeValues( inputSeries2.keySet() ) );
        Logger.getLogger( "" ).debug( "seriesTD2: " + seriesTD2 );
        seriesTD3.putAll( segmentsTD3.getInterpolateSegments().computeValues( inputSeries3.keySet() ) );
        Logger.getLogger( "" ).debug( "seriesTD3: " + seriesTD3 );

        seriesSW1.putAll( segmentsSW1.getInterpolateSegments().computeValues( inputSeries1.keySet() ) );
        Logger.getLogger( "" ).debug( "seriesSW1: " + seriesSW1 );
        seriesSW2.putAll( segmentsSW2.getInterpolateSegments().computeValues( inputSeries2.keySet() ) );
        Logger.getLogger( "" ).debug( "seriesSW2: " + seriesSW2 );
        seriesSW3.putAll( segmentsSW3.getInterpolateSegments().computeValues( inputSeries3.keySet() ) );
        Logger.getLogger( "" ).debug( "seriesSW3: " + seriesSW3 );

        // creating differential equations objects, with the rebuilt inputs
        try {
            equation1InputPLRInterpolateBU = DifferentialEquation2IO.XMLRead20FromFile( XSD_FILE_PATH,
                    EQUATION_1_PATH );
            double size = (double) Memory.printDifferentialEquationSize( equation1InputPLRInterpolateBU );
            experiementBU1.putMemorySize( size );
            experiementBU1.putNumberOfElements( 0.0 );
            equation1InputPLRInterpolateTD = DifferentialEquation2IO.XMLRead20FromFile( XSD_FILE_PATH,
                    EQUATION_1_PATH );
            size = (double) Memory.printDifferentialEquationSize( equation1InputPLRInterpolateTD );
            experiementTD1.putMemorySize( size );
            experiementTD1.putNumberOfElements( 0.0 );
            equation1InputPLRInterpolateSW = DifferentialEquation2IO.XMLRead20FromFile( XSD_FILE_PATH,
                    EQUATION_1_PATH );
            size = (double) Memory.printDifferentialEquationSize( equation1InputPLRInterpolateSW );
            experiementSW1.putMemorySize( size );
            experiementSW1.putNumberOfElements( 0.0 );

            equation2InputPLRInterpolateBU = DifferentialEquation2IO.XMLRead20FromFile( XSD_FILE_PATH,
                    EQUATION_2_PATH );
            size = (double) Memory.printDifferentialEquationSize( equation2InputPLRInterpolateBU );
            experiementBU2.putMemorySize( size );
            experiementBU2.putNumberOfElements( 0.0 );
            equation2InputPLRInterpolateTD = DifferentialEquation2IO.XMLRead20FromFile( XSD_FILE_PATH,
                    EQUATION_2_PATH );
            size = (double) Memory.printDifferentialEquationSize( equation2InputPLRInterpolateTD );
            experiementTD2.putMemorySize( size );
            experiementTD2.putNumberOfElements( 0.0 );
            equation2InputPLRInterpolateSW = DifferentialEquation2IO.XMLRead20FromFile( XSD_FILE_PATH,
                    EQUATION_2_PATH );
            size = (double) Memory.printDifferentialEquationSize( equation2InputPLRInterpolateSW );
            experiementSW2.putMemorySize( size );
            experiementSW2.putNumberOfElements( 0.0 );

            equation3InputPLRInterpolateBU = DifferentialEquation2IO.XMLRead20FromFile( XSD_FILE_PATH,
                    EQUATION_3_PATH );
            size = (double) Memory.printDifferentialEquationSize( equation3InputPLRInterpolateBU );
            experiementBU3.putMemorySize( size );
            experiementBU3.putNumberOfElements( 0.0 );
            equation3InputPLRInterpolateTD = DifferentialEquation2IO.XMLRead20FromFile( XSD_FILE_PATH,
                    EQUATION_3_PATH );
            size = (double) Memory.printDifferentialEquationSize( equation3InputPLRInterpolateBU );
            experiementTD3.putMemorySize( size );
            experiementTD3.putNumberOfElements( 0.0 );
            equation3InputPLRInterpolateSW = DifferentialEquation2IO.XMLRead20FromFile( XSD_FILE_PATH,
                    EQUATION_3_PATH );
            size = (double) Memory.printDifferentialEquationSize( equation3InputPLRInterpolateBU );
            experiementSW3.putMemorySize( size );
            experiementSW3.putNumberOfElements( 0.0 );

            // setting the inputs functions
            inputsBU1 = new HashMap<FunctionKey, TimeSeriesDoubleDouble>();
            inputsBU2 = new HashMap<FunctionKey, TimeSeriesDoubleDouble>();
            inputsBU3 = new HashMap<FunctionKey, TimeSeriesDoubleDouble>();

            inputsSW1 = new HashMap<FunctionKey, TimeSeriesDoubleDouble>();
            inputsSW2 = new HashMap<FunctionKey, TimeSeriesDoubleDouble>();
            inputsSW3 = new HashMap<FunctionKey, TimeSeriesDoubleDouble>();

            inputsTD1 = new HashMap<FunctionKey, TimeSeriesDoubleDouble>();
            inputsTD2 = new HashMap<FunctionKey, TimeSeriesDoubleDouble>();
            inputsTD3 = new HashMap<FunctionKey, TimeSeriesDoubleDouble>();

            inputsBU1.put( new FunctionKey( "u", 0 ), seriesBU1 );
            equation1InputPLRInterpolateBU.setInputFunctions( inputsBU1 );
            inputsBU2.put( new FunctionKey( "u", 0 ), seriesBU2 );
            equation2InputPLRInterpolateBU.setInputFunctions( inputsBU2 );
            inputsBU3.put( new FunctionKey( "u", 0 ), seriesBU3 );
            equation3InputPLRInterpolateBU.setInputFunctions( inputsBU3 );

            inputsTD1.put( new FunctionKey( "u", 0 ), seriesTD1 );
            equation1InputPLRInterpolateTD.setInputFunctions( inputsTD1 );
            inputsTD2.put( new FunctionKey( "u", 0 ), seriesTD2 );
            equation2InputPLRInterpolateTD.setInputFunctions( inputsTD2 );
            inputsTD3.put( new FunctionKey( "u", 0 ), seriesTD3 );
            equation3InputPLRInterpolateTD.setInputFunctions( inputsTD3 );

            inputsSW1.put( new FunctionKey( "u", 0 ), seriesSW1 );
            equation1InputPLRInterpolateSW.setInputFunctions( inputsSW1 );
            inputsSW2.put( new FunctionKey( "u", 0 ), seriesSW2 );
            equation2InputPLRInterpolateSW.setInputFunctions( inputsSW2 );
            inputsSW3.put( new FunctionKey( "u", 0 ), seriesSW3 );
            equation3InputPLRInterpolateSW.setInputFunctions( inputsSW3 );

        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    @Test
    public void dimensionCheck() {
        Logger.getLogger( getClass() ).debug( "dim input series 1: " + inputSeries1.size() );
        assertTrue( inputSeries1.size() == seriesBU1.size() );
        assertTrue( inputSeries1.size() == seriesSW1.size() );
        assertTrue( inputSeries1.size() == seriesTD1.size() );
        Logger.getLogger( getClass() ).debug( "dim input series 2: " + inputSeries2.size() );
        assertTrue( inputSeries2.size() == seriesBU2.size() );
        assertTrue( inputSeries2.size() == seriesSW2.size() );
        assertTrue( inputSeries2.size() == seriesTD2.size() );
        Logger.getLogger( getClass() ).debug( "dim input series 3: " + inputSeries3.size() );
        assertTrue( inputSeries3.size() == seriesBU3.size() );
        assertTrue( inputSeries3.size() == seriesSW3.size() );
        assertTrue( inputSeries3.size() == seriesTD3.size() );
    }

    @Test
    public void seriesReconstructionTest() {
        // given
        // the three differential equations, with the three different inputs for
        // each one
        // creating the numerical method objects, to prepare values recovery
        try {
            numericalMethod1InputPLRInterpolateBU = new NumericalMethod2( equation1InputPLRInterpolateBU );
            numericalMethod1InputPLRInterpolateSW = new NumericalMethod2( equation1InputPLRInterpolateSW );
            numericalMethod1InputPLRInterpolateTD = new NumericalMethod2( equation1InputPLRInterpolateTD );

            numericalMethod2InputPLRInterpolateBU = new NumericalMethod2( equation2InputPLRInterpolateBU );
            numericalMethod2InputPLRInterpolateSW = new NumericalMethod2( equation2InputPLRInterpolateSW );
            numericalMethod2InputPLRInterpolateTD = new NumericalMethod2( equation2InputPLRInterpolateTD );

            numericalMethod3InputPLRInterpolateBU = new NumericalMethod2( equation3InputPLRInterpolateBU );
            numericalMethod3InputPLRInterpolateSW = new NumericalMethod2( equation3InputPLRInterpolateSW );
            numericalMethod3InputPLRInterpolateTD = new NumericalMethod2( equation3InputPLRInterpolateTD );

            // when
            // rebuilding original values from equations
            Logger.getLogger( getClass() ).debug( "generating values of equation 1, for input with BU" );
            time = 0;
            intermediateTime = System.nanoTime();
            modelSeries1InputPLRInterpolateBU = numericalMethod1InputPLRInterpolateBU
                    .equationToTimeseriesRK4();
            intermediateTime = System.nanoTime() - intermediateTime;
            time = time + intermediateTime;
            experiementBU1.putTime( (double) time / 1000000.0 );

            Logger.getLogger( getClass() ).debug( "generating values of equation 1, for input with SW" );
            time = 0;
            intermediateTime = System.nanoTime();
            modelSeries1InputPLRInterpolateSW = numericalMethod1InputPLRInterpolateSW
                    .equationToTimeseriesRK4();
            intermediateTime = System.nanoTime() - intermediateTime;
            time = time + intermediateTime;
            experiementSW1.putTime( (double) time / 1000000.0 );

            Logger.getLogger( getClass() ).debug( "generating values of equation 1, for input with TD" );
            time = 0;
            intermediateTime = System.nanoTime();
            modelSeries1InputPLRInterpolateTD = numericalMethod1InputPLRInterpolateTD
                    .equationToTimeseriesRK4();
            intermediateTime = System.nanoTime() - intermediateTime;
            time = time + intermediateTime;
            experiementTD1.putTime( (double) time / 1000000.0 );

            Logger.getLogger( getClass() ).debug( "generating values of equation 2, for input with BU" );
            time = 0;
            intermediateTime = System.nanoTime();
            modelSeries2InputPLRInterpolateBU = numericalMethod2InputPLRInterpolateBU
                    .equationToTimeseriesRK4();
            intermediateTime = System.nanoTime() - intermediateTime;
            time = time + intermediateTime;
            experiementBU2.putTime( (double) time / 1000000.0 );

            Logger.getLogger( getClass() ).debug( "generating values of equation 2, for input with SW" );
            time = 0;
            intermediateTime = System.nanoTime();
            modelSeries2InputPLRInterpolateSW = numericalMethod2InputPLRInterpolateSW
                    .equationToTimeseriesRK4();
            intermediateTime = System.nanoTime() - intermediateTime;
            time = time + intermediateTime;
            experiementSW2.putTime( (double) time / 1000000.0 );

            Logger.getLogger( getClass() ).debug( "generating values of equation 2, for input with TD" );
            time = 0;
            intermediateTime = System.nanoTime();
            modelSeries2InputPLRInterpolateTD = numericalMethod2InputPLRInterpolateTD
                    .equationToTimeseriesRK4();
            intermediateTime = System.nanoTime() - intermediateTime;
            time = time + intermediateTime;
            experiementTD2.putTime( (double) time / 1000000.0 );

            Logger.getLogger( getClass() ).debug( "generating values of equation 3, for input with BU" );
            time = 0;
            intermediateTime = System.nanoTime();
            modelSeries3InputPLRInterpolateBU = numericalMethod3InputPLRInterpolateBU
                    .equationToTimeseriesRK4();
            intermediateTime = System.nanoTime() - intermediateTime;
            time = time + intermediateTime;
            experiementBU3.putTime( (double) time / 1000000.0 );

            Logger.getLogger( getClass() ).debug( "generating values of equation 3, for input with SW" );
            time = 0;
            intermediateTime = System.nanoTime();
            modelSeries3InputPLRInterpolateSW = numericalMethod3InputPLRInterpolateSW
                    .equationToTimeseriesRK4();
            intermediateTime = System.nanoTime() - intermediateTime;
            time = time + intermediateTime;
            experiementSW3.putTime( (double) time / 1000000.0 );

            Logger.getLogger( getClass() ).debug( "generating values of equation 3, for input with TD" );
            time = 0;
            intermediateTime = System.nanoTime();
            modelSeries3InputPLRInterpolateTD = numericalMethod3InputPLRInterpolateTD
                    .equationToTimeseriesRK4();
            intermediateTime = System.nanoTime() - intermediateTime;
            time = time + intermediateTime;
            experiementTD3.putTime( (double) time / 1000000.0 );

            // then
            // corresponding series must be same size
            assertTrue( modelSeries1InputPLRInterpolateBU.size() == rawSeries1.size() );
            assertTrue( modelSeries1InputPLRInterpolateSW.size() == rawSeries1.size() );
            assertTrue( modelSeries1InputPLRInterpolateTD.size() == rawSeries1.size() );

            assertTrue( modelSeries2InputPLRInterpolateBU.size() == rawSeries2.size() );
            assertTrue( modelSeries2InputPLRInterpolateSW.size() == rawSeries2.size() );
            assertTrue( modelSeries2InputPLRInterpolateTD.size() == rawSeries2.size() );

            assertTrue( modelSeries3InputPLRInterpolateBU.size() == rawSeries3.size() );
            assertTrue( modelSeries3InputPLRInterpolateSW.size() == rawSeries3.size() );
            assertTrue( modelSeries3InputPLRInterpolateTD.size() == rawSeries3.size() );

            // checking reconstruction quality
            Logger.getLogger( getClass() ).debug( "model series 1: " + modelSeries1InputPLRInterpolateBU );
            Logger.getLogger( getClass() ).debug( "raw series 1: " + rawSeries1 );
            Logger.getLogger( getClass() ).debug( "equation 1 with PLR Interpolate BU" );
            CompareAlgorithm.statistics( modelSeries1InputPLRInterpolateBU, rawSeries1 );
            CompareAlgorithm.printResults();
            experiementBU1.putAvg( CompareAlgorithm.getStatsMap().get( CompareAlgorithm.MEAN_DELTA ) );
            experiementBU1.putMaxAvg( CompareAlgorithm.getStatsMap().get( CompareAlgorithm.MEAN_DELTA_MAX ) );
            experiementBU1.putMinAvg( CompareAlgorithm.getStatsMap().get( CompareAlgorithm.MEAN_DELTA_MIN ) );
            experiementBU1
                    .putStandardDeviation( CompareAlgorithm.getStatsMap().get( CompareAlgorithm.STANDARD_DEVIATION ) );

            Logger.getLogger( getClass() ).debug( "equation 1 with PLR Interpolate SW" );
            CompareAlgorithm.statistics( modelSeries1InputPLRInterpolateSW, rawSeries1 );
            CompareAlgorithm.printResults();
            experiementSW1.putAvg( CompareAlgorithm.getStatsMap().get( CompareAlgorithm.MEAN_DELTA ) );
            experiementSW1.putMaxAvg( CompareAlgorithm.getStatsMap().get( CompareAlgorithm.MEAN_DELTA_MAX ) );
            experiementSW1.putMinAvg( CompareAlgorithm.getStatsMap().get( CompareAlgorithm.MEAN_DELTA_MIN ) );
            experiementSW1
                    .putStandardDeviation( CompareAlgorithm.getStatsMap().get( CompareAlgorithm.STANDARD_DEVIATION ) );

            Logger.getLogger( getClass() ).debug( "equation 1 with PLR Interpolate TD" );
            CompareAlgorithm.statistics( modelSeries1InputPLRInterpolateTD, rawSeries1 );
            CompareAlgorithm.printResults();
            experiementTD1.putAvg( CompareAlgorithm.getStatsMap().get( CompareAlgorithm.MEAN_DELTA ) );
            experiementTD1.putMaxAvg( CompareAlgorithm.getStatsMap().get( CompareAlgorithm.MEAN_DELTA_MAX ) );
            experiementTD1.putMinAvg( CompareAlgorithm.getStatsMap().get( CompareAlgorithm.MEAN_DELTA_MIN ) );
            experiementTD1
                    .putStandardDeviation( CompareAlgorithm.getStatsMap().get( CompareAlgorithm.STANDARD_DEVIATION ) );

            Logger.getLogger( getClass() ).debug( "equation 2 with PLR Interpolate BU" );
            CompareAlgorithm.statistics( modelSeries2InputPLRInterpolateBU, rawSeries2 );
            CompareAlgorithm.printResults();
            experiementBU2.putAvg( CompareAlgorithm.getStatsMap().get( CompareAlgorithm.MEAN_DELTA ) );
            experiementBU2.putMaxAvg( CompareAlgorithm.getStatsMap().get( CompareAlgorithm.MEAN_DELTA_MAX ) );
            experiementBU2.putMinAvg( CompareAlgorithm.getStatsMap().get( CompareAlgorithm.MEAN_DELTA_MIN ) );
            experiementBU2
                    .putStandardDeviation( CompareAlgorithm.getStatsMap().get( CompareAlgorithm.STANDARD_DEVIATION ) );

            Logger.getLogger( getClass() ).debug( "equation 2 with PLR Interpolate SW" );
            CompareAlgorithm.statistics( modelSeries2InputPLRInterpolateSW, rawSeries2 );
            CompareAlgorithm.printResults();
            experiementSW2.putAvg( CompareAlgorithm.getStatsMap().get( CompareAlgorithm.MEAN_DELTA ) );
            experiementSW2.putMaxAvg( CompareAlgorithm.getStatsMap().get( CompareAlgorithm.MEAN_DELTA_MAX ) );
            experiementSW2.putMinAvg( CompareAlgorithm.getStatsMap().get( CompareAlgorithm.MEAN_DELTA_MIN ) );
            experiementSW2
                    .putStandardDeviation( CompareAlgorithm.getStatsMap().get( CompareAlgorithm.STANDARD_DEVIATION ) );

            Logger.getLogger( getClass() ).debug( "equation 2 with PLR Interpolate TD" );
            CompareAlgorithm.statistics( modelSeries2InputPLRInterpolateTD, rawSeries2 );
            CompareAlgorithm.printResults();
            experiementTD2.putAvg( CompareAlgorithm.getStatsMap().get( CompareAlgorithm.MEAN_DELTA ) );
            experiementTD2.putMaxAvg( CompareAlgorithm.getStatsMap().get( CompareAlgorithm.MEAN_DELTA_MAX ) );
            experiementTD2.putMinAvg( CompareAlgorithm.getStatsMap().get( CompareAlgorithm.MEAN_DELTA_MIN ) );
            experiementTD2
                    .putStandardDeviation( CompareAlgorithm.getStatsMap().get( CompareAlgorithm.STANDARD_DEVIATION ) );

            Logger.getLogger( getClass() ).debug( "equation 3 with PLR Interpolate BU" );
            CompareAlgorithm.statistics( modelSeries3InputPLRInterpolateBU, rawSeries3 );
            CompareAlgorithm.printResults();
            experiementBU3.putAvg( CompareAlgorithm.getStatsMap().get( CompareAlgorithm.MEAN_DELTA ) );
            experiementBU3.putMaxAvg( CompareAlgorithm.getStatsMap().get( CompareAlgorithm.MEAN_DELTA_MAX ) );
            experiementBU3.putMinAvg( CompareAlgorithm.getStatsMap().get( CompareAlgorithm.MEAN_DELTA_MIN ) );
            experiementBU3
                    .putStandardDeviation( CompareAlgorithm.getStatsMap().get( CompareAlgorithm.STANDARD_DEVIATION ) );

            Logger.getLogger( getClass() ).debug( "equation 3 with PLR Interpolate SW" );
            CompareAlgorithm.statistics( modelSeries3InputPLRInterpolateSW, rawSeries3 );
            CompareAlgorithm.printResults();
            experiementSW3.putAvg( CompareAlgorithm.getStatsMap().get( CompareAlgorithm.MEAN_DELTA ) );
            experiementSW3.putMaxAvg( CompareAlgorithm.getStatsMap().get( CompareAlgorithm.MEAN_DELTA_MAX ) );
            experiementSW3.putMinAvg( CompareAlgorithm.getStatsMap().get( CompareAlgorithm.MEAN_DELTA_MIN ) );
            experiementSW3
                    .putStandardDeviation( CompareAlgorithm.getStatsMap().get( CompareAlgorithm.STANDARD_DEVIATION ) );

            Logger.getLogger( getClass() ).debug( "equation 3 with PLR Interpolate TD" );
            CompareAlgorithm.statistics( modelSeries3InputPLRInterpolateTD, rawSeries3 );
            CompareAlgorithm.printResults();
            experiementTD3.putAvg( CompareAlgorithm.getStatsMap().get( CompareAlgorithm.MEAN_DELTA ) );
            experiementTD3.putMaxAvg( CompareAlgorithm.getStatsMap().get( CompareAlgorithm.MEAN_DELTA_MAX ) );
            experiementTD3.putMinAvg( CompareAlgorithm.getStatsMap().get( CompareAlgorithm.MEAN_DELTA_MIN ) );
            experiementTD3
                    .putStandardDeviation( CompareAlgorithm.getStatsMap().get( CompareAlgorithm.STANDARD_DEVIATION ) );

        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    @Test
    public void segmentReconstructionTest() {
        try {
            Logger.getLogger( getClass() ).debug( "seriesBU1" );
            CompareAlgorithm.statistics( seriesBU1, inputSeries1 );
            CompareAlgorithm.printResults();
            experiementBU1.putInputAvg( CompareAlgorithm.getStatsMap().get( CompareAlgorithm.MEAN_DELTA ) );
            experiementBU1.putInputMaxAvg( CompareAlgorithm.getStatsMap().get( CompareAlgorithm.MEAN_DELTA_MAX ) );
            experiementBU1.putInputMinAvg( CompareAlgorithm.getStatsMap().get( CompareAlgorithm.MEAN_DELTA_MIN ) );
            experiementBU1.putInputStandardDeviation(
                    CompareAlgorithm.getStatsMap().get( CompareAlgorithm.STANDARD_DEVIATION ) );

            Logger.getLogger( getClass() ).debug( "seriesBU2" );
            CompareAlgorithm.statistics( seriesBU2, inputSeries2 );
            CompareAlgorithm.printResults();
            experiementBU2.putInputAvg( CompareAlgorithm.getStatsMap().get( CompareAlgorithm.MEAN_DELTA ) );
            experiementBU2.putInputMaxAvg( CompareAlgorithm.getStatsMap().get( CompareAlgorithm.MEAN_DELTA_MAX ) );
            experiementBU2.putInputMinAvg( CompareAlgorithm.getStatsMap().get( CompareAlgorithm.MEAN_DELTA_MIN ) );
            experiementBU2.putInputStandardDeviation(
                    CompareAlgorithm.getStatsMap().get( CompareAlgorithm.STANDARD_DEVIATION ) );

            Logger.getLogger( getClass() ).debug( "seriesBU3" );
            CompareAlgorithm.statistics( seriesBU3, inputSeries3 );
            CompareAlgorithm.printResults();
            experiementBU3.putInputAvg( CompareAlgorithm.getStatsMap().get( CompareAlgorithm.MEAN_DELTA ) );
            experiementBU3.putInputMaxAvg( CompareAlgorithm.getStatsMap().get( CompareAlgorithm.MEAN_DELTA_MAX ) );
            experiementBU3.putInputMinAvg( CompareAlgorithm.getStatsMap().get( CompareAlgorithm.MEAN_DELTA_MIN ) );
            experiementBU3.putInputStandardDeviation(
                    CompareAlgorithm.getStatsMap().get( CompareAlgorithm.STANDARD_DEVIATION ) );

            Logger.getLogger( getClass() ).debug( "seriesTD1" );
            CompareAlgorithm.statistics( seriesTD1, inputSeries1 );
            CompareAlgorithm.printResults();
            experiementTD1.putInputAvg( CompareAlgorithm.getStatsMap().get( CompareAlgorithm.MEAN_DELTA ) );
            experiementTD1.putInputMaxAvg( CompareAlgorithm.getStatsMap().get( CompareAlgorithm.MEAN_DELTA_MAX ) );
            experiementTD1.putInputMinAvg( CompareAlgorithm.getStatsMap().get( CompareAlgorithm.MEAN_DELTA_MIN ) );
            experiementTD1.putInputStandardDeviation(
                    CompareAlgorithm.getStatsMap().get( CompareAlgorithm.STANDARD_DEVIATION ) );

            Logger.getLogger( getClass() ).debug( "seriesTD2" );
            CompareAlgorithm.statistics( seriesTD2, inputSeries2 );
            CompareAlgorithm.printResults();
            experiementTD2.putInputAvg( CompareAlgorithm.getStatsMap().get( CompareAlgorithm.MEAN_DELTA ) );
            experiementTD2.putInputMaxAvg( CompareAlgorithm.getStatsMap().get( CompareAlgorithm.MEAN_DELTA_MAX ) );
            experiementTD2.putInputMinAvg( CompareAlgorithm.getStatsMap().get( CompareAlgorithm.MEAN_DELTA_MIN ) );
            experiementTD2.putInputStandardDeviation(
                    CompareAlgorithm.getStatsMap().get( CompareAlgorithm.STANDARD_DEVIATION ) );

            Logger.getLogger( getClass() ).debug( "seriesTD3" );
            CompareAlgorithm.statistics( seriesTD3, inputSeries3 );
            CompareAlgorithm.printResults();
            experiementTD3.putInputAvg( CompareAlgorithm.getStatsMap().get( CompareAlgorithm.MEAN_DELTA ) );
            experiementTD3.putInputMaxAvg( CompareAlgorithm.getStatsMap().get( CompareAlgorithm.MEAN_DELTA_MAX ) );
            experiementTD3.putInputMinAvg( CompareAlgorithm.getStatsMap().get( CompareAlgorithm.MEAN_DELTA_MIN ) );
            experiementTD3.putInputStandardDeviation(
                    CompareAlgorithm.getStatsMap().get( CompareAlgorithm.STANDARD_DEVIATION ) );

            Logger.getLogger( getClass() ).debug( "seriesSW1" );
            CompareAlgorithm.statistics( seriesSW1, inputSeries1 );
            CompareAlgorithm.printResults();
            experiementSW1.putInputAvg( CompareAlgorithm.getStatsMap().get( CompareAlgorithm.MEAN_DELTA ) );
            experiementSW1.putInputMaxAvg( CompareAlgorithm.getStatsMap().get( CompareAlgorithm.MEAN_DELTA_MAX ) );
            experiementSW1.putInputMinAvg( CompareAlgorithm.getStatsMap().get( CompareAlgorithm.MEAN_DELTA_MIN ) );
            experiementSW1.putInputStandardDeviation(
                    CompareAlgorithm.getStatsMap().get( CompareAlgorithm.STANDARD_DEVIATION ) );

            Logger.getLogger( getClass() ).debug( "seriesSW2" );
            CompareAlgorithm.statistics( seriesSW2, inputSeries2 );
            CompareAlgorithm.printResults();
            experiementSW2.putInputAvg( CompareAlgorithm.getStatsMap().get( CompareAlgorithm.MEAN_DELTA ) );
            experiementSW2.putInputMaxAvg( CompareAlgorithm.getStatsMap().get( CompareAlgorithm.MEAN_DELTA_MAX ) );
            experiementSW2.putInputMinAvg( CompareAlgorithm.getStatsMap().get( CompareAlgorithm.MEAN_DELTA_MIN ) );
            experiementSW2.putInputStandardDeviation(
                    CompareAlgorithm.getStatsMap().get( CompareAlgorithm.STANDARD_DEVIATION ) );

            Logger.getLogger( getClass() ).debug( "seriesSW3" );
            CompareAlgorithm.statistics( seriesSW3, inputSeries3 );
            CompareAlgorithm.printResults();
            experiementSW3.putInputAvg( CompareAlgorithm.getStatsMap().get( CompareAlgorithm.MEAN_DELTA ) );
            experiementSW3.putInputMaxAvg( CompareAlgorithm.getStatsMap().get( CompareAlgorithm.MEAN_DELTA_MAX ) );
            experiementSW3.putInputMinAvg( CompareAlgorithm.getStatsMap().get( CompareAlgorithm.MEAN_DELTA_MIN ) );
            experiementSW3.putInputStandardDeviation(
                    CompareAlgorithm.getStatsMap().get( CompareAlgorithm.STANDARD_DEVIATION ) );
        } catch ( Exception e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @After
    public void writeResults() {
        experiementBU1.write();
        experiementBU2.write();
        experiementBU3.write();

        experiementTD1.write();
        experiementTD2.write();
        experiementTD3.write();

        experiementSW1.write();
        experiementSW2.write();
        experiementSW3.write();
    }

}
