package fr.ensma.lias.timeseriesreductorslib;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.HashMap;

import org.apache.log4j.Logger;

/**
 * 
 * This class is similar to the Experiment class, but adds information about how
 * the series behave regarding the input.
 * 
 * @see fr.ensma.lias.timeseriesreductorlib.Experiment
 * @author cyrille ponchateau (ponchateau@ensma.fr)
 *
 */
public class ExperimentWithInput extends Experiment {

    public HashMap<String, Double> inputResults;

    public ExperimentWithInput( String name ) {
        super( name );
        inputResults = new HashMap<String, Double>();
        inputResults.put( name, (double) System.currentTimeMillis() );
    }

    public HashMap<String, Double> getInputResults() {
        return inputResults;
    }

    public void putInputAvg( Double value ) {
        inputResults.put( AVG, value );
    }

    public void putInputMinAvg( Double value ) {
        inputResults.put( MIN_AVG, value );
    }

    public void putInputMaxAvg( Double value ) {
        inputResults.put( MAX_AVG, value );
    }

    public void putInputStandardDeviation( Double value ) {
        inputResults.put( STANDARD_DEVIATION, value );
    }

    public void putInputTime( Double value ) {
        inputResults.put( TIME, value );
    }

    public void putInputNumberOfElements( Double value ) {
        inputResults.put( NUMBER_OF_ELEMENTS, value );
    }

    public void putInputRatio( Double value ) {
        inputResults.put( RATIO, value );
    }

    public void putInputMemorySize( Double value ) {
        inputResults.put( MEMORY_SIZE, value );
        putInputRatio();
    }

    public void putInputOriginalMemorySize( Double value ) {
        inputResults.put( ORIGINAL_MEMORY_SIZE, value );
        putInputRatio();
    }

    protected void putInputRatio() {
        if ( inputResults.containsKey( ORIGINAL_MEMORY_SIZE )
                && inputResults.get( ORIGINAL_MEMORY_SIZE ) != null && inputResults.containsKey( MEMORY_SIZE )
                && inputResults.get( MEMORY_SIZE ) != null ) {
            Double ratio = ( Math
                    .abs( inputResults.get( ORIGINAL_MEMORY_SIZE ) - inputResults.get( MEMORY_SIZE ) ) * 100 )
                    / inputResults.get( ORIGINAL_MEMORY_SIZE );
            inputResults.put( RATIO, ratio );
        }
    }

    /**
     * Writes itself in a file named: name+date
     */
    @Override
    public void write() {
        String[] keyTab = { name, NUMBER_OF_ELEMENTS, MEMORY_SIZE, ORIGINAL_MEMORY_SIZE, RATIO, TIME, AVG, MIN_AVG,
                MAX_AVG, STANDARD_DEVIATION };

        Timestamp expDate = new Timestamp( (long) Math.round( experimentResults.get( name ) ) );
        File file = new File( SAVE_DIRECTORY_PATH + name + "-"
                + expDate.toString().replaceAll( ":", "-" ).replaceAll( " ", "-" ) + ".csv" );
        FileWriter fr = null;
        BufferedWriter br = null;

        try {
            fr = new FileWriter( file );
            br = new BufferedWriter( fr );
            String line;
            line = "Equation vs raw data\n";
            br.write( line );
            for ( int i = 0; i < keyTab.length; i++ ) {
                line = keyTab[i] + ":" + experimentResults.get( keyTab[i] ) + "\n";
                br.write( line );
            }

            line = "\n";
            br.write( line );

            line = "input vs raw input data\n";
            br.write( line );

            if ( !inputResults.containsKey( TIME ) || inputResults.get( TIME ) == null ) {
                inputResults.put( TIME, -1.0 );
            }

            for ( int i = 0; i < keyTab.length; i++ ) {
                line = keyTab[i] + ":" + inputResults.get( keyTab[i] ) + "\n";
                br.write( line );
            }

            Double aggregatedSize = experimentResults.get( MEMORY_SIZE ) + inputResults.get( MEMORY_SIZE );
            Double aggregatedRatio = ( Math.abs( experimentResults.get( ORIGINAL_MEMORY_SIZE ) - aggregatedSize )
                    * 100 )
                    / experimentResults.get( ORIGINAL_MEMORY_SIZE );

            line = "\n";
            br.write( line );

            line = "aggregated results\n";
            br.write( line );

            line = "aggregated size:" + aggregatedSize + "\n";
            br.write( line );

            line = "aggregated ratio:" + aggregatedRatio + "\n";
            br.write( line );
            Logger.getLogger( getClass() ).debug( "wrote in: " + file.getAbsolutePath() );
        } catch ( IOException e ) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
                fr.close();
            } catch ( IOException e ) {
                e.printStackTrace();
            }
        }
    }
}
