package fr.ensma.lias.javarabbitmqapi.enumerations;

public enum ESpecialCharacter {
        NEWLINE( "\n" ), SEPARATOR( ";" ), SEPARATOR_SERIES( "   " );

    private String value;

    private ESpecialCharacter( String value ) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    public static ESpecialCharacter getSpecialCharacter( String character ) {
        ESpecialCharacter[] values = values();

        ESpecialCharacter result = null;

        for ( int i = 0; i < values.length; i++ ) {
            if ( values[i].value().equals( character ) )
                result = values[i];
        }

        return result;
    }
}
