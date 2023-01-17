package fr.ensma.lias.timeseriesreductorslib.reductors.segmentors.plr.regression.segmentationalgorithms;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import fr.ensma.lias.timeseriesreductorslib.reductors.segmentors.plr.regression.PLRRegressionSegment;
import fr.ensma.lias.timeseriesreductorslib.reductors.segmentors.plr.regression.PLRRegressionSegments;
import fr.ensma.lias.timeseriesreductorslib.timeseries.TimeSeriesDoubleDouble;

/**
 * The class implements a Bottom-Up segmentation algorithm, with best fit line
 * segments.
 * 
 * @author cyrille ponchateau (cyrille.ponchateau@ensma.fr)
 *
 */
public class PLRRegressionBU implements IPLRRegressionAlg {
    private TimeSeriesDoubleDouble motherSeries;
    private PLRRegressionSegments  segments;

    public PLRRegressionBU( TimeSeriesDoubleDouble motherSeries ) {
        this.motherSeries = motherSeries;
        segments = new PLRRegressionSegments();
    }

    public void setMotherSeries( TimeSeriesDoubleDouble other ) {
        motherSeries = other;
    }

    @Override
    public PLRRegressionSegments getRegressionSegments() {
        return segments;
    }

    /**
     * 
     * @param max_error
     * @return
     */
    @Override
    public void process( double max_error ) {
        Double leftKey = motherSeries.firstKey();
        Double rightKey = motherSeries.higherKey( leftKey );
        boolean keepgoing = true;
        Map<Double, Double> mergeCosts = new HashMap<Double, Double>();
        Double minCostKey;

        segments.put( leftKey, new PLRRegressionSegment( motherSeries.get( leftKey, rightKey ) ) );

        while ( keepgoing ) {
            leftKey = motherSeries.higherKey( rightKey );
            if ( leftKey != null )
                rightKey = motherSeries.higherKey( leftKey );
            else
                rightKey = null;

            if ( leftKey == null ) {
                keepgoing = false;
            } else if ( rightKey == null ) {
                segments.put( leftKey, new PLRRegressionSegment( motherSeries.get( leftKey, leftKey ) ) );
                keepgoing = false;
            } else {
                segments.put( leftKey, new PLRRegressionSegment( motherSeries.get( leftKey, rightKey ) ) );
            }
        }

        for ( Double key : segments.keySet() ) {
            if ( segments.higherKey( key ) != null )
                mergeCosts.put( key, computeMergeCost( segments.get( key ), segments.higherEntry( key ).getValue() ) );
        }

        while ( Collections.min( mergeCosts.values() ) < max_error ) {
            // Logger.getLogger( getClass() ).debug( "segments size: " +
            // segments.size() + ", segments: " + segments );
            // Logger.getLogger( getClass() ).debug( "merge costs size: " +
            // mergeCosts.size() );
            minCostKey = minKey( mergeCosts );
            // Logger.getLogger( getClass() ).debug( "min cost key: " +
            // minCostKey );
            Double minHigherKey = segments.higherKey( minCostKey );
            // Logger.getLogger( getClass() ).debug( "higher min cost key: " +
            // minHigherKey );
            merge( minCostKey, minHigherKey );
            if ( mergeCosts.containsKey( minHigherKey ) )
                mergeCosts.remove( minHigherKey );
            else
                mergeCosts.remove( segments.lowerKey( minHigherKey ) );

            if ( segments.higherKey( minCostKey ) != null )
                mergeCosts.put( minCostKey,
                        computeMergeCost( segments.get( minCostKey ), segments.higherEntry( minCostKey ).getValue() ) );
            if ( segments.lowerKey( minCostKey ) != null )
                mergeCosts.put( segments.lowerKey( minCostKey ),
                        computeMergeCost( segments.lowerEntry( minCostKey ).getValue(), segments.get( minCostKey ) ) );
        }

    }

    private void merge( Double minCostKey, Double higherKey ) {
        PLRRegressionSegment segment = new PLRRegressionSegment( motherSeries
                .get( segments.get( minCostKey ).getLeftBound(), segments.get( higherKey ).getRightBound() ) );
        segments.remove( minCostKey );
        segments.put( minCostKey, segment );
        segments.remove( higherKey );
    }

    private Double computeMergeCost( PLRRegressionSegment left, PLRRegressionSegment right ) {
        PLRRegressionSegment segment = new PLRRegressionSegment(
                motherSeries.get( left.getLeftBound(), right.getRightBound() ) );
        return segment.calculate_error( motherSeries.get( left.getLeftBound(), right.getRightBound() ) );
    }

    private Double minKey( Map<Double, Double> map ) {
        Double minKey = Double.POSITIVE_INFINITY;
        Double currentMin = Double.POSITIVE_INFINITY;

        for ( Double key : map.keySet() ) {
            if ( map.get( key ) < currentMin ) {
                minKey = key;
                currentMin = map.get( key );
            }
        }

        return minKey;
    }

}
