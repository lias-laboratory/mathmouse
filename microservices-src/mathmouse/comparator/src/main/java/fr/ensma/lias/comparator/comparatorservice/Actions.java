package fr.ensma.lias.comparator.comparatorservice;

import fr.ensma.lias.javarabbitmqapi.RabbitMQBasicSender;
import fr.ensma.lias.javarabbitmqapi.enumerations.EQueueName;
import fr.ensma.lias.javarabbitmqapi.enumerations.ESpecialCharacter;
import fr.ensma.lias.timeseriesreductorslib.comparison.CompareAlgorithm;
import fr.ensma.lias.timeseriesreductorslib.timeseries.TimeSeriesDoubleDouble;
import fr.ensma.lias.timeseriesreductorslib.timeseries.interpolation.EInterpolationFunction;

/**
 * Implements all the actions available in the comparator service.
 * 
 * @author Cyrille Ponchateau (ponchateau@ensma.fr)
 *
 */
public class Actions {
    public static final int     METADATA_INDEX = 0;
    public static final int     HASH_INDEX     = 0;
    public static final int     ID_INDEX       = 1;
    public static final int     NUMBER_INDEX   = 2;
    public static final int     NAME_INDEX     = 3;
    public static final int     FORMULA_INDEX  = 4;

    private RabbitMQBasicSender sender;

    private static Actions      actions;

    private Actions( RabbitMQBasicSender sender ) {
        this.sender = sender;
    }

    public static void initialize( RabbitMQBasicSender sender ) {
        actions = new Actions( sender );
    }

    public static Actions getInstance() {
        return actions;
    }

    /**
     * 
     * Reads series metadata and values from a string and compares to the corresponding series
     * (matching series names). When the comparison process is done, the results are sent via
     * rabbitmq.
     * 
     * @param string
     */
    public void executeComparison( String message ) {
        try {
            message = message.replace( "\r", "" );
            int hash;
            long id;
            int numberOfExpectedSeries;
            String name;
            String formula;
            TimeSeriesDoubleDouble generatedSeries = new TimeSeriesDoubleDouble();
            TimeSeriesDoubleDouble rawSeries = new TimeSeriesDoubleDouble();

            // decomposing the message
            String[] lines = message.split( ESpecialCharacter.NEWLINE.value() );

            // reading metadata, first line of the message sent. Metadata items are seperated with a
            // semicolon
            String[] metadata = lines[METADATA_INDEX].split( ESpecialCharacter.SEPARATOR.value() );
            hash = Integer.parseInt( metadata[HASH_INDEX] );
            id = Long.parseLong( metadata[ID_INDEX] );
            numberOfExpectedSeries = Integer.parseInt( metadata[NUMBER_INDEX] );
            name = metadata[NAME_INDEX];
            formula = metadata[FORMULA_INDEX];

            // reading series data
            int currentIndex = 1;// keeps track of the number of lines read in the message
            int size;// size of the series to read
            // reading the generated series, returns the size of the read series
            // the series is read, from currentIndex line in the message and is saved in
            // generatedSeries
            size = readSeriesFromMessage( currentIndex, generatedSeries, lines );

            // reading the raw series
            currentIndex++;// current index needs to be incremented, because the number of lines to
                           // read contains one more line, for the size of the series to read
            currentIndex = currentIndex + size + 1;// the current index is incremented by the size,
                                                   // plus one, so the line pointed is just after
                                                   // the last read line.
            // the raw series can be read now
            size = readSeriesFromMessage( currentIndex, rawSeries, lines );

            // calculating statistics (average error and standard deviation between generated series
            // and raw series)
            CompareAlgorithm.leastSquareStatistics( generatedSeries, rawSeries );

            // sending results
            sender.publish( EQueueName.COMPARISON_RESPONSE_QUEUE_NAME,
                    Serializer.serializeResults( hash, id, numberOfExpectedSeries, name, formula,
                            CompareAlgorithm.getStatsMap() ) );
            sender.publish( EQueueName.COMPARISON_PERFORMED, String.valueOf( hash ) );
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    /**
     * Reads the time series (normally two in a raw) contained in the message.
     * 
     * @param currentIndex
     * @param series
     * @param lines
     * @return
     */
    private int readSeriesFromMessage( int currentIndex, TimeSeriesDoubleDouble series, String[] lines ) {
        int size = Integer.parseInt( lines[currentIndex] );

        // going one line further
        currentIndex++;
        series
                .setInterpolationFunction( EInterpolationFunction.getInterpolationFunction( lines[currentIndex] ) );
        // reading data from third line, the number of line to be read is equal
        // to the size parameter read above
        currentIndex++;
        for ( int i = currentIndex; i < currentIndex + size; i++ ) {
            String[] values = lines[i].split( "   " );
            series.put( Double.parseDouble( values[0] ), Double.parseDouble( values[1] ) );
        }

        return size;
    }
}
