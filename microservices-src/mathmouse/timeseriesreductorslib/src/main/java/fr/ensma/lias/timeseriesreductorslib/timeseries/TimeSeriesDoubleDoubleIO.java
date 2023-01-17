package fr.ensma.lias.timeseriesreductorslib.timeseries;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import fr.ensma.lias.timeseriesreductorslib.timeseries.interpolation.EInterpolationFunction;

public class TimeSeriesDoubleDoubleIO {
    public static final String SEPARATOR = "   ";
    public static final String DELIMITER = "";
    public static final String NEWLINE   = "\n";

    public static String write( TimeSeriesDoubleDouble series ) {
        StringBuffer buffer = new StringBuffer();
        buffer.append( DELIMITER + series.getInterpolationFunction().getInterpolationFunctionName().value() + DELIMITER
                + NEWLINE );
        for ( Double key : series.keySet() ) {
            buffer.append(
                    DELIMITER + key + DELIMITER + SEPARATOR + DELIMITER + series.get( key ) + DELIMITER + NEWLINE );
        }
        return buffer.toString();
    }

    public static void writeInFile( TimeSeriesDoubleDouble series, File file ) {
        try {
            BufferedWriter bw = new BufferedWriter( new FileWriter( file ) );
            bw.write( DELIMITER + series.getInterpolationFunction().getInterpolationFunctionName().value() + DELIMITER
                    + NEWLINE );
            for ( Double key : series.keySet() ) {
                bw.write(
                        DELIMITER + key + DELIMITER + SEPARATOR + DELIMITER + series.get( key ) + DELIMITER + NEWLINE );
                System.out.println( key + "   " + series.get( key ) );
            }
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }

    public static void writeInFile( TimeSeriesDoubleDouble series, String filePath ) {
        File file = new File( filePath );
        writeInFile( series, file );
    }

    public static TimeSeriesDoubleDouble read( String seriesString ) {
        TimeSeriesDoubleDouble series = new TimeSeriesDoubleDouble();
        String[] lines = seriesString.split( NEWLINE );

        series.setInterpolationFunction( EInterpolationFunction.getInterpolationFunction( lines[0] ) );

        for ( int i = 1; i < lines.length; i++ ) {
            String[] values = lines[i].split( SEPARATOR );
            series.put( Double.parseDouble( values[0] ), Double.parseDouble( values[1] ) );
        }

        return series;
    }

    public static TimeSeriesDoubleDouble read( File file ) {
        TimeSeriesDoubleDouble series = null;
        try {
            FileReader fr = new FileReader( file );
            BufferedReader bf = new BufferedReader( fr );
            String line;

            series = new TimeSeriesDoubleDouble();

            if ( ( line = bf.readLine() ) != null ) {
                series.setInterpolationFunction( EInterpolationFunction.getInterpolationFunction( line ) );
            }

            while ( ( line = bf.readLine() ) != null ) {
                String[] values = line.split( SEPARATOR );
                if ( values.length == 3 )
                    series.put( Double.parseDouble( values[0] ), Double.parseDouble( values[2] ) );
                else
                    series.put( Double.parseDouble( values[0] ), Double.parseDouble( values[1] ) );
            }
        } catch ( IOException e ) {
            e.printStackTrace();
        } finally {
            return series;
        }
    }

    public static TimeSeriesDoubleDouble readFromFile( String filePath ) {
        File file = new File( filePath );
        return read( file );
    }

}
