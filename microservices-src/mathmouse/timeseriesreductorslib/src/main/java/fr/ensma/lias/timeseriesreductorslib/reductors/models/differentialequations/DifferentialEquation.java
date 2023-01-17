package fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.tree.BinaryOperator;
import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.tree.BinaryOperatorEnum;
import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.tree.Function;
import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.tree.NodeObject;
import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.tree.Number;
import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.tree.UnaryOperator;
import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.tree.Variable;
import fr.ensma.lias.timeseriesreductorslib.timeseries.TimeSeriesDoubleDouble;

/**
 * Permet de représenter une équation et ses caractérisques (liste des
 * conditions initiales, liste des constantes, etc.)
 * 
 * @author albertf
 *
 */
@Deprecated
public class DifferentialEquation implements Serializable, IXMLFileIO {
    /**
     * JSON Fields
     */
    public static final String                  ID               = "id";
    public static final String                  EQUATION         = "equation formula";
    public static final String                  PARAMETERS       = "parameters set";

    /**
     * 
     */
    private static final long                   serialVersionUID = -6370287140901377791L;

    private Long                                id;

    private NodeObject                          equation;

    private DiffEquaSystem                      system;

    private Map<String, TimeSeriesDoubleDouble> inputFunctions;

    public DifferentialEquation() {
        this.equation = null;
        this.system = new DiffEquaSystem();
        this.inputFunctions = new HashMap<String, TimeSeriesDoubleDouble>();
    }

    public DifferentialEquation( NodeObject equation, DiffEquaSystem system, boolean check ) throws Exception {
        super();
        this.equation = equation;
        this.system = system;
        if ( check && ( !this.checkMapping() || !this.checkInputFunctionMapping() ) )
            throw new Exception( "Error mapping - " + equation );
        inputFunctions = new HashMap<String, TimeSeriesDoubleDouble>();
        if ( !( this.system.getInputFunction() == null ) && !this.system.getInputFunction().isEmpty() )
            for ( String key : this.system.getInputFunction().keySet() ) {
                if ( this.system.getInputFunction().get( key ) == null
                        || this.system.getInputFunction().get( key ).equals( "" ) )
                    throw new Exception( "No file path for input function " + key );
                inputFunctions.put( key,
                        new TimeSeriesDoubleDouble( this.system.getInputFunction().get( key ), "   ", "" ) );
                this.system.setInitial( inputFunctions.get( key ).firstKey() );
            }
    }

    public DifferentialEquation( NodeObject equation, DiffEquaSystem system, Map<String, TimeSeriesDoubleDouble> inputs,
            boolean check ) throws Exception {
        super();
        this.equation = equation;
        this.system = system;
        if ( check && ( !this.checkMapping() || !this.checkInputFunctionMapping() ) )
            throw new Exception( "Error mapping - " + equation );
        inputFunctions = inputs;
    }

    public DifferentialEquation( DifferentialEquation diffEq ) {
        this.equation = diffEq.getEquation().clone();
        this.system = diffEq.getSystem().clone();

        inputFunctions = new HashMap<String, TimeSeriesDoubleDouble>();

        if ( diffEq.getInputFunctions() != null )
            for ( String key : diffEq.getInputFunctions().keySet() )
                this.inputFunctions.put( key, diffEq.getInputFunctions().get( key ).clone() );
    }

    public DifferentialEquation( String file, int systemIndex, boolean check ) throws Exception {
        DifferentialEquation eq = this.reader( file, systemIndex, check );
        this.equation = eq.equation;
        this.system = eq.system;
        this.inputFunctions = eq.getInputFunctions();
    }

    public DiffEquaSystem getSystem() {
        return system;
    }

    public void setSystem( DiffEquaSystem system ) {
        this.system = system;
    }

    public DifferentialEquation( NodeObject equation ) {
        super();
        this.equation = equation;
    }

    public NodeObject getEquation() {
        return equation;
    }

    public void setEquation( NodeObject equation ) {
        this.equation = equation;
    }

    public Map<String, TimeSeriesDoubleDouble> getInputFunctions() {
        return inputFunctions;
    }

    public void setInputFunctions( Map<String, TimeSeriesDoubleDouble> newInputFunctions ) {
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
        return "DifferentialEquation [equation : " + equation
                +
                " System : {" + ( ( system == null ) ? "" : system ) + "} ]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ( ( equation == null ) ? 0 : equation.hashCode() );
        result = prime * result + ( ( id == null ) ? 0 : id.hashCode() );
        result = prime * result + ( ( system == null ) ? 0 : system.hashCode() );
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
        DifferentialEquation other = (DifferentialEquation) obj;
        if ( equation == null ) {
            if ( other.equation != null )
                return false;
        } else if ( !equation.equals( other.equation ) )
            return false;
        if ( id == null ) {
            if ( other.id != null )
                return false;
        } else if ( !id.equals( other.id ) )
            return false;
        if ( system == null ) {
            if ( other.system != null )
                return false;
        } else if ( !system.equals( other.system ) )
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
        return this.equation.isLinear( system.getInputFunction() );
    }

