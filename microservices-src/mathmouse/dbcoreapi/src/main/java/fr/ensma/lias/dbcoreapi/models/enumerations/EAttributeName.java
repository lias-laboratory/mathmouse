package fr.ensma.lias.dbcoreapi.models.enumerations;

public enum EAttributeName {
    ID_ATTRIBUTE( "id" ), ID_EQUATION_ATTRIBUTE( "id_eq" ), ID_NODE_ATTRIBUTE( "id_node" ), ID_PARENT_ATTRIBUTE(
            "id_parent" ), ID_LEFT_ATTRIBUTE( "id_left" ), ID_RIGHT_ATTRIBUTE( "id_right" ), ID_CONTENT_ATTRIBUTE(
                    "id_content" ), ID_INPUT_FUNCTION_ATTRIBUTE( "id_input_function" ), ID_VARIABLE_ATTRIBUTE(
                            "id_var" ), ID_INITIAL_ATTRIBUTE( "id_initial" ), NODE_DEPTH_ATTRIBUTE(
                                    "node_depth" ), MATH_OBJECT_ATTRIBUTE( "math_object" ), NAME_ATTRIBUTE(
                                            "name" ), DERIV_ATTRIBUTE( "deriv" ), SERIAL_KEY_ATTRIBUTE(
                                                    "serial_key" ), VARIABLE_VALUE_ATTRIBUTE(
                                                            "var_value" ), INITIAL_VALUE_ATTRIBUTE(
                                                                    "init_value" ), INTERPOLATION_FUNCTION_ATTRIBUTE(
                                                                            "interpolation_function" ), GROUP_ATTRIBUTE(
                                                                                    "group_name" ), GROUP_ID_ATTRIBUTE(
                                                                                            "id_group" );

    private final String value;

    EAttributeName( String v ) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static EAttributeName fromValue( String v ) {
        for ( EAttributeName c : EAttributeName.values() ) {
            if ( c.value.equals( v ) ) {
                return c;
            }
        }
        throw new IllegalArgumentException( v );
    }

}
