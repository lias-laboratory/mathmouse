package fr.ensma.lias.timeseriesreductorslib.comparison;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import fr.ensma.lias.timeseriesreductorslib.timeseries.TimeSeriesDoubleDouble;

/**
 * The class implements an algorithm used to perform comparison between two series.
 * 
 * @author ponchatc
 *
 */
public class CompareAlgorithm
{
    // elements keys
    public static final String         MEAN_DELTA         = "meandelta";
    public static final String         MEAN_DELTA_MIN     = "meandeltamin";
    public static final String         MEAN_DELTA_MAX     = "meandeltamax";

    public static final String         STANDARD_DEVIATION = "standarddeviation";

    // the statistics values are stored into a map
    private static Map<String, Double> statsMap;

    /**
     * Compute statistics properties of to time series (mean deltas, delta min, delta max,
     * standardDeviation).
     * 
     * The mean, percent and standard deviation are calculated using the incremental update formula.
     * Two reasons motivated that choice:</br>
     * <ul>
     * <li>Those formulas avoid values summation to grow to big and enhanced the accuracy of the
     * computation</li>
     * <li>Those formulas seems easier to adapt to a stream</li>
     * </ul>
     * 
     * @param t1
     * @param t2
     * @param computePercents
     * @param searchMinMax
     * @param computeStandardDeviation
     * @param computeCorrelation
     * @return
     */
    public static void statistics( TimeSeriesDoubleDouble modelSeries, TimeSeriesDoubleDouble rawSeries )
            throws Exception
    {	
    	Logger.getLogger("").debug("starting statistics…");
    	reset();
        // initializing statsMap
        statsMap.put( MEAN_DELTA, Double.POSITIVE_INFINITY );
        statsMap.put( STANDARD_DEVIATION, Double.POSITIVE_INFINITY );
        statsMap.put( MEAN_DELTA_MIN, Double.POSITIVE_INFINITY );
        statsMap.put( MEAN_DELTA_MAX, Double.POSITIVE_INFINITY );
        Logger.getLogger("").debug("statsMap: " + statsMap);
        
        // the models series size must be at least equal to the raw series size
        if ( modelSeries.size() >= rawSeries.size() )
        {
            double modelFirstKey = modelSeries.firstKey();
            double rawFirstKey = rawSeries.firstKey();
            double modelSecondKey = modelSeries.higherKey( modelFirstKey );
            double rawSecondKey = rawSeries.higherKey( rawFirstKey );
            double modelH = modelSecondKey - modelFirstKey;
            double rawH = rawSecondKey - rawFirstKey;

            if ( Math.abs( modelH - rawH ) <= 10e-7 )
            {
                // the current delta value, needed in the for loop
                double currentDelta = Math.sqrt(Math.abs(
                        modelSeries.get( modelSeries.firstKey() ).doubleValue()
                                - rawSeries.get( modelSeries.firstKey() ) ));
                // when calculation the n-th value of the mean, it contains the
                // (n-1)-th
                // value
                double previousMean = currentDelta;

                // mean variables
                double meanDelta = currentDelta;
                double deltaMin = Double.POSITIVE_INFINITY;
                double deltaMax = Double.NEGATIVE_INFINITY;

                // standard deviation variables
                double standardDeviation = 0.0;

                // double correlation;
                TimeSeriesDoubleDouble clone = rawSeries.clone();
                Set<Double> keys = clone.keySet();
                keys.remove( clone.firstKey() );

                int n = 2;
                // going through the time series
                for ( Double t : keys )
                {
                    // computes current delta
                    if ( rawSeries.get( t ) == null )
                    {
                        meanDelta = Double.POSITIVE_INFINITY;
                        standardDeviation = Double.POSITIVE_INFINITY;
                        deltaMin = Double.POSITIVE_INFINITY;
                        deltaMax = Double.NEGATIVE_INFINITY;
                    } else
                    {
                        currentDelta = Math.abs( modelSeries.get( t ) - rawSeries.get( t ) );
                        //Logger.getLogger("").debug("currentDelta: " + currentDelta);
                        // update mean, percent and standard deviation
                        meanDelta = meanDelta + ( currentDelta - meanDelta ) / n;
                        standardDeviation = standardDeviation
                                + ( currentDelta - previousMean ) * ( currentDelta - meanDelta );
                        // prepares previous mean value for the next loop
                        previousMean = currentDelta;

                        // min values seeking
                        if ( deltaMin > meanDelta )
                            deltaMin = meanDelta;

                        // max values seeking
                        if ( deltaMax < meanDelta )
                            deltaMax = meanDelta;

                        n++;
                    }
                }

                // standard deviation is not calculated directly in the loop, in
                // fact it is the variance that is calculated, the standard
                // deviation is derived from it.
                standardDeviation = Math.sqrt( standardDeviation / rawSeries.size() );

                // storing the results
                statsMap.put( MEAN_DELTA, meanDelta );
                statsMap.put( STANDARD_DEVIATION, standardDeviation );
                statsMap.put( MEAN_DELTA_MIN, deltaMin );
                statsMap.put( MEAN_DELTA_MAX, deltaMax );
            }
        }
    }

