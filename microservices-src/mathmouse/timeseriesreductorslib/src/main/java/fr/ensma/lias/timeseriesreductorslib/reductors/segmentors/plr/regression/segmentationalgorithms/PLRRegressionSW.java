package fr.ensma.lias.timeseriesreductorslib.reductors.segmentors.plr.regression.segmentationalgorithms;

import fr.ensma.lias.timeseriesreductorslib.reductors.segmentors.plr.regression.PLRRegressionSegment;
import fr.ensma.lias.timeseriesreductorslib.reductors.segmentors.plr.regression.PLRRegressionSegments;
import fr.ensma.lias.timeseriesreductorslib.timeseries.TimeSeriesDoubleDouble;

/**
 * Implements a sliding window algorithm with best fit line segments.
 * 
 * @author cyrille ponchateau (cyrille.ponchateau@ensma.fr)
 *
 */
public class PLRRegressionSW implements IPLRRegressionAlg {
    private TimeSeriesDoubleDouble motherSeries;
    private PLRRegressionSegments  segments;

    public PLRRegressionSW( TimeSeriesDoubleDouble motherSeries ) {
        this.motherSeries = motherSeries;
        segments = new PLRRegressionSegments();
    }

    @Override
    public PLRRegressionSegments getRegressionSegments() {
        return segments;
    }

    public void setMotherSeries( TimeSeriesDoubleDouble motherSeries ) {
        this.motherSeries = motherSeries;
    }

    @Override
    public void process( double max_error ) {
        segments.clear();
        Double anchor = motherSeries.firstKey();
        Double currentKey = anchor;

        PLRRegressionSegment segment = new PLRRegressionSegment();

        try {
            // go through the series
            while ( motherSeries.higherKey( anchor ) != null && currentKey != null ) {
                // Logger.getLogger( getClass() ).debug( "anchor: " + anchor );
                currentKey = motherSeries.higherKey( anchor );
                // Logger.getLogger( getClass() ).debug( "current key: " +
                // currentKey );
                // if not at the end
                if ( currentKey != null ) {
                    // put the new segment beginning at the anchor
                    // Logger.getLogger( getClass() ).debug( "current series: "
                    // + motherSeries.get( anchor, currentKey ) );
                    segment = new PLRRegressionSegment( anchor, currentKey, motherSeries.get( anchor, currentKey ) );
                    // Logger.getLogger( getClass() ).debug( "current segment: "
                    // + segment );
                    // slides the window on the right
                    while ( currentKey != null
                            && segment.calculate_error( motherSeries.get( anchor, currentKey ) ) < max_error ) {
                        currentKey = motherSeries.higherKey( currentKey );
                        // Logger.getLogger( getClass() ).debug( "current key: "
                        // + currentKey );
                        // if the end is not reached
                        if ( currentKey != null ) {
                            // sets the curent right bound
                            // Logger.getLogger( getClass() )
                            // .debug( "current series: " + motherSeries.get(
                            // anchor, currentKey ) );
                            segment = new PLRRegressionSegment( anchor, currentKey,
                                    motherSeries.get( anchor, currentKey ) );
                            // Logger.getLogger( getClass() ).debug( "current
                            // segment: " + segment );
                        }
                    }

                    // if the end is not reached
                    if ( currentKey != null ) {
                        // a new segment is defined between anchor and current
                        // key
                        segments.put( anchor,
                                new PLRRegressionSegment( anchor, motherSeries.lowerKey( currentKey ),
                                        motherSeries.get( anchor, motherSeries.lowerKey( currentKey ) ) ) );
                        // Logger.getLogger( getClass() ).debug( "new segment: "
                        // + segment );
                        // the current key becomes the new anchor
                        anchor = currentKey;
                    } else {// if the end is reached
                        // the new segment starts at the last anchor to goes to
                        // the end
                        segments.put( anchor, new PLRRegressionSegment( anchor, motherSeries.lastKey(),
                                motherSeries.get( anchor, motherSeries.lastKey() ) ) );
                        // Logger.getLogger( getClass() ).debug( "new segment: "
                        // + segment );
                    }
                } else {// if the end is reached
                    // the new segment starts at the last anchor to goes to the
                    // end
                    segments.put( anchor, new PLRRegressionSegment( anchor, motherSeries.lastKey(),
                            motherSeries.get( anchor, motherSeries.lastKey() ) ) );
                    // Logger.getLogger( getClass() ).debug( "new segment: " +
                    // segment );
                }
            }
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

}
