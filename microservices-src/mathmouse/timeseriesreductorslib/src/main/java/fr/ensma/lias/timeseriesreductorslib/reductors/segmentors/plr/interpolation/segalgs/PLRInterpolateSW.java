package fr.ensma.lias.timeseriesreductorslib.reductors.segmentors.plr.interpolation.segalgs;

import fr.ensma.lias.timeseriesreductorslib.reductors.segmentors.ISegmentationAlgorithm;
import fr.ensma.lias.timeseriesreductorslib.reductors.segmentors.plr.interpolation.PLRInterpolateSegment;
import fr.ensma.lias.timeseriesreductorslib.reductors.segmentors.plr.interpolation.PLRInterpolateSegments;
import fr.ensma.lias.timeseriesreductorslib.timeseries.TimeSeriesDoubleDouble;

/**
 * Implements Sliding-Window algorithm, with the PLR representation, where
 * values are represented by the interpolating line.
 * 
 * @author cyrille ponchateau (cyrille.ponchateau@ensma.fr)
 *
 */
public class PLRInterpolateSW implements ISegmentationAlgorithm {
    private TimeSeriesDoubleDouble motherSeries;
    private PLRInterpolateSegments segments;

    public PLRInterpolateSW( TimeSeriesDoubleDouble motherSeries ) {
        this.motherSeries = motherSeries;
        segments = new PLRInterpolateSegments();
    }

    public void setMotherSeries( TimeSeriesDoubleDouble other ) {
        motherSeries = other;
    }

    public PLRInterpolateSegments getInterpolateSegments() {
        return segments;
    }

    @Override
    public void process( double max_error ) {
        segments.clear();
        Double anchor = motherSeries.firstKey();
        Double currentKey = anchor;

        PLRInterpolateSegment segment = new PLRInterpolateSegment();

        // go through the series
        while ( motherSeries.higherKey( anchor ) != null && currentKey != null ) {
            currentKey = motherSeries.higherKey( anchor );
            // if not at the end
            if ( currentKey != null ) {
                // put the new segment beginning at the anchor
                segment.setLeftBound( anchor );
                segment.setRightBound( currentKey );
                segment.setLeftValue( motherSeries.get( anchor ) );
                segment.setRightValue( motherSeries.get( currentKey ) );
                // slides the window on the right
                while ( currentKey != null
                        && segment.calculate_error( motherSeries.get( anchor, currentKey ) ) < max_error ) {
                    currentKey = motherSeries.higherKey( currentKey );
                    // if the end is not reached
                    if ( currentKey != null ) {
                        // sets the curent right bound
                        segment.setRightBound( currentKey );
                        segment.setRightValue( motherSeries.get( currentKey ) );
                    }
                }

                // if the end is not reached
                if ( currentKey != null ) {
                    // a new segment is defined between anchor and current key
                    segments.put( anchor,
                            new PLRInterpolateSegment( anchor, motherSeries.lowerKey( currentKey ),
                                    motherSeries.get( anchor ),
                                    motherSeries.lowerEntry( currentKey ).getValue() ) );
                    // the current key becomes the new anchor
                    anchor = currentKey;
                } else {// if the end is reached
                    // the new segment starts at the last anchor to goes to the
                    // end
                    segments.put( anchor,
                            new PLRInterpolateSegment( anchor, motherSeries.lastKey(), motherSeries.get( anchor ),
                                    motherSeries.lastEntry().getValue() ) );
                }
            } else {// if the end is reached
                // the new segment starts at the last anchor to goes to the
                // end
                segments.put( anchor,
                        new PLRInterpolateSegment( anchor, motherSeries.lastKey(), motherSeries.get( anchor ),
                                motherSeries.lastEntry().getValue() ) );
            }
        }
    }

}
