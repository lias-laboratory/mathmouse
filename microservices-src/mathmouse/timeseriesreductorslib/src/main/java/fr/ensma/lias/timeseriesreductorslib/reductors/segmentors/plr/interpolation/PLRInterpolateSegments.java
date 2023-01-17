package fr.ensma.lias.timeseriesreductorslib.reductors.segmentors.plr.interpolation;

import java.util.Set;
import java.util.TreeMap;

import fr.ensma.lias.timeseriesreductorslib.reductors.segmentors.IAbstractSegments;

/**
 * List of PLRInterpolateSegments. Keys are the left bound value of each
 * segment. The segments are ordered using the key values.
 * 
 * @author cyrille ponchateau (cyrille.ponchateau@ensma.fr)
 *
 */
public class PLRInterpolateSegments extends TreeMap<Double, PLRInterpolateSegment>
        implements IAbstractSegments<Double, Double> {

    public PLRInterpolateSegments() {
        super();
    }

    @Override
    public boolean equals( Object o ) {
        // the object must not be null
        if ( o == null )
            return false;

        // if references are the same, objects are equal
        if ( o == this )
            return true;

        // check if instances are the same class
        if ( !( o instanceof PLRInterpolateSegments ) )
            return false;

        PLRInterpolateSegments segs = (PLRInterpolateSegments) o;

        // number of elements must be the same
        if ( segs.size() != this.size() )
            return false;

        // each element must be equal
        for ( Double key : this.keySet() )
            if ( !segs.get( key ).equals( this.get( key ) ) )
                return false;

        // if all the previous tests do not fail, then the objects are equal
        return true;
    }

    @Override
    public String toString() {
        String res = "{";

        for ( Double key : this.keySet() ) {
            res = res + this.get( key ).toString();
        }

        res = res + "}";
        return res;
    }

    @Override
    public TreeMap<Double, Double> computeValues( Set<Double> keySet ) {
        Double currentSegmentKey = this.firstKey();
        TreeMap<Double, Double> result = new TreeMap<Double, Double>();

        // go through the set of time index for which we need the values
        for ( Double key : keySet ) {
            // if the current key is higher than the right bound of the current
            // segment, we need to use the next segment
            if ( key > this.get( currentSegmentKey ).getRightBound() )
                currentSegmentKey = this.higherKey( currentSegmentKey );
            result.put( key, this.get( currentSegmentKey ).computeValue( key ) );
        }
        return result;
    }

}
