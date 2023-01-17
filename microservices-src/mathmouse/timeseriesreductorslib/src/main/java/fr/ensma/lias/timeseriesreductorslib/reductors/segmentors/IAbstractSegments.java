package fr.ensma.lias.timeseriesreductorslib.reductors.segmentors;

import java.util.Set;
import java.util.TreeMap;

/**
 * 
 * @author cyrille ponchateau (cyrille.ponchateau@ensma.fr)
 *
 * @param <K>,
 *            generic key type
 * @param <V>,
 *            generiv value type
 */
public interface IAbstractSegments<K, V> {

    TreeMap<Double, Double> computeValues( Set<Double> keySet );

}
