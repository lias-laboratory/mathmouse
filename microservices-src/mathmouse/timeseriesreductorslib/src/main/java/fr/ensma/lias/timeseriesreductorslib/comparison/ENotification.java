package fr.ensma.lias.timeseriesreductorslib.comparison;

public enum ENotification {
    MODEL_ACCEPTED( "ModelAccepted" ), MODEL_REJECTED( "ModelRejected" ), UNDETERMINED( "Undetermined" );

    private final String value;

    private ENotification( String v ) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static ENotification fromValue( String v ) {
        for ( ENotification c : ENotification.values() ) {
            if ( c.value.equals( v ) ) {
                return c;
            }
        }
        throw new IllegalArgumentException( v );
    }

}
