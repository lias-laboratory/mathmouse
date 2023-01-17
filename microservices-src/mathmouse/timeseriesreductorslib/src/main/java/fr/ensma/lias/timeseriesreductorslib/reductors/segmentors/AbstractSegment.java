package fr.ensma.lias.timeseriesreductorslib.reductors.segmentors;

import java.util.Set;
import java.util.TreeMap;

import fr.ensma.lias.timeseriesreductorslib.timeseries.AbstractTimeSeries;

/**
 * Represents a segment by its left and right bound and an attribute storing the
 * data representation on the segment
 * 
 * @author cyrille ponchateau (cyrille.ponchateau@ensma.fr)
 *
 * @param <K>
 * @param <V>
 */
public abstract class AbstractSegment<K, V> {
    protected K leftBound;
    protected K rightBound;

    /**
     * Constructor using fields
     * 
     * @param leftBound
     * @param rightBound
     * @param dataRepresentator
     */
    public AbstractSegment( K leftBound, K rightBound ) {
        this.leftBound = leftBound;
        this.rightBound = rightBound;
    }

    /**
     * default constructor
     */
    public AbstractSegment() {

    }

    // Automatically generated getters and setters
    public K getLeftBound() {
        return leftBound;
    }

    public void setLeftBound( K leftBound ) {
        this.leftBound = leftBound;
    }

    public K getRightBound() {
        return rightBound;
    }

    public void setRightBound( K rightBound ) {
        this.rightBound = rightBound;
    }

    /**
     * Sets left and right bounds.
     * 
     * @param leftBound
     * @param rightBound
     */
    public void setBounds( K leftBound, K rightBound ) {
        setLeftBound( leftBound );
        setRightBound( rightBound );
    }

    @Override
    public String toString() {
        return "{left: " + leftBound + ", right: " + rightBound + ", ";
    }

    /**
     * The error function to use in the segmentation algorithms
     * 
     * @return
     */
    public abstract V calculate_error( AbstractTimeSeries<K, V> TS ) throws ArrayIndexOutOfBoundsException;

    /**
     * Compute the value of the series, at time key, from the segment data.
     * 
     * @param key
     * @return
     * @throws ArrayIndexOutOfBoundsException
     */
    public abstract V computeValue( K key ) throws ArrayIndexOutOfBoundsException;

    /**
     * compute the values of the series, for each key of the given set.
     * 
     * @param keySet
     * @return a time series, containing the values.
     * @throws ArrayIndexOutOfBoundsException
     */
    public TreeMap<K, V> computeValues( Set<K> keySet ) throws ArrayIndexOutOfBoundsException {

        TreeMap<K, V> result = new TreeMap<K, V>();

        // computes values for each key of the set and put it into the map
        for ( K key : keySet ) {
            result.put( key, computeValue( key ) );
        }

        return result;
    }
}
