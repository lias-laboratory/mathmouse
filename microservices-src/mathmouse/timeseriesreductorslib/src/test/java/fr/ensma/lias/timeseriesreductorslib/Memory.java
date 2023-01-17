package fr.ensma.lias.timeseriesreductorslib;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.DifferentialEquation;
import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.DifferentialEquation2;
import fr.ensma.lias.timeseriesreductorslib.reductors.segmentors.plr.interpolation.PLRInterpolateSegment;
import fr.ensma.lias.timeseriesreductorslib.reductors.segmentors.plr.interpolation.PLRInterpolateSegments;
import fr.ensma.lias.timeseriesreductorslib.reductors.segmentors.plr.regression.PLRRegressionSegment;
import fr.ensma.lias.timeseriesreductorslib.reductors.segmentors.plr.regression.PLRRegressionSegments;
import fr.ensma.lias.timeseriesreductorslib.timeseries.TimeSeriesDoubleDouble;

public class Memory {
    private static final String  STATIC_LOGGER = "STATIC_LOGGER:Memory";

    private static final Runtime s_runtime     = Runtime.getRuntime();

    public static int printDifferentialEquationSize( DifferentialEquation2 equation ) throws Exception {
        // Warm up all classes/methods we will use
        runGC();
        usedMemory();
        // Array to keep strong references to allocated objects
        final int count = 5000;
        Logger.getRootLogger().info( "declaring objects array list" );
        ArrayList<DifferentialEquation2> objects = new ArrayList<DifferentialEquation2>();

        long heap1 = 0;
        // Allocate count+1 objects, discard the first one
        Logger.getRootLogger().info( "allocating objects" );
        for ( int i = -1; i < count; ++i ) {
            if ( i % 1000 == 0 )
                Logger.getRootLogger().info( i );
            // Instantiate your data here and assign it to object

            DifferentialEquation2 object = new DifferentialEquation2( equation );
            object.setInputFunctions( null );

            if ( i >= 0 )
                objects.add( object );
            else {
                object = null; // Discard the warm up object
                runGC();
                heap1 = usedMemory(); // Take a before heap snapshot
            }
        }

        runGC();

        long heap2 = usedMemory(); // Take an after
                                   // heap snapshot:

        final int size = Math.round( ( (float) ( heap2 - heap1 ) )
                / count );
        Logger.getLogger( STATIC_LOGGER ).debug( "'before' heap: " + heap1 + ", 'after' heap: " + heap2 );
        Logger.getLogger( STATIC_LOGGER )
                .debug( "heap delta: " + ( heap2 - heap1 ) + ", {" + objects.get( 0 ).getClass() + "} size = "
                        + size + " bytes" );
        // for ( int i = 0; i < count; ++i )
        objects.clear();
        objects = null;

        return size;
    }

    private static int emptyDifferentialEquationSize2() throws Exception {
        // Warm up all classes/methods we will use
        runGC();
        usedMemory();
        // Array to keep strong references to allocated objects
        final int count = 5000;
        Logger.getRootLogger().info( "declaring objects array list" );
        ArrayList<DifferentialEquation2> objects = new ArrayList<DifferentialEquation2>();

        long heap1 = 0;
        // Allocate count+1 objects, discard the first one
        Logger.getRootLogger().info( "allocating objects" );
        for ( int i = -1; i < count; ++i ) {
            if ( i % 1000 == 0 )
                Logger.getRootLogger().info( i );
            // Instantiate your data here and assign it to object

            DifferentialEquation2 object = new DifferentialEquation2();
            object.setInputFunctions( null );

            if ( i >= 0 )
                objects.add( object );
            else {
                object = null; // Discard the warm up object
                runGC();
                heap1 = usedMemory(); // Take a before heap snapshot
            }
        }

        runGC();

        long heap2 = usedMemory(); // Take an after
                                   // heap snapshot:

        final int size = Math.round( ( (float) ( heap2 - heap1 ) )
                / count );
        Logger.getLogger( STATIC_LOGGER ).debug( "'before' heap: " + heap1 + ", 'after' heap: " + heap2 );
        Logger.getLogger( STATIC_LOGGER )
                .debug( "heap delta: " + ( heap2 - heap1 ) + ", {" + objects.get( 0 ).getClass() + "} size = "
                        + size + " bytes" );
        // for ( int i = 0; i < count; ++i )
        objects.clear();
        objects = null;

        return size;
    }

