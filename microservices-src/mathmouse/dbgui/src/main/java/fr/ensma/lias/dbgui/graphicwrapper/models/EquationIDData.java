package fr.ensma.lias.dbgui.graphicwrapper.models;

public class EquationIDData {
    private long   id;
    private String name;
    private String group;

    public EquationIDData( long id, String name, String group ) {
        super();
        this.id = id;
        this.name = name;
        this.group = group;
    }

    public long getId() {
        return id;
    }

    public void setId( long id ) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup( String group ) {
        this.group = group;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( group == null ) ? 0 : group.hashCode() );
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
        EquationIDData other = (EquationIDData) obj;
        if ( group == null ) {
            if ( other.group != null )
                return false;
        } else if ( !group.equals( other.group ) )
            return false;
        if ( name == null ) {
            if ( other.name != null )
                return false;
        } else if ( !name.equals( other.name ) )
            return false;
        return true;
    }

}
