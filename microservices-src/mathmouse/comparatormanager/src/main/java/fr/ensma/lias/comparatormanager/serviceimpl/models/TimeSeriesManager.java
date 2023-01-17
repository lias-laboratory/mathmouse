package fr.ensma.lias.comparatormanager.serviceimpl.models;

import java.util.HashMap;
import java.util.Map;

import fr.ensma.lias.timeseriesreductorslib.timeseries.TimeSeriesDoubleDouble;

/**
 * Handles a set of series involved in a comparison process.
 * 
 * @author Cyrille Ponchateau (cyrille.ponchateau@ensma.fr)
 *
 */
public class TimeSeriesManager {
    private Map<Integer, ComparisonContextData> comparisonData;

    // pattern singleton
    private static TimeSeriesManager            timeSeriesManager;

    private TimeSeriesManager() {
        comparisonData = new HashMap<Integer, ComparisonContextData>();
    }

    public static TimeSeriesManager getInstance() {
        if ( timeSeriesManager == null )
            timeSeriesManager = new TimeSeriesManager();
        return timeSeriesManager;
    }

    public void put( int hash, TimeSeriesDoubleDouble series ) {
        comparisonData.put( hash, new ComparisonContextData( series ) );
    }

    public void setExpectedComparisons( int hash, int expectedComparisons ) {
        if ( comparisonData.containsKey( hash ) ) {
            comparisonData.get( hash ).setExpectedComparisons( expectedComparisons );
        }
    }

    public TimeSeriesDoubleDouble getSeriesByHash( int hash ) {
        if ( comparisonData.containsKey( hash ) )
            return comparisonData.get( hash ).getSeries();
        else
            return null;
    }

    public void remove( int hash ) {
        comparisonData.remove( hash );
    }

    /**
     * Increases the number of performed comparison of the series identified by hash. If the number
     * has reached the number of expected comparisons, it is deleted.
     * 
     * @param hash
     */
    public void newComparisonPerformed( int hash ) {
        // checks if the hash exists
        if ( comparisonData.containsKey( hash ) ) {
            // increases the number of comparison performed so far
            comparisonData.get( hash ).incremente();
            // if the number of comparisons has reached the number of expected
            // comparisons, the comparison process of the series is over and the
            // data are deleted.
            if ( comparisonData.get( hash ).operationOver() )
                comparisonData.remove( hash );
        }
    }

}
