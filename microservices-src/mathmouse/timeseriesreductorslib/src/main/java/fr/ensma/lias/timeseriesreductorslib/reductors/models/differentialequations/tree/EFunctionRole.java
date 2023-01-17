package fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.tree;

/**
 * Lists the different role a function van have (a function has only one role).
 * 
 * @author cyrille ponchateau (cyrille.ponchateau@ensma.fr)
 *
 */
public enum EFunctionRole {
    EQUATION_INPUT( "equationInput" ), EQUATION_OUTPUT( "equationOutput" ), BASIC_MATH_FUNCTON(
            "basicMathFunction" ), BASIC_USER_DEFINED_FUNCTION( "basicUserDefinedFunction" );

    private String name = "";

    EFunctionRole( String name ) {
        this.name = name;
    }

    public String getValue() {
        return this.name;
    }

    @Override
    public String toString() {
        return name;
    }

    public static EFunctionRole getFunctionRole( String role ) {
        EFunctionRole[] values = values();

        EFunctionRole result = null;

        for ( int i = 0; i < values.length; i++ ) {
            if ( values[i].getValue().equals( role ) )
                result = values[i];
        }

        return result;
    }
}
