package fr.ensma.lias.comparatormanager.serviceimpl;

import fr.ensma.lias.comparatormanager.serviceimpl.models.TimeSeriesManager;
import fr.ensma.lias.dockermanagerapi.enumerations.EServiceName;
import fr.ensma.lias.javarabbitmqapi.RabbitMQBasicSender;
import fr.ensma.lias.javarabbitmqapi.enumerations.EQueueName;
import fr.ensma.lias.javarabbitmqapi.enumerations.ESpecialCharacter;
import fr.ensma.lias.timeseriesreductorslib.timeseries.TimeSeriesDoubleDouble;
import fr.ensma.lias.timeseriesreductorslib.timeseries.TimeSeriesDoubleDoubleIO;

/**
 * Implements the functionalities available in the service.
 * 
 * @author Cyrille Ponchateau (cyrille.ponchateau@ensma.fr)
 *
 */
public class Actions {
    private static final int         HASH_INDEX                 = 0;
    private static final int         ID_INDEX                   = 1;
    private static final int         EXPECTED_COMPARISONS_INDEX = 2;
    private static final int         NAME_INDEX                 = 3;
    private static final int         FORMULA_INDEX              = 4;

    // handles the series currently processed
    private static TimeSeriesManager timeSeriesManager;

    // necessary for singleton pattern conception
    private static Actions           actions;

    private RabbitMQBasicSender      sender;

    private Actions( RabbitMQBasicSender sender ) {
        this.sender = sender;
        timeSeriesManager = TimeSeriesManager.getInstance();
    }

    public static void initialize( RabbitMQBasicSender sender ) {
        actions = new Actions( sender );
    }

    public static Actions getInstance() {
        return actions;
    }

    public void putNewSeries( String message ) {
        message = message.replace( "\r", "" );
        // reading the hash code at the beginnig of the message
        String hashString = message.substring( 0, message.indexOf( ESpecialCharacter.NEWLINE.value() ) );
        int hash = Integer.parseInt( hashString );
        // reading the series contained in the message
        String seriesString = message.substring( message.indexOf( ESpecialCharacter.NEWLINE.value() ) + 1,
                message.length() - 1 );
        TimeSeriesDoubleDouble series = TimeSeriesDoubleDoubleIO.read( seriesString );
        // adding series to series manager
        timeSeriesManager.put( hash, series );
        // sending generation request, the message is attached to the hash code,
        // that will be used to know which series the message is about. The hash
        // code is used as an identifier of the series during the whole process.
        sender.publish( EQueueName.GENERATE_REQUEST_QUEUE_NAME, String.valueOf( hash ) );
        sender.publish( EQueueName.PROGRESS,
                EServiceName.COMPARATOR_MANAGER.value() + ESpecialCharacter.SEPARATOR.value() + hash );
    }

    public void checkComparisonState( String message ) {
        message = message.replace( "\r", "" );
        int hash = Integer.parseInt( message );
        // for each series, a particuliar number of comparison operations is
        // awaited. The manager keeps tack of this number and automatically
        // delete the series, when the number is reached.
        timeSeriesManager.newComparisonPerformed( hash );
    }

    public void dispatchComparisonData( String message ) {
        message = message.replace( "\r", "" );
        // reads the first line to extract the hash and expected comparisons
        // number
        String[] metadata = message.substring( 0, message.indexOf( ESpecialCharacter.NEWLINE.value() ) )
                .split( ESpecialCharacter.SEPARATOR.value() );
        int hash = Integer.parseInt( metadata[HASH_INDEX] );
        int expectedComparisons = Integer.parseInt( metadata[EXPECTED_COMPARISONS_INDEX] );
        // sets the expected comparisons number, with the received number
        timeSeriesManager.setExpectedComparisons( hash, expectedComparisons );
        // gets the series identified by the received hash
        TimeSeriesDoubleDouble rawSeries = timeSeriesManager.getSeriesByHash( hash );
        // forward the comparison data to the comparison service(s), adding the
        // original series data to the received generated series data
        String forward = buildForwardMessage( metadata, message, rawSeries );
        sender.publish( EQueueName.COMPARISON_DISPATCH, forward );
        sender.publish( EQueueName.PROGRESS,
                EServiceName.COMPARATOR_MANAGER.value() + ESpecialCharacter.SEPARATOR.value() + hash );
    }

    private String buildForwardMessage( String[] metadata, String message, TimeSeriesDoubleDouble rawSeries ) {
        // isolates the lines of the series into a table, in order to access the
        // size of the series
        String[] generatedSeries = message
                .substring( message.indexOf( ESpecialCharacter.NEWLINE.value() ) + 1, message.length() - 1 )
                .split( ESpecialCharacter.NEWLINE.value() );
        String text = "";
        // first lines is metadata line : hash;id;expectedNumber;name;formula
        text = text + metadata[HASH_INDEX] + ESpecialCharacter.SEPARATOR.value()
                + metadata[ID_INDEX] + ESpecialCharacter.SEPARATOR.value()
                + metadata[EXPECTED_COMPARISONS_INDEX] + ESpecialCharacter.SEPARATOR.value()
                + metadata[NAME_INDEX] + ESpecialCharacter.SEPARATOR.value()
                + metadata[FORMULA_INDEX] + ESpecialCharacter.NEWLINE.value();
        // next line is generated series size
        text = text + ( generatedSeries.length - 1 ) + ESpecialCharacter.NEWLINE.value();
        // next lines are the generated series data
        text = text + message
                .substring( message.indexOf( ESpecialCharacter.NEWLINE.value() ) + 1, message.length() - 1 )
                + ESpecialCharacter.NEWLINE.value();
        // next line is the raw original series size
        text = text + rawSeries.size() + ESpecialCharacter.NEWLINE.value();
        // next lines are the original series data
        text = text + TimeSeriesDoubleDoubleIO.write( rawSeries ) + ESpecialCharacter.NEWLINE.value();
        return text;
    }

    public void deleteJob( String message ) {
        message = message.replace( "/r", "" );

        int hash = Integer.parseInt( message );

        timeSeriesManager.remove( hash );
    }
}
