package fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations;

import java.util.HashMap;

public class InputFunctions extends HashMap<FunctionKey, String> {

    public InputFunctions() {
        super();
    }

    public boolean containsFunctionName( String functionName ) {
        for ( FunctionKey key : this.keySet() ) {
            if ( key.getName().equals( functionName ) )
                return true;
        }

        return false;
    }
}
