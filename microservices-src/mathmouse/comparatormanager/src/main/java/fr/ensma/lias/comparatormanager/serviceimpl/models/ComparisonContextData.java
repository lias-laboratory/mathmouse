package fr.ensma.lias.comparatormanager.serviceimpl.models;

import fr.ensma.lias.timeseriesreductorslib.timeseries.TimeSeriesDoubleDouble;

/**
 * Each series will be associated to a context in the comparison service. Here,
 * the context contains two integers.
 * 
 * @author Cyrille Ponchateau (cyrille.ponchateau@ensma.fr)
 *
 */
public class ComparisonContextData {
    /**
     * The series to be compared with models of the database
     */
    private TimeSeriesDoubleDouble series;
    /**
     * Initially set to zero, is incremented by one each time a comparison with
     * a model is performed.
     */
    private int                    performedComparisons;
    /**
     * Gives expected number of comparison expected for the series (the total
     * number of models it has to be compared with).
     */
    private int                    expectedComparisons;

    public ComparisonContextData( TimeSeriesDoubleDouble series ) {
        super();
        this.series = series;
        performedComparisons = 0;
    }

    public int getExpectedComparisons() {
        return expectedComparisons;
    }

    public void setExpectedComparisons( int expectedComparisons ) {
        this.expectedComparisons = expectedComparisons;
    }

    public TimeSeriesDoubleDouble getSeries() {
        return series;
    }

    /**
     * Returns true when performedComparisons reaches the number of
     * expectedComparisons.
     * 
     * @return
     */
    public boolean operationOver() {
        return ( performedComparisons == expectedComparisons );
    }

    public void incremente() {
        performedComparisons++;
    }
}
