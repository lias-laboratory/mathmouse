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
 * Stores values from comparison between two series
 * 
 * @author cyrille ponchateau (ponchateau@ensma.fr)
 *
 */
public class Experiment {
    // name of the different parameters calculated for each experience.
    // each name defines a key for the map, that will store the results.
    protected static final String     AVG                  = "average error";
    protected static final String     MIN_AVG              = "minimum average error";
    protected static final String     MAX_AVG              = "maximum average error";
    protected static final String     STANDARD_DEVIATION   = "standard deviation";
    protected static final String     TIME                 = "generation time";
    protected static final String     NUMBER_OF_ELEMENTS   = "number of elements";
    /**
     * calculated attribute: ratio = ((orig mem size - mem size) / orig mem
     * size)*100
     */
    protected static final String     RATIO                = "compression ratio";
    protected static final String     MEMORY_SIZE          = "memory size";
    protected static final String     ORIGINAL_MEMORY_SIZE = "original memory size";

    protected static final String     SAVE_DIRECTORY_PATH  = "src/test/resources/experiments/results/";

    // name of the experience
    protected String                  name;

    // map of the results
    protected HashMap<String, Double> experimentResults;

    public Experiment( String name ) {
        this.name = name;
        experimentResults = new HashMap<String, Double>();
        experimentResults.put( name, (double) System.currentTimeMillis() );
    }

    public HashMap<String, Double> getExperiementResults() {
        return experimentResults;
    }

    public void putAvg( Double value ) {
        experimentResults.put( AVG, value );
    }

    public void putMinAvg( Double value ) {
        experimentResults.put( MIN_AVG, value );
    }

    public void putMaxAvg( Double value ) {
        experimentResults.put( MAX_AVG, value );
    }

    public void putStandardDeviation( Double value ) {
        experimentResults.put( STANDARD_DEVIATION, value );
    }

    public void putTime( Double value ) {
        experimentResults.put( TIME, value );
    }

    public void putNumberOfElements( Double value ) {
        experimentResults.put( NUMBER_OF_ELEMENTS, value );
    }

    public void putRatio( Double value ) {
        experimentResults.put( RATIO, value );
    }

    public void putMemorySize( Double value ) {
        experimentResults.put( MEMORY_SIZE, value );
        putRatio();
    }

    public void putOriginalMemorySize( Double value ) {
        experimentResults.put( ORIGINAL_MEMORY_SIZE, value );
        putRatio();
    }

    protected void putRatio() {
        if ( experimentResults.containsKey( ORIGINAL_MEMORY_SIZE )
                && experimentResults.get( ORIGINAL_MEMORY_SIZE ) != null && experimentResults.containsKey( MEMORY_SIZE )
                && experimentResults.get( MEMORY_SIZE ) != null ) {
            Double ratio = ( Math
                    .abs( experimentResults.get( ORIGINAL_MEMORY_SIZE ) - experimentResults.get( MEMORY_SIZE ) ) * 100 )
                    / experimentResults.get( ORIGINAL_MEMORY_SIZE );
            experimentResults.put( RATIO, ratio );
        }
    }

    /**
     * Writes itself in a file named: name+date
     */
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
            for ( int i = 0; i < keyTab.length; i++ ) {
                line = keyTab[i] + ":" + experimentResults.get( keyTab[i] ) + "\n";
                br.write( line );
            }
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