    /**
     * Compute statistics properties of to time series based on least squares method.
     * 
     * The mean, percent and standard deviation are calculated using the incremental update formula.
     * Two reasons motivated that choice:</br>
     * <ul>
     * <li>Those formulas avoid values summation to grow to big and enhanced the accuracy of the
     * computation</li>
     * <li>Those formulas seems easier to adapt to a stream</li>
     * </ul>
     * 
     * @param t1
     * @param t2
     * @param computePercents
     * @param searchMinMax
     * @param computeStandardDeviation
     * @param computeCorrelation
     * @return
     */
    public static void leastSquareStatistics( TimeSeriesDoubleDouble modelSeries, TimeSeriesDoubleDouble rawSeries )
            throws Exception
    {
    	Logger.getLogger("").debug("starting least squared statistics…");
    	reset();
        // initializing statsMap
        statsMap.put( MEAN_DELTA, Double.POSITIVE_INFINITY );
        statsMap.put( STANDARD_DEVIATION, Double.POSITIVE_INFINITY );
        statsMap.put( MEAN_DELTA_MIN, Double.POSITIVE_INFINITY );
        statsMap.put( MEAN_DELTA_MAX, Double.POSITIVE_INFINITY );
        Logger.getLogger("").debug("statsMap: " + statsMap);

        // the models series size must be at least equal to the raw series size
        if ( modelSeries.size() >= rawSeries.size() )
        {
            double modelFirstKey = modelSeries.firstKey();
            double rawFirstKey = rawSeries.firstKey();
            double modelSecondKey = modelSeries.higherKey( modelFirstKey );
            double rawSecondKey = rawSeries.higherKey( rawFirstKey );
            double modelH = modelSecondKey - modelFirstKey;
            double rawH = rawSecondKey - rawFirstKey;

            if ( Math.abs( modelH - rawH ) <= 10e-7 )
            {
                // the current delta value, needed in the for loop
                double currentDeltaSquared = Math.sqrt(Math.abs(
                        modelSeries.get( modelSeries.firstKey() ).doubleValue()
                                - rawSeries.get( modelSeries.firstKey() ) ));
                // when calculation the n-th value of the mean, it contains the
                // (n-1)-th
                // value
                double previousMean = currentDeltaSquared;

                // mean variables
                double meanDeltaSquared = currentDeltaSquared;
                double deltaMin = Double.POSITIVE_INFINITY;
                double deltaMax = Double.NEGATIVE_INFINITY;

                // standard deviation variables
                double standardDeviation = 0.0;

                // double correlation;
                TimeSeriesDoubleDouble clone = rawSeries.clone();
                Set<Double> keys = clone.keySet();
                keys.remove( clone.firstKey() );

                int n = 2;
                // going through the time series
                for ( Double t : keys )
                {
                    // computes current delta
                    if ( rawSeries.get( t ) == null )
                    {
                        meanDeltaSquared = Double.POSITIVE_INFINITY;
                        standardDeviation = Double.POSITIVE_INFINITY;
                        deltaMin = Double.POSITIVE_INFINITY;
                        deltaMax = Double.NEGATIVE_INFINITY;
                    } else
                    {
                        currentDeltaSquared = Math.sqrt(Math.abs( modelSeries.get( t ) - rawSeries.get( t ) ));
                        //Logger.getLogger("").debug("currentDelta: " + currentDeltaSquared);
                        // update mean, percent and standard deviation
                        meanDeltaSquared = meanDeltaSquared + ( currentDeltaSquared - meanDeltaSquared ) / n;
                        standardDeviation = standardDeviation
                                + ( currentDeltaSquared - previousMean ) * ( currentDeltaSquared - meanDeltaSquared );
                        // prepares previous mean value for the next loop
                        previousMean = currentDeltaSquared;

                        // min values seeking
                        if ( deltaMin > meanDeltaSquared )
                            deltaMin = meanDeltaSquared;

                        // max values seeking
                        if ( deltaMax < meanDeltaSquared )
                            deltaMax = meanDeltaSquared;

                        n++;
                    }
                }

                // standard deviation is not calculated directly in the loop, in
                // fact it is the variance that is calculated, the standard
                // deviation is derived from it.
                standardDeviation = Math.sqrt( standardDeviation / rawSeries.size() );

                // storing the results
                statsMap.put( MEAN_DELTA, meanDeltaSquared );
                statsMap.put( STANDARD_DEVIATION, standardDeviation );
                statsMap.put( MEAN_DELTA_MIN, deltaMin );
                statsMap.put( MEAN_DELTA_MAX, deltaMax );
            }
        }
    }
    
    public static void set()
    {
    	if (statsMap == null )
    	{
    		statsMap = new HashMap<String, Double>();	
    	}
    	statsMap.clear();
    }
    
    public static void reset()
    {
    	set();
    }
    
    public static void printResults()
    {
        String results = "";
        results = "Mean deltas: " + statsMap.get( MEAN_DELTA ) + "; ";
        results = results + "Mean deltas min: " + statsMap.get( MEAN_DELTA_MIN ) + "; ";
        results = results + "Mean deltas max: " + statsMap.get( MEAN_DELTA_MAX ) + "; ";
        results = results + "Standard deviation: " + statsMap.get( STANDARD_DEVIATION ) + ";";
        Logger.getLogger( "STATIC_LOGGER:CompareAlgorithm" ).debug( results );
    }

    public static Map<String, Double> getStatsMap()
    {
        return statsMap;
    }

    public static ENotification notify( double meanErrorMax, double standardDeviationMax, double meanErrorMin,
            double standardDeviationMin )
    {
        if ( statsMap.isEmpty() )
            return ENotification.MODEL_REJECTED;
        else if ( statsMap.get( MEAN_DELTA ) > meanErrorMax
                || statsMap.get( STANDARD_DEVIATION ) > standardDeviationMax )
            return ENotification.MODEL_REJECTED;
        else if ( statsMap.get( MEAN_DELTA ) < meanErrorMin
                && statsMap.get( STANDARD_DEVIATION ) < standardDeviationMin )
            return ENotification.MODEL_ACCEPTED;
        else
            return ENotification.UNDETERMINED;
    }
}