    /**
     * Récupération de la fonction inconnue
     * 
     * @return Nom de l'inconnue
     * @throws Exception
     *             Si plusieurs inconnues
     */
    public String getUnknownFunction() throws Exception {
        return this.equation.getUnknownFunction( system.getInputFunction() );
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
        if ( system == null )
            throw new Exception( "This equation do not have any system, it have not been properly set." );
        return this.equation.transformEquationWithSystem( system );
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
        else if ( this.equation.isLinear( system.getInputFunction() ) )
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
        else if ( this.equation.isLinear( system.getInputFunction() ) ) {
            return this.equation.findFunction( deriv, name ).getCoefficient( deriv, name );
        } else
            throw new Exception( "Error : Non linear equation" );
    }

    /**
     * @return true if the initial condition or set in the system (parameters
     *         set).
     */
    public boolean hasInitialCondition() {
        if ( system != null && system.hasInitialCondition() )
            return true;
        return false;
    }

    public void setInitialCondition( double x, double y ) {
        this.system.setInitial( y );
        this.system.setAbscissa( x );
    }

    /**
     * Vérification du mapping
     * 
     * @return True si mapping bon (variable déclarée dans le système)
     */
    public boolean checkMapping() {
        List<String> variables = this.equation.getVariable();

        // check if system is not null
        if ( system == null )
            return false;

        // check if the system can be mapped
        if ( !system.canbeMapped() )
            return false;

        // check if all the variable of the equation appears in the mapping
        if ( this.equation.hasUnknownVariable( system.getMapping() ) )
            return false;

        // check if the mapping do not contains to much variables
        if ( !variables.containsAll( system.getMapping().keySet() ) )
            return false;

        return true;
    }

