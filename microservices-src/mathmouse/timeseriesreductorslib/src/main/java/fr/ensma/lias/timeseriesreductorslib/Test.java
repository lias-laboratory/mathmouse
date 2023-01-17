package fr.ensma.lias.timeseriesreductorslib;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import fr.ensma.lias.timeseriesreductorslib.comparison.CompareAlgorithm;
import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.DifferentialEquation2;
import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.DifferentialEquation2IO;
import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.numericalmethod.NumericalMethod2;
import fr.ensma.lias.timeseriesreductorslib.timeseries.TimeSeriesDoubleDouble;
import fr.ensma.lias.timeseriesreductorslib.timeseries.TimeSeriesDoubleDoubleIO;

public class Test {
    public static final String XSD_FILE_PATH = "src/main/resources/differential-equation-2.0.xsd";
    public static final String MODEL_FILE    = "src/main/resources/equation3.xml";
    public static final String SERIES_FILE   = "src/main/resources/Data3.csv";

    public static void main( String[] args ) {
        BasicConfigurator.configure();
        DifferentialEquation2 equation = DifferentialEquation2IO.XMLRead20FromFile( XSD_FILE_PATH, MODEL_FILE );
        Logger.getLogger( "" ).debug( equation );
        try {
            NumericalMethod2 numericalMethod = new NumericalMethod2( equation );
            TimeSeriesDoubleDouble modelSeries = numericalMethod.equationToTimeseriesRK4();
            Logger.getLogger( "" ).debug( modelSeries );
            TimeSeriesDoubleDouble rawSeries = TimeSeriesDoubleDoubleIO.readFromFile( SERIES_FILE );
            Logger.getLogger( "" ).debug( rawSeries );
            CompareAlgorithm.statistics( modelSeries, rawSeries );
            Logger.getLogger( "" ).debug( CompareAlgorithm.getStatsMap().get( CompareAlgorithm.MEAN_DELTA ) );
            Logger.getLogger( "" ).debug( CompareAlgorithm.getStatsMap().get( CompareAlgorithm.MEAN_DELTA_MIN ) );
            Logger.getLogger( "" ).debug( CompareAlgorithm.getStatsMap().get( CompareAlgorithm.MEAN_DELTA_MAX ) );
            Logger.getLogger( "" ).debug( CompareAlgorithm.getStatsMap().get( CompareAlgorithm.STANDARD_DEVIATION ) );
            Logger.getLogger( "" ).debug( CompareAlgorithm.notify( 10e0, 10e0, 10e-3, 10e-3 ) );
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }
}
