package fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Defines the parameters set of an equation
 * 
 * @author albertf + cyrille ponchateau (ponchateau@ensma.fr)
 *
 */
public class ParametersSet implements Serializable {
    private static final long        serialVersionUID = -6076365521410988771L;

    private Long                     id;
    private Map<String, Double>      mapping          = new HashMap<String, Double>();
    private Map<FunctionKey, Double> initialValues    = new HashMap<FunctionKey, Double>();
    private InputFunctions           inputFunctions   = new InputFunctions();

    public Long getId() {
        return id;
    }

    public void setId( Long id ) {
        this.id = id;
    }

    public Map<FunctionKey, Double> getInitialValues() {
        return initialValues;
    }

    public void setInitialValues( Map<FunctionKey, Double> initialValues ) {
        this.initialValues = initialValues;
    }

    public InputFunctions getInputFunctions() {
        return inputFunctions;
    }

    public void setInputFunctions( InputFunctions inputFunction ) {
        this.inputFunctions = inputFunction;
    }

    public Map<String, Double> getMapping() {
        return mapping;
    }

    public void setMapping( Map<String, Double> mapping ) {
        this.mapping = mapping;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    /**
     * 
     * @param mapping
     * @param initialValues
     * @param inputFunctions
     */
    public ParametersSet( Map<String, Double> mapping, Map<FunctionKey, Double> initialValues,
            InputFunctions inputFunctions ) {
        this.mapping = mapping;
        this.initialValues = initialValues;
        this.inputFunctions = inputFunctions;
    }

    /**
     * 
     */
    public ParametersSet() {
        mapping = new HashMap<String, Double>();
        initialValues = new HashMap<FunctionKey, Double>();
        inputFunctions = new InputFunctions();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( initialValues == null ) ? 0 : initialValues.hashCode() );
        result = prime * result + ( ( inputFunctions == null ) ? 0 : inputFunctions.hashCode() );
        result = prime * result + ( ( mapping == null ) ? 0 : mapping.hashCode() );
        return result;
    }

    @Override
    public String toString() {
        return "DiffEquaSystem [mapping=" + mapping + ", initial values=" + initialValues
                + ", inputFunction=" + inputFunctions + "]";
    }

    @Override
    public boolean equals( Object obj ) {
        if ( this == obj )
            return true;
        if ( obj == null )
            return false;
        if ( getClass() != obj.getClass() )
            return false;
        ParametersSet other = (ParametersSet) obj;
        if ( initialValues == null ) {
            if ( other.initialValues != null )
                return false;
        } else if ( !initialValues.equals( other.initialValues ) )
            return false;
        if ( inputFunctions == null ) {
            if ( other.inputFunctions != null )
                return false;
        } else if ( !inputFunctions.equals( other.inputFunctions ) )
            return false;
        if ( mapping == null ) {
            if ( other.mapping != null )
                return false;
        } else if ( !mapping.equals( other.mapping ) )
            return false;
        return true;
    }

    /**
     * Permet de vérifier l'existance d'une variable dans le tableau de mapping
     * 
     * @param obj
     *            Variable
     * @return
     */
    public boolean constainsObject( Object obj ) {
        return this.mapping.containsValue( obj );
    }

    /**
     * Permet de vérifier l'existance d'une clef dans le tableau de mapping
     * 
     * @param obj
     *            Clef
     * @return
     */
    public boolean constainsKey( String key ) {
        if ( this.mapping != null )
            return this.mapping.containsKey( key );
        else
            return false;
    }

    /**
     * Permet de vérifier l'existance de conditions initiales
     * 
     * @return True si les conditions initiales sont initialisées, False sinon
     */
    public boolean hasInitialCondition() {
        if ( this.initialValues != null ) {
            for ( FunctionKey key : initialValues.keySet() ) {
                if ( initialValues.get( key ).equals( Double.NaN ) ) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Permet de vérifier l'existance du tableau de mapping
     * 
     * @return
     */
    public boolean canbeMapped() {
        return this.mapping != null && !this.mapping.isEmpty();
    }

    /**
     * 
     */
    public ParametersSet clone() {
        Map<String, Double> mappingclone = new HashMap<String, Double>();
        InputFunctions inputFunctionclone = new InputFunctions();
        Map<FunctionKey, Double> initialValuesclone = new HashMap<FunctionKey, Double>();

        // copy of the mapping list
        for ( String key : mapping.keySet() )
            mappingclone.put( key, mapping.get( key ) );

        // copy of the input functions list
        for ( FunctionKey key : inputFunctions.keySet() )
            inputFunctionclone.put( key, inputFunctions.get( key ) );

        for ( FunctionKey key : initialValues.keySet() )
            initialValuesclone.put( key, initialValues.get( key ) );

        // copy of the whole object
        ParametersSet clone = new ParametersSet( mappingclone, initialValuesclone, inputFunctionclone );

        return clone;
    }
}
