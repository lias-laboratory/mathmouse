package fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.validation.Schema;
import javax.xml.validation.Validator;

import org.apache.log4j.Logger;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.tree.BinaryOperator;
import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.tree.Function;
import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.tree.NodeObject;
import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.tree.Number;
import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.tree.UnaryOperator;
import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.tree.Variable;
import fr.ensma.lias.timeseriesreductorslib.timeseries.TimeSeriesDoubleDouble;
import fr.ensma.lias.timeseriesreductorslib.timeseries.interpolation.EInterpolationFunction;

/**
 * 
 * Implements reading and writing XML files functions, using the XML Schema in resources directory.
 * 
 * @author cyrille ponchateau (ponchateau@ensma.fr)
 *
 */
public class DifferentialEquation2IO extends FileIO {
    /**
     * Logger name
     */
    private static final String   LOGGER_NAME                         = "DifferentialEquation2IO (FileIO)";

    /**
     * XML tag names
     */
    protected static final String ROOT_TAG_NAME                       = "differential-equation";
    protected static final String FORMULA_TAG_NAME                    = "formula";
    protected static final String BINARY_OPERATOR_TAG_NAME            = "binary-operator";
    protected static final String UNARY_OPERATOR_TAG_NAME             = "unary-operator";
    protected static final String FUNCTION_TAG_NAME                   = "function";
    protected static final String NUMBER_TAG_NAME                     = "number";
    protected static final String VARIABLE_TAG_NAME                   = "variable";
    protected static final String PARAMETERS_SETS_TAG_NAME            = "parameters-sets";
    protected static final String PARAMETERS_SET_TAG_NAME             = "parameters-set";
    protected static final String INITIAL_VALUE_TAG_NAME              = "initial-value";
    protected static final String INITIAL_VALUES_TAG_NAME             = "initial-values";
    protected static final String VARIABLES_VALUES_TAG_NAME           = "variables-values";
    protected static final String VARIABLE_VALUE_TAG_NAME             = "variable-value";
    protected static final String INPUT_FUNCTIONS_TAG_NAME            = "input-functions";
    protected static final String INPUT_FUNCTION_TAG_NAME             = "input-function";
    protected static final String TIME_SERIES_TAG_NAME                = "time-series";
    protected static final String TIME_VALUE_TAG_NAME                 = "time-value";

    /**
     * XML attributes names
     */
    protected static final String NUMBER_OF_VARIABLES_ATTRIBUTE       = "numberOfVariables";
    protected static final String NUMBER_OF_INPUT_FUNCTIONS_ATTRIBUTE = "numberOfInputFunctions";
    protected static final String NUMBER_OF_INITIAL_VALUES_ATTRIBUTE  = "numberOfInitialValues";
    protected static final String OUTPUT_NAME_ATTRIBUTE               = "outputName";
    protected static final String NUMBER_OF_PARAMETERS_SETS_ATTRIBUTE = "numberOfParametersSets";
    protected static final String VALUE_ATTRIBUTE                     = "value";
    protected static final String DERIV_ATTRIBUTE                     = "deriv";
    protected static final String ROLE_ATTRIBUTE                      = "role";
    protected static final String NAME_ATTRIBUTE                      = "name";
    protected static final String PATH_ATTRIBUTE                      = "path";
    protected static final String TIME_ATTRIBUTE                      = "time";
    protected static final String INTERPOLATION_METHOD_ATTRIBUTE      = "interpolationMethod";
    protected static final String GROUP_ATTRIBUTE                     = "group";

    /**
     * 
     * 
     * @param XMLFilePath
     * @return
     */
    @Deprecated
    public static DifferentialEquation2 XMLRead10FromFile( String XMLFilePath ) {
        Document document = null;
        try {
            // loading the file
            File XMLFile = new File( XMLFilePath );
            // parsing file into DOM object
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            document = documentBuilder.parse( XMLFile );
        } catch ( SAXException | IOException | ParserConfigurationException e ) {
            e.printStackTrace();
        }
        return XMLRead10( document );
    }

    /**
     * 
     * @param file
     * @return
     */
    @Deprecated
    public static DifferentialEquation2 XMLRead10FromFile( File file ) {
        Document document = null;
        try {
            // parsing file into DOM object
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            document = documentBuilder.parse( file );
        } catch ( SAXException | IOException | ParserConfigurationException e ) {
            e.printStackTrace();
        }
        return XMLRead10( document );
    }