    public static int printDifferentialEquationSize( DifferentialEquation equation ) throws Exception {
        // Warm up all classes/methods we will use
        runGC();
        usedMemory();
        // Array to keep strong references to allocated objects
        final int count = 5000;
        Logger.getRootLogger().info( "declaring objects array list" );
        ArrayList<DifferentialEquation> objects = new ArrayList<DifferentialEquation>();

        long heap1 = 0;
        // Allocate count+1 objects, discard the first one
        Logger.getRootLogger().info( "allocating objects" );
        for ( int i = -1; i < count; ++i ) {
            if ( i % 1000 == 0 )
                Logger.getRootLogger().info( i );
            // Instantiate your data here and assign it to object

            DifferentialEquation object = new DifferentialEquation( equation );
            object.setInputFunctions( null );

            if ( i >= 0 )
                objects.add( object );
            else {
                object = null; // Discard the warm up object
                runGC();
                heap1 = usedMemory(); // Take a before heap snapshot
            }
        }

        runGC();

        long heap2 = usedMemory(); // Take an after
                                   // heap snapshot:

        final int size = Math.round( ( (float) ( heap2 - heap1 ) )
                / count );
        Logger.getLogger( STATIC_LOGGER ).debug( "'before' heap: " + heap1 + ", 'after' heap: " + heap2 );
        Logger.getLogger( STATIC_LOGGER )
                .debug( "heap delta: " + ( heap2 - heap1 ) + ", {" + objects.get( 0 ).getClass() + "} size = "
                        + size + " bytes" );
        // for ( int i = 0; i < count; ++i )
        objects.clear();
        objects = null;

        return size;
    }

    @Deprecated
    private static int emptyDifferentialEquationSize() throws Exception {
        // Warm up all classes/methods we will use
        runGC();
        usedMemory();
        // Array to keep strong references to allocated objects
        final int count = 5000;
        Logger.getRootLogger().info( "declaring objects array list" );
        ArrayList<DifferentialEquation> objects = new ArrayList<DifferentialEquation>();

        long heap1 = 0;
        // Allocate count+1 objects, discard the first one
        Logger.getRootLogger().info( "allocating objects" );
        for ( int i = -1; i < count; ++i ) {
            if ( i % 1000 == 0 )
                Logger.getRootLogger().info( i );
            // Instantiate your data here and assign it to object

            DifferentialEquation object = new DifferentialEquation();
            object.setInputFunctions( null );

            if ( i >= 0 )
                objects.add( object );
            else {
                object = null; // Discard the warm up object
                runGC();
                heap1 = usedMemory(); // Take a before heap snapshot
            }
        }

        runGC();

        long heap2 = usedMemory(); // Take an after
                                   // heap snapshot:

        final int size = Math.round( ( (float) ( heap2 - heap1 ) )
                / count );
        Logger.getLogger( STATIC_LOGGER ).debug( "'before' heap: " + heap1 + ", 'after' heap: " + heap2 );
        Logger.getLogger( STATIC_LOGGER )
                .debug( "heap delta: " + ( heap2 - heap1 ) + ", {" + objects.get( 0 ).getClass() + "} size = "
                        + size + " bytes" );
        // for ( int i = 0; i < count; ++i )
        objects.clear();
        objects = null;

        return size;
    }

    public static int printSeriesSize( String seriesName, int seriesIndex ) throws Exception {
        // Warm up all classes/methods we will use
        runGC();
        usedMemory();
        // Array to keep strong references to allocated objects
        final int count = 5000;
        Logger.getRootLogger().info( "declaring objects array list" );
        ArrayList<TimeSeriesDoubleDouble> objects = new ArrayList<TimeSeriesDoubleDouble>();

        Logger.getRootLogger().info( "declaring auxiliary time series" );
        TimeSeriesDoubleDouble aux = new TimeSeriesDoubleDouble( seriesName, "   ", "", seriesIndex );

        long heap1 = 0;
        // Allocate count+1 objects, discard the first one
        Logger.getRootLogger().info( "allocating objects" );
        for ( int i = -1; i < count; ++i ) {
            if ( i % 1000 == 0 )
                Logger.getRootLogger().info( i );
            // Instantiate your data here and assign it to object

            TimeSeriesDoubleDouble object = new TimeSeriesDoubleDouble();
            for ( Double key : aux.keySet() )
                object.put( key, aux.get( key ) );

            if ( i >= 0 )
                objects.add( object );
            else {
                object = null; // Discard the warm up object
                runGC();
                heap1 = usedMemory(); // Take a before heap snapshot
            }
        }

        runGC();

        long heap2 = usedMemory(); // Take an after
                                   // heap snapshot:

        final int size = Math.round( ( (float) ( heap2 - heap1 ) )
                / count );
        Logger.getLogger( STATIC_LOGGER ).debug( "'before' heap: " + heap1 + ", 'after' heap: " + heap2 );
        Logger.getLogger( STATIC_LOGGER )
                .debug( "heap delta: " + ( heap2 - heap1 ) + ", {" + objects.get( 0 ).getClass() + "} size = "
                        + size + " bytes" );
        // for ( int i = 0; i < count; ++i )
        objects.clear();
        objects = null;

        return size;
    }

