package fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.numericalmethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.DifferentialEquation2;
import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.FunctionKey;
import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.ParametersSet;
import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.tree.BinaryOperator;
import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.tree.BinaryOperatorEnum;
import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.tree.Function;
import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.tree.NodeObject;
import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.tree.Number;
import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.tree.UnaryOperator;
import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.tree.UnaryOperatorEnum;
import fr.ensma.lias.timeseriesreductorslib.timeseries.TimeSeriesDoubleDouble;

public class NumericalMethod2 {
    private ParametersSet                            parametersSet;
    private DifferentialEquation2                    differentialEquation;
    private int                                      order;
    private Map<FunctionKey, TimeSeriesDoubleDouble> inputFunctionsValues;
    private String                                   unkownFunctionName;
    /**
     * DY = DY/dt = D([y^(0),...,y^(order-1)]^T)/dt
     */
    private List<NodeObject>                         DY;

    /**
     * Constructor from a differential equation
     * 
     * @param eq
     *            Equation différentielle à transformer
     * @throws Exception
     */
    public NumericalMethod2( DifferentialEquation2 eq ) throws Exception {
        super();
        parametersSet = eq.getParametersSet();
        order = eq.getOrder();
        unkownFunctionName = eq.getUnknownFunction();
        inputFunctionsValues = eq.getInputFunctions();
        differentialEquation = eq;
        // builds the vector DY = d(Y)/dt = d([y^(0),...,y^(order-1)]^T)/dt,
        // where the last element is equal to y^(order).
        DY = new ArrayList<NodeObject>();
        // the higher order function of the equation is isolated on the left
        // part of the equation, so we have an equation in this form: y^(order)
        // = F(t,y^(0),...,y^(order-1)) = f(t,Y)
        NodeObject yForm = eq.transformEquationToYForm( eq.getOrder() )
                .transformEquationWithSystem( eq.getParametersSet() );
        String unknown = eq.getUnknownFunction();
        for ( int i = 1; i <= order; i++ ) {
            if ( i < eq.getOrder() ) // each i-th component of the vector is
                                     // equal to y^(i)
                DY.add( new Function( unkownFunctionName, i ) );
            else // the last component y^(order) is replaced the expression of
                 // f(t,Y)
                DY.add( yForm.getRightPart() );
        }

        // replaces the variables by their values
        for ( String variable : parametersSet.getMapping().keySet() ) {
            for ( int j = 0; j < DY.size(); j++ ) {
                DY.set( j, DY.get( j ).replaceVar( variable,
                        new Number( String.valueOf( parametersSet.getMapping().get( variable ) ) ) ) );
            }
        }

        for ( int j = 0; j < DY.size(); j++ ) {
            while ( DY.get( j ).canBeSimplified() )
                DY.set( j, DY.get( j ).simplify() );
        }
    }

    public ParametersSet getParametersSet() {
        return parametersSet;
    }

    public DifferentialEquation2 getDifferentialEquation() {
        return differentialEquation;
    }

    public int getOrder() {
        return order;
    }

    public Map<FunctionKey, TimeSeriesDoubleDouble> getInputFunctionsValues() {
        return inputFunctionsValues;
    }

    public String getUnkownFunctionName() {
        return unkownFunctionName;
    }