    /**
     * 
     * @param XSDFilePath
     * @param file
     * @return
     */
    @Deprecated
    public static DifferentialEquation2 XMLRead10FromFile( String XSDFilePath, File file ) {
        setSchema( XSDFilePath );
        return XMLRead10FromFile( file );
    }

    /**
     * Reads differential equation 1.0 XML formatted files.
     * 
     * @param XSDFilePath
     * @param XMLFilePath
     * @return
     */
    @Deprecated
    public static DifferentialEquation2 XMLRead10FromFile( String XSDFilePath, String XMLFilePath ) {
        setSchema( XSDFilePath );
        return XMLRead10FromFile( XMLFilePath );
    }

    /**
     * Reads differential equation 1.0 XML formatted files.
     * 
     * @param XMLSchema
     * @param XMLFilePath
     * @return
     */
    @Deprecated
    public static DifferentialEquation2 XMLRead10FromFile( Schema XMLSchema, String XMLFilePath ) {
        setSchema( XMLSchema );
        return XMLRead10FromFile( XMLFilePath );
    }

    /**
     * Reads differential equation 1.0 XML formatted files.
     * 
     * @param document
     * @return
     */
    @Deprecated
    private static DifferentialEquation2 XMLRead10( Document document ) {
        DifferentialEquation2 equation = null;
        try {
            // the document is validated before reading
            Validator validator = schema.newValidator();
            validator.validate( new DOMSource( document ) );
            // should not be very useful in our case, but still
            document.getDocumentElement().normalize();
            // reading the formula
            NodeList binaryOperators = document.getElementsByTagName( BINARY_OPERATOR_TAG_NAME );
            Element root = (Element) binaryOperators.item( 0 );

            NodeObject equationFormula = createNodeObject( root );
            // reading the parameters set
            ParametersSet parametersSet = createParametersSet( document );
            // creating the equation
            equation = new DifferentialEquation2( equationFormula, parametersSet, true );
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        return equation;
    }

    /**
     * Builds an equation tree, from its XML description.
     * 
     * @param element
     * @return
     */
    private static NodeObject createNodeObject( Element element ) {
        NodeObject newNode = null;
        if ( element.getTagName().equals( BINARY_OPERATOR_TAG_NAME ) ) {
            // the current element is a binary-operator element
            newNode = new BinaryOperator();
            // sets operator value
            newNode.setValue( element.getAttribute( VALUE_ATTRIBUTE ) );
            List<Element> children = getChildNodes( element );
            // sets left and right children recursively
            newNode.setLeftChild( createNodeObject( children.get( 0 ) ) );
            newNode.setRightChild( createNodeObject( children.get( 1 ) ) );
            return newNode;
        } else if ( element.getTagName().equals( UNARY_OPERATOR_TAG_NAME ) ) {
            // the current element is an unary operator
            newNode = new UnaryOperator();
            // sets operator value
            newNode.setValue( element.getAttribute( VALUE_ATTRIBUTE ) );
            List<Element> children = getChildNodes( element );
            // sets left child recursively
            newNode.setRightChild( createNodeObject( children.get( 0 ) ) );
            return newNode;
        } else if ( element.getTagName().equals( FUNCTION_TAG_NAME ) ) {
            // the current node is a function
            newNode = new Function();
            // sets deriv and value (name) of the function
            newNode.setDeriv( Integer.parseInt( element.getAttribute( DERIV_ATTRIBUTE ) ) );
            newNode.setValue( element.getAttribute( VALUE_ATTRIBUTE ) );
            List<Element> child = getChildNodes( element );
            // if the function has a child, it is set recursively as well
            if ( !child.isEmpty() ) {
                newNode.setLeftChild( createNodeObject( child.get( 0 ) ) );
            }
            return newNode;
        } else if ( element.getTagName().equals( VARIABLE_TAG_NAME ) ) {
            // the current node is a variable
            newNode = new Variable();
            // sets the value (name) of the variable and stops the recursion
            // (variables have no children)
            newNode.setValue( element.getAttribute( VALUE_ATTRIBUTE ) );
            return newNode;
        } else if ( element.getTagName().equals( NUMBER_TAG_NAME ) ) {
            // the current node is a number
            newNode = new Number();
            // sets the number value and stops the recursion (numbers have no
            // children)
            newNode.setValue( element.getAttribute( VALUE_ATTRIBUTE ) );
            return newNode;
        }
        return newNode;
    }

    /**
     * Select all the element type nodes among the children of the current node
     * 
     * @param element
     * @return
     */
    private static List<Element> getChildNodes( Element element ) {
        // gets the complete nodes list of the current node
        NodeList nodeList = element.getChildNodes();
        // creates an element array list and fills it with only with the element
        // type nodes contained in the nodes list
        ArrayList<Element> elements = new ArrayList<Element>();
        for ( int i = 0; i < nodeList.getLength(); i++ ) {
            if ( nodeList.item( i ).getNodeType() == Node.ELEMENT_NODE )
                elements.add( (Element) nodeList.item( i ) );
        }
        return elements;
    }

    /**
     * Reads the parameters set node list, to set the parameters set of the equation.
     * 
     * @param parametersSetNodeList
     * @return
     */
    private static ParametersSet createParametersSet( Document document ) {
        // initializes the initial values, input functions and variables values
        // maps
        Map<FunctionKey, Double> initialValues = new HashMap<FunctionKey, Double>();
        InputFunctions inputFunctions = new InputFunctions();
        Map<String, Double> variablesValues = new HashMap<String, Double>();
        // get all the initial values tags
        NodeList nodeList = document.getElementsByTagName( INITIAL_VALUE_TAG_NAME );
        for ( int i = 0; i < nodeList.getLength(); i++ ) {
            // goes through the initial values tags list and add new initial
            // value in the initial values map, for each tag of the list
            Element element = (Element) nodeList.item( i );
            initialValues.put( new FunctionKey( element.getAttribute( NAME_ATTRIBUTE ),
                    Integer.parseInt( element.getAttribute( DERIV_ATTRIBUTE ) ) ),
                    Double.parseDouble( element.getAttribute( VALUE_ATTRIBUTE ) ) );
        }
        // idem with input function tags
        nodeList = document.getElementsByTagName( INPUT_FUNCTION_TAG_NAME );
        for ( int i = 0; i < nodeList.getLength(); i++ ) {
            Element element = (Element) nodeList.item( i );
            inputFunctions.put( new FunctionKey( element.getAttribute( NAME_ATTRIBUTE ),
                    Integer.parseInt( element.getAttribute( DERIV_ATTRIBUTE ) ) ),
                    element.getAttribute( PATH_ATTRIBUTE ) );
        }
        // and idem with variable value tags
        nodeList = document.getElementsByTagName( VARIABLE_VALUE_TAG_NAME );
        for ( int i = 0; i < nodeList.getLength(); i++ ) {
            Element element = (Element) nodeList.item( i );
            variablesValues.put( element.getAttribute( NAME_ATTRIBUTE ),
                    Double.parseDouble( element.getAttribute( VALUE_ATTRIBUTE ) ) );
        }
        ParametersSet parametersSet = new ParametersSet( variablesValues, initialValues, inputFunctions );
        return parametersSet;
    }

    /**
     * 
     * @param document
     * @param differentialEquation
     */
    private static void createParametersSet20( Document document,
            DifferentialEquation2 differentialEquation ) {
        // initializes the initial values, input functions and variables values
        // maps
        Map<FunctionKey, Double> initialValues = new HashMap<FunctionKey, Double>();
        Map<FunctionKey, String> inputFunctions = new HashMap<FunctionKey, String>();
        Map<String, Double> variablesValues = new HashMap<String, Double>();
        // get all the initial values tags
        NodeList nodeList = document.getElementsByTagName( INITIAL_VALUE_TAG_NAME );
        for ( int i = 0; i < nodeList.getLength(); i++ ) {
            // goes through the initial values tags list and add new initial
            // value in the initial values map, for each tag of the list
            Element element = (Element) nodeList.item( i );
            differentialEquation.getParametersSet().getInitialValues()
                    .put( new FunctionKey( element.getAttribute( NAME_ATTRIBUTE ),
                            Integer.parseInt( element.getAttribute( DERIV_ATTRIBUTE ) ) ),
                            Double.parseDouble( element.getAttribute( VALUE_ATTRIBUTE ) ) );
        }

        // idem with input function tags
        nodeList = document.getElementsByTagName( INPUT_FUNCTION_TAG_NAME );
        for ( int i = 0; i < nodeList.getLength(); i++ ) {
            Element element = (Element) nodeList.item( i );
            FunctionKey key = new FunctionKey( element.getAttribute( NAME_ATTRIBUTE ),
                    Integer.parseInt( element.getAttribute( DERIV_ATTRIBUTE ) ) );
            differentialEquation.getParametersSet().getInputFunctions().put( key, "" );
            NodeList timeseriesElements = element.getElementsByTagName( TIME_SERIES_TAG_NAME );
            Element timeseriesElement = (Element) timeseriesElements.item( 0 );
            NodeList timeValuesNodes = element.getElementsByTagName( TIME_VALUE_TAG_NAME );
            // reading current input function values
            TimeSeriesDoubleDouble series = new TimeSeriesDoubleDouble();
            for ( int j = 0; j < timeValuesNodes.getLength(); j++ ) {
                Element timeValueElement = (Element) timeValuesNodes.item( j );
                series.put( Double.parseDouble( timeValueElement.getAttribute( TIME_ATTRIBUTE ) ),
                        Double.parseDouble( timeValueElement.getAttribute( VALUE_ATTRIBUTE ) ) );
                differentialEquation.getInputFunctions().put( key, series );
            }
            differentialEquation.getInputFunctions().get( key ).setInterpolationFunction( EInterpolationFunction
                    .getInterpolationFunction( timeseriesElement.getAttribute( INTERPOLATION_METHOD_ATTRIBUTE ) ) );
        }
        // and idem with variable value tags
        nodeList = document.getElementsByTagName( VARIABLE_VALUE_TAG_NAME );
        for ( int i = 0; i < nodeList.getLength(); i++ ) {
            Element element = (Element) nodeList.item( i );
            differentialEquation.getParametersSet().getMapping().put( element.getAttribute( NAME_ATTRIBUTE ),
                    Double.parseDouble( element.getAttribute( VALUE_ATTRIBUTE ) ) );
        }
    }

    /**
     * 
     * Reads an equation from an XML formatted string, which schema is in an XSD File.
     * 
     * @param XSDFilePath
     * @param XMLString
     * @return
     */
    public static DifferentialEquation2 XMLRead20FromString( String XSDFilePath, String XMLString ) {
        setSchema( XSDFilePath );

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db;
            db = dbf.newDocumentBuilder();
            Document doc = db.parse( new ByteArrayInputStream( XMLString.getBytes() ) );
            return XMLRead20( doc );
        } catch ( ParserConfigurationException | SAXException | IOException e ) {
            Logger.getLogger( LOGGER_NAME ).debug( "Problem occured with string: " + XMLString );
            e.printStackTrace();
            return null;
        }
    }

