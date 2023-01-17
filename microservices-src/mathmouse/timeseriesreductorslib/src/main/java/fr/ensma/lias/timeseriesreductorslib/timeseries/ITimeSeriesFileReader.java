package fr.ensma.lias.timeseriesreductorslib.timeseries;

import java.io.FileNotFoundException;

/**
 * 
 * @author cyrille ponchateau (cyrille.ponchateau@ensma.fr)
 *
 */
public interface ITimeSeriesFileReader {
    /**
     * Reads a series from a file. If the file contains several series, it gets
     * the first one. The file MUST be in the resources/timeseries directory of
     * the project. The file name is enough. The file MUST be in a csv-like
     * format. If the file is not found, a FileNotFoundException is thrown.
     * 
     * @param fileName
     * @return
     * @throws FileNotFoundException
     */
    void getSeriesFromFile( String fileName, String separator, String delimiter )
            throws FileNotFoundException;

    /**
     * Reads a series from a file. If the file contains several series, it gets
     * the series at the position specified by seriesIndex. If seriesIndex is
     * negative or greater than the number of series in the file, an
     * ArrayIndexOutOfBoundsException is thrown.</br>
     * The file MUST be in the resources/timeseries directory of the project.
     * The file name is enough. The file MUST be in a csv-like format. If the
     * file is not found, a FileNotFoundException is thrown.
     * 
     * @param fileName
     * @param seriesIndex
     *            (index starting at zero)
     * @return
     * @throws FileNotFoundException
     * @throws ArrayIndexOutOfBoundsException
     */
    void getSeriesFromFile( String fileName, String separator, String delimiter, int seriesIndex )
            throws FileNotFoundException, ArrayIndexOutOfBoundsException;

}
