package fr.ensma.lias.dbgui.graphicwrapper.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.DifferentialEquation2;
import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.FunctionKey;

public class EquationForm {
    private String                       name;
    private String                       group;
    private String                       formula;
    private String                       timeOrigin;
    private Map<String, String>          variablesValues;
    private Map<String, String>          initialValues;
    private ArrayList<InputFunctionForm> inputFunctionsForms;

    public EquationForm() {
    }

    public EquationForm( DifferentialEquation2 equation ) {
        name = equation.getName();
        formula = equation.getEquation().toString();

        variablesValues = new HashMap<String, String>();
        for ( String key : equation.getParametersSet().getMapping().keySet() ) {
            variablesValues.put( key, String.valueOf( equation.getParametersSet().getMapping().get( key ) ) );
        }

        initialValues = new HashMap<String, String>();
        for ( FunctionKey key : equation.getParametersSet().getInitialValues().keySet() ) {
            initialValues.put( String.valueOf( key.getDeriv() ),
                    String.valueOf( equation.getParametersSet().getInitialValues().get( key ) ) );
        }

        inputFunctionsForms = new ArrayList<InputFunctionForm>();
        for ( FunctionKey key : equation.getInputFunctions().keySet() ) {
            inputFunctionsForms.add( new InputFunctionForm( key, equation.getInputFunctions().get( key ) ) );
        }
        timeOrigin = String.valueOf( ( (TreeMap) inputFunctionsForms.get( 0 ).getValues() ).firstKey() );
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

    public String getFormula() {
        return formula;
    }

    public void setFormula( String formula ) {
        this.formula = formula;
    }

    public String getTimeOrigin() {
        return timeOrigin;
    }

    public void setTimeOrigin( String timeOrigin ) {
        this.timeOrigin = timeOrigin;
    }

    public Map<String, String> getVariablesValues() {
        return variablesValues;
    }

    public void setVariablesValues( Map<String, String> variableValues ) {
        this.variablesValues = variableValues;
    }

    public ArrayList<InputFunctionForm> getInputFunctionsForms() {
        return inputFunctionsForms;
    }

    public void setInputFunctionsForms( ArrayList<InputFunctionForm> inputFunctionsForms ) {
        this.inputFunctionsForms = inputFunctionsForms;
    }

    public Map<String, String> getInitialValues() {
        return initialValues;
    }

    public void setInitialValues( Map<String, String> initialValues ) {
        this.initialValues = initialValues;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( formula == null ) ? 0 : formula.hashCode() );
        result = prime * result + ( ( group == null ) ? 0 : group.hashCode() );
        result = prime * result + ( ( inputFunctionsForms == null ) ? 0 : inputFunctionsForms.hashCode() );
        result = prime * result + ( ( name == null ) ? 0 : name.hashCode() );
        result = prime * result + ( ( timeOrigin == null ) ? 0 : timeOrigin.hashCode() );
        result = prime * result + ( ( variablesValues == null ) ? 0 : variablesValues.hashCode() );
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
        EquationForm other = (EquationForm) obj;
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
        if ( inputFunctionsForms == null ) {
            if ( other.inputFunctionsForms != null )
                return false;
        } else if ( !inputFunctionsForms.equals( other.inputFunctionsForms ) )
            return false;
        if ( name == null ) {
            if ( other.name != null )
                return false;
        } else if ( !name.equals( other.name ) )
            return false;
        if ( timeOrigin == null ) {
            if ( other.timeOrigin != null )
                return false;
        } else if ( !timeOrigin.equals( other.timeOrigin ) )
            return false;
        if ( variablesValues == null ) {
            if ( other.variablesValues != null )
                return false;
        } else if ( !variablesValues.equals( other.variablesValues ) )
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "EquationForm [name=" + name + ", group=" + group + ", formula=" + formula + ", timeOrigin=" + timeOrigin
                + ", variableValues=" + variablesValues + ", inputFunctionsForms=" + inputFunctionsForms + "]";
    }

}