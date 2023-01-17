package fr.ensma.lias.dbcoreapi.models.enumerations;

public enum ETableName {

    SCHEMA( "schema_mmw." ), DIFFERENTIAL_EQUATION_TABLE(
            SCHEMA.value() + "Differential_Equation" ), NODE_CONTENT_TABLE(
                    SCHEMA.value() + "Node_Content" ), NODE_TABLE( SCHEMA.value() + "Node" ), NODE_CONNECTION_TABLE(
                            SCHEMA.value() + "Node_Connection" ), INPUT_FUNCTION_TABLE(
                                    SCHEMA.value() + "Input_Function" ), EQUATION_INPUT_TABLE(
                                            SCHEMA.value() + "Equation_Input" ), VARIABLE_VALUE_TABLE(
                                                    SCHEMA.value() + "Variable_Value" ), EQUATION_VARIABLE_VALUE_TABLE(
                                                            SCHEMA.value()
                                                                    + "Equation_Variable_Value" ), INITIAL_VALUE_TABLE(
                                                                            SCHEMA.value()
                                                                                    + "Initial_Value" ), EQUATION_INITIAL_VALUE_TABLE(
                                                                                            SCHEMA.value()
                                                                                                    + "Equation_Initial_Value" ), GROUP_TABLE(
                                                                                                            SCHEMA.value()
                                                                                                                    + "Group_Name" );

    private final String value;

    ETableName( String v ) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ETableName fromValue( String v ) {
        for ( ETableName c : ETableName.values() ) {
            if ( c.value.equals( v ) ) {
                return c;
            }
        }
        throw new IllegalArgumentException( v );
    }

}
