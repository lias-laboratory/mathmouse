package fr.ensma.lias.timeseriesreductorslib.reductors.segmentors.plr.interpolation;

import java.util.Set;
import java.util.TreeMap;

import fr.ensma.lias.timeseriesreductorslib.reductors.segmentors.AbstractSegment;
import fr.ensma.lias.timeseriesreductorslib.timeseries.AbstractTimeSeries;

/**
 * A PLR segment, is a segment, where data are reprsented by the interpolating
 * line.
 * 
 * 
 * @author cyrille ponchateau (cyrille.ponchateau@ensma.fr)
 *
 */
public class PLRInterpolateSegment extends AbstractSegment<Double, Double> {
    private Double leftValue;
    private Double rightValue;

    public PLRInterpolateSegment() {
        super();
    }

    public PLRInterpolateSegment( Double leftBound, Double rightBound, Double leftValue, Double rightValue ) {
        super( leftBound, rightBound );
        this.leftValue = leftValue;
        this.rightValue = rightValue;
    }

    public Double getLeftValue() {
        return leftValue;
    }

    public void setLeftValue( Double leftValue ) {
        this.leftValue = leftValue;
    }

    public Double getRightValue() {
        return rightValue;
    }

    public void setRightValue( Double rightValue ) {
        this.rightValue = rightValue;
    }

    public void setRight( Double newRightBound, Double newRightValue ) {
        rightBound = newRightBound;
        rightValue = newRightValue;
    }

    @Override
    public Double calculate_error( AbstractTimeSeries<Double, Double> TS ) throws ArrayIndexOutOfBoundsException {
        Double segment_error = 0.0;
        for ( Double timeKey : TS.keySet() ) {
            segment_error = segment_error + Math.abs( computeValue( timeKey ) - TS.get( timeKey ) );
        }
        return segment_error;
    }

    @Override
    public Double computeValue( Double timeKey ) {
        if ( timeKey == leftBound )
            return leftValue;
        if ( timeKey == rightBound )
            return rightValue;
        Double value = ( timeKey - leftBound ) * ( rightValue - leftValue ) / ( rightBound - leftBound ) + leftValue;
        return value;
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
        if ( !( o instanceof PLRInterpolateSegment ) )
            return false;

        PLRInterpolateSegment seg = (PLRInterpolateSegment) o;

        // all the fields must have the same values
        if ( !seg.leftBound.equals( leftBound ) )
            return false;
        if ( !seg.rightBound.equals( rightBound ) )
            return false;
        if ( !seg.leftValue.equals( leftValue ) )
            return false;
        if ( !seg.rightValue.equals( rightValue ) )
            return false;

        // if all the previous tests do not fail, then the objects are equal
        return true;
    }

    @Override
    public String toString() {
        String res = super.toString();
        res = res + "left value: " + leftValue + ", right value: " + rightValue + "}";
        return res;
    }

    public TreeMap<Double, Double> computeValues( Set<Double> keySet )
            throws ArrayIndexOutOfBoundsException {
        TreeMap<Double, Double> result = new TreeMap<Double, Double>();

        // computes values for each key of the set and put it into the map
        for ( Double key : keySet ) {
            result.put( key, computeValue( key ) );
        }

        return result;
    }
}
