package fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.numericalmethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.DiffEquaSystem;
import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.DifferentialEquation;
import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.tree.BinaryOperator;
import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.tree.BinaryOperatorEnum;
import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.tree.Function;
import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.tree.NodeObject;
import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.tree.Number;
import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.tree.Variable;
import fr.ensma.lias.timeseriesreductorslib.timeseries.TimeSeriesDoubleDouble;

public class NumericalMethod {
    private Map<Integer, NodeObject>            syst;
    private DiffEquaSystem                      diffEqSyst;
    private int                                 order;
    private String                              unknownFunction;
    private Map<String, TimeSeriesDoubleDouble> inputFunctions;

    public String getUnknownFunction() {
        return unknownFunction;
    }

    public void setUnknownFunction( String unknownFunction ) {
        this.unknownFunction = unknownFunction;
    }

    public DiffEquaSystem getDiffEqSyst() {
        return diffEqSyst;
    }

    public void setDiffEqSyst( DiffEquaSystem diffEqSyst ) {
        this.diffEqSyst = diffEqSyst;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder( int order ) {
        this.order = order;
    }

    public void setSyst( Map<Integer, NodeObject> syst ) {
        this.syst = syst;
    }

    public Map<Integer, NodeObject> getSyst() {
        return syst;
    }

    /**
     * Constructeur
     * 
     * @param eq
     *            Equation différentielle à transformer
     * @param system
     *            Numéro du système
     * @throws Exception
     */
    public NumericalMethod( DifferentialEquation eq ) throws Exception {
        super();
        syst = new HashMap<Integer, NodeObject>();
        NodeObject yForm = eq.transformEquationToYForm( eq.getOrder() )
                .transformEquationWithSystem( eq.getSystem() );
        String unknown = eq.getUnknownFunction();
        for ( int i = 1; i <= eq.getOrder(); i++ ) {
            if ( i < eq.getOrder() )
                syst.put( i, new Function( unknown, i ) );
            else
                syst.put( i, yForm.getRightPart() );
        }
        this.diffEqSyst = eq.getSystem();
        this.order = eq.getOrder();
        this.unknownFunction = eq.getUnknownFunction();
        this.inputFunctions = eq.getInputFunctions();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ( ( diffEqSyst == null ) ? 0 : diffEqSyst.hashCode() );
        result = prime * result + order;
        result = prime * result + ( ( syst == null ) ? 0 : syst.hashCode() );
        result = prime * result
                + ( ( unknownFunction == null ) ? 0 : unknownFunction.hashCode() );
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
        NumericalMethod other = (NumericalMethod) obj;
        if ( diffEqSyst == null ) {
            if ( other.diffEqSyst != null )
                return false;
        } else if ( !diffEqSyst.equals( other.diffEqSyst ) )
            return false;
        if ( order != other.order )
            return false;
        if ( syst == null ) {
            if ( other.syst != null )
                return false;
        } else if ( !syst.equals( other.syst ) )
            return false;
        if ( unknownFunction == null ) {
            if ( other.unknownFunction != null )
                return false;
        } else if ( !unknownFunction.equals( other.unknownFunction ) )
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "RungeKutta [syst=" + syst + ", diffEqSyst=" + diffEqSyst
                + ", order=" + order + ", unknownFunction=" + unknownFunction
                + "]";
    }

    /**
     * Initialisation de Yn
     * 
     * @param system
     *            Système correspondant
     * @return Map contenant la condition initiale de l'équation et de ces
     *         composantes
     */
    private Map<Integer, NodeObject> initU1() {
        Map<Integer, NodeObject> result = new HashMap<Integer, NodeObject>();
        for ( int i = 1; i <= this.order; i++ ) {
            if ( i != order )
                result.put( i, new Number( "0" ) );
            else
                result.put( i, new Number( this.diffEqSyst.getInitial().toString() ) );
        }
        return result;
    }

    /**
     * Permet de remplacer une fonction et ses dérivées par les composantes d'un
     * vecteur
     * 
     * @param v
     *            Vecteur
     * @param funcname
     *            Nom de la fonction
     * @return Nouveau vecteur calculé
     * @throws Exception
     */
    private Map<Integer, NodeObject> computeKx( Map<Integer, NodeObject> v, String funcname )
            throws Exception {
        Map<Integer, NodeObject> result = new HashMap<Integer, NodeObject>( this.syst );
        for ( int i = 1; i <= this.order; i++ ) {
            result.put( i, result.get( i ).replaceFunctionByVector( funcname, v ) );
        }
        return result;
    }

    /**
     * Permet de calculer les formules de Ux
     * 
     * @param U1
     *            Vecteur contenant les valeurs de U1
     * @param Kx
     *            Vecteur contenant les valeurs de Kx (x étant 1,2,3 ou 4)
     * @param h
     *            Pas de U (h ou h/2)
     * @return vecteur contenant Ux
     */
    private Map<Integer, NodeObject> computeUx( Map<Integer, NodeObject> U1, Map<Integer, NodeObject> Kx,
            NodeObject h ) {
        Map<Integer, NodeObject> result = new HashMap<Integer, NodeObject>( this.syst );
        for ( int i = 1; i <= this.order; i++ ) {
            NodeObject temp = new BinaryOperator( BinaryOperatorEnum.STAR.getValue(), h.clone(), Kx.get( i ).clone() );
            temp = new BinaryOperator( BinaryOperatorEnum.PLUS.getValue(), U1.get( i ).clone(), temp.clone() );
            result.put( i, temp );
        }
        return result;
    }

    /**
     * Construit la formule de Un+1
     * 
     * @return Formule Un+1
     */
    private NodeObject initUNP1() {
        NodeObject K3 = new BinaryOperator( BinaryOperatorEnum.STAR.getValue(), new Number( "2" ),
                new Variable( "K3" ) );
        NodeObject K2 = new BinaryOperator( BinaryOperatorEnum.STAR.getValue(), new Number( "2" ),
                new Variable( "K2" ) );
        NodeObject K3K4 = new BinaryOperator( BinaryOperatorEnum.PLUS.getValue(), K3, new Variable( "K4" ) );
        NodeObject K1K2 = new BinaryOperator( BinaryOperatorEnum.PLUS.getValue(), new Variable( "K1" ), K2 );

        NodeObject K1K2K3K4 = new BinaryOperator( BinaryOperatorEnum.PLUS.getValue(), K1K2, K3K4 );

        NodeObject h6 = new BinaryOperator( BinaryOperatorEnum.DIVI.getValue(), new Variable( "H" ),
                new Number( "6" ) );
        h6 = new BinaryOperator( BinaryOperatorEnum.STAR.getValue(), h6.clone(), K1K2K3K4 );

        return new BinaryOperator( BinaryOperatorEnum.PLUS.getValue(), new Variable( "U1" ), h6 );

    }

    /**
     * Remplace les Ki de la formule Un+1 par ceux donnés en paramètre
     * 
     * @param ki
     *            Liste des K
     * @return Map contenant, pour chaque composante, l'équation Un+1
     * @throws Exception
     */
    private Map<Integer, NodeObject> computeUNP1( List<Map<Integer, NodeObject>> ki ) throws Exception {
        if ( ki.size() > 4 )
            throw new Exception( "Ki > 4" );
        Map<Integer, NodeObject> result = new HashMap<Integer, NodeObject>( this.order );
        NodeObject Unp1 = this.initUNP1();
        for ( int i = 1; i <= this.order; i++ ) {
            NodeObject temp = Unp1.replaceVar( "K1", ki.get( 0 ).get( i ) );
            temp = temp.replaceVar( "K2", ki.get( 1 ).get( i ) );
            temp = temp.replaceVar( "K3", ki.get( 2 ).get( i ) );
            temp = temp.replaceVar( "K4", ki.get( 3 ).get( i ) );
            result.put( i, temp );
        }
        return result;
    }

    /**
     * Permet de calculer les différents coefficients K
     * 
     * @param U1
     *            Ensemble de U1
     * @return Liste Ki
     * @throws Exception
     */
    private List<Map<Integer, NodeObject>> computeKi( Map<Integer, NodeObject> U1 ) throws Exception {
        List<Map<Integer, NodeObject>> result = new ArrayList<Map<Integer, NodeObject>>();

        NodeObject TpH = new BinaryOperator( BinaryOperatorEnum.DIVI.getValue(), new Variable( "H" ),
                new Number( "2" ) );
        TpH = new BinaryOperator( BinaryOperatorEnum.PLUS.getValue(), new Variable( "t" ), TpH.clone() );

        Map<Integer, NodeObject> K1 = this.computeKx( U1, this.unknownFunction );
        result.add( K1 );
        Map<Integer, NodeObject> U2 = this.computeUx( U1, K1,
                new BinaryOperator( BinaryOperatorEnum.DIVI.getValue(), new Variable( "H" ), new Number( "2" ) ) );
        Map<Integer, NodeObject> K2 = this.computeKx( U2, this.unknownFunction );
        result.add( K2 );
        Map<Integer, NodeObject> U3 = this.computeUx( U1, K2,
                new BinaryOperator( BinaryOperatorEnum.DIVI.getValue(), new Variable( "H" ), new Number( "2" ) ) );
        Map<Integer, NodeObject> K3 = this.computeKx( U3, this.unknownFunction );
        result.add( K3 );
        Map<Integer, NodeObject> U4 = this.computeUx( U1, K3, new Variable( "H" ) );
        Map<Integer, NodeObject> K4 = this.computeKx( U4, this.unknownFunction );
        result.add( K4 );
        return result;
    }

    /**
     * Permet de remplacer les fonctions & variables par leur valeur
     * 
     * @param n
     *            Valeur de t
     * @param v
     *            Vecteur
     * @return Vecteur résultant
     * @throws Exception
     */
    private Map<Integer, NodeObject> replaceAndSimplify( Double n, Map<String, Double> values,
            Map<Integer, NodeObject> v,
            Map<Integer, NodeObject> U1 ) throws Exception {
        Map<Integer, NodeObject> result = new HashMap<Integer, NodeObject>( this.order );
        Double stepValue = this.diffEqSyst.getStep();
        for ( int i = 1; i <= this.order; i++ ) {
            NodeObject temp = v.get( i ).replaceVar( "H", new Number( stepValue.toString() ) );
            temp = temp.replaceVar( "U1", U1.get( i ) );
            temp = this.replaceFunctionByValue( temp, values );
            temp = temp.replaceVar( "t", new Number( new Double( n - stepValue ).toString() ) ); // tn
            while ( temp.canBeSimplified() )
                temp = temp.simplify();
            result.put( i, temp );
        }
        return result;
    }

    /**
     * Permet de remplacer une fonction par sa valeur (pour les forces)
     * 
     * @param eq
     *            Equation
     * @param n
     *            Valeur de T
     * @return Equation résultante
     * @throws Exception
     */
    private NodeObject replaceFunctionByValue( NodeObject eq, Map<String, Double> values ) throws Exception {
        NodeObject result = eq.clone();// eq is cloned in order not to alter it

        // walk through the function map
        for ( Entry<String, Double> entry : values.entrySet() ) {
            // replace the function by its value
            result = result.replaceFunctionByVar( entry.getKey(), entry.getValue() );
        }
        return result;
    }

    /**
     * Permet de calculer l'approximation de Y à la valeur t = n La méthode
     * d'approximation utilisée est Runge-Kutta de degré 4
     * 
     * @param n
     *            indice
     * @return Approximation de Y
     * @throws Exception
     */
    /*
     * public Double computeRK4( Double n ) throws Exception { if ( n <
     * this.diffEqSyst.getAbscissa() ) throw new Exception( "n < initials" );
     * Map<Integer, NodeObject> U1 = this.initU1(); for ( Double i =
     * this.diffEqSyst.getInitial(); i < n; i += this.diffEqSyst.getStep() ) {
     * U1 = this.replaceAndSimplify( i, this.computeUNP1( this.computeKi( U1 )
     * ), U1 ); } Double res = Double.parseDouble( U1.get( 1 ).toString() );
     * return res; }
     */

    /**
     * Calcul Un+1 avec la méthode d'Euler
     * 
     * @param v
     *            Condition initial à t = n
     * @return Vecteur résultat
     */
    private Map<Integer, NodeObject> computeUNP1Euler( Map<Integer, NodeObject> v ) {
        Map<Integer, NodeObject> result = new HashMap<Integer, NodeObject>();
        String unknown = this.getUnknownFunction();
        NodeObject U1 = new Variable( "U1" );
        NodeObject H = new Variable( "H" );
        for ( int i = 1; i <= this.getOrder(); i++ ) {
            NodeObject temp = this.syst.get( i ).clone();
            temp = temp.replaceFunctionByVector( unknown, v );
            NodeObject temp1 = new BinaryOperator( BinaryOperatorEnum.STAR.getValue(), H, temp );
            result.put( i, new BinaryOperator( BinaryOperatorEnum.PLUS.getValue(), U1, temp1 ) );
        }
        return result;
    }

    /**
     * Permet de calculer l'approximation de y à la valeur t=n La méthode
     * d'approximation utilisée est Euler
     * 
     * @param n
     *            indice
     * @return Approximation de Y
     * @throws Exception
     */
    /*
     * public Double computeEuler( Double n ) throws Exception { if ( n <
     * this.diffEqSyst.getAbscissa() ) throw new Exception( "n < initials" );
     * Map<Integer, NodeObject> U1 = this.initU1(); for ( Double i =
     * this.diffEqSyst.getInitial(); i < n; i += this.diffEqSyst.getStep() ) {
     * U1 = this.replaceAndSimplify( i, this.computeUNP1Euler( U1 ), U1 ); }
     * Double res = Double.parseDouble( U1.get( 1 ).toString() ); return res; }
     */

    /**
     * Permet de comparer une valeur à une valeur approximative d'une équation
     * différentielle avec RK4
     * 
     * @param n
     *            Nombre d'itération (valeur de t)
     * @param value
     *            Valeur à comparer
     * @param delta
     *            Delta admis entre la valeur et la valeur approx.
     * @return True si la valeur approx. est égale (delta compris), False sinon
     * @throws Exception
     *             this.compute(n)
     */
    /*
     * public boolean compareRK4( Double n, Double value, Double delta ) throws
     * Exception { if ( Math.abs( this.computeRK4( n ) - value ) < delta )
     * return true; else return false; }
     */

    /**
     * Permet de comparer une valeur à une valeur approximative d'une équation
     * différentielle avec Euler
     * 
     * @param n
     *            Nombre d'itération (valeur de t)
     * @param value
     *            Valeur à comparer
     * @param delta
     *            Delta admis entre la valeur et la valeur approx.
     * @return True si la valeur approx. est égale (delta compris), False sinon
     * @throws Exception
     *             this.compute(n)
     */
    /*
     * public boolean compareEuler( Double n, Double value, Double delta )
     * throws Exception { if ( Math.abs( this.computeEuler( n ) - value ) <
     * delta ) return true; else return false; }
     */

    /**
     * Permet de comparer une TimeSeries à une équation différentielle avec RK4
     * 
     * @param ts
     *            TimeSeries
     * @param delta
     *            Max des carrés
     * @return False si différent, true si identique
     * @throws Exception
     */
    /*
     * public boolean compareRK4( TimeSeriesDoubleDouble ts, Double delta )
     * throws Exception { // get series step and t0 Double step =
     * this.getDiffEqSyst().getStep(); Double start =
     * this.diffEqSyst.getAbscissa();
     * 
     * Map<Integer, NodeObject> U1 = this.initU1();
     * 
     * for ( Double key : ts.keySet() ) { U1 = this.replaceAndSimplify( start,
     * this.computeUNP1( this.computeKi( U1 ) ), U1 ); Double res =
     * Double.parseDouble( U1.get( 1 ).toString() ); Double diff = Math.pow(
     * ts.get( key ) - res, 2 );
     * 
     * if ( diff > delta ) return false;
     * 
     * start += step; } return true; }
     */

    /**
     * Permet de comparer une TimeSeries à une équation différentielle avec
     * Euler
     * 
     * @param ts
     *            TimeSeries
     * @param delta
     *            Max des carrés
     * @return False si différent, true si identique
     * @throws Exception
     */
    /*
     * public boolean compareEuler( TimeSeriesDoubleDouble ts, Double delta )
     * throws Exception { int itemCount = ts.size(); Double step =
     * this.getDiffEqSyst().getStep(); Double start =
     * this.diffEqSyst.getAbscissa();
     * 
     * Map<Integer, NodeObject> U1 = this.initU1();
     * 
     * for ( Double key : ts.keySet() ) { U1 = this.replaceAndSimplify( start,
     * this.computeUNP1Euler( U1 ), U1 ); Double res = Double.parseDouble(
     * U1.get( 1 ).toString() ); Double diff = Math.pow( ts.get( key ) - res, 2
     * );
     * 
     * if ( diff > delta ) return false;
     * 
     * start += step; } return true; }
     */

    /**
     * Permet de créer une TS avec RK4
     * 
     * @param t
     *            Instant t max
     * @return TimeSeries créée
     * @throws Exception
     */
    /*
     * public TimeSeriesDoubleDouble equationToTimeseriesRK4( Double t ) throws
     * Exception { TimeSeriesDoubleDouble result = new TimeSeriesDoubleDouble();
     * Double step = this.getDiffEqSyst().getStep(); Logger.getLogger(
     * getClass() ).info( "step: " + step ); Double start =
     * this.diffEqSyst.getAbscissa(); Logger.getLogger( getClass() ).info(
     * "start: " + start );
     * 
     * Map<Integer, NodeObject> U1 = this.initU1();
     * 
     * for ( Double i = start; i < t; i += step ) { // Logger.getLogger(
     * getClass() ).info( "i: " + i ); U1 = this.replaceAndSimplify( i,
     * this.computeUNP1( this.computeKi( U1 ) ), U1 ); Double value =
     * Double.parseDouble( U1.get( 1 ).toString() ); result.put( i, value ); }
     * return result; }
     */

    /**
     * Permet de créer une TS avec RK4
     * 
     * @param Nombre
     *            d'éléments
     * @return TimeSeries créée
     * @throws Exception
     */
    public TimeSeriesDoubleDouble equationToTimeseriesRK4( int n ) throws Exception {
        TimeSeriesDoubleDouble result = new TimeSeriesDoubleDouble();
        // Double step = this.getDiffEqSyst().getStep();
        // Double start = this.diffEqSyst.getAbscissa();

        // get the initial time t_0 from the input time series
        Iterator<String> inputKeysIterator = inputFunctions.keySet().iterator();
        String firstKey = inputKeysIterator.next();
        Double currentTime = inputFunctions.get( firstKey ).firstKey();

        // if there is not enough (less than n) input values, the series cannot
        // generate n values
        if ( n > inputFunctions.get( firstKey ).size() )
            throw new Exception( "Won't be able to generate " + n + " values." );

        // uses initial conditions to initialize
        // Y_0=[y_0^(0),y_0^(1),...,y_0^(p-1)]^T, where p is the order of the
        // equation. For j in [1,..,p-1], y_0^(j) = y(j)^(t_0).
        Map<Integer, NodeObject> U1 = this.initU1();

        // the first values of the numerical solution of the equation is
        // y_0^(0).
        result.put( currentTime, this.getDiffEqSyst().getAbscissa() );

        // then we use the recurrence relation to calculate y_i, 1 < i < n
        for ( int i = 1; i < n; i++ ) {
            HashMap<String, Double> values = new HashMap<String, Double>();
            for ( String key : inputFunctions.keySet() ) {
                values.put( key, inputFunctions.get( key ).get( currentTime ) );
            }
            U1 = this.replaceAndSimplify( currentTime, values, this.computeUNP1( this.computeKi( U1 ) ), U1 );
            Double value = Double.parseDouble( U1.get( 1 ).toString() );
            currentTime = inputFunctions.get( firstKey ).higherKey( currentTime );
            result.put( currentTime, value );
        }
        return result;
    }

    /**
     * Permet de créer une TS avec RK4
     * 
     * @param Nombre
     *            d'éléments
     * @return TimeSeries créée
     * @throws Exception
     */
    public TimeSeriesDoubleDouble equationToTimeseriesRK4() throws Exception {
        TimeSeriesDoubleDouble result = new TimeSeriesDoubleDouble();
        // Double step = this.getDiffEqSyst().getStep();
        // Double start = this.diffEqSyst.getAbscissa();

        Iterator<String> inputKeysIterator = inputFunctions.keySet().iterator();
        String firstKey = inputKeysIterator.next();
        Double currentTime = inputFunctions.get( firstKey ).firstKey();
        int n = inputFunctions.get( firstKey ).size();

        Map<Integer, NodeObject> U1 = this.initU1();

        result.put( currentTime, this.getDiffEqSyst().getAbscissa() );

        for ( int i = 1; i < n; i++ ) {
            HashMap<String, Double> values = new HashMap<String, Double>();
            for ( String key : inputFunctions.keySet() ) {
                values.put( key, inputFunctions.get( key ).get( currentTime ) );
            }
            U1 = this.replaceAndSimplify( currentTime, values, this.computeUNP1( this.computeKi( U1 ) ), U1 );
            Double value = Double.parseDouble( U1.get( 1 ).toString() );
            currentTime = inputFunctions.get( firstKey ).higherKey( currentTime );
            result.put( currentTime, value );
        }
        return result;
    }

    /**
     * Permet de créer une TS avec Euler
     * 
     * @param t
     *            Instant t limite
     * @return TimeSeries créée
     * @throws Exception
     */
    /*
     * public TimeSeriesDoubleDouble equationToTimeseriesEuler( Double t )
     * throws Exception { TimeSeriesDoubleDouble result = new
     * TimeSeriesDoubleDouble(); Double step = this.getDiffEqSyst().getStep();
     * Double start = this.diffEqSyst.getAbscissa();
     * 
     * Map<Integer, NodeObject> U1 = this.initU1();
     * 
     * for ( Double i = start; i < t; i += step ) { U1 =
     * this.replaceAndSimplify( i, this.computeUNP1Euler( U1 ), U1 ); Double
     * value = Double.parseDouble( U1.get( 1 ).toString() ); result.put( i,
     * value ); } return result; }
     */

    /**
     * Permet de créer une TS avec Euler
     * 
     * @param n
     *            nombre d'éléments
     * @return TimeSeries créée
     * @throws Exception
     */
    /*
     * public TimeSeriesDoubleDouble equationToTimeseriesEuler( int n ) throws
     * Exception { TimeSeriesDoubleDouble result = new TimeSeriesDoubleDouble();
     * Double step = this.getDiffEqSyst().getStep(); Double start =
     * this.diffEqSyst.getAbscissa(); Double currentTime = start;
     * 
     * Map<Integer, NodeObject> U1 = this.initU1();
     * 
     * for ( int i = 0; i < n; i++ ) { U1 = this.replaceAndSimplify(
     * currentTime, this.computeUNP1Euler( U1 ), U1 ); Double value =
     * Double.parseDouble( U1.get( 1 ).toString() ); result.put( currentTime,
     * value );
     * 
     * currentTime += step; } return result; }
     */

}
