package fr.ensma.lias.timeseriesreductorslib.reductors.segmentors;

public interface ISegmentationAlgorithm {

    /**
     * Perform a segmentation algorithm on a time series
     * 
     * @param max_error,
     *            the maximum error (sum of the error of on point) of each
     *            segment
     * 
     * @author cyrille ponchateau (cyrille.ponchateau@ensma.fr)
     */
    void process( double max_error );

}