    public static int printSeriesSize( TimeSeriesDoubleDouble series ) throws Exception {
        // Warm up all classes/methods we will use
        runGC();
        usedMemory();
        // Array to keep strong references to allocated objects
        final int count = 5000;
        ArrayList<TimeSeriesDoubleDouble> objects = new ArrayList<TimeSeriesDoubleDouble>();

        long heap1 = 0;
        // Allocate count+1 objects, discard the first one
        for ( int i = -1; i < count; ++i ) {
            if ( i % 1000 == 0 )
                Logger.getRootLogger().info( i );
            // Instantiate your data here and assign it to object

            TimeSeriesDoubleDouble object = new TimeSeriesDoubleDouble();
            for ( Double key : series.keySet() )
                object.put( key, series.get( key ) );

            if ( i >= 0 )
                objects.add( object );
            else {
                object = null; // Discard the warm up object
                runGC();
                heap1 = usedMemory(); // Take a before heap snapshot
            }
        }

        runGC();

        long heap2 = usedMemory(); // Take an after
                                   // heap snapshot:

        final int size = Math.round( ( (float) ( heap2 - heap1 ) )
                / count );
        Logger.getLogger( STATIC_LOGGER ).debug( "'before' heap: " + heap1 + ", 'after' heap: " + heap2 );
        Logger.getLogger( STATIC_LOGGER )
                .debug( "heap delta: " + ( heap2 - heap1 ) + ", {" + objects.get( 0 ).getClass() + "} size = "
                        + size + " bytes" );
        // for ( int i = 0; i < count; ++i )
        objects.clear();
        objects = null;

        return size;
    }

    @Deprecated
    private static int emptySeriesSize() throws Exception {
        // Warm up all classes/methods we will use
        runGC();
        usedMemory();
        // Array to keep strong references to allocated objects
        final int count = 5000;
        ArrayList<TimeSeriesDoubleDouble> objects = new ArrayList<TimeSeriesDoubleDouble>();

        long heap1 = 0;
        // Allocate count+1 objects, discard the first one
        for ( int i = -1; i < count; ++i ) {
            if ( i % 1000 == 0 )
                Logger.getRootLogger().info( i );
            // Instantiate your data here and assign it to object

            TimeSeriesDoubleDouble object = new TimeSeriesDoubleDouble();

            if ( i >= 0 )
                objects.add( object );
            else {
                object = null; // Discard the warm up object
                runGC();
                heap1 = usedMemory(); // Take a before heap snapshot
            }
        }

        runGC();

        long heap2 = usedMemory(); // Take an after
                                   // heap snapshot:

        final int size = Math.round( ( (float) ( heap2 - heap1 ) )
                / count );
        Logger.getLogger( STATIC_LOGGER ).debug( "'before' heap: " + heap1 + ", 'after' heap: " + heap2 );
        Logger.getLogger( STATIC_LOGGER )
                .debug( "heap delta: " + ( heap2 - heap1 ) + ", {" + objects.get( 0 ).getClass() + "} size = "
                        + size + " bytes" );
        // for ( int i = 0; i < count; ++i )
        objects.clear();
        objects = null;

        return size;
    }

