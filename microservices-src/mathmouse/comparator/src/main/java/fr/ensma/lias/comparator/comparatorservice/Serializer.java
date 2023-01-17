package fr.ensma.lias.comparator.comparatorservice;

import java.util.Map;

import fr.ensma.lias.javarabbitmqapi.enumerations.ESpecialCharacter;
import fr.ensma.lias.timeseriesreductorslib.comparison.CompareAlgorithm;

/**
 * Help serializing comparison data, in order to send it via rabbitMQ.
 * 
 * @author Cyrille Ponchateau (cyrille.ponchateau@ensma.fr)
 *
 */
public class Serializer
{

    /**
     * Data are serialized in the following pattern :</br>
     * <ul>
     * <li>hash;id;numberOfExpectedSeries;name;formula</li>
     * <li>average error;minimum error;maximum error;standard deviation;decision</li>
     * </ul>
     * 
     * @param hash
     * @param id
     * @param numberOfExpectedSeries
     * @param name
     * @param formula
     * @param statsMap
     * @return
     */
    public static String serializeResults( int hash, long id, int numberOfExpectedSeries, String name, String formula,
            Map<String, Double> statsMap )
    {
    	double meanErrorMax = 10e-1;
    	double meanErrorMin = 10e-3;
    	double sdMax = 10e-1;
    	double sdMin = 10e-3;
        String text = "";
        System.out.println( statsMap );
        // first line
        text = text + hash + ESpecialCharacter.SEPARATOR.value()
                + id + ESpecialCharacter.SEPARATOR.value()
                + numberOfExpectedSeries + ESpecialCharacter.SEPARATOR.value()
                + name + ESpecialCharacter.SEPARATOR.value()
                + formula + ESpecialCharacter.NEWLINE.value();
        // second line
        text = text + statsMap.get( CompareAlgorithm.MEAN_DELTA ) + ESpecialCharacter.SEPARATOR.value()
                + statsMap.get( CompareAlgorithm.MEAN_DELTA_MIN ) + ESpecialCharacter.SEPARATOR.value()
                + statsMap.get( CompareAlgorithm.MEAN_DELTA_MAX ) + ESpecialCharacter.SEPARATOR.value()
                + statsMap.get( CompareAlgorithm.STANDARD_DEVIATION ) + ESpecialCharacter.SEPARATOR.value()
                + CompareAlgorithm.notify( meanErrorMax, sdMax, meanErrorMin, sdMin ).value() + ESpecialCharacter.NEWLINE.value();
        System.out.println( text );
        return text;
    }

}
