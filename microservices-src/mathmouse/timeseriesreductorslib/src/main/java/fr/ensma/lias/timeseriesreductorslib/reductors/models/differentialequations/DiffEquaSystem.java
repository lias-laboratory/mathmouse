package fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Permet de définir un système d'une équation Un système correspond à la liste
 * des constantes, forces et les conditions initiales d'une équation Une
 * équation peut avoir plusieurs systèmes
 * 
 * @author albertf
 *
 */
@Deprecated
public class DiffEquaSystem implements Serializable {
    /**
     * JSON Fields
     */
    public static final String  ID               = "id";
    public static final String  INITIAL          = "initial";
    public static final String  ABSCISSA         = "abscissa";
    public static final String  STEP             = "step";
    public static final String  MAPPING          = "mapping";
    public static final String  INPUT_FUNCTIONS  = "input functions";

    /**
     * 
     */
    private static final long   serialVersionUID = -6076365521410988771L;

    private Long                id;

    private Map<String, String> mapping          = new HashMap<String, String>();

    private Double              initial;
    private Double              abscissa;
    private Double              step;

    private Map<String, String> inputFunction    = new HashMap<String, String>();

    public Long getId() {
        return id;
    }

    public void setId( Long id ) {
        this.id = id;
    }

    public Double getStep() {
        return step;
    }

    public void setStep( Double step ) {
        this.step = step;
    }

    public DiffEquaSystem( Map<String, String> mapping, Double initial,
            Double abscissa, Double step, Map<String, String> inputFunction ) {
        super();
        this.mapping = mapping;
        this.initial = initial;
        this.abscissa = abscissa;
        this.step = step;
        this.inputFunction = inputFunction;
    }

    public Map<String, String> getInputFunction() {
        return inputFunction;
    }

    public void setInputFunction( Map<String, String> inputFunction ) {
        this.inputFunction = inputFunction;
    }

    public DiffEquaSystem( Map<String, String> mapping, Double initial, Double abscissa ) {
        super();
        this.mapping = mapping;
        this.initial = initial;
        this.abscissa = abscissa;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ( ( abscissa == null ) ? 0 : abscissa.hashCode() );
        result = prime * result + ( ( initial == null ) ? 0 : initial.hashCode() );
        result = prime * result
                + ( ( inputFunction == null ) ? 0 : inputFunction.hashCode() );
        result = prime * result + ( ( mapping == null ) ? 0 : mapping.hashCode() );
        result = prime * result + ( ( step == null ) ? 0 : step.hashCode() );
        return result;
    }

    @Override
    public String toString() {
        return "DiffEquaSystem [mapping=" + mapping + ", initial=" + initial
                + ", abscissa=" + abscissa + ", step=" + step
                + ", inputFunction=" + inputFunction + "]";
    }

    @Override
    public boolean equals( Object obj ) {
        if ( this == obj )
            return true;
        if ( obj == null )
            return false;
        if ( getClass() != obj.getClass() )
            return false;
        DiffEquaSystem other = (DiffEquaSystem) obj;
        if ( abscissa == null ) {
            if ( other.abscissa != null )
                return false;
        } else if ( !abscissa.equals( other.abscissa ) )
            return false;
        if ( initial == null ) {
            if ( other.initial != null )
                return false;
        } else if ( !initial.equals( other.initial ) )
            return false;
        if ( inputFunction == null ) {
            if ( other.inputFunction != null )
                return false;
        } else if ( !inputFunction.equals( other.inputFunction ) )
            return false;
        if ( mapping == null ) {
            if ( other.mapping != null )
                return false;
        } else if ( !mapping.equals( other.mapping ) )
            return false;
        if ( step == null ) {
            if ( other.step != null )
                return false;
        } else if ( !step.equals( other.step ) )
            return false;
        return true;
    }

    public Double getAbscissa() {
        return abscissa;
    }

    public void setAbscissa( Double abscissa ) {
        this.abscissa = abscissa;
    }

    public Double getInitial() {
        return initial;
    }

    public void setInitial( Double initial ) {
        this.initial = initial;
    }

    public DiffEquaSystem() {
        super();
        this.mapping = new HashMap<String, String>();
        this.inputFunction = new HashMap<String, String>();
    }

    public DiffEquaSystem( Map<String, String> mapping ) {
        super();
        this.mapping = mapping;
    }

    public Map<String, String> getMapping() {
        return mapping;
    }

    public void setMapping( Map<String, String> mapping ) {
        this.mapping = mapping;
    }

    public void addElement( String name, String obj ) {
        this.mapping.put( name, obj );
    }

    public String getElement( String name ) {
        return this.mapping.get( name );
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

    public String removeElement( String key ) {
        return this.mapping.remove( key );
    }

    /**
     * Permet de vérifier l'existance de conditions initiales
     * 
     * @return True si les conditions initiales sont initialisées, False sinon
     */
    public boolean hasInitialCondition() {
        if ( this.initial != null && !this.initial.equals( Double.NaN ) )
            return true;
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

    public DiffEquaSystem clone() {
        Map<String, String> mappingclone = new HashMap<String, String>();
        Map<String, String> inputFunctionclone = new HashMap<String, String>();

        // copy of the mapping list
        for ( String key : mapping.keySet() )
            mappingclone.put( key, mapping.get( key ) );

        // copy of the input functions list
        for ( String key : inputFunction.keySet() )
            inputFunctionclone.put( key, inputFunction.get( key ) );

        // copy of the whole object
        DiffEquaSystem clone = new DiffEquaSystem( mappingclone, new Double( this.initial ),
                new Double( this.abscissa ), new Double( this.step ), inputFunctionclone );

        return clone;
    }
}