    public static int printSegmentSize( PLRInterpolateSegments segments ) throws Exception {
        Logger.getLogger( STATIC_LOGGER ).debug( "segments size: " + segments.size() );
        // Warm up all classes/methods we will use
        runGC();
        usedMemory();
        // Array to keep strong references to allocated objects
        final int count = 5000;
        Logger.getRootLogger().info( "declaring objects array list" );
        ArrayList<PLRInterpolateSegments> objects = new ArrayList<PLRInterpolateSegments>();

        long heap1 = 0;
        // Allocate count+1 objects, discard the first one
        Logger.getRootLogger().info( "allocating objects" );
        for ( int i = -1; i < count; ++i ) {
            if ( i % 1000 == 0 )
                Logger.getRootLogger().info( i );
            // Instantiate your data here and assign it to object

            PLRInterpolateSegments object = new PLRInterpolateSegments();
            for ( Double key : segments.keySet() )
                object.put( key, new PLRInterpolateSegment( segments.get( key ).getLeftBound(),
                        segments.get( key ).getRightBound(), segments.get( key ).getLeftValue(),
                        segments.get( key ).getRightValue() ) );

            if ( i >= 0 )
                objects.add( object );
            else {
                object = null; // Discard the warm up object
                runGC();
                heap1 = usedMemory(); // Take a before heap snapshot
            }
        }

        runGC();

        long heap2 = usedMemory(); // Take an after
                                   // heap snapshot:

        final int size = Math.round( ( (float) ( heap2 - heap1 ) )
                / count );
        Logger.getLogger( STATIC_LOGGER ).debug( "'before' heap: " + heap1 + ", 'after' heap: " + heap2 );
        Logger.getLogger( STATIC_LOGGER )
                .debug( "heap delta: " + ( heap2 - heap1 ) + ", {" + objects.get( 0 ).getClass() + "} size = "
                        + size + " bytes" );
        // for ( int i = 0; i < count; ++i )
        objects.clear();
        objects = null;

        return size;
    }

    @Deprecated
    private static int emptyPLRInterpolateSize() throws Exception {
        // Warm up all classes/methods we will use
        runGC();
        usedMemory();
        // Array to keep strong references to allocated objects
        final int count = 5000;
        Logger.getRootLogger().info( "declaring objects array list" );
        ArrayList<PLRInterpolateSegments> objects = new ArrayList<PLRInterpolateSegments>();

        long heap1 = 0;
        // Allocate count+1 objects, discard the first one
        Logger.getRootLogger().info( "allocating objects" );
        for ( int i = -1; i < count; ++i ) {
            if ( i % 1000 == 0 )
                Logger.getRootLogger().info( i );
            // Instantiate your data here and assign it to object

            PLRInterpolateSegments object = new PLRInterpolateSegments();

            if ( i >= 0 )
                objects.add( object );
            else {
                object = null; // Discard the warm up object
                runGC();
                heap1 = usedMemory(); // Take a before heap snapshot
            }
        }

        runGC();

        long heap2 = usedMemory(); // Take an after
                                   // heap snapshot:

        final int size = Math.round( ( (float) ( heap2 - heap1 ) )
                / count );
        Logger.getLogger( STATIC_LOGGER ).debug( "'before' heap: " + heap1 + ", 'after' heap: " + heap2 );
        Logger.getLogger( STATIC_LOGGER )
                .debug( "heap delta: " + ( heap2 - heap1 ) + ", {" + objects.get( 0 ).getClass() + "} size = "
                        + size + " bytes" );
        // for ( int i = 0; i < count; ++i )
        objects.clear();
        objects = null;

        return size;
    }

    public static int printSegmentSize( PLRRegressionSegments segments ) throws Exception {
        Logger.getLogger( STATIC_LOGGER ).debug( "segments size: " + segments.size() );
        // Warm up all classes/methods we will use
        runGC();
        usedMemory();
        // Array to keep strong references to allocated objects
        final int count = 5000;
        Logger.getRootLogger().info( "declaring objects array list" );
        ArrayList<PLRRegressionSegments> objects = new ArrayList<PLRRegressionSegments>();

        long heap1 = 0;
        // Allocate count+1 objects, discard the first one
        Logger.getRootLogger().info( "allocating objects" );
        for ( int i = -1; i < count; ++i ) {
            if ( i % 1000 == 0 )
                Logger.getRootLogger().info( i );
            // Instantiate your data here and assign it to object

            PLRRegressionSegments object = new PLRRegressionSegments();
            for ( Double key : segments.keySet() )
                object.put( key, new PLRRegressionSegment( segments.get( key ).getLeftBound(),
                        segments.get( key ).getRightBound(), segments.get( key ).getSlope(),
                        segments.get( key ).getyIntersect() ) );

            if ( i >= 0 )
                objects.add( object );
            else {
                object = null; // Discard the warm up object
                runGC();
                heap1 = usedMemory(); // Take a before heap snapshot
            }
        }

        runGC();

        long heap2 = usedMemory(); // Take an after
                                   // heap snapshot:

        final int size = Math.round( ( (float) ( heap2 - heap1 ) )
                / count );
        Logger.getLogger( STATIC_LOGGER ).debug( "'before' heap: " + heap1 + ", 'after' heap: " + heap2 );
        Logger.getLogger( STATIC_LOGGER )
                .debug( "heap delta: " + ( heap2 - heap1 ) + ", {" + objects.get( 0 ).getClass() + "} size = "
                        + size + " bytes" );
        // for ( int i = 0; i < count; ++i )
        objects.clear();
        objects = null;

        return size;
    }

