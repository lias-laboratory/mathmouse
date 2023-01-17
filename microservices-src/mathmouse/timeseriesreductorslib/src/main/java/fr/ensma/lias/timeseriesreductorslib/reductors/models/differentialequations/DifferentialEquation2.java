package fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.tree.BinaryOperator;
import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.tree.BinaryOperatorEnum;
import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.tree.Function;
import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.tree.NodeObject;
import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.tree.Number;
import fr.ensma.lias.timeseriesreductorslib.timeseries.TimeSeriesDoubleDouble;
import fr.ensma.lias.timeseriesreductorslib.timeseries.TimeSeriesDoubleDoubleIO;

/**
 * Permet de représenter une équation et ses caractérisques (liste des
 * conditions initiales, liste des constantes, etc.)
 * 
 * @author albertf + cyrille ponchateau (ponchateau@ensma.fr)
 *
 */
public class DifferentialEquation2 implements Serializable {
    /**
     * JSON Fields
     */
    public static final String                       ID               = "id";
    public static final String                       EQUATION         = "equation formula";
    public static final String                       PARAMETERS       = "parameters set";

    /**
     * 
     */
    private static final long                        serialVersionUID = -6370287140901377791L;

    private Long                                     id;
    private String                                   name;
    private String                                   group;
    private NodeObject                               equation;
    private ParametersSet                            parametersSet;
    private Map<FunctionKey, TimeSeriesDoubleDouble> inputFunctions;

    /**
     * 
     */
    public DifferentialEquation2() {
        this.equation = null;
        this.parametersSet = new ParametersSet();
        this.inputFunctions = new HashMap<FunctionKey, TimeSeriesDoubleDouble>();
    }

    /**
     * 
     * @param equation
     * @param parametersSet
     * @param check
     * @throws Exception
     */
    public DifferentialEquation2( NodeObject equation, ParametersSet parametersSet, boolean check ) throws Exception {
        this.equation = equation;
        this.parametersSet = parametersSet;
        inputFunctions = new HashMap<FunctionKey, TimeSeriesDoubleDouble>();
        if ( check && ( !this.checkMapping() || !this.checkInputFunctionMapping() ) )
            throw new Exception( "Error mapping - " + equation );
        if ( !( this.parametersSet.getInputFunctions() == null ) && !this.parametersSet.getInputFunctions().isEmpty() )
            for ( FunctionKey key : this.parametersSet.getInputFunctions().keySet() ) {
                if ( this.parametersSet.getInputFunctions().get( key ) == null
                        || this.parametersSet.getInputFunctions().get( key ).equals( "" ) )
                    throw new Exception( "No file path for input function " + key );
                inputFunctions.put( key,
                        TimeSeriesDoubleDoubleIO.readFromFile( this.parametersSet.getInputFunctions().get( key ) ) );
            }
    }

    /**
     * 
     * @param equation
     * @param parametersSet
     * @param inputs
     * @param check
     * @throws Exception
     */
    public DifferentialEquation2( NodeObject equation, ParametersSet parametersSet,
            Map<FunctionKey, TimeSeriesDoubleDouble> inputs, boolean check ) throws Exception {
        super();
        this.equation = equation;
        this.parametersSet = parametersSet;
        if ( check && ( !this.checkMapping() || !this.checkInputFunctionMapping() ) )
            throw new Exception( "Error mapping - " + equation );
        inputFunctions = inputs;
    }

    /**
     * 
     * @param diffEq
     */
    public DifferentialEquation2( DifferentialEquation2 diffEq ) {
        this.equation = diffEq.getEquation().clone();
        this.parametersSet = diffEq.getParametersSet().clone();

        inputFunctions = new HashMap<FunctionKey, TimeSeriesDoubleDouble>();

        if ( diffEq.getInputFunctions() != null )
            for ( FunctionKey key : diffEq.getInputFunctions().keySet() )
                this.inputFunctions.put( key, diffEq.getInputFunctions().get( key ).clone() );
    }

    public ParametersSet getParametersSet() {
        return parametersSet;
    }

    public void setParametersSet( ParametersSet parametersSet ) {
        this.parametersSet = parametersSet;
    }