    /**
     * @return True if all input function exists in the equation and are linked
     *         to a time series
     */
    public boolean checkInputFunctionMapping() {
        if ( system == null )
            return false;
        else if ( system.getInputFunction() == null )
            return false;
        else {
            // for each input function of the system
            for ( Entry<String, String> entry : system.getInputFunction().entrySet() ) {
                String key = entry.getKey();
                // check if the key is in the equation
                if ( !this.equation.hasFunction( key ) )
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
        if ( system == null )
            throw new Exception( "No system set" );

        // check the equation integrity
        if ( this.equation.hasUnknownVariable( system.getMapping() ) )
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
        if ( !this.equation.hasUnknownVariable( system.getMapping() ) ) {
            while ( eq.canBeSimplified() )
                eq = eq.simplify();
        } else
            throw new Exception( "Equation must be transformed" );

        if ( system == null )
            throw new Exception( "No system set" );

        // when expression is simplified, the system is used to literal
        // constants by their real values
        eq = eq.transformEquationWithSystem( system );

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
    public void setInputFunctionFromTs( Map<String, String> tsc ) throws Exception {
        // check that every key of the tsc map is a function appearing in the
        // equation
        for ( Entry<String, String> entry : tsc.entrySet() ) {
            if ( !system.getInputFunction().containsKey( entry.getKey() ) )
                throw new Exception( "Error : " + entry.getKey() + "is not a input function" );
        }

        // add in the input function map, the new keys and links
        for ( Entry<String, String> entry : tsc.entrySet() ) {
            system.getInputFunction().put( entry.getKey(), tsc.get( entry.getKey() ) );
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
    public boolean equalEquation( DifferentialEquation diffeq ) throws Exception {
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
    public boolean equalEquationWithVariable( DifferentialEquation diffeq ) throws Exception {
        // check if both systems are set
        if ( system == null || diffeq.getSystem() == null )
            return false;

        // check if the systems are equal
        if ( !system.equals( diffeq.getSystem() ) )
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
    public boolean equalEquationWithFunction( DifferentialEquation diffeq ) {
        if ( system == null || diffeq.getSystem() == null )
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
        Map<String, String> mappingFunction = system.getInputFunction();

        // if there is no input function, there is nothing to do
        if ( mappingFunction == null || mappingFunction.isEmpty() )
            return result;

        // otherwise
        // go through the input function map, it is a list of values <String
        // key, String value>, key being the function symbol in the equation,
        // the value being the path of the file containing the real values of
        // the function.
        for ( Entry<String, String> entry : mappingFunction.entrySet() ) {
            String key = entry.getKey();// get the symbol of the function
            // reads the function values in the series text file
            TimeSeriesDoubleDouble ts = new TimeSeriesDoubleDouble( entry.getValue(), "  ", "", 0 );
            // replaces all occurences of the symbol key, by its values at time
            // n.
            result = result.replaceFunctionByVar( key, ts.get( n ) );
        }
        return result;
    }

    @Override
    public void writer( String xml, DifferentialEquation eqdiff, String dtdpathfile ) {
        // TODO Auto-generated method stub

    }

    /**
     * Reads an equation from the XML file
     * 
     * @throws Exception
     */
    @Override
    public DifferentialEquation reader( String file, int systemIndex, boolean check ) throws Exception {
        // Make an instance of the DocumentBuilderFactory
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setValidating( true ); // Well-formed xml
        NodeObject equation = null;
        // Map<String, Object> inputFunction = null;
        DiffEquaSystem system = null;
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            db.setErrorHandler( new ErrorHandler() {
                @Override
                public void error( SAXParseException exception ) throws SAXException {
                    exception.printStackTrace();
                }

                @Override
                public void fatalError( SAXParseException exception ) throws SAXException {
                    exception.printStackTrace();
                }

                @Override
                public void warning( SAXParseException exception ) throws SAXException {
                    exception.printStackTrace();
                }
            } );
            Element doc = db.parse( file ).getDocumentElement();
            NodeList nList = doc.getChildNodes();
            for ( int i = 0; i < nList.getLength(); i++ ) {
                if ( nList.item( i ).getNodeType() == 1 ) {
                    if ( nList.item( i ).getNodeName().equals( "equation" ) ) {
                        NodeList equationChildren = ( (Element) nList.item( i ) ).getElementsByTagName( "operator" );
                        equation = this.reader( (Element) equationChildren.item( 0 ) );
                    }
                    /*
                     * else if(nList.item(i).getNodeName().equals("input")){
                     * inputFunction =
                     * this.readerInputFunction((Element)nList.item(i)); }
                     */
                    else if ( nList.item( i ).getNodeName().equals( "systems" ) ) {
                        system = this.readerSystem( (Element) nList.item( i ), systemIndex );
                    }
                }
            }
            if ( equation == null )
                throw new Exception( "Empty equation " );
            /*
             * else if(systems == null || systems.isEmpty() ) throw new
             * Exception("Empty system (initial value or mapping not defined)");
             */
            return new DifferentialEquation( equation, system, check );
        } catch ( ParserConfigurationException e ) {
            e.printStackTrace();
        } catch ( SAXException e ) {
            e.printStackTrace();
        } catch ( IOException e ) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Retrieval of the system
     * 
     * @param item
     *            Node <system>
     * @param systemIndex,
     *            position of the system in the systems list of the XML file
     * @return Liste des systemes trouv�s
     */
    private DiffEquaSystem readerSystem( Element item, int systemIndex )
            throws Exception, ArrayIndexOutOfBoundsException {
        // get the systems list
        NodeList nList = item.getElementsByTagName( "system" );
        NodeList list;
        Map<String, String> mapping;

        // check if systemIndex is in the range of the systems list
        if ( nList.item( systemIndex ) == null )
            throw new ArrayIndexOutOfBoundsException(
                    "fr.ensma.lias.differentialequation.model.DifferentialEquation:readerSytem - Index of the systems list range" );
        // get the mapping list contained in the system
        list = ( (Element) nList.item( systemIndex ) ).getElementsByTagName( "mapping" );
        if ( list == null || list.getLength() == 0 )
            mapping = null;
        else
            mapping = this.readerMapping( (Element) list.item( 0 ) );
        // get the initial values (initial = t0,abscissa = y0)
        list = ( (Element) nList.item( systemIndex ) ).getElementsByTagName( "initial" );
        double initial = this.readerInitialValue( (Element) list.item( 0 ) );
        double abscissa;
        if ( new Double( initial ).equals( Double.NaN ) )
            abscissa = 0;
        else {
            abscissa = this.readerInitialAbscissa( (Element) list.item( 0 ) );
        }
        // get the input function of the equation
        list = ( (Element) nList.item( systemIndex ) ).getElementsByTagName( "input" );
        Map<String, String> functions = this.readerInputFunction( (Element) list.item( 0 ) );
        list = ( (Element) nList.item( systemIndex ) ).getElementsByTagName( "step" );
        double step = this.readerStepValue( (Element) list.item( 0 ) );
        return new DiffEquaSystem( mapping, initial, abscissa, step, functions );
    }

    /**
     * R�cup�ration de l'abscisse de la condition initiale
     * 
     * @param item
     *            Arbre dont la racine est <initial>
     * @return X
     */
    private Double readerInitialAbscissa( Element item ) {
        if ( item == null )
            return new Double( 0 );
        else if ( item.getAttribute( "abscissa" ) == null || "".equals( item.getAttribute( "abscissa" ) ) )
            return new Double( 0 );
        else
            return Double.parseDouble( item.getAttribute( "abscissa" ) );
    }

    /**
     * R�cup�ration de la condition initiale
     * 
     * @param item
     *            Arbre dont la racine est <initial>
     * @return y
     * @throws Exception
     *             Exception Erreur lors du traitement de Y
     */
    private Double readerInitialValue( Element item ) {
        if ( item == null )
            return Double.NaN;
        else if ( item.getAttribute( "value" ) == null || "".equals( item.getAttribute( "value" ) ) )
            return Double.NaN;
        else
            return Double.parseDouble( item.getAttribute( "value" ) );
    }

    /**
     * R�cup�ration du pas (step)
     * 
     * @param item
     *            Arbre dont la racine est <step>
     * @return Le pas
     */
    private Double readerStepValue( Element item ) {
        if ( item == null )
            return Double.NaN;
        else if ( item.getAttribute( "value" ) == null || "".equals( item.getAttribute( "value" ) ) )
            return Double.NaN;
        else
            return Double.parseDouble( item.getAttribute( "value" ) );
    }

    /**
     * R�cup�ration des r�gles de mappage concernant les variables
     * 
     * @param item
     *            Arbre dont la racine est <mapping>
     * @return Map contenant les variables et leur valeur
     */
    private Map<String, String> readerMapping( Element item ) {
        Map<String, String> mapping = new HashMap<String, String>();
        NodeList mapList = null;
        mapList = item.getElementsByTagName( "map" );
        if ( mapList == null )
            return null;
        for ( int j = 0; j < mapList.getLength(); j++ ) {
            Element elt = (Element) mapList.item( j );
            mapping.put( elt.getAttribute( "name" ), elt.getAttribute( "value" ) );
        }
        return mapping;
    }

    /**
     * Cr�ation de l'�quation sous forme d'arbre (recursif)
     * 
     * @param n
     *            Node courant dans le fichier XML
     * @return Node courant + fils
     */
    private NodeObject reader( Element n ) {
        NodeObject left = null;
        NodeObject right = null;
        if ( "operator".equals( n.getNodeName() ) ) {
            NodeList nList = n.getChildNodes();
            for ( int i = 0; i < nList.getLength(); i++ ) {
                if ( nList.item( i ).getNodeType() == 1 ) {
                    if ( left == null )
                        left = this.reader( (Element) nList.item( i ) );
                    else {
                        right = this.reader( (Element) nList.item( i ) );
                        break;
                    }
                }
            }
            if ( right == null )
                return new UnaryOperator( n.getAttribute( "value" ), left );
            else
                return new BinaryOperator( n.getAttribute( "value" ), left, right );
        } else if ( "function".equals( n.getNodeName() ) ) {
            NodeList nList = n.getChildNodes();
            NodeObject elt = null;
            for ( int i = 0; i < nList.getLength(); i++ ) {
                if ( nList.item( i ).getNodeType() == 1 ) {
                    elt = this.reader( (Element) nList.item( i ) );
                    break;
                }
            }
            if ( !"".equals( n.getAttribute( "deriv" ) ) )
                return new Function( n.getAttribute( "value" ), Integer.parseInt( n.getAttribute( "deriv" ) ), elt );
            else
                return new Function( n.getAttribute( "value" ), elt );
        } else if ( n.getNodeName().equals( "number" ) )
            return new Number( n.getAttribute( "value" ) );
        else
            return new Variable( n.getAttribute( "value" ) );
    }

    /**
     * R�cup�ration des fonctions ajout�es
     * 
     * @param elt
     *            Element <input> dans le fichier XML
     * @return MAP contenant le nom des fonctions
     */
    private Map<String, String> readerInputFunction( Element elt ) {
        Map<String, String> result = new HashMap<String, String>();
        if ( elt == null )
            return null;
        NodeList nList = elt.getElementsByTagName( "inputFunction" );
        for ( int i = 0; i < nList.getLength(); i++ ) {
            result.put( ( (Element) nList.item( i ) ).getAttribute( "name" ),
                    ( (Element) nList.item( i ) ).getAttribute( "file" ) );
        }
        return result;
    }

}