    public List<NodeObject> getDY() {
        return DY;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( differentialEquation == null ) ? 0 : differentialEquation.hashCode() );
        result = prime * result + ( ( inputFunctionsValues == null ) ? 0 : inputFunctionsValues.hashCode() );
        result = prime * result + order;
        result = prime * result + ( ( parametersSet == null ) ? 0 : parametersSet.hashCode() );
        result = prime * result + ( ( unkownFunctionName == null ) ? 0 : unkownFunctionName.hashCode() );
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
        NumericalMethod2 other = (NumericalMethod2) obj;
        if ( differentialEquation == null ) {
            if ( other.differentialEquation != null )
                return false;
        } else if ( !differentialEquation.equals( other.differentialEquation ) )
            return false;
        if ( inputFunctionsValues == null ) {
            if ( other.inputFunctionsValues != null )
                return false;
        } else if ( !inputFunctionsValues.equals( other.inputFunctionsValues ) )
            return false;
        if ( order != other.order )
            return false;
        if ( parametersSet == null ) {
            if ( other.parametersSet != null )
                return false;
        } else if ( !parametersSet.equals( other.parametersSet ) )
            return false;
        if ( unkownFunctionName == null ) {
            if ( other.unkownFunctionName != null )
                return false;
        } else if ( !unkownFunctionName.equals( other.unkownFunctionName ) )
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "NumericalMethod2 [parametersSet=" + parametersSet + ", differentialEquation=" + differentialEquation
                + ", order=" + order + ", inputFunctionsValues=" + inputFunctionsValues + ", unkownFunctionName="
                + unkownFunctionName + ", DY=" + DY + "]";
    }

    /**
     * uses initial conditions to initialize Y_0=[y_0^(0),y_0^(1),...,y_0^(p-1)]^T, where p is the
     * order of the equation. For j in [1,..,p-1], y_0^(j) = y(j)^(t_0).
     * 
     * @param system
     *            Système correspondant
     * @return Map contenant la condition initiale de l'équation et de ces composantes
     */
    private List<Double> initU1( Map<FunctionKey, Double> initialValues, String unknownFunctionName )
            throws Exception {
        // if the order of the equation is not equal to the number of initial
        // values, the equation cannot be solved
        if ( initialValues.size() != order )
            throw new Exception( "Initial values number incoherent with equation order." );

        List<Double> result = new ArrayList<Double>();

        // builds the y_0 vector
        for ( int i = 0; i < this.order; i++ ) {
            // creates the key representing y^(i) (the i-th derivative of y)
            // which value at t_0 is y_0^(i)
            FunctionKey key = new FunctionKey( unknownFunctionName, i );
            // if the key appears in the initial values list, it is added with
            // its value in the vector, else the equation cannot be solved with
            // a missing initial condition value.
            if ( initialValues.containsKey( key ) )
                result.add( initialValues.get( key ) );
            else
                throw new Exception( "Looking for the initial value of " + key + " but it seems to be missing." );
        }
        return result;
    }

    /**
     * Permet de remplacer une fonction et ses dérivées par les composantes d'un vecteur
     * 
     * @param v
     *            Vecteur
     * @param funcname
     *            Nom de la fonction
     * @return Nouveau vecteur calculé
     * @throws Exception
     *//*
       * private Map<Integer, NodeObject> computeKx( Map<Integer, NodeObject> v, String funcname )
       * throws Exception { Map<Integer, NodeObject> result = new HashMap<Integer, NodeObject>(
       * this.syst ); for ( int i = 1; i <= this.order; i++ ) { result.put( i, result.get( i
       * ).replaceFunctionByVector( funcname, v ) ); } return result; }
       */

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
     *//*
       * private Map<Integer, NodeObject> computeUx( Map<Integer, NodeObject> U1, Map<Integer,
       * NodeObject> Kx, NodeObject h ) { Map<Integer, NodeObject> result = new HashMap<Integer,
       * NodeObject>( this.syst ); for ( int i = 1; i <= this.order; i++ ) { NodeObject temp = new
       * BinaryOperator( BinaryOperatorEnum.STAR.getValue(), h.clone(), Kx.get( i ).clone() ); temp
       * = new BinaryOperator( BinaryOperatorEnum.PLUS.getValue(), U1.get( i ).clone(), temp.clone()
       * ); result.put( i, temp ); } return result; }
       */

    /**
     * Construit la formule de Un+1
     * 
     * @return Formule Un+1
     *//*
       * private NodeObject initUNP1() { NodeObject K3 = new BinaryOperator(
       * BinaryOperatorEnum.STAR.getValue(), new Number( "2" ), new Variable( "K3" ) ); NodeObject
       * K2 = new BinaryOperator( BinaryOperatorEnum.STAR.getValue(), new Number( "2" ), new
       * Variable( "K2" ) ); NodeObject K3K4 = new BinaryOperator(
       * BinaryOperatorEnum.PLUS.getValue(), K3, new Variable( "K4" ) ); NodeObject K1K2 = new
       * BinaryOperator( BinaryOperatorEnum.PLUS.getValue(), new Variable( "K1" ), K2 );
       * 
       * NodeObject K1K2K3K4 = new BinaryOperator( BinaryOperatorEnum.PLUS.getValue(), K1K2, K3K4 );
       * 
       * NodeObject h6 = new BinaryOperator( BinaryOperatorEnum.DIVI.getValue(), new Variable( "H"
       * ), new Number( "6" ) ); h6 = new BinaryOperator( BinaryOperatorEnum.STAR.getValue(),
       * h6.clone(), K1K2K3K4 );
       * 
       * return new BinaryOperator( BinaryOperatorEnum.PLUS.getValue(), new Variable( "U1" ), h6 );
       * }
       */

    /**
     * Remplace les Ki de la formule Un+1 par ceux donnés en paramètre
     * 
     * @param ki
     *            Liste des K
     * @return Map contenant, pour chaque composante, l'équation Un+1
     * @throws Exception
     *//*
       * private Map<Integer, NodeObject> computeUNP1( List<Map<Integer, NodeObject>> ki ) throws
       * Exception { if ( ki.size() > 4 ) throw new Exception( "Ki > 4" ); Map<Integer, NodeObject>
       * result = new HashMap<Integer, NodeObject>( this.order ); NodeObject Unp1 = this.initUNP1();
       * for ( int i = 1; i <= this.order; i++ ) { NodeObject temp = Unp1.replaceVar( "K1", ki.get(
       * 0 ).get( i ) ); temp = temp.replaceVar( "K2", ki.get( 1 ).get( i ) ); temp =
       * temp.replaceVar( "K3", ki.get( 2 ).get( i ) ); temp = temp.replaceVar( "K4", ki.get( 3
       * ).get( i ) ); result.put( i, temp ); } return result; }
       */

    /**
     * Permet de calculer les différents coefficients K
     * 
     * @param U1
     *            Ensemble de U1
     * @return Liste Ki
     * @throws Exception
     *//*
       * private List<Map<Integer, NodeObject>> computeKi( Map<Integer, NodeObject> U1 ) throws
       * Exception { List<Map<Integer, NodeObject>> result = new ArrayList<Map<Integer,
       * NodeObject>>();
       * 
       * NodeObject TpH = new BinaryOperator( BinaryOperatorEnum.DIVI.getValue(), new Variable( "H"
       * ), new Number( "2" ) ); TpH = new BinaryOperator( BinaryOperatorEnum.PLUS.getValue(), new
       * Variable( "t" ), TpH.clone() );
       * 
       * Map<Integer, NodeObject> K1 = this.computeKx( U1, this.unknownFunction ); result.add( K1 );
       * Map<Integer, NodeObject> U2 = this.computeUx( U1, K1, new BinaryOperator(
       * BinaryOperatorEnum.DIVI.getValue(), new Variable( "H" ), new Number( "2" ) ) );
       * Map<Integer, NodeObject> K2 = this.computeKx( U2, this.unknownFunction ); result.add( K2 );
       * Map<Integer, NodeObject> U3 = this.computeUx( U1, K2, new BinaryOperator(
       * BinaryOperatorEnum.DIVI.getValue(), new Variable( "H" ), new Number( "2" ) ) );
       * Map<Integer, NodeObject> K3 = this.computeKx( U3, this.unknownFunction ); result.add( K3 );
       * Map<Integer, NodeObject> U4 = this.computeUx( U1, K3, new Variable( "H" ) ); Map<Integer,
       * NodeObject> K4 = this.computeKx( U4, this.unknownFunction ); result.add( K4 ); return
       * result; }
       */

    /**
     * Permet de remplacer les fonctions & variables par leur valeur
     * 
     * @param n
     *            Valeur de t
     * @param v
     *            Vecteur
     * @return Vecteur résultant
     * @throws Exception
     *//*
       * private Map<Integer, NodeObject> replaceAndSimplify( Double n, Map<String, Double> values,
       * Map<Integer, NodeObject> v, Map<Integer, NodeObject> U1 ) throws Exception { Map<Integer,
       * NodeObject> result = new HashMap<Integer, NodeObject>( this.order ); Double stepValue =
       * this.diffEqSyst.getStep(); for ( int i = 1; i <= this.order; i++ ) { NodeObject temp =
       * v.get( i ).replaceVar( "H", new Number( stepValue.toString() ) ); temp = temp.replaceVar(
       * "U1", U1.get( i ) ); temp = this.replaceFunctionByValue( temp, values ); temp =
       * temp.replaceVar( "t", new Number( new Double( n - stepValue ).toString() ) ); // tn while (
       * temp.canBeSimplified() ) temp = temp.simplify(); result.put( i, temp ); } return result; }
       */

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
     * Permet de calculer l'approximation de Y à la valeur t = n La méthode d'approximation utilisée
     * est Runge-Kutta de degré 4
     * 
     * @param n
     *            indice
     * @return Approximation de Y
     * @throws Exception
     */
    /*
     * public Double computeRK4( Double n ) throws Exception { if ( n <
     * this.diffEqSyst.getAbscissa() ) throw new Exception( "n < initials" ); Map<Integer,
     * NodeObject> U1 = this.initU1(); for ( Double i = this.diffEqSyst.getInitial(); i < n; i +=
     * this.diffEqSyst.getStep() ) { U1 = this.replaceAndSimplify( i, this.computeUNP1(
     * this.computeKi( U1 ) ), U1 ); } Double res = Double.parseDouble( U1.get( 1 ).toString() );
     * return res; }
     */

    /**
     * Calcul Un+1 avec la méthode d'Euler
     * 
     * @param v
     *            Condition initial à t = n
     * @return Vecteur résultat
     *//*
       * private Map<Integer, NodeObject> computeUNP1Euler( Map<Integer, NodeObject> v ) {
       * Map<Integer, NodeObject> result = new HashMap<Integer, NodeObject>(); String unknown =
       * this.getUnknownFunction(); NodeObject U1 = new Variable( "U1" ); NodeObject H = new
       * Variable( "H" ); for ( int i = 1; i <= this.getOrder(); i++ ) { NodeObject temp =
       * this.syst.get( i ).clone(); temp = temp.replaceFunctionByVector( unknown, v ); NodeObject
       * temp1 = new BinaryOperator( BinaryOperatorEnum.STAR.getValue(), H, temp ); result.put( i,
       * new BinaryOperator( BinaryOperatorEnum.PLUS.getValue(), U1, temp1 ) ); } return result; }
       */

    /**
     * Permet de calculer l'approximation de y à la valeur t=n La méthode d'approximation utilisée
     * est Euler
     * 
     * @param n
     *            indice
     * @return Approximation de Y
     * @throws Exception
     */
    /*
     * public Double computeEuler( Double n ) throws Exception { if ( n <
     * this.diffEqSyst.getAbscissa() ) throw new Exception( "n < initials" ); Map<Integer,
     * NodeObject> U1 = this.initU1(); for ( Double i = this.diffEqSyst.getInitial(); i < n; i +=
     * this.diffEqSyst.getStep() ) { U1 = this.replaceAndSimplify( i, this.computeUNP1Euler( U1 ),
     * U1 ); } Double res = Double.parseDouble( U1.get( 1 ).toString() ); return res; }
     */

    /**
     * Permet de comparer une valeur à une valeur approximative d'une équation différentielle avec
     * RK4
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
     * public boolean compareRK4( Double n, Double value, Double delta ) throws Exception { if (
     * Math.abs( this.computeRK4( n ) - value ) < delta ) return true; else return false; }
     */

    /**
     * Permet de comparer une valeur à une valeur approximative d'une équation différentielle avec
     * Euler
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
     * public boolean compareEuler( Double n, Double value, Double delta ) throws Exception { if (
     * Math.abs( this.computeEuler( n ) - value ) < delta ) return true; else return false; }
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
     * public boolean compareRK4( TimeSeriesDoubleDouble ts, Double delta ) throws Exception { //
     * get series step and t0 Double step = this.getDiffEqSyst().getStep(); Double start =
     * this.diffEqSyst.getAbscissa();
     * 
     * Map<Integer, NodeObject> U1 = this.initU1();
     * 
     * for ( Double key : ts.keySet() ) { U1 = this.replaceAndSimplify( start, this.computeUNP1(
     * this.computeKi( U1 ) ), U1 ); Double res = Double.parseDouble( U1.get( 1 ).toString() );
     * Double diff = Math.pow( ts.get( key ) - res, 2 );
     * 
     * if ( diff > delta ) return false;
     * 
     * start += step; } return true; }
     */

    /**
     * Permet de comparer une TimeSeries à une équation différentielle avec Euler
     * 
     * @param ts
     *            TimeSeries
     * @param delta
     *            Max des carrés
     * @return False si différent, true si identique
     * @throws Exception
     */
    /*
     * public boolean compareEuler( TimeSeriesDoubleDouble ts, Double delta ) throws Exception { int
     * itemCount = ts.size(); Double step = this.getDiffEqSyst().getStep(); Double start =
     * this.diffEqSyst.getAbscissa();
     * 
     * Map<Integer, NodeObject> U1 = this.initU1();
     * 
     * for ( Double key : ts.keySet() ) { U1 = this.replaceAndSimplify( start,
     * this.computeUNP1Euler( U1 ), U1 ); Double res = Double.parseDouble( U1.get( 1 ).toString() );
     * Double diff = Math.pow( ts.get( key ) - res, 2 );
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
     * public TimeSeriesDoubleDouble equationToTimeseriesRK4( Double t ) throws Exception {
     * TimeSeriesDoubleDouble result = new TimeSeriesDoubleDouble(); Double step =
     * this.getDiffEqSyst().getStep(); Logger.getLogger( getClass() ).info( "step: " + step );
     * Double start = this.diffEqSyst.getAbscissa(); Logger.getLogger( getClass() ).info( "start: "
     * + start );
     * 
     * Map<Integer, NodeObject> U1 = this.initU1();
     * 
     * for ( Double i = start; i < t; i += step ) { // Logger.getLogger( getClass() ).info( "i: " +
     * i ); U1 = this.replaceAndSimplify( i, this.computeUNP1( this.computeKi( U1 ) ), U1 ); Double
     * value = Double.parseDouble( U1.get( 1 ).toString() ); result.put( i, value ); } return
     * result; }
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

        // gets the initial time t_0 from the input time series
        Iterator<FunctionKey> inputKeysIterator = inputFunctionsValues.keySet().iterator();
        FunctionKey firstKey = inputKeysIterator.next();
        Double currentTime = inputFunctionsValues.get( firstKey ).firstKey();

        // if there is not enough (less than n) input values, the series cannot
        // generate n values
        if ( n > inputFunctionsValues.get( firstKey ).size() )
            throw new Exception( "Won't be able to generate " + n + " values." );

        // uses initial conditions to initialize
        // Y_0=[y_0^(0),y_0^(1),...,y_0^(p-1)]^T, where p is the order of the
        // equation. For j in [1,..,p-1], y_0^(j) = y(j)^(t_0).
        List<Double> Y_n = this.initU1( parametersSet.getInitialValues(), unkownFunctionName );

        // the first values of the numerical solution of the equation is
        // y_0^(0).
        result.put( currentTime, parametersSet.getInitialValues().get( new FunctionKey( unkownFunctionName, 0 ) ) );

        // then we use the recurrence relation to calculate the y_i's values,
        // 1<i<n
        for ( int i = 1; i < n; i++ ) {
            Map<FunctionKey, Double> values = new HashMap<FunctionKey, Double>();
            // extracts from the input functions values, the values of each
            // input functions for the current time and copies them into the
            // values map
            for ( FunctionKey key : inputFunctionsValues.keySet() ) {
                values.put( key, inputFunctionsValues.get( key ).get( currentTime ) );
            }
            Y_n = computeYNP1( Y_n, currentTime, inputFunctionsValues.get( firstKey ).higherKey( currentTime ) );
            Double value = Y_n.get( 0 );
            currentTime = inputFunctionsValues.get( firstKey ).higherKey( currentTime );
            result.put( currentTime, value );
        }
        return result;
    }

    /**
     * 
     * Generates a time series from an equation. It generates as many values as available in the
     * inputs functions.
     * 
     * @param function,
     *            the approximation function to use
     * @return
     * @throws Exception
     */
    public TimeSeriesDoubleDouble equationToTimeseriesRK4() throws Exception {
        TimeSeriesDoubleDouble result = new TimeSeriesDoubleDouble();
        // Double step = this.getDiffEqSyst().getStep();
        // Double start = this.diffEqSyst.getAbscissa();

        // gets the initial time t_0 from the input time series
        FunctionKey firstKey = null;
        Double currentTime = 0.0d;

        // uses initial conditions to initialize
        // Y_0=[y_0^(0),y_0^(1),...,y_0^(p-1)]^T, where p is the order of the
        // equation. For j in [1,..,p-1], y_0^(j) = y(j)^(t_0).
        List<Double> Y_n = this.initU1( parametersSet.getInitialValues(), unkownFunctionName );

        if ( !inputFunctionsValues.isEmpty() ) {
            Iterator<FunctionKey> inputKeysIterator = inputFunctionsValues.keySet().iterator();
            firstKey = inputKeysIterator.next();
            currentTime = inputFunctionsValues.get( firstKey ).firstKey();

            // the first values of the numerical solution of the equation is
            // y_0^(0).
            result.put( currentTime, parametersSet.getInitialValues().get( new FunctionKey( unkownFunctionName, 0 ) ) );

            // then we use the recurrence relation to calculate the y_i's values,
            // 1<i<n
            while ( inputFunctionsValues.get( firstKey ).higherKey( currentTime ) != null ) {
                Y_n = computeYNP1( Y_n, currentTime, inputFunctionsValues.get( firstKey ).higherKey( currentTime ) );
                Double value = Y_n.get( 0 );
                currentTime = inputFunctionsValues.get( firstKey ).higherKey( currentTime );
                result.put( currentTime, value );
            }
        } else {
            // the first values of the numerical solution of the equation is
            // y_0^(0).
            result.put( currentTime, parametersSet.getInitialValues().get( new FunctionKey( unkownFunctionName, 0 ) ) );

            double nextTime;

            // then we use the recurrence relation to calculate the y_i's values,
            // 1<i<n
            for ( int i = 1; i < 5000; i++ ) {
                nextTime = currentTime + 1.0;
                nextTime = (double) Math.round( nextTime );
                Y_n = computeYNP1( Y_n, currentTime, nextTime );
                Double value = Y_n.get( 0 );
                currentTime = nextTime;
                result.put( currentTime, value );
            }
        }

        return result;
    }

    /**
     * 
     * Computes Y_(n+1) = Y_n + (k1 + 2*k2 + 2*k3 + k4) / 6, using the RK4 method formulas
     * 
     * @param Y,
     *            Y_n
     * @param t,
     *            the current time t_n
     * @param tph
     *            = t_(n+1)
     * @return
     */
    private List<Double> computeYNP1( List<Double> Y, Double t1, Double t2 ) {
        // YNP1 is initialized to 0
        List<Double> YNP1 = new ArrayList<Double>();

        // vector and time to make computations with
        List<Double> vector = new ArrayList<Double>();
        double time;
        double h = t2 - t1;

        // computes the Ki values
        List<Double> K1 = new ArrayList<Double>();
        List<Double> K2 = new ArrayList<Double>();
        List<Double> K3 = new ArrayList<Double>();
        List<Double> K4 = new ArrayList<Double>();
        List<Double> F = new ArrayList<Double>();

        // K1
        time = t1;
        for ( int i = 0; i < Y.size(); i++ ) {
            vector.add( Y.get( i ) );
        }
        F = computeF( vector, t1, t2, time );
        for ( int i = 0; i < F.size(); i++ ) {
            K1.add( F.get( i ) * h );
        }

        // K2
        time = t1 + 0.5d * h;
        for ( int i = 0; i < K1.size(); i++ ) {
            vector.set( i, Y.get( i ) + 0.5d * K1.get( i ) );
        }
        F = computeF( vector, t1, t2, time );
        for ( int i = 0; i < F.size(); i++ ) {
            K2.add( F.get( i ) * h );
        }

        // K3
        for ( int i = 0; i < K1.size(); i++ ) {
            vector.set( i, Y.get( i ) + 0.5d * K2.get( i ) );
        }
        F = computeF( vector, t1, t2, time );
        for ( int i = 0; i < F.size(); i++ ) {
            K3.add( F.get( i ) * h );
        }

        // K4
        time = t1 + h;
        for ( int i = 0; i < K1.size(); i++ ) {
            vector.set( i, Y.get( i ) + K3.get( i ) );
        }
        F = computeF( vector, t1, t2, time );
        for ( int i = 0; i < F.size(); i++ ) {
            K4.add( F.get( i ) * h );
        }

        // YNP1 = Y + (K1 + 2*K2 + 2*K3 + K4)/6
        for ( int i = 0; i < Y.size(); i++ ) {
            YNP1.add( Y.get( i ) + ( K1.get( i ) + 2 * K2.get( i ) + 2 * K3.get( i ) + K4.get( i ) ) / 6.0d );
        }

        return YNP1;
    }

    /**
     * 
     * Computes the value of F(Y,t), where t1 and t2 are time keys of the input series that point
     * out a segment of the series that will be approximated by a straight line in order to be able
     * to evaluate the input function at any point t.
     * 
     * @param Y
     * @param t1
     * @param t2
     * @param t
     * @return
     */
    private List<Double> computeF( List<Double> Y, Double t1, Double t2, Double t ) {
        List<Double> F = new ArrayList<Double>();
        List<NodeObject> DYclone = new ArrayList<NodeObject>();
        // cloning DY to avoid altering it
        for ( int i = 0; i < DY.size(); i++ ) {
            DYclone.add( DY.get( i ).clone() );
        }

        // replace the value of the unknown by the values of Y
        for ( int i = 0; i < Y.size(); i++ ) {
            for ( int j = 0; j < DYclone.size(); j++ ) {
                DYclone.set( j, DYclone.get( j ).replaceFunctionByVar2( new FunctionKey( unkownFunctionName, i ),
                        Y.get( i ) ) );
            }
        }

        // replaces the input functions by their value at t, using a straight
        // line approximation
        for ( FunctionKey key : inputFunctionsValues.keySet() ) {
            double value = inputFunctionsValues.get( key ).getInterpolationFunction()
                    .interpolate( inputFunctionsValues.get( key ), t1, t2, t );
            for ( int j = 0; j < DYclone.size(); j++ ) {
                DYclone.set( j, DYclone.get( j ).replaceFunctionByVar2( key, value ) );
            }
        }

        // computes the value of F
        for ( int j = 0; j < DYclone.size(); j++ ) {
            F.add( reversePolishEvaluation( DYclone.get( j ) ) );
        }

        return F;
    }

    private double reversePolishEvaluation( NodeObject expression ) {
        // if the node is a number it returns its own value
        if ( expression instanceof Number )
            return ( (Number) expression ).getDoubleValue();
        // if the node is a binary operator, it evaluates both of its children,
        // do the operation and returns the value
        else if ( expression instanceof BinaryOperator ) {
            double left = reversePolishEvaluation( expression.getLeftPart() );
            double right = reversePolishEvaluation( expression.getRightPart() );
            try {
                return BinaryOperatorEnum.compute( left, right, expression.getValue() );
            } catch ( Exception e ) {
                e.printStackTrace();
            }
            // if it is a unary operator, it evaluate its child, do the
            // operation and returns the value
        } else if ( expression instanceof UnaryOperator ) {
            double right = reversePolishEvaluation( expression.getLeftPart() );
            try {
                return UnaryOperatorEnum.compute( right, expression.getValue() );
            } catch ( Exception e ) {
                e.printStackTrace();
            }
        }
        // in case something went wrong (a node were not a number or an
        // operator), it return an infinite value
        return Double.POSITIVE_INFINITY;
    }

    /**
     * Permet de créer une TS avec RK4
     * 
     * @param Nombre
     *            d'éléments
     * @return TimeSeries créée
     * @throws Exception
     *//*
       * public TimeSeriesDoubleDouble equationToTimeseriesRK4() throws Exception {
       * TimeSeriesDoubleDouble result = new TimeSeriesDoubleDouble(); // Double step =
       * this.getDiffEqSyst().getStep(); // Double start = this.diffEqSyst.getAbscissa();
       * 
       * Iterator<String> inputKeysIterator = inputFunctions.keySet().iterator(); String firstKey =
       * inputKeysIterator.next(); Double currentTime = inputFunctions.get( firstKey ).firstKey();
       * int n = inputFunctions.get( firstKey ).size();
       * 
       * Map<Integer, NodeObject> U1 = this.initU1();
       * 
       * result.put( currentTime, this.getDiffEqSyst().getAbscissa() );
       * 
       * for ( int i = 1; i < n; i++ ) { HashMap<String, Double> values = new HashMap<String,
       * Double>(); for ( String key : inputFunctions.keySet() ) { values.put( key,
       * inputFunctions.get( key ).get( currentTime ) ); } U1 = this.replaceAndSimplify(
       * currentTime, values, this.computeUNP1( this.computeKi( U1 ) ), U1 ); Double value =
       * Double.parseDouble( U1.get( 1 ).toString() ); currentTime = inputFunctions.get( firstKey
       * ).higherKey( currentTime ); result.put( currentTime, value ); } return result; }
       */

    /**
     * Permet de créer une TS avec Euler
     * 
     * @param t
     *            Instant t limite
     * @return TimeSeries créée
     * @throws Exception
     */
    /*
     * public TimeSeriesDoubleDouble equationToTimeseriesEuler( Double t ) throws Exception {
     * TimeSeriesDoubleDouble result = new TimeSeriesDoubleDouble(); Double step =
     * this.getDiffEqSyst().getStep(); Double start = this.diffEqSyst.getAbscissa();
     * 
     * Map<Integer, NodeObject> U1 = this.initU1();
     * 
     * for ( Double i = start; i < t; i += step ) { U1 = this.replaceAndSimplify( i,
     * this.computeUNP1Euler( U1 ), U1 ); Double value = Double.parseDouble( U1.get( 1 ).toString()
     * ); result.put( i, value ); } return result; }
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
     * public TimeSeriesDoubleDouble equationToTimeseriesEuler( int n ) throws Exception {
     * TimeSeriesDoubleDouble result = new TimeSeriesDoubleDouble(); Double step =
     * this.getDiffEqSyst().getStep(); Double start = this.diffEqSyst.getAbscissa(); Double
     * currentTime = start;
     * 
     * Map<Integer, NodeObject> U1 = this.initU1();
     * 
     * for ( int i = 0; i < n; i++ ) { U1 = this.replaceAndSimplify( currentTime,
     * this.computeUNP1Euler( U1 ), U1 ); Double value = Double.parseDouble( U1.get( 1 ).toString()
     * ); result.put( currentTime, value );
     * 
     * currentTime += step; } return result; }
     */

}
