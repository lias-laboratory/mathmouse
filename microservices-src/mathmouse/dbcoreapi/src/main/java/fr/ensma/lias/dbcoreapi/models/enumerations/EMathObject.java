package fr.ensma.lias.dbcoreapi.models.enumerations;

/**
 * Enumeration of the different mathematical type of a node.
 * 
 * @author cyrille ponchateau (ponchateau@ensma.fr)
 *
 */
public enum EMathObject {

    BINARY_OPERATOR( "binary-operator" ), UNARY_OPERATOR( "unary-operator" ), FUNCTION( "function" ), VARIABLE(
            "variable" ), NUMBER( "number" );

    private final String value;

    EMathObject( String v ) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static EMathObject fromValue( String v ) {
        for ( EMathObject c : EMathObject.values() ) {
            if ( c.value.equals( v ) ) {
                return c;
            }
        }
        throw new IllegalArgumentException( v );
    }

}
