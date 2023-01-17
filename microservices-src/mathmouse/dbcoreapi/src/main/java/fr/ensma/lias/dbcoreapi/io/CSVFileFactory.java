package fr.ensma.lias.dbcoreapi.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.slf4j.LoggerFactory;

import fr.ensma.lias.timeseriesreductorslib.timeseries.TimeSeriesDoubleDouble;
import fr.ensma.lias.timeseriesreductorslib.timeseries.interpolation.EInterpolationFunction;

/**
 * 
 * @author cyrille ponchateau (ponchateau@ensma.fr)
 *
 */
public class CSVFileFactory extends FileIOFactory {
    public static final String    DELIMITER = "";
    public static final String    SEPARATOR = ",";
    public static final String    NEWLINE   = "\n";

    private static CSVFileFactory factory   = new CSVFileFactory();

    private CSVFileFactory() {
    }

    public static CSVFileFactory getInstance() {
        return factory;
    }

    /**
     * 
     * @param series
     * @param filePath
     */
    public void writeSeriesInFile( TimeSeriesDoubleDouble series, String filePath ) {

        File file = null;
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            file = new File( filePath );
            LoggerFactory.getLogger( getClass() ).debug( "writing in " + file.getAbsolutePath() );
            fw = new FileWriter( file );
            bw = new BufferedWriter( fw );
            String line;

            bw.write( series.getInterpolationFunction().getInterpolationFunctionName().value() + NEWLINE );

            for ( Double key : series.keySet() ) {
                line = DELIMITER + key + DELIMITER + SEPARATOR + DELIMITER + series.get( key ) + DELIMITER + NEWLINE;
                bw.write( line );
            }
        } catch ( IOException e ) {
            e.printStackTrace();
        } finally {
            try {
                if ( bw != null )
                    bw.close();
                if ( fw != null )
                    fw.close();
            } catch ( IOException e ) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 
     * @param filePath
     * @return
     */
    public TimeSeriesDoubleDouble readSeriesFromFile( String filePath ) {
        FileReader fr = null;
        BufferedReader br = null;
        TimeSeriesDoubleDouble series = null;

        try {
            fr = new FileReader( filePath );
            br = new BufferedReader( fr );

            String line;
            series = new TimeSeriesDoubleDouble();

            line = br.readLine();
            series.setInterpolationFunction( EInterpolationFunction.getInterpolationFunction( line ) );

            while ( ( line = br.readLine() ) != null ) {
                String[] data = line.split( SEPARATOR );
                series.put( Double.parseDouble( data[0].replaceAll( DELIMITER, "" ) ),
                        Double.parseDouble( data[1].replaceAll( DELIMITER, "" ).replaceAll( NEWLINE, "" ) ) );
            }
            return series;
        } catch ( IOException e ) {
            e.printStackTrace();
            return series;
        } finally {
            try {
                if ( fr != null )
                    fr.close();
                if ( br != null )
                    br.close();
            } catch ( IOException e ) {
                e.printStackTrace();
            }
        }
    }

}
