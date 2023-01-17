package fr.ensma.lias.dbgui.graphicwrapper.enumerations;

public enum EStatus {

        CREATED( "created" ),
        STARTING( "starting" ),
        WAITING( "waiting" ),
        IN_PROGRESS( "in progress" ),
        OVER( "over" );

    private final String value;

    EStatus( String v ) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static EStatus fromValue( String v ) {
        for ( EStatus c : EStatus.values() ) {
            if ( c.value.equals( v ) ) {
                return c;
            }
        }
        throw new IllegalArgumentException( v );
    }

}
