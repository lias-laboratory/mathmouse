package fr.ensma.lias.dbcoreapi.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import fr.ensma.lias.timeseriesreductorslib.timeseries.TimeSeriesDoubleDouble;
import fr.ensma.lias.timeseriesreductorslib.timeseries.interpolation.EInterpolationFunction;

public class TestCSVFileFactory {
    private static final String    FILE_PATH = "src/test/resources/csv/test.csv";

    private TimeSeriesDoubleDouble series;
    private CSVFileFactory         factory;

    @Before
    public void initialize() {
        factory = CSVFileFactory.getInstance();

        series = new TimeSeriesDoubleDouble();
        series.setInterpolationFunction( EInterpolationFunction.LINEAR_INTERPOLATION );
        series.put( 12.5d, 14.5d );
    }

    @Test
    public void testWriteSeriesInFile() {

        try {
            factory.writeSeriesInFile( series, FILE_PATH );

            File file = new File( FILE_PATH );
            Assert.assertTrue( file.exists() );
            BufferedReader bf = new BufferedReader( new FileReader( file ) );

            String content;

            content = bf.readLine();
            Assert.assertEquals( EInterpolationFunction.LINEAR_INTERPOLATION.value(), content );

            content = bf.readLine();
            Assert.assertEquals( 12.5d + CSVFileFactory.SEPARATOR + series.get( 12.5d ), content );

            file.delete();
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }

    @Test
    public void testReadSeriesFromFile() {
        factory.writeSeriesInFile( series, FILE_PATH );

        TimeSeriesDoubleDouble series = factory.readSeriesFromFile( FILE_PATH );

        Assert.assertEquals( EInterpolationFunction.LINEAR_INTERPOLATION.value(),
                series.getInterpolationFunction().getInterpolationFunctionName().value() );
        Assert.assertTrue( series.size() == 1 );
        Assert.assertTrue( Math.abs( 12.5d - series.firstKey().doubleValue() ) < 10e-8 );
        Assert.assertTrue( Math.abs( 14.5d - series.get( series.firstKey() ) ) < 10e-8 );
    }
}
