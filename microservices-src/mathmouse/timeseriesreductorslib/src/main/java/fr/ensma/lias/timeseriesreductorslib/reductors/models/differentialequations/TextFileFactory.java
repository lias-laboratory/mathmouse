package fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.validation.Validator;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.tree.BinaryOperator;
import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.tree.EFunctionRole;
import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.tree.Function;
import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.tree.NodeObject;
import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.tree.Number;
import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.tree.UnaryOperator;
import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.tree.Variable;

/**
 * The class is made to implement any file writing function. Currently, it only
 * implement a function able to write an equation into an XML file. The XML is
 * confirmed to an XML Schema, see resources/differential-equation.xsd. This XML
 * Schema where made to be as similar as the PMML XML Schema, hoping it will
 * help produce an extended PMML XML Schema, that can support this
 * differential-equation representation.
 * 
 * @author cyrille ponchateau (cyrille.ponchateau@ensma.fr)
 *
 */
public class TextFileFactory extends FileIO {
    /**
     * XML tag names
     */
    protected static final String  ROOT_TAG_NAME                       = "differential-equation";
    protected static final String  FORMULA_TAG_NAME                    = "formula";
    protected static final String  BINARY_OPERATOR_TAG_NAME            = "binary-operator";
    protected static final String  UNARY_OPERATOR_TAG_NAME             = "unary-operator";
    protected static final String  FUNCTION_TAG_NAME                   = "function";
    protected static final String  NUMBER_TAG_NAME                     = "number";
    protected static final String  VARIABLE_TAG_NAME                   = "variable";
    protected static final String  PARAMETERS_SETS_TAG_NAME            = "parameters-sets";
    protected static final String  PARAMETERS_SET_TAG_NAME             = "parameters-set";
    protected static final String  STEP_TAG_NAME                       = "step";
    protected static final String  INITIAL_TAG_NAME                    = "initial";
    protected static final String  VARIABLES_VALUES_TAG_NAME           = "variables-values";
    protected static final String  VARIABLE_VALUE_TAG_NAME             = "variable-value";
    protected static final String  INPUT_FUNCTIONS_TAG_NAME            = "input-functions";
    protected static final String  INPUT_FUNCTION_TAG_NAME             = "input-function";

    /**
     * XML atrributes names
     */
    protected static final String  NUMBER_OF_VARIABLES_ATTRIBUTE       = "numberOfVariables";
    protected static final String  NUMBER_OF_INPUT_FUNCTIONS_ATTRIBUTE = "numberOfInputFunctions";
    protected static final String  OUTPUT_NAME_ATTRIBUTE               = "outputName";
    protected static final String  NUMBER_OF_PARAMETERS_SETS_ATTRIBUTE = "numberOfParametersSets";
    protected static final String  VALUE_ATTRIBUTE                     = "value";
    protected static final String  DERIV_ATTRIBUTE                     = "deriv";
    protected static final String  ROLE_ATTRIBUTE                      = "role";
    protected static final String  NAME_ATTRIBUTE                      = "name";
    protected static final String  PATH_ATTRIBUTE                      = "path";

    /**
     * PMML tag names
     */
    protected static final String  PMML_ROOT_TAG_NAME                  = "PMML";
    protected static final String  PMML_HEADER_TAG_NAME                = "Header";
    protected static final String  PMML_APPLICATION_TAG_NAME           = "Application";
    protected static final String  PMML_DATA_DICTIONNARY_TAG_NAME      = "DataDictionary";
    protected static final String  PMML_DATA_FIELD_TAG_NAME            = "DataField";

    /**
     * PMML attributes names
     */
    protected static final String  PMML_VERSION_ATTRIBUTE              = "version";
    protected static final String  PMML_XMLNS_ATTRIBUTE                = "xmlns";
    protected static final String  PMML_NAME_ATTRIBUTE                 = "name";
    protected static final String  PMML_COPYRIGHT_ATTRIBUTE            = "copyright";

    /**
     * PMML data type names
     */
    protected static final String  PMML_CONTINUOUS_DATA_TYPE           = "continuous";

    /**
     * some other need variable
     */
    private List<String>           variablesList;
    private List<String>           inputFunctionsList;
    private String                 outputName;
    private int                    numberOfParametersSets;

    /**
     * Static instance
     */
    private static TextFileFactory factory;

    /**
     * Private constructor, for singleton pattern purposes
     */
    private TextFileFactory() {
    }

    /**
     * Static accessors
     */
    public static TextFileFactory getInstance() {
        if ( factory == null )
            factory = new TextFileFactory();
        return factory;
    }

