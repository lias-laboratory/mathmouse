package fr.ensma.lias.timeseriesreductorslib.timeseries;

public class Main {
    public static void main( String[] args ) {
        TimeSeriesDoubleDouble ts = TimeSeriesDoubleDoubleIO.readFromFile(
                "D:\\work\\projects\\Eclipse\\timeseriesreductorslib\\resources\\timeseries-sources\\Data3.txt" );
        System.out.println( ts.size() );
        TimeSeriesDoubleDoubleIO.writeInFile( ts, "D:\\work\\sources\\Data3.csv" );
    }
}
