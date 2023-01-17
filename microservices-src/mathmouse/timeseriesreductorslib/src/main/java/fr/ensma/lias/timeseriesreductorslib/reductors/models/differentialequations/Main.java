package fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.xml.DOMConfigurator;

public class Main {
    private static final String XSD_FILE_PATH = "resources/differential-equation-1.0.xsd";

    public static void main( String[] args ) {
        String log4j = "resources/log4j.xml";
        DOMConfigurator.configure( log4j );

        Map<FunctionKey, String> test = new HashMap<FunctionKey, String>();
        test.put( new FunctionKey( "u", 1 ), "e" );
        System.out.println( test );
        test.put( new FunctionKey( "u", 1 ), "f" );
        System.out.println( test );

    }

}
