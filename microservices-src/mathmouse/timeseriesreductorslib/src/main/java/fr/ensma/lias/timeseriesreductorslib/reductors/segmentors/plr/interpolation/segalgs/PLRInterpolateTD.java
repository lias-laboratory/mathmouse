package fr.ensma.lias.timeseriesreductorslib.reductors.segmentors.plr.interpolation.segalgs;

import fr.ensma.lias.timeseriesreductorslib.reductors.segmentors.plr.interpolation.PLRInterpolateSegment;
import fr.ensma.lias.timeseriesreductorslib.reductors.segmentors.plr.interpolation.PLRInterpolateSegments;
import fr.ensma.lias.timeseriesreductorslib.timeseries.TimeSeriesDoubleDouble;

/**
 * 
 * @author cyrille ponchateau (cyrille.ponchateau@ensma.fr)
 *
 */
public class PLRInterpolateTD {
    private TimeSeriesDoubleDouble motherSeries;
    private PLRInterpolateSegments segments;

    public PLRInterpolateTD( TimeSeriesDoubleDouble motherSeries ) {
        this.motherSeries = motherSeries;
        segments = new PLRInterpolateSegments();
    }

    public void setMotherSeries( TimeSeriesDoubleDouble other ) {
        motherSeries = other;
    }

    public PLRInterpolateSegments getInterpolateSegments() {
        return segments;
    }

    /**
     * Segments series using a top down algorithm, with a PLR interpolate
     * representation.
     * 
     * @param max_error
     * @return
     */
    public PLRInterpolateSegments process( Double max_error ) {
        // Logger.getLogger( getClass() ).debug( "mother series: " +
        // motherSeries );
        Double firstKey = motherSeries.firstKey();
        Double lastKey = motherSeries.lastKey();
        // Logger.getLogger( getClass() ).debug( "mother series first key: " +
        // firstKey );
        // Logger.getLogger( getClass() ).debug( "mother series last key: " +
        // lastKey );
        segments = new PLRInterpolateSegments();
        segments.put( motherSeries.firstKey(), new PLRInterpolateSegment( firstKey,
                lastKey, motherSeries.get( firstKey ), motherSeries.get( lastKey ) ) );
        // Logger.getLogger( getClass() ).debug( "segments: " + segments );
        Double bestSoFar = Double.POSITIVE_INFINITY;
        Double improvementInApproximation = 0.0;
        Double breakpoint = 0.0;

        Double currentKey = motherSeries.higherKey( motherSeries.firstKey() );

        // looking for the best splitting point
        for ( int i = 1; i < motherSeries.size() - 2; i++ ) {
            improvementInApproximation = improveSplittingHere( currentKey );

            if ( improvementInApproximation < bestSoFar ) {
                breakpoint = currentKey;
                bestSoFar = improvementInApproximation;
            }
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

        return segments;
    }

    /**
     * Segments series using top down approach, with PLR interpolate
     * representation.
     * 
     * @param series
     * @param max_error
     */
    private void process( TimeSeriesDoubleDouble series, Double max_error ) {
        Double bestSoFar = Double.POSITIVE_INFINITY;
        Double improvementInApproximation = 0.0;
        Double breakpoint = series.firstKey();

        // Logger.getLogger( getClass() ).debug( "series: " + series );
        // Logger.getLogger( getClass() ).debug( "segments: " + segments );
        Double currentKey = series.higherKey( series.firstKey() );

        for ( int i = 1; i < series.size() - 2; i++ ) {
            improvementInApproximation = improveSplittingHere( currentKey );

            if ( improvementInApproximation < bestSoFar ) {
                breakpoint = currentKey;
                bestSoFar = improvementInApproximation;
            }

            currentKey = series.higherKey( currentKey );
        }

        // split series
        // Logger.getLogger( getClass() ).debug( "breakpoint: " + breakpoint );
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
        // get the segment passing over key
        PLRInterpolateSegment left = new PLRInterpolateSegment();
        if ( key.equals( segments.firstKey() ) ) {
            left = new PLRInterpolateSegment( segments.firstKey(), segments.firstEntry().getValue().getRightBound(),
                    segments.firstEntry().getValue().getLeftValue(), segments.firstEntry().getValue().getRightValue() );
        } else {
            left = new PLRInterpolateSegment( segments.lowerKey( key ),
                    segments.lowerEntry( key ).getValue().getRightBound(),
                    segments.lowerEntry( key ).getValue().getLeftValue(),
                    segments.lowerEntry( key ).getValue().getRightValue() );
        }
        // calculate the right part of the segment, from key
        PLRInterpolateSegment right = new PLRInterpolateSegment( motherSeries.higherKey( key ), left.getRightBound(),
                motherSeries.higherEntry( key ).getValue(), left.getRightValue() );
        // sets the new right bound and value of the segment
        left.setRightBound( key );
        left.setRightValue( motherSeries.get( key ) );
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
        // copying the segment that contains the key, the segment copy, will be
        // redefined as the new left segment
        PLRInterpolateSegment left;
        // Logger.getLogger( getClass() ).debug( "key " + key + " first key " +
        // segments.firstKey() );
        if ( key.equals( segments.firstKey() ) ) {
            left = new PLRInterpolateSegment( segments.firstKey(), segments.firstEntry().getValue().getRightBound(),
                    segments.firstEntry().getValue().getLeftValue(), segments.firstEntry().getValue().getRightValue() );
        } else {
            left = new PLRInterpolateSegment( segments.lowerKey( key ),
                    segments.lowerEntry( key ).getValue().getRightBound(),
                    segments.lowerEntry( key ).getValue().getLeftValue(),
                    segments.lowerEntry( key ).getValue().getRightValue() );
        }

        // calculate the new right segment
        PLRInterpolateSegment right = new PLRInterpolateSegment( motherSeries.higherKey( key ), left.getRightBound(),
                motherSeries.higherEntry( key ).getValue(), left.getRightValue() );
        // sets the right bounds and value of the new left segment
        left.setRightBound( key );
        left.setRightValue( motherSeries.get( key ) );
        // replace the segment by the two new smaller segments
        segments.remove( left.getLeftBound() );
        segments.put( left.getLeftBound(), left );
        segments.put( right.getLeftBound(), right );
    }
}