    public static TextFileFactory getInstance( String XSDFilePath ) {
        if ( factory == null )
            factory = new TextFileFactory();
        factory.setSchema( XSDFilePath );
        return factory;
    }

    public void PMMLWrite( String destinationXMLFile, DifferentialEquation eqdiff, String numericalAlgorithm ) {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();

            // creating the root
            Element rootElement = document.createElement( PMML_ROOT_TAG_NAME );
            rootElement.setAttribute( PMML_XMLNS_ATTRIBUTE, "http://www.dmg.org/PMML-4_3" );
            rootElement.setAttribute( PMML_VERSION_ATTRIBUTE, "4.3" );
            document.appendChild( rootElement );

            // creating header
            Element headerElement = document.createElement( PMML_HEADER_TAG_NAME );
            headerElement.setAttribute( PMML_COPYRIGHT_ATTRIBUTE, "Cyrille Ponchateau (cyrille.ponchateau@ensma.fr)" );
            Element applicationElement = document.createElement( PMML_APPLICATION_TAG_NAME );
            applicationElement.setAttribute( NAME_ATTRIBUTE, "Java" );
            applicationElement.setAttribute( PMML_VERSION_ATTRIBUTE, "1.8" );
            headerElement.appendChild( applicationElement );
            rootElement.appendChild( headerElement );

            // writing into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty( OutputKeys.INDENT, "yes" );
            transformer.setOutputProperty( "{http://xml.apache.org/xslt}indent-amount", "2" );
            DOMSource source = new DOMSource( document );
            StreamResult result = new StreamResult( new File( destinationXMLFile ) );
            transformer.transform( source, result );

            // validates the files
            // Validator validator = schema.newValidator();
            // validator.validate( new DOMSource( document ) );
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    /**
     * Writes an equation into an XML file.
     * 
     * @param destinationXMLFile,
     *            where to write the XML file
     * @param eqdiff,
     *            the equation to write
     * @param XSDFilePath,
     *            the XML Schema description
     */
    public void XMLWrite( String destinationXMLFile, DifferentialEquation eqdiff ) {
        // reset lists
        variablesList = new ArrayList<String>();
        inputFunctionsList = new ArrayList<String>();
        outputName = null;
        numberOfParametersSets = 1;
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();

            // creating root
            Element rootElement = document.createElement( ROOT_TAG_NAME );
            document.appendChild( rootElement );

            // creating formula tag
            Element formula = document.createElement( FORMULA_TAG_NAME );

            // recursive creation of the elements of the equation
            formula.appendChild( CreateElement( eqdiff.getEquation(), document ) );

            // settings attributes of formula
            formula.setAttribute( NUMBER_OF_INPUT_FUNCTIONS_ATTRIBUTE, String.valueOf( inputFunctionsList.size() ) );
            formula.setAttribute( NUMBER_OF_VARIABLES_ATTRIBUTE, String.valueOf( variablesList.size() ) );
            formula.setAttribute( OUTPUT_NAME_ATTRIBUTE, outputName );

            // appending formula to root
            rootElement.appendChild( formula );

            // creating parameters sets tag
            Element parametersSetsElement = document.createElement( PARAMETERS_SETS_TAG_NAME );
            parametersSetsElement.setAttribute( NUMBER_OF_PARAMETERS_SETS_ATTRIBUTE,
                    String.valueOf( numberOfParametersSets ) );
            parametersSetsElement.appendChild( CreateElement( eqdiff.getSystem(), document ) );
            rootElement.appendChild( parametersSetsElement );

            // writing into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource( document );
            StreamResult result = new StreamResult( new File( destinationXMLFile ) );
            transformer.transform( source, result );

            // validates the files
            Validator validator = schema.newValidator();
            validator.validate( new DOMSource( document ) );
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    /**
     * Creates the element structure for a parameters set (system).
     * 
     * @param parametersSet
     * @param document
     * @return
     */
    private Element CreateElement( DiffEquaSystem parametersSet, Document document ) {
        // create new parameters set element
        Element parametersSetElement = document.createElement( PARAMETERS_SET_TAG_NAME );

        // create step element
        Element stepElement = document.createElement( STEP_TAG_NAME );
        stepElement.setAttribute( VALUE_ATTRIBUTE, String.valueOf( parametersSet.getStep() ) );
        parametersSetElement.appendChild( stepElement );

        // create initial element
        Element initialElement = document.createElement( INITIAL_TAG_NAME );
        initialElement.setAttribute( VALUE_ATTRIBUTE, String.valueOf( parametersSet.getAbscissa() ) );
        parametersSetElement.appendChild( initialElement );

        // create variables value element
        Element variablesValuesElement = document.createElement( VARIABLES_VALUES_TAG_NAME );
        variablesValuesElement.setAttribute( NUMBER_OF_VARIABLES_ATTRIBUTE,
                String.valueOf( parametersSet.getMapping().size() ) );

        // going through the variable list of the parameters set
        for ( String variable : parametersSet.getMapping().keySet() ) {
            // create a variable value element
            Element variableValueElement = document.createElement( VARIABLE_VALUE_TAG_NAME );
            variableValueElement.setAttribute( NAME_ATTRIBUTE, variable );
            variableValueElement.setAttribute( VALUE_ATTRIBUTE, parametersSet.getMapping().get( variable ) );
            variablesValuesElement.appendChild( variableValueElement );
        }

        // appending variables values element to parameters set element
        parametersSetElement.appendChild( variablesValuesElement );

        // create input functions element
        Element inputFunctionsElement = document.createElement( INPUT_FUNCTIONS_TAG_NAME );
        inputFunctionsElement.setAttribute( NUMBER_OF_INPUT_FUNCTIONS_ATTRIBUTE,
                String.valueOf( parametersSet.getInputFunction().size() ) );

        // going through the input functions list of the parameters set
        for ( String inputFunction : parametersSet.getInputFunction().keySet() ) {
            // create input function element
            Element inputFunctionElement = document.createElement( INPUT_FUNCTION_TAG_NAME );
            inputFunctionElement.setAttribute( NAME_ATTRIBUTE, inputFunction );
            inputFunctionElement.setAttribute( PATH_ATTRIBUTE, parametersSet.getInputFunction().get( inputFunction ) );
            inputFunctionsElement.appendChild( inputFunctionElement );
        }

        // appending input functions element to parameters set element
        parametersSetElement.appendChild( inputFunctionsElement );

        return parametersSetElement;
    }

    /**
     * Recursive building of the element structure corresponding to the
     * equation.
     * 
     * @param node
     * @param document
     * @return
     */
    private Element CreateElement( NodeObject node, Document document ) {
        Element element = null;
        if ( node instanceof BinaryOperator ) {
            // creating binary operator element
            element = document.createElement( BINARY_OPERATOR_TAG_NAME );

            // setting value attribute
            Attr value = document.createAttribute( VALUE_ATTRIBUTE );
            value.setValue( node.getValue() );
            element.setAttributeNode( value );

            // creating left child element
            element.appendChild( CreateElement( node.getLeftPart(), document ) );

            // creating right child element
            element.appendChild( CreateElement( node.getRightPart(), document ) );
        } else if ( node instanceof UnaryOperator ) {
            // creating unary operator element
            element = document.createElement( UNARY_OPERATOR_TAG_NAME );

            // setting value attribute
            element.setAttribute( VALUE_ATTRIBUTE, node.getValue() );

            // creating child
            element.appendChild( CreateElement( node.getLeftPart(), document ) );
        } else if ( node instanceof Function ) {
            // creating function element
            element = document.createElement( FUNCTION_TAG_NAME );

            // setting attributes
            element.setAttribute( VALUE_ATTRIBUTE, node.getValue() );
            element.setAttribute( DERIV_ATTRIBUTE, String.valueOf( node.getDeriv() ) );
            element.setAttribute( ROLE_ATTRIBUTE, node.getFunctionRole().getValue() );

            // check if the function has a child
            if ( node.hasChildren() )
                element.appendChild( CreateElement( node.getLeftPart(), document ) );

            // if the function is an input of the equation and has not been met
            // before, it is added into the input function list
            if ( node.getFunctionRole() == EFunctionRole.EQUATION_INPUT
                    && !inputFunctionsList.contains( node.getValue() ) )
                inputFunctionsList.add( node.getValue() );
            // if the function is the output of the equation, its name is
            // registered
            else if ( node.getFunctionRole() == EFunctionRole.EQUATION_OUTPUT )
                outputName = node.getValue();

        } else if ( node instanceof Number ) {
            // creating number element
            element = document.createElement( NUMBER_TAG_NAME );

            // setting attribute
            element.setAttribute( VALUE_ATTRIBUTE, node.getValue() );
        } else if ( node instanceof Variable ) {
            // creating variable element
            element = document.createElement( VARIABLE_TAG_NAME );

            // setting attribute
            element.setAttribute( VALUE_ATTRIBUTE, node.getValue() );

            // adds the variable to the variable list, if it has not been
            // already met
            if ( !variablesList.contains( node.getValue() ) )
                variablesList.add( node.getValue() );
        }

        return element;
    }

}
