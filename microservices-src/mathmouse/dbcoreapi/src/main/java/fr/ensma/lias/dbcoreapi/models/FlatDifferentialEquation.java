package fr.ensma.lias.dbcoreapi.models;

import java.sql.ResultSet;

/**
 * 
 * @author Cyrille Ponchateau (cyrille.ponchateau@ensma.fr)
 *
 */
public class FlatDifferentialEquation {
    private long          id;
    private Formula       formula;
    private Variables     variables;
    private Inputs        inputs;
    private InitialValues initialValues;
    private int           order;
    private String        name;
    private long          idGroup;
    private String        group;
    private boolean       groupExist;
    private boolean       allExist;

    public FlatDifferentialEquation() {
        formula = new Formula();
        variables = new Variables();
        inputs = new Inputs();
        initialValues = new InitialValues();
        allExist = false;
        groupExist = false;
    }

    public FlatDifferentialEquation( long id, Formula formula, Variables variables, Inputs inputs,
            InitialValues initialValues ) {
        this.id = id;
        this.formula = formula;
        this.variables = variables;
        this.inputs = inputs;
        this.initialValues = initialValues;
        allExist = false;
        groupExist = false;
    }

    public FlatDifferentialEquation( ResultSet nodesTable, ResultSet variablesTable, ResultSet inputsTable,
            ResultSet initialValuesTable ) {
        id = 0;
        formula = new Formula( nodesTable );
        variables = new Variables( variablesTable );
        inputs = new Inputs( inputsTable );
        initialValues = new InitialValues( initialValuesTable );
        allExist = false;
    }

    public long getId() {
        return id;
    }

    public void setId( long id ) {
        this.id = id;
    }

    public Formula getFormula() {
        return formula;
    }

    public void setFormula( Formula formula ) {
        this.formula = formula;
    }

    public Variables getVariables() {
        return variables;
    }

    public void setVariables( Variables variables ) {
        this.variables = variables;
    }

    public Inputs getInputs() {
        return inputs;
    }

    public void setInputs( Inputs inputs ) {
        this.inputs = inputs;
    }

    public InitialValues getInitialValues() {
        return initialValues;
    }

    public void setInitialValues( InitialValues initialValues ) {
        this.initialValues = initialValues;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder() {
        order = -1;
        if ( formula != null ) {
            for ( Long key : formula.keySet() ) {
                if ( formula.get( key ).getDeriv() > order ) {
                    order = formula.get( key ).getDeriv();
                }
            }
        }
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public long getIdGroup() {
        return idGroup;
    }

    public void setIdGroup( long idGroup ) {
        this.idGroup = idGroup;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup( String group ) {
        this.group = group;
    }

    public boolean groupExist() {
        return groupExist;
    }

    public void setGroupExist( boolean groupExist ) {
        this.groupExist = groupExist;
    }

    public boolean allExist() {
        return allExist;
    }

    public void setAllExist( boolean allExist ) {
        this.allExist = allExist;
    }

    public void setInputSerialKeys() {
        inputs.setSerialKeys( id );
    }

    public void updateAllExist() {
        formula.updateExist();
        variables.updateExist();
        inputs.updateExist();
        initialValues.updateExist();
        allExist = formula.allExist() && variables.allExist() && inputs.allExist() && initialValues.allExist();
    }

    public void updateId( long id ) {
        this.id = id;
        for ( Long key : formula.keySet() ) {
            formula.get( key ).setEquation( id );
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( formula == null ) ? 0 : formula.hashCode() );
        result = prime * result + ( ( group == null ) ? 0 : group.hashCode() );
        result = prime * result + ( ( initialValues == null ) ? 0 : initialValues.hashCode() );
        result = prime * result + ( ( inputs == null ) ? 0 : inputs.hashCode() );
        result = prime * result + ( ( name == null ) ? 0 : name.hashCode() );
        result = prime * result + order;
        result = prime * result + ( ( variables == null ) ? 0 : variables.hashCode() );
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
        FlatDifferentialEquation other = (FlatDifferentialEquation) obj;
        if ( formula == null ) {
            if ( other.formula != null )
                return false;
        } else if ( !formula.equals( other.formula ) )
            return false;
        if ( group == null ) {
            if ( other.group != null )
                return false;
        } else if ( !group.equals( other.group ) )
            return false;
        if ( initialValues == null ) {
            if ( other.initialValues != null )
                return false;
        } else if ( !initialValues.equals( other.initialValues ) )
            return false;
        if ( inputs == null ) {
            if ( other.inputs != null )
                return false;
        } else if ( !inputs.equals( other.inputs ) )
            return false;
        if ( name == null ) {
            if ( other.name != null )
                return false;
        } else if ( !name.equals( other.name ) )
            return false;
        if ( order != other.order )
            return false;
        if ( variables == null ) {
            if ( other.variables != null )
                return false;
        } else if ( !variables.equals( other.variables ) )
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "FlatDifferentialEquation [id=" + id + ", formula=" + formula + ", variables=" + variables + ", inputs="
                + inputs + ", initialValues=" + initialValues + ", order=" + order + ", name=" + name + ", group="
                + group + "]";
    }

}