    /**
     * 
     * @param equation
     */
    public DifferentialEquation2( NodeObject equation ) {
        this.equation = equation;
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

    public NodeObject getEquation() {
        return equation;
    }

    public void setEquation( NodeObject equation ) {
        this.equation = equation;
    }

    public Map<FunctionKey, TimeSeriesDoubleDouble> getInputFunctions() {
        return inputFunctions;
    }

    public void setInputFunctions( Map<FunctionKey, TimeSeriesDoubleDouble> newInputFunctions ) {
        inputFunctions = newInputFunctions;
    }

    public Long getId() {
        return id;
    }

    public void setId( Long id ) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "DifferentialEquation2 [id=" + id + ", name=" + name + ", group=" + group + ", equation=" + equation
                + ", parametersSet=" + parametersSet + ", inputFunctions=" + inputFunctions + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( equation == null ) ? 0 : equation.hashCode() );
        result = prime * result + ( ( inputFunctions == null ) ? 0 : inputFunctions.hashCode() );
        result = prime * result + ( ( parametersSet == null ) ? 0 : parametersSet.hashCode() );
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
        DifferentialEquation2 other = (DifferentialEquation2) obj;
        if ( equation == null ) {
            if ( other.equation != null )
                return false;
        } else if ( !equation.equals( other.equation ) )
            return false;
        if ( inputFunctions == null ) {
            if ( other.inputFunctions != null )
                return false;
        } else if ( !inputFunctions.equals( other.inputFunctions ) )
            return false;
        if ( parametersSet == null ) {
            if ( other.parametersSet != null )
                return false;
        } else if ( !parametersSet.equals( other.parametersSet ) )
            return false;
        return true;
    }

    /**
     * Permet de récupérer l'ordre de l'équation
     * 
     * @return Correspond au degré de dérivé. ex : y' => 1
     */
    public int getOrder() {
        return this.equation.getOrder();
    }

    /**
     * Permet de savoir si l'équation est linéaire ou non
     * 
     * @return True si linéaire, false sinon
     */
    public boolean isLinear() {
        return this.equation.isLinear2( parametersSet.getInputFunctions() );
    }

    /**
     * Récupération de la fonction inconnue
     * 
     * @return Nom de l'inconnue
     * @throws Exception
     *             Si plusieurs inconnues
     */
    public String getUnknownFunction() throws Exception {
        return this.equation.getUnknownFunction2( parametersSet.getInputFunctions() );
    }

    /**
     * Permet de faire le mapping entre les variables de l'équations et des
     * valeurs données dans son system
     * 
     * @return Nouvelle équation (sous forme d'arbre) après transformation
     * @throws Exception
     *             Out of Range si l'indice du système donnée est trop grand ou
     *             trop petit
     */
    public NodeObject transformEquationWithSystem() throws Exception {
        if ( parametersSet == null )
            throw new Exception( "This equation do not have any parameters set, it have not been properly set." );
        return this.equation.transformEquationWithSystem( parametersSet );
    }

    /**
     * Permet de trouver le noeud comportant le coefficient et la fonction
     * donnée en paramètre
     * 
     * @param deriv
     *            Degré de dérivée de la fonction recherchée
     * @param name
     *            Nom de la fonction
     * @return noeud comportant le coefficient et la fonction donnée en
     *         paramètre
     * @throws Exception
     *             Si l'équation est non linéaire OU Si i est suppérieur à
     *             l'ordre de l'équation ou inférieur à 0
     */
    public NodeObject findFunction( int deriv, String name ) throws Exception {
        if ( this.equation.getOrder() < deriv || 0 > deriv )
            throw new Exception( "Error : Equation order : " + this.equation.getOrder() + " deriv:" + deriv );
        else if ( this.equation.isLinear2( parametersSet.getInputFunctions() ) )
            return this.equation.findFunction( deriv, name );
        else
            throw new Exception( "Error : Non linear equation" );
    }

    /**
     * Permet de récupérer le coefficient d'une fonction dans l'équation
     * 
     * @param deriv
     *            Degré de dérivée de la fonction recherchée
     * @param name
     *            Nom de la fonction
     * @return noeud comportant le coefficient de la fonction donnée en
     *         paramètre
     * @throws Exception
     *             Si l'équation est non linéaire OU Si i est suppérieur à
     *             l'ordre de l'équation ou inférieur à 0
     */
    public NodeObject getCoefficient( int deriv, String name ) throws Exception {
        if ( this.equation.getOrder() < deriv || 0 > deriv )
            throw new Exception( "Error : Equation order : " + this.equation.getOrder() + " deriv:" + deriv );
        else if ( this.equation.isLinear2( parametersSet.getInputFunctions() ) ) {
            return this.equation.findFunction( deriv, name ).getCoefficient( deriv, name );
        } else
            throw new Exception( "Error : Non linear equation" );
    }

    /**
     * @return true if the initial condition or set in the system (parameters
     *         set).
     */
    public boolean hasInitialCondition() {
        if ( parametersSet != null && parametersSet.hasInitialCondition() )
            return true;
        return false;
    }

    /**
     * Vérification du mapping
     * 
     * @return True si mapping bon (variable déclarée dans le système)
     */
    public boolean checkMapping() {
        List<String> variables = this.equation.getVariable();

        // check if system is not null
        if ( parametersSet == null )
            return false;

        // check if the system can be mapped
        if ( !parametersSet.canbeMapped() )
            return false;

        // check if all the variable of the equation appears in the mapping
        if ( this.equation.hasUnknownVariable2( parametersSet.getMapping() ) )
            return false;

        // check if the mapping do not contains to much variables
        if ( !variables.containsAll( parametersSet.getMapping().keySet() ) )
            return false;

        return true;
    }

    /**
     * @return True if all input function exists in the equation and are linked
     *         to a time series
     */
    public boolean checkInputFunctionMapping() {
        if ( parametersSet == null )
            return false;
        else if ( parametersSet.getInputFunctions() == null )
            return false;
        else {
            // for each input function of the system
            for ( Entry<FunctionKey, String> entry : parametersSet.getInputFunctions().entrySet() ) {
                FunctionKey key = entry.getKey();
                // check if the key is in the equation
                if ( !this.equation.hasFunction( key.getName() ) )
                    return false;
                // check if the link is not null or empty
                String obj = entry.getValue();
                if ( obj == null || obj.equals( "" ) )
                    return false;
                // check if the file exists
                File f = new File( "" );
                String dossierPath = f.getAbsolutePath() + File.separator + obj;
                File file = new File( dossierPath );
                if ( !file.exists() ) {
                    Logger.getLogger( getClass() ).debug(
                            "fr.ensma.lias.differentialequation.DifferentialEquation.checkInputFunction - File not found: "
                                    + dossierPath );
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Permet de vérifier si l'équation peut être simplifiée
     * 
     * @param system
     *            Système utilisée
     * @return True si peut être simplifiée, False sinon
     * @throws Exception
     *             Int system out of range
     */
    public boolean canBeSimplified() throws Exception {
        // check if the system is set
        if ( parametersSet == null )
            throw new Exception( "No system set" );

        // check the equation integrity
        if ( this.equation.hasUnknownVariable2( parametersSet.getMapping() ) )
            throw new Exception( "Equation must be transformed" );

        // true is the equation can be simplified and if the use of the system
        // allows some further simplification
        return this.equation.canBeSimplified() || this.transformEquationWithSystem().canBeSimplified();
    }

    /**
     * Simplification de l'équation
     * 
     * @return Equation simplifiée
     * @throws Exception
     *             "Equation must be transformed" si l'équation possède des
     *             variables
     */
    public NodeObject simplify() throws Exception {
        // simplifies the equation first
        // the equation is cloned in order not to alter the object
        NodeObject eq = this.equation.clone();
        // All variables of the equation must be known
        if ( !this.equation.hasUnknownVariable2( parametersSet.getMapping() ) ) {
            while ( eq.canBeSimplified() )
                eq = eq.simplify();
        } else
            throw new Exception( "Equation must be transformed" );

        if ( parametersSet == null )
            throw new Exception( "No system set" );

        // when expression is simplified, the system is used to literal
        // constants by their real values
        eq = eq.transformEquationWithSystem( parametersSet );

        // once equation transformed, goes through some further simplification,
        // that may have been introduced
        while ( eq.canBeSimplified() )
            eq = eq.simplify();

        return eq;
    }

    /**
     * Set input function of the equation with known time series
     * 
     * @param tsc,
     *            Map<String, String> (key, value), where key is the function
     *            symbol in the equation and value is the link (file path) to
     *            the time series
     * @throws Exception
     */
    public void setInputFunctionFromTs( Map<FunctionKey, String> tsc ) throws Exception {
        // check that every key of the tsc map is a function appearing in the
        // equation
        for ( Entry<FunctionKey, String> entry : tsc.entrySet() ) {
            if ( !parametersSet.getInputFunctions().containsKey( entry.getKey() ) )
                throw new Exception( "Error : " + entry.getKey() + "is not a input function" );
        }

        // add in the input function map, the new keys and links
        for ( Entry<FunctionKey, String> entry : tsc.entrySet() ) {
            parametersSet.getInputFunctions().put( entry.getKey(), tsc.get( entry.getKey() ) );
        }
    }

    /**
     * Check if the formulas of the equation in parameter is the same than the
     * one of this.
     * 
     * @param diffeq,
     *            equation to compare this with
     * @return True if the formulas are equal, false otherwise
     * @throws Exception
     */
    public boolean equalEquation( DifferentialEquation2 diffeq ) throws Exception {
        if ( this.equation.equals( diffeq.getEquation() ) )
            return true;
        return false;
    }

    /**
     * Check if the formulas are the same, once the variables replaced by their
     * real values
     * 
     * @param diffeq
     * @return True if the formulas are equivalent, false otherwise
     * @throws Exception
     */
    public boolean equalEquationWithVariable( DifferentialEquation2 diffeq ) throws Exception {
        // check if both systems are set
        if ( parametersSet == null || diffeq.getParametersSet() == null )
            return false;

        // check if the systems are equal
        if ( !parametersSet.equals( diffeq.getParametersSet() ) )
            return false;

        // check if the equation formulas are the same, taking the system into
        // account
        if ( !this.transformEquationWithSystem().equals( diffeq.transformEquationWithSystem() ) )
            return false;

        return true;
    }

    /**
     * Check if the equation in parameter is equal to the transformed equation,
     * taking the input function into account
     * 
     * @param diffeq
     * @return
     */
    public boolean equalEquationWithFunction( DifferentialEquation2 diffeq ) {
        if ( parametersSet == null || diffeq.getParametersSet() == null )
            return false;
        return true;
    }

    /**
     * Permet de transformer une équation en une autre équation sous la forme
     * Y^(n) = F(x,Y,Y',...,Y^(n-1))
     * 
     * @param deriv
     *            Ordre de la dérivée
     * @return Equation sous la forme Y^(n) = F(x,Y,Y',...,Y^(n-1))
     * @throws Exception
     */
    public NodeObject transformEquationToYForm( int deriv ) throws Exception {
        NodeObject eq = this.equation.clone();
        if ( eq.getLeftPart() == null )
            return null; // Excep
        // throw new WrongEquationDefinitionException() ?
        while ( !( eq.getLeftPart() instanceof Function ) ) // Pas assez sûr !
                                                            // boucle infinie ?
            eq = this.computeYForm( deriv, eq );
        return eq;
    }

    /**
     * 
     * @param deriv
     * @param diffEqua
     * @return
     * @throws Exception
     */
    private NodeObject computeYForm( int deriv, NodeObject diffEqua ) throws Exception {
        NodeObject eq = diffEqua.clone();
        String func = this.getUnknownFunction();
        if ( !( eq.getLeftPart() instanceof BinaryOperator ) ) // Pas assez sûr
                                                               // ! boucle
                                                               // infinie ?
            return eq;

        NodeObject left = eq.getLeftPart().clone();
        NodeObject right = eq.getRightPart().clone();
        if ( ( (BinaryOperator) left ).getLeft().hasFunction( func, deriv ) ) {
            right = this.transformRight( ( (BinaryOperator) left ).getRight(), false,
                    ( (BinaryOperator) left ).getValue(), right ); // Modif
            left = ( (BinaryOperator) left ).getLeft().clone();
        } else {
            right = this.transformRight( ( (BinaryOperator) left ).getLeft(), true,
                    ( (BinaryOperator) left ).getValue(), right ); // Modif
            left = ( (BinaryOperator) left ).getRight().clone();
        }

        eq = new BinaryOperator( BinaryOperatorEnum.EQUAL.getValue(), left, right );

        while ( eq.canBeSimplified() )
            eq = eq.simplify();

        if ( eq.getLeftPart() instanceof Function || eq.getLeftPart() instanceof BinaryOperator )
            return eq;
        else
            throw new Exception( "Error transformEquationToYFormV2 - compute : " + eq );
    }

    /**
     * Moves a term of the equation from the left side to the right side.
     * 
     * @param node,
     *            the node to move
     * @param isRight,
     *            true if the node is the right term of its operator, false
     *            otherwise
     * @param op,
     *            the operator of the node
     * @param right,
     * @return
     */
    private NodeObject transformRight( NodeObject node, boolean isRight, String op, NodeObject right ) {
        NodeObject result = null;
        // If the node is the division operator
        if ( op.equals( BinaryOperatorEnum.DIVI.getValue() ) ) {
            // we need to now if the node is the denominator or the numerator
            if ( isRight ) // if it is the denominator, we need to multiply by
                           // this term to move it to the right side
                result = new BinaryOperator( BinaryOperatorEnum.STAR.getValue(), right.clone(), node.clone() );
            else
                // if it is the numerator, we need to divide by the same term
                result = new BinaryOperator( BinaryOperatorEnum.DIVI.getValue(), node.clone(), right.clone() );
            // If the node is a multiplication operator
        } else if ( op.equals( BinaryOperatorEnum.STAR.getValue() ) ) {
            // we have to divide by the same term
            result = new BinaryOperator( BinaryOperatorEnum.DIVI.getValue(), right.clone(), node.clone() );
            // If the node is a power operation
        } else if ( op.equals( BinaryOperatorEnum.POW.getValue() ) ) {
            // we need to know if it is the element or the exponant
            if ( isRight ) // if it is the exponant,
                result = new BinaryOperator( BinaryOperatorEnum.STAR.getValue(), right.clone(), node.clone() );
            else {
                NodeObject temp = new BinaryOperator( BinaryOperatorEnum.DIVI.getValue(), new Number( "1" ),
                        node.clone() );
                result = new BinaryOperator( BinaryOperatorEnum.POW.getValue(), right.clone(), temp );
            }
        } else { // op = { + , - }
            result = new BinaryOperator( BinaryOperatorEnum.MINUS.getValue(), right.clone(), node.clone() );
        }
        return result;
    }

    /**
     * Permet de remplacer les functions (forces) par leur valeurs en t
     * 
     * @param eq
     *            Equation contenant les fonctions
     * @param n
     *            Valeur de t
     * @param system
     *            Numéro du système
     * @return
     * @throws Exception
     */
    public NodeObject replaceFunctionByValue( NodeObject eq, Double n ) throws Exception {
        // the equation is cloned, in order not to alter it
        NodeObject result = eq.clone();
        // get the input function of the equation
        Map<FunctionKey, String> mappingFunction = parametersSet.getInputFunctions();

        // if there is no input function, there is nothing to do
        if ( mappingFunction == null || mappingFunction.isEmpty() )
            return result;

        // otherwise
        // go through the input function map, it is a list of values <String
        // key, String value>, key being the function symbol in the equation,
        // the value being the path of the file containing the real values of
        // the function.
        for ( Entry<FunctionKey, String> entry : mappingFunction.entrySet() ) {
            FunctionKey key = entry.getKey();// get the symbol of the
                                             // function
            // reads the function values in the series text file
            TimeSeriesDoubleDouble ts = new TimeSeriesDoubleDouble( entry.getValue(), "  ", "", 0 );
            // replaces all occurences of the symbol key, by its values at time
            // n.
            result = result.replaceFunctionByVar2( key, ts.get( n ) );
        }
        return result;
    }

}
