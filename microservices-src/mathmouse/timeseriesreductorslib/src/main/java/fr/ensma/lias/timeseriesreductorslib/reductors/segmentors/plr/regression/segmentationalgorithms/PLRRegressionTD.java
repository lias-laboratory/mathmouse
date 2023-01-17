package fr.ensma.lias.timeseriesreductorslib.reductors.segmentors.plr.regression.segmentationalgorithms;

import fr.ensma.lias.timeseriesreductorslib.reductors.segmentors.plr.regression.PLRRegressionSegment;
import fr.ensma.lias.timeseriesreductorslib.reductors.segmentors.plr.regression.PLRRegressionSegments;
import fr.ensma.lias.timeseriesreductorslib.timeseries.TimeSeriesDoubleDouble;

/**
 * Implements a Top-Down segmentation algorithm, with best fit line segments.
 * 
 * @author cyrille ponchateau (cyrille.ponchateau@ensma.fr)
 *
 */
public class PLRRegressionTD implements IPLRRegressionAlg {
    private TimeSeriesDoubleDouble motherSeries;
    private PLRRegressionSegments  segments;

    public PLRRegressionTD( TimeSeriesDoubleDouble motherSeries ) {
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
     * Segments series using a top down algorithm, with a PLR interpolate
     * representation.
     * 
     * @param max_error
     * @return
     */
    @Override
    public void process( double max_error ) {
        // Logger.getLogger( getClass() ).debug( "mother series: " +
        // motherSeries );
        Double firstKey = motherSeries.firstKey();
        Double lastKey = motherSeries.lastKey();
        // Logger.getLogger( getClass() ).debug( "mother series first key: " +
        // firstKey );
        // Logger.getLogger( getClass() ).debug( "mother series last key: " +
        // lastKey );
        segments = new PLRRegressionSegments();
        segments.put( motherSeries.firstKey(), new PLRRegressionSegment( motherSeries.get( firstKey, lastKey ) ) );
        // Logger.getLogger( getClass() ).debug( "segments: " + segments );
        Double bestSoFar = Double.POSITIVE_INFINITY;
        Double improvementInApproximation = 0.0;
        Double breakpoint = 0.0;

        Double currentKey = motherSeries.higherKey( motherSeries.firstKey() );

        // looking for the best splitting point
        for ( int i = 1; i < motherSeries.size() - 2; i++ ) {
            // Logger.getLogger( getClass() ).debug( "breakpoint: " + breakpoint
            // );
            // Logger.getLogger( getClass() ).debug( "current key: " +
            // currentKey );
            improvementInApproximation = improveSplittingHere( currentKey );
            // Logger.getLogger( getClass() ).debug( "improvement: " +
            // improvementInApproximation );

            if ( improvementInApproximation < bestSoFar ) {
                breakpoint = currentKey;
                bestSoFar = improvementInApproximation;
            }
            // Logger.getLogger( getClass() ).debug( "best so far: " + bestSoFar
            // );
            currentKey = motherSeries.higherKey( currentKey );
        }

        // split series
        // Logger.getLogger( getClass() ).debug( "breakpoint: " + breakpoint );
        splitHere( breakpoint );
        // Logger.getLogger( getClass() ).debug( "segments: " + segments );

        // recursively splits left segment if necessary
        if ( segments.get( motherSeries.firstKey() )
                .calculate_error( motherSeries.get( motherSeries.firstKey(), breakpoint ) ) > max_error ) {
            // Logger.getLogger( getClass() ).debug( "left" );
            process( motherSeries.get( motherSeries.firstKey(), breakpoint ), max_error );
        }

        // same with right segment
        if ( segments.get( motherSeries.higherKey( breakpoint ) ).calculate_error(
                motherSeries.get( motherSeries.higherKey( breakpoint ), motherSeries.lastKey() ) ) > max_error ) {
            // Logger.getLogger( getClass() ).debug( "right" );
            process( motherSeries.get( motherSeries.higherKey( breakpoint ), motherSeries.lastKey() ), max_error );
        }

    }

    /**
     * Segments series using top down approach, with PLR interpolate
     * representation.
     * 
     * @param series
     * @param max_error
     */
    private void process( TimeSeriesDoubleDouble series, Double max_error ) {
        // Logger.getLogger( getClass() ).debug( "" );
        Double bestSoFar = Double.POSITIVE_INFINITY;
        Double improvementInApproximation = 0.0;
        Double breakpoint = series.firstKey();

        // Logger.getLogger( getClass() ).debug( "series: " + series );
        // Logger.getLogger( getClass() ).debug( "segments: " + segments );
        Double currentKey = series.higherKey( series.firstKey() );

        for ( int i = 1; i < series.size() - 2; i++ ) {
            // Logger.getLogger( getClass() ).debug( "breakpoint: " + breakpoint
            // );
            // Logger.getLogger( getClass() ).debug( "current key: " +
            // currentKey );
            improvementInApproximation = improveSplittingHere( currentKey );
            // Logger.getLogger( getClass() ).debug( "improvement: " +
            // improvementInApproximation );

            if ( improvementInApproximation < bestSoFar ) {
                breakpoint = currentKey;
                bestSoFar = improvementInApproximation;
            }
            // Logger.getLogger( getClass() ).debug( "best so far: " + bestSoFar
            // );
            currentKey = motherSeries.higherKey( currentKey );
        }

        // split series
        // Logger.getLogger( getClass() ).debug( "split breakpoint: " +
        // breakpoint );
        // Logger.getLogger( getClass() ).debug( "segments: " + segments );
        splitHere( breakpoint );
        // Logger.getLogger( getClass() ).debug( "segments: " + segments );

        // recursively splits left segment if necessary
        if ( segments.get( series.firstKey() )
                .calculate_error( series.get( series.firstKey(), breakpoint ) ) > max_error ) {
            // Logger.getLogger( getClass() ).debug( "left" );
            process( series.get( series.firstKey(), breakpoint ), max_error );
        }

        // same with right segment
        if ( segments.get( series.higherKey( breakpoint ) )
                .calculate_error( series.get( series.higherKey( breakpoint ), series.lastKey() ) ) > max_error ) {
            // Logger.getLogger( getClass() ).debug( "right" );
            process( series.get( series.higherKey( breakpoint ), series.lastKey() ), max_error );
        }

    }

    private Double improveSplittingHere( Double key ) {
        // Logger.getLogger( getClass() ).debug( "key: " + key );
        // get the segment passing over key
        PLRRegressionSegment left;
        PLRRegressionSegment right;
        PLRRegressionSegment segment;

        Double segmentKey;

        // gets the segment to split
        if ( segments.get( key ) != null ) {
            segmentKey = key;
        } else {
            segmentKey = segments.lowerKey( key );
        }
        // Logger.getLogger( getClass() ).debug( "segment key: " + segmentKey );
        segment = segments.get( segmentKey );
        // Logger.getLogger( getClass() ).debug( "segment: " + segment );

        // calculate the right part of the segment, from key
        right = new PLRRegressionSegment( motherSeries.get( motherSeries.higherKey( key ), segment.getRightBound() ) );
        // Logger.getLogger( getClass() ).debug( "right segment: " + right );

        // and the left part
        left = new PLRRegressionSegment( motherSeries.get( segment.getLeftBound(), key ) );
        // Logger.getLogger( getClass() ).debug( "left segment: " + left );

        // calculates the error on each segments and sums them. The error of the
        // split segments is to be compared with the error of the entire
        // segment.
        return left.calculate_error( motherSeries.get( left.getLeftBound(), left.getRightBound() ) )
                + right.calculate_error( motherSeries.get( right.getLeftBound(), right.getRightBound() ) );
    }

    /**
     * Splits a segment into two at key
     * 
     * @param key,
     *            splitting key position
     */
    private void splitHere( Double key ) {
        // Logger.getLogger( getClass() ).debug( "split key: " + key );
        // get the segment passing over key
        PLRRegressionSegment left;
        PLRRegressionSegment right;
        PLRRegressionSegment segment;

        Double segmentKey;

        // gets the segment to split
        if ( segments.get( key ) != null ) {
            segmentKey = key;
        } else {
            segmentKey = segments.lowerKey( key );
        }
        // Logger.getLogger( getClass() ).debug( "segment key: " + segmentKey );
        segment = segments.get( segmentKey );
        // Logger.getLogger( getClass() ).debug( "segment: " + segment );

        // calculate the right part of the segment, from key
        right = new PLRRegressionSegment( motherSeries.get( motherSeries.higherKey( key ), segment.getRightBound() ) );

        // and the left part
        left = new PLRRegressionSegment( motherSeries.get( segment.getLeftBound(), key ) );

        // replace the segment by the two new smaller segments
        segments.remove( left.getLeftBound() );
        segments.put( left.getLeftBound(), left );
        segments.put( right.getLeftBound(), right );
    }
}
