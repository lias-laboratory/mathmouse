package fr.ensma.lias.timeseriesreductorslib.reductors.segmentors.plr.regression;

import java.util.Set;

import fr.ensma.lias.timeseriesreductorslib.reductors.segmentors.AbstractSegment;
import fr.ensma.lias.timeseriesreductorslib.timeseries.AbstractTimeSeries;
import fr.ensma.lias.timeseriesreductorslib.timeseries.TimeSeriesDoubleDouble;

/**
 * Represent a segment, where data are represented using the best fitting
 * straight line. The best fitting line is found using classical formulas. </br>
 * </br>
 * <ul>
 * <li>slope = (avg(X)*avg(Y) - avg(XY))/(avg(X)*avg(X) - avg(X²))</li>
 * <li>yIntersect = avg(Y) - slope*avg(X)</li>
 * </ul>
 * 
 * X represents the set of inputs (time) and Y the set of values and 'avg' is
 * the function that returns the average of a set.
 * 
 * @author cyrille ponchateau (cyrille.ponchateau@ensma.fr)
 *
 */
public class PLRRegressionSegment extends AbstractSegment<Double, Double> {

    private Double slope;
    private Double yIntersect;

    public PLRRegressionSegment() {
        super();
    }

    public PLRRegressionSegment( Double leftBound, Double rightBound, Double slope, Double yIntersect ) {
        super( leftBound, rightBound );
        this.slope = slope;
        this.yIntersect = yIntersect;
    }

    public PLRRegressionSegment( Double leftBound, Double rightBound, TimeSeriesDoubleDouble TS ) {
        super( leftBound, rightBound );

        if ( TS.size() == 1 ) {
            slope = 0.0;
            yIntersect = TS.firstEntry().getValue();
        } else {
            Double firstKey = TS.firstKey();

            Double meanX = firstKey;
            Double meanY = TS.get( firstKey );
            Double meanXY = firstKey * TS.get( firstKey );
            Double meanX2 = firstKey * firstKey;

            int n = 2;

            Set<Double> keys = TS.navigableKeySet();
            keys.remove( TS.firstKey() );

            for ( Double key : TS.keySet() ) {
                meanX = meanX + ( key - meanX ) / n;
                meanY = meanY + ( TS.get( key ) - meanY ) / n;
                meanXY = meanXY + ( key * TS.get( key ) - meanXY ) / n;
                meanX2 = meanX2 + ( key * key - meanX2 ) / n;
                n++;
            }

            slope = ( meanX * meanY - meanXY ) / ( meanX * meanX - meanX2 );
            yIntersect = meanY - slope * meanX;
        }
    }

    public PLRRegressionSegment( TimeSeriesDoubleDouble TS ) {
        super( TS.firstKey(), TS.lastKey() );

        if ( TS.size() == 1 ) {
            slope = 0.0;
            yIntersect = TS.firstEntry().getValue();
        } else {
            Double firstKey = TS.firstKey();

            Double meanX = firstKey;
            Double meanY = TS.get( firstKey );
            Double meanXY = firstKey * TS.get( firstKey );
            Double meanX2 = firstKey * firstKey;

            int n = 2;

            Set<Double> keys = TS.navigableKeySet();
            keys.remove( TS.firstKey() );

            for ( Double key : TS.keySet() ) {
                meanX = meanX + ( key - meanX ) / n;
                meanY = meanY + ( TS.get( key ) - meanY ) / n;
                meanXY = meanXY + ( key * TS.get( key ) - meanXY ) / n;
                meanX2 = meanX2 + ( key * key - meanX2 ) / n;
                n++;
            }

            slope = ( meanX * meanY - meanXY ) / ( meanX * meanX - meanX2 );
            yIntersect = meanY - slope * meanX;
        }
    }

    public Double getSlope() {
        return slope;
    }

    public void setSlope( Double slope ) {
        this.slope = slope;
    }

    public Double getyIntersect() {
        return yIntersect;
    }

    public void setyIntersect( Double yIntersect ) {
        this.yIntersect = yIntersect;
    }

    @Override
    public boolean equals( Object o ) {
        // the object must not be null
        if ( o == null )
            return false;

        // if references are the same, objects are equal
        if ( o == this )
            return true;

        // if type is different, objects are different
        if ( !( o instanceof PLRRegressionSegment ) )
            return false;

        PLRRegressionSegment seg = (PLRRegressionSegment) o;

        // all the fields must have the same values
        if ( !seg.leftBound.equals( leftBound ) )
            return false;
        if ( !seg.rightBound.equals( rightBound ) )
            return false;
        if ( !seg.slope.equals( slope ) )
            return false;
        if ( !seg.yIntersect.equals( yIntersect ) )
            return false;

        // if all the previous tests do not fail, then the objects are equal
        return true;
    }

    @Override
    public String toString() {
        String res = super.toString();
        res = res + "slope: " + slope + ", y intersect: " + yIntersect + "}";
        return res;
    }

    @Override
    public Double calculate_error( AbstractTimeSeries<Double, Double> TS ) throws ArrayIndexOutOfBoundsException {
        Double segment_error = 0.0;
        for ( Double timeKey : TS.keySet() ) {
            segment_error = segment_error + Math.abs( computeValue( timeKey ) - TS.get( timeKey ) );
        }
        // Logger.getLogger( getClass() ).debug( "error: " + segment_error );
        return segment_error;
    }

    @Override
    public Double computeValue( Double key ) throws ArrayIndexOutOfBoundsException {
        return new Double( slope * key + yIntersect );
    }

}
