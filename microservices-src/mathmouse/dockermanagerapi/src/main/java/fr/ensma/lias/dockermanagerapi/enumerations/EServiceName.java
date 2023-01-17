package fr.ensma.lias.dockermanagerapi.enumerations;

public enum EServiceName {
        IHM( "dbgui" ),
        COMPARATOR( "comparator" ),
        COMPARATOR_MANAGER( "comparator_manager" ),
        GENERATOR( "generator" ),
        DB_CORE( "db_core" ),
        RABBITMQ( "rabbitmq" ),
        MMW_DB( "mathmouse_db" );

    String value;

    private EServiceName( String v ) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static EServiceName fromValue( String v ) {
        for ( EServiceName c : EServiceName.values() ) {
            if ( c.value.equals( v ) ) {
                return c;
            }
        }
        throw new IllegalArgumentException( v );
    }
}
