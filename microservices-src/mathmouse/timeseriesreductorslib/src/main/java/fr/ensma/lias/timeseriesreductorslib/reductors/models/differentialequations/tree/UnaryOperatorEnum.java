package fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.tree;

/**
 * Permet de lister les opérateurs unaires
 * 
 * @author albertf
 *
 */
public enum UnaryOperatorEnum {
    FACTO( "!" ), ROOT( "√" ), MINUS( "-" ), PLUS( "+" ), ABS( "||" );

    private String name = "";

    UnaryOperatorEnum( String name ) {
        this.name = name;
    }

    public String getValue() {
        return this.name;
    }

    @Override
    public String toString() {
        return name;
    }

    public static double compute( double value, String op ) throws Exception {
        switch ( op ) {
        case "!":
            return factoriel( value );
        case "√":
            return Math.sqrt( value );
        case "||":
            return Math.abs( value );
        case "-":
            return -value;
        case "+":
            return +value;
        default:
            throw new Exception( "Error : " + op + value + "?" );
        }
    }

    private static double factoriel( double n ) {
        return n > 1 ? n * factoriel( n - 1 ) : 1;
    }
}
