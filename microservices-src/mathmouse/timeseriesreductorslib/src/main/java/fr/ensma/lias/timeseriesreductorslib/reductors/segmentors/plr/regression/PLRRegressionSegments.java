package fr.ensma.lias.timeseriesreductorslib.reductors.segmentors.plr.regression;

import java.util.Set;
import java.util.TreeMap;

import fr.ensma.lias.timeseriesreductorslib.reductors.segmentors.IAbstractSegments;

/**
 * Represent an order list of PLRRegressionSegment. The list is indexed using
 * the left bound of the segments. The class extends the TreeMap structure, that
 * keeps an ascending order point of view of the list.
 * 
 * @author ponchatc
 * @see fr.ensma.lias.timeseriesreductorslib.reductors.segmentors.plr.regression.PLRRegressionSegment
 *
 */
public class PLRRegressionSegments extends TreeMap<Double, PLRRegressionSegment>
        implements IAbstractSegments<Double, Double> {

    public PLRRegressionSegments() {
        super();
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
    public boolean equals( Object o ) {
        // the object must not be null
        if ( o == null )
            return false;

        // if references are the same, objects are equal
        if ( o == this )
            return true;

        // check if instances are the same class
        if ( !( o instanceof PLRRegressionSegments ) )
            return false;

        PLRRegressionSegments segs = (PLRRegressionSegments) o;

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