    @Deprecated
    private static int emptyPLRRegressionSize() throws Exception {
        // Warm up all classes/methods we will use
        runGC();
        usedMemory();
        // Array to keep strong references to allocated objects
        final int count = 5000;
        Logger.getRootLogger().info( "declaring objects array list" );
        ArrayList<PLRRegressionSegments> objects = new ArrayList<PLRRegressionSegments>();

        long heap1 = 0;
        // Allocate count+1 objects, discard the first one
        Logger.getRootLogger().info( "allocating objects" );
        for ( int i = -1; i < count; ++i ) {
            if ( i % 1000 == 0 )
                Logger.getRootLogger().info( i );
            // Instantiate your data here and assign it to object

            PLRRegressionSegments object = new PLRRegressionSegments();

            if ( i >= 0 )
                objects.add( object );
            else {
                object = null; // Discard the warm up object
                runGC();
                heap1 = usedMemory(); // Take a before heap snapshot
            }
        }

        runGC();

        long heap2 = usedMemory(); // Take an after
                                   // heap snapshot:

        final int size = Math.round( ( (float) ( heap2 - heap1 ) )
                / count );
        Logger.getLogger( STATIC_LOGGER ).debug( "'before' heap: " + heap1 + ", 'after' heap: " + heap2 );
        Logger.getLogger( STATIC_LOGGER )
                .debug( "heap delta: " + ( heap2 - heap1 ) + ", {" + objects.get( 0 ).getClass() + "} size = "
                        + size + " bytes" );
        // for ( int i = 0; i < count; ++i )
        objects.clear();
        objects = null;

        return size;
    }

    public static int printTableSize( double[] table ) throws Exception {
        Logger.getLogger( STATIC_LOGGER ).debug( "table length: " + table.length );
        // Warm up all classes/methods we will use
        runGC();
        usedMemory();
        // Array to keep strong references to allocated objects
        final int count = 5000;
        Logger.getRootLogger().info( "declaring objects array list" );
        ArrayList<double[]> objects = new ArrayList<double[]>();

        long heap1 = 0;
        // Allocate count+1 objects, discard the first one
        Logger.getRootLogger().info( "allocating objects" );
        for ( int i = -1; i < count; ++i ) {
            if ( i % 1000 == 0 )
                Logger.getRootLogger().info( i );
            // Instantiate your data here and assign it to object

            double[] object = new double[table.length];
            for ( int j = 0; j < table.length; j++ )
                object[j] = table[j];

            if ( i >= 0 )
                objects.add( object );
            else {
                object = null; // Discard the warm up object
                runGC();
                heap1 = usedMemory(); // Take a before heap snapshot
            }
        }

        runGC();

        long heap2 = usedMemory(); // Take an after
                                   // heap snapshot:

        final int size = Math.round( ( (float) ( heap2 - heap1 ) )
                / count );
        Logger.getLogger( STATIC_LOGGER ).debug( "'before' heap: " + heap1 + ", 'after' heap: " + heap2 );
        Logger.getLogger( STATIC_LOGGER )
                .debug( "heap delta: " + ( heap2 - heap1 ) + ", {" + objects.get( 0 ).getClass() + "} size = "
                        + size + " bytes" );
        // for ( int i = 0; i < count; ++i )
        objects.clear();
        objects = null;

        return size;
    }

    private static void runGC() throws Exception {
        // It helps to call Runtime.gc()
        // using several method calls:
        for ( int r = 0; r < 4; ++r )
            _runGC();
    }

    private static void _runGC() throws Exception {
        long usedMem1 = usedMemory(), usedMem2 = Long.MAX_VALUE;
        for ( int i = 0; ( usedMem1 < usedMem2 ) && ( i < 500 ); ++i ) {
            s_runtime.runFinalization();
            s_runtime.gc();
            Thread.currentThread().yield();

            usedMem2 = usedMem1;
            usedMem1 = usedMemory();
        }
    }

    private static long usedMemory() {
        return s_runtime.totalMemory() - s_runtime.freeMemory();
    }

}
