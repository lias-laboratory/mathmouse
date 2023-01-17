package fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations;

public class FunctionKey {
    private String name;
    private int    deriv;

    public FunctionKey( String name, int deriv ) {
        this.name = name;
        this.deriv = deriv;
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public int getDeriv() {
        return deriv;
    }

    public void setDeriv( int deriv ) {
        this.deriv = deriv;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + deriv;
        result = prime * result + ( ( name == null ) ? 0 : name.hashCode() );
        return result;
    }

    @Override
    public boolean equals( Object obj ) {
        if ( this == obj )
            return true;
        if ( obj == null )
            return false;
        if ( getClass() != obj.getClass() )
            return false;
        FunctionKey other = (FunctionKey) obj;
        if ( deriv != other.deriv )
            return false;
        if ( name == null ) {
            if ( other.name != null )
                return false;
        } else if ( !name.equals( other.name ) )
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "FunctionKey [name=" + name + ", deriv=" + deriv + "]";
    }

}
