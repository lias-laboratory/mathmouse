package fr.ensma.lias.timeseriesreductorslib.reductors.segmentors.plr.regression.segmentationalgorithms;

import fr.ensma.lias.timeseriesreductorslib.reductors.segmentors.ISegmentationAlgorithm;
import fr.ensma.lias.timeseriesreductorslib.reductors.segmentors.plr.regression.PLRRegressionSegments;

/**
 * Interface for classes implementing a segmentation algorithm, based on best
 * fit line segments.
 * 
 * @author cyrille ponchateau (cyrille.ponchateau@ensma.fr)
 *
 */
public interface IPLRRegressionAlg extends ISegmentationAlgorithm {
    PLRRegressionSegments getRegressionSegments();
}
