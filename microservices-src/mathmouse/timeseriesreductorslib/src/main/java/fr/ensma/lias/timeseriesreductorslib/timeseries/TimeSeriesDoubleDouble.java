package fr.ensma.lias.timeseriesreductorslib.timeseries;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.log4j.Logger;

import fr.ensma.lias.timeseriesreductorslib.timeseries.interpolation.EInterpolationFunction;
import fr.ensma.lias.timeseriesreductorslib.timeseries.interpolation.IInterpolationFunction;

/**
 * This class defines a TimeSeries<Long, Double> object, inheriting from
 * AbstractTimeSeries.
 * 
 * @author cyrille ponchateau (cyrille.ponchateau@ensma.fr)
 * @see reductor.AbstractTimeSeries
 *
 */
public class TimeSeriesDoubleDouble extends AbstractTimeSeries<Double, Double> implements ITimeSeriesFileReader {

    /**
     * Default constructor, calling constructor from superclass.
     */
    public TimeSeriesDoubleDouble() {
        super();
    }

    /**
     * Time Series with interpolation function
     * 
     * @param function
     */
    public TimeSeriesDoubleDouble( IInterpolationFunction function ) {
        super( function );
    }

    public TimeSeriesDoubleDouble( EInterpolationFunction interpolationMethodName ) {
        super( interpolationMethodName );
    }

    /**
     * Constructor from csv formatted file.
     * 
     * @param fileName
     * @param separator
     * @param delimiter
     */
    @Deprecated
    public TimeSeriesDoubleDouble( String fileName, String separator, String delimiter ) {
        try {
            getSeriesFromFile( fileName, separator, delimiter );
            // Logger.getLogger( getClass() ).debug( this );
            // Logger.getLogger( getClass() ).info( "Series dimension: " +
            // this.size() );
        } catch ( FileNotFoundException e ) {
            e.printStackTrace();
        }
    }

    /**
     * 
     * Builds a time series from data contained in a csv-like file. The file may
     * contain several time series, seriesIndex specifies which series has to be
     * loaded.
     * 
     * @param fileName
     * @param separator
     * @param delimiter
     * @param seriesIndex
     */
    @Deprecated
    public TimeSeriesDoubleDouble( String fileName, String separator, String delimiter, int seriesIndex ) {
        try {
            getSeriesFromFile( fileName, separator, delimiter, seriesIndex );
            // Logger.getLogger( getClass() ).debug( this );
            // Logger.getLogger( getClass() ).debug( "Series dimension: " +
            // dimension );
        } catch ( FileNotFoundException e ) {
            e.printStackTrace();
        }
    }

    public TimeSeriesDoubleDouble get( Double first, Double last ) {
        Double currentKey = first;
        TimeSeriesDoubleDouble result = new TimeSeriesDoubleDouble();
        while ( currentKey != null && currentKey <= last ) {
            result.put( currentKey, this.get( currentKey ) );
            currentKey = this.higherKey( currentKey );
        }
        return result;
    }

    @Override
    public void getSeriesFromFile( String fileName, String separator, String delimiter ) throws FileNotFoundException {
        getSeriesFromFile( fileName, separator, delimiter, 0 );
    }

    @Override
    public void getSeriesFromFile( String fileName, String separator, String delimiter, int seriesIndex )
            throws FileNotFoundException, ArrayIndexOutOfBoundsException {
        // get file from its name
        File seriesFile = new File( fileName );
        // Logger.getLogger( getClass() ).debug( seriesFile.getAbsolutePath() );

        FileReader fr = null;
        // used to store data flow from the file
        BufferedReader br = null;

        try {
            // connecting reader to buffer
            fr = new FileReader( seriesFile );
            br = new BufferedReader( fr );

            // reading number of TS from the first line of the file
            String numberOfTSString = br.readLine();
            int numberOfTS = Integer.parseInt( numberOfTSString );

            if ( numberOfTS != 0 ) {
                // going through the lines of the file
                for ( String line = br.readLine(); line != null; line = br.readLine() ) {
                    // splitting data from the line, using the separator symbol
                    String[] aData = line.split( separator );
                    try {
                        this.put( Double.parseDouble( aData[0] ),
                                Double.parseDouble( aData[1 + seriesIndex] ) );
                    } catch ( NumberFormatException e ) {
                        e.printStackTrace();
                    } catch ( Exception e ) {
                        e.printStackTrace();
                    }
                }
            }
        } catch ( IOException e ) {
            Logger.getLogger( getClass() ).debug( "Attempted to read the file, but an error occured" );
            e.printStackTrace();
        } finally {// even if the code breaks, the program will (or will try)
                   // close the buffer and file reader.
            try {
                if ( br != null )
                    br.close();
                if ( fr != null )
                    fr.close();
            } catch ( IOException e ) {
                Logger.getLogger( getClass() ).debug( "Couldn't close buffer or file reader." );
                e.printStackTrace();
            }
        }
    }

    /**
     * Prints the series in the format: Timestamp: value
     */
    public void printSeries() {
        if ( this.size() != 0 ) {
            Logger.getLogger( getClass() )
                    .debug( "The series contains: " + this.size() + " élément" + ( this.size() == 1 ? "." : "s." ) );
            for ( Double key : this.keySet() ) {
                Logger.getLogger( getClass() ).debug( key + ": " + this.get( key ) );
            }
        } else {
            Logger.getLogger( getClass() ).debug( "The series is empty" );
        }
    }

    public TimeSeriesDoubleDouble clone() {
        TimeSeriesDoubleDouble clone = new TimeSeriesDoubleDouble();

        for ( Double key : this.keySet() )
            clone.put( key, this.get( key ) );

        return clone;
    }

}