    public static DifferentialEquation2 XMLRead20FromFile( String XSDFilePath, String filePath ) {
        setSchema( XSDFilePath );

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse( new FileInputStream( new File( filePath ) ) );
            return XMLRead20( doc );
        } catch ( ParserConfigurationException | SAXException | IOException e ) {
            Logger.getLogger( LOGGER_NAME ).debug( "Problem occured with file: " + filePath );
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Reads an equation from a DOM Document representing an XML string, which schema is in an XSD
     * File.
     * 
     * @param document
     * @return
     */
    public static DifferentialEquation2 XMLRead20( Document document ) {
        DifferentialEquation2 equation = null;
        try {
            // the document is validated before reading
            Validator validator = schema.newValidator();
            validator.validate( new DOMSource( document ) );
            // should not be very useful in our case, but still
            document.getDocumentElement().normalize();

            NodeList docRootElements = document.getElementsByTagName( ROOT_TAG_NAME );
            Element docRootElement = (Element) docRootElements.item( 0 );
            equation = new DifferentialEquation2();
            equation.setName( docRootElement.getAttribute( NAME_ATTRIBUTE ) );
            equation.setGroup( docRootElement.getAttribute( GROUP_ATTRIBUTE ) );

            // reading the formula
            NodeList binaryOperators = document.getElementsByTagName( BINARY_OPERATOR_TAG_NAME );
            Element root = (Element) binaryOperators.item( 0 );

            NodeObject equationFormula = createNodeObject( root );
            equation.setEquation( equationFormula );

            // reading the parameters set
            createParametersSet20( document, equation );

        } catch ( Exception e ) {
            e.printStackTrace();
        }
        return equation;
    }

    /**
     * 
     * Converts an equation into an XML formatted file.
     * 
     * @param differentialEquation
     * @param destinationXMLFile,
     *            path of the file to write in.
     */
    public static void XMLWrite20InFile( DifferentialEquation2 differentialEquation, String destinationXMLFile ) {
        try {
            // writing into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer;
            transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource( XMLWrite20( differentialEquation ) );
            StreamResult result = new StreamResult( new File( destinationXMLFile ) );
            transformer.transform( source, result );
        } catch ( TransformerException e ) {
            e.printStackTrace();
        }
    }

    /**
     * 
     * Converts an equation into an XML formatted string.
     * 
     * @param differentialEquation
     * @return
     */
    public static String XMLWrite20InString( DifferentialEquation2 differentialEquation ) {
        try {
            // writing into string
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer;
            transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource( XMLWrite20( differentialEquation ) );
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult( writer );
            transformer.transform( source, result );
            return writer.getBuffer().toString().replaceAll( "\n|\r", "" );
        } catch ( TransformerException e ) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Converts an equation into an XML formatted string, which schema is in an XSD file.
     * 
     * @param XSDFilePath
     * @param differentialEquation
     * @return
     */
    public static String XMLWrite20InString( String XSDFilePath, DifferentialEquation2 differentialEquation ) {
        setSchema( XSDFilePath );
        return XMLWrite20InString( differentialEquation );
    }

    /**
     * 
     */
    private static Document XMLWrite20( DifferentialEquation2 differentialEquation ) {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();
            // creating root
            Element rootElement = document.createElement( ROOT_TAG_NAME );
            if ( differentialEquation.getName() != null )
                rootElement.setAttribute( NAME_ATTRIBUTE, differentialEquation.getName() );
            if ( differentialEquation.getGroup() != null )
                rootElement.setAttribute( GROUP_ATTRIBUTE, differentialEquation.getGroup() );
            document.appendChild( rootElement );
            // creating formula tag
            Element formula = document.createElement( FORMULA_TAG_NAME );
            // recursive creation of the elements of the equation
            formula.appendChild( createElement( differentialEquation.getEquation(), document ) );
            // settings attributes of formula
            InputsOutputsCounter ioCounter = new InputsOutputsCounter( differentialEquation );
            formula.setAttribute( NUMBER_OF_INPUT_FUNCTIONS_ATTRIBUTE, String.valueOf( ioCounter.getInputsCount() ) );
            formula.setAttribute( NUMBER_OF_VARIABLES_ATTRIBUTE, String.valueOf( ioCounter.getOutputsCount() ) );
            formula.setAttribute( OUTPUT_NAME_ATTRIBUTE, ioCounter.getOutputs().get( 0 ).getName() );
            // appending formula to root
            rootElement.appendChild( formula );
            // creating parameters sets tag
            Element parametersSetsElement = document.createElement( PARAMETERS_SETS_TAG_NAME );
            parametersSetsElement.setAttribute( NUMBER_OF_PARAMETERS_SETS_ATTRIBUTE,
                    String.valueOf( 1 ) );
            parametersSetsElement.appendChild( createElement( differentialEquation, document ) );
            rootElement.appendChild( parametersSetsElement );
            // validates the files
            Validator validator = schema.newValidator();
            validator.validate( new DOMSource( document ) );
            return document;
        } catch ( Exception e ) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Recursive building of the element structure corresponding to the equation.
     * 
     * @param node
     * @param document
     * @return
     */
    private static Element createElement( NodeObject node, Document document ) {
        Element element = null;
        if ( node instanceof BinaryOperator ) {
            // creating binary operator element
            element = document.createElement( BINARY_OPERATOR_TAG_NAME );
            // setting value attribute
            Attr value = document.createAttribute( VALUE_ATTRIBUTE );
            value.setValue( node.getValue() );
            element.setAttributeNode( value );
            // creating left child element
            element.appendChild( createElement( node.getLeftPart(), document ) );
            // creating right child element
            element.appendChild( createElement( node.getRightPart(), document ) );
        } else if ( node instanceof UnaryOperator ) {
            // creating unary operator element
            element = document.createElement( UNARY_OPERATOR_TAG_NAME );
            // setting value attribute
            element.setAttribute( VALUE_ATTRIBUTE, node.getValue() );
            // creating child
            element.appendChild( createElement( node.getLeftPart(), document ) );
        } else if ( node instanceof Function ) {
            // creating function element
            element = document.createElement( FUNCTION_TAG_NAME );
            // setting attributes
            element.setAttribute( VALUE_ATTRIBUTE, node.getValue() );
            element.setAttribute( DERIV_ATTRIBUTE, String.valueOf( node.getDeriv() ) );
            // check if the function has a child
            if ( node.hasChildren() )
                element.appendChild( createElement( node.getLeftPart(), document ) );
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
        }
        return element;
    }

    /**
     * Creates the element structure for a parameters set (system).
     * 
     * @param parametersSet
     * @param document
     * @return
     */
    private static Element createElement( DifferentialEquation2 equation, Document document ) {
        ParametersSet parametersSet = equation.getParametersSet();
        // create new parameters set element
        Element parametersSetElement = document.createElement( PARAMETERS_SET_TAG_NAME );
        // create initial values element
        Element initialValuesElement = document.createElement( INITIAL_VALUES_TAG_NAME );
        initialValuesElement.setAttribute( NUMBER_OF_INITIAL_VALUES_ATTRIBUTE,
                String.valueOf( parametersSet.getInitialValues().size() ) );
        // going through the initial values list
        for ( FunctionKey key : parametersSet.getInitialValues().keySet() ) {
            // create an initial value element
            Element initialValueElement = document.createElement( INITIAL_VALUE_TAG_NAME );
            initialValueElement.setAttribute( NAME_ATTRIBUTE, key.getName() );
            initialValueElement.setAttribute( DERIV_ATTRIBUTE, String.valueOf( key.getDeriv() ) );
            initialValueElement.setAttribute( VALUE_ATTRIBUTE,
                    String.valueOf( parametersSet.getInitialValues().get( key ) ) );
            initialValuesElement.appendChild( initialValueElement );
        }
        // appending initial values element
        parametersSetElement.appendChild( initialValuesElement );
        // create variables value element
        Element variablesValuesElement = document.createElement( VARIABLES_VALUES_TAG_NAME );
        variablesValuesElement.setAttribute( NUMBER_OF_VARIABLES_ATTRIBUTE,
                String.valueOf( parametersSet.getMapping().size() ) );
        // going through the variable list of the parameters set
        for ( String variable : parametersSet.getMapping().keySet() ) {
            // create a variable value element
            Element variableValueElement = document.createElement( VARIABLE_VALUE_TAG_NAME );
            variableValueElement.setAttribute( NAME_ATTRIBUTE, variable );
            variableValueElement.setAttribute( VALUE_ATTRIBUTE,
                    String.valueOf( parametersSet.getMapping().get( variable ) ) );
            variablesValuesElement.appendChild( variableValueElement );
        }
        // appending variables values element to parameters set element
        parametersSetElement.appendChild( variablesValuesElement );
        // create input functions element
        Element inputFunctionsElement = document.createElement( INPUT_FUNCTIONS_TAG_NAME );
        inputFunctionsElement.setAttribute( NUMBER_OF_INPUT_FUNCTIONS_ATTRIBUTE,
                String.valueOf( equation.getInputFunctions().size() ) );
        // going through the input functions list of the parameters set
        for ( FunctionKey key : equation.getInputFunctions().keySet() ) {
            // create input function element
            Element inputFunctionElement = document.createElement( INPUT_FUNCTION_TAG_NAME );
            inputFunctionElement.setAttribute( NAME_ATTRIBUTE, key.getName() );
            inputFunctionElement.setAttribute( DERIV_ATTRIBUTE, String.valueOf( key.getDeriv() ) );
            // creates a time series tag
            Element timeSeriesElement = document.createElement( TIME_SERIES_TAG_NAME );
            timeSeriesElement.setAttribute( INTERPOLATION_METHOD_ATTRIBUTE, equation.getInputFunctions().get( key )
                    .getInterpolationFunction().getInterpolationFunctionName().value() );
            // reads the input series of the equation and copy the value into
            // time value tags, which are then appended to the time series tag
            for ( Double timeKey : equation.getInputFunctions().get( key ).keySet() ) {
                Element timeValueElement = document.createElement( TIME_VALUE_TAG_NAME );
                timeValueElement.setAttribute( TIME_ATTRIBUTE, String.valueOf( timeKey ) );
                timeValueElement.setAttribute( VALUE_ATTRIBUTE,
                        String.valueOf( equation.getInputFunctions().get( key ).get( timeKey ) ) );
                timeSeriesElement.appendChild( timeValueElement );
            }
            inputFunctionElement.appendChild( timeSeriesElement );
            inputFunctionsElement.appendChild( inputFunctionElement );
        }
        // appending input functions element to parameters set element
        parametersSetElement.appendChild( inputFunctionsElement );
        return parametersSetElement;
    }
}
