package fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.tree;

/**
 * Permet de lister les opérateurs binaires
 * 
 * @author albertf
 *
 */
public enum BinaryOperatorEnum {
    MINUS( "-" ), PLUS( "+" ), STAR( "*" ), DIVI( "/" ), EQUAL( "=" ),
    /*
     * DIFF("<>"), NOTEQUAL("!="), SUPEQ("=>"), INFEQ("=<"), SUP(">"), INF("<"),
     */
    ROOT( "√" ), POW( "^" );

    private String name = "";

    BinaryOperatorEnum( String name ) {
        this.name = name;
    }

    public String getValue() {
        return this.name;
    }

    @Override
    public String toString() {
        return name;
    }

    public static double compute( double val1, double val2, String op ) throws Exception {
        switch ( op ) {
        case "-":
            return val1 - val2;
        case "+":
            return val1 + val2;
        case "*":
            return val1 * val2;
        case "/":
            return val1 / val2;
        case "√":
            return Math.pow( val1, 1 / val2 );
        case "^":
            return Math.pow( val1, val2 );
        default:
            throw new Exception( "Error : " + val1 + op + val2 + "?" );
        }
    }
}
