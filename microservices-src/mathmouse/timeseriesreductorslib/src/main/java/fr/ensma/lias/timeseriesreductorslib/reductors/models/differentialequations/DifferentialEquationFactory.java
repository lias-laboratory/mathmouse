package fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Validator;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.tree.BinaryOperator;
import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.tree.EFunctionRole;
import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.tree.Function;
import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.tree.NodeObject;
import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.tree.Number;
import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.tree.UnaryOperator;
import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.tree.Variable;

/**
 * This class is meant to build differential equation from various sources. The
 * only current implementation supports XML files following the specifications
 * of the resources/differential-equation.xsd file.
 * 
 * @author cyrille ponchateau (cyrille.ponchateau@ensma.fr)
 *
 */
public class DifferentialEquationFactory extends FileIO {
    /**
     * XML tag names
     */
    protected static final String              ROOT_TAG_NAME                       = "differential-equation";
    protected static final String              FORMULA_TAG_NAME                    = "formula";
    protected static final String              BINARY_OPERATOR_TAG_NAME            = "binary-operator";
    protected static final String              UNARY_OPERATOR_TAG_NAME             = "unary-operator";
    protected static final String              FUNCTION_TAG_NAME                   = "function";
    protected static final String              NUMBER_TAG_NAME                     = "number";
    protected static final String              VARIABLE_TAG_NAME                   = "variable";
    protected static final String              PARAMETERS_SETS_TAG_NAME            = "parameters-sets";
    protected static final String              PARAMETERS_SET_TAG_NAME             = "parameters-set";
    protected static final String              STEP_TAG_NAME                       = "step";
    protected static final String              INITIAL_TAG_NAME                    = "initial";
    protected static final String              VARIABLES_VALUES_TAG_NAME           = "variables-values";
    protected static final String              VARIABLE_VALUE_TAG_NAME             = "variable-value";
    protected static final String              INPUT_FUNCTIONS_TAG_NAME            = "input-functions";
    protected static final String              INPUT_FUNCTION_TAG_NAME             = "input-function";

    /**
     * XML atrributes names
     */
    protected static final String              NUMBER_OF_VARIABLES_ATTRIBUTE       = "numberOfVariables";
    protected static final String              NUMBER_OF_INPUT_FUNCTIONS_ATTRIBUTE = "numberOfInputFunctions";
    protected static final String              OUTPUT_NAME_ATTRIBUTE               = "outputName";
    protected static final String              NUMBER_OF_PARAMETERS_SETS_ATTRIBUTE = "numberOfParametersSets";
    protected static final String              VALUE_ATTRIBUTE                     = "value";
    protected static final String              DERIV_ATTRIBUTE                     = "deriv";
    protected static final String              ROLE_ATTRIBUTE                      = "role";
    protected static final String              NAME_ATTRIBUTE                      = "name";
    protected static final String              PATH_ATTRIBUTE                      = "path";

    /**
     * PMML tag names
     */
    protected static final String              PMML_ROOT_TAG_NAME                  = "PMML";
    protected static final String              PMML_HEADER_TAG_NAME                = "Header";
    protected static final String              PMML_APPLICATION_TAG_NAME           = "Application";
    protected static final String              PMML_DATA_DICTIONNARY_TAG_NAME      = "DataDictionary";
    protected static final String              PMML_DATA_FIELD_TAG_NAME            = "DataField";

    /**
     * PMML attributes names
     */
    protected static final String              PMML_VERSION_ATTRIBUTE              = "version";
    protected static final String              PMML_XMLNS_ATTRIBUTE                = "xmlns";
    protected static final String              PMML_NAME_ATTRIBUTE                 = "name";
    protected static final String              PMML_COPYRIGHT_ATTRIBUTE            = "copyright";

    /**
     * PMML data type names
     */
    protected static final String              PMML_CONTINUOUS_DATA_TYPE           = "continuous";

    /**
     * Static instance
     */
    private static DifferentialEquationFactory factory;

    /**
     * Private constructor for singleton pattern purposes
     */
    private DifferentialEquationFactory() {
    }

    /**
     * Instances getters
     */
    public static DifferentialEquationFactory getInstance() {
        if ( factory == null )
            factory = new DifferentialEquationFactory();
        return factory;
    }

    public static DifferentialEquationFactory getInstance( String XSDFilePath ) {
        if ( factory == null )
            factory = new DifferentialEquationFactory();
        factory.setSchema( XSDFilePath );
        return factory;
    }

    /**
     * Reads the XMLFile pointed by the path in parameter, into a differential
     * equation object (DifferentialEquation).
     * 
     * @param XMLFilePath
     * @return the corresponding differential equation, null if an error occured
     * @see fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequation.DifferentialEquation
     */
    public DifferentialEquation XMLRead( String XMLFilePath ) {
        DifferentialEquation equation = null;
        try {
            // loading the file
            File XMLFile = new File( XMLFilePath );

            // parsing file into DOM object
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse( XMLFile );

            // the document is validated before reading
            Validator validator = schema.newValidator();
            validator.validate( new DOMSource( document ) );

            // should not be very useful in our case, but still
            document.getDocumentElement().normalize();

            // reading the formula
            NodeList formulaTagsNodeList = document.getElementsByTagName( FORMULA_TAG_NAME );
            Node formulaNode = null;

            for ( int i = 0; i < formulaTagsNodeList.getLength(); i++ ) {
                Node currentNode = formulaTagsNodeList.item( i );
                if ( currentNode.getNodeType() == Node.ELEMENT_NODE )
                    formulaNode = currentNode;
            }

            NodeObject equationFormula = CreateNodeObject( formulaNode.getChildNodes(), true );

            // reading the parameters set
            NodeList parametersSetTagsNodeList = document.getElementsByTagName( PARAMETERS_SET_TAG_NAME );
            Node parametersSetNode = null;

            for ( int i = 0; i < parametersSetTagsNodeList.getLength(); i++ ) {
                if ( parametersSetTagsNodeList.item( i ).getNodeType() == Node.ELEMENT_NODE )
                    parametersSetNode = parametersSetTagsNodeList.item( i );
            }

            DiffEquaSystem parametersSet = CreateParametersSet( parametersSetNode.getChildNodes() );

            // creating the equation
            equation = new DifferentialEquation( equationFormula, parametersSet, true );
        } catch ( Exception e ) {
            e.printStackTrace();
        }

        return equation;
    }

    /**
     * Reads the parameters set node list, to set the parameters set of the
     * equation.
     * 
     * @param parametersSetNodeList
     * @return
     */
    private DiffEquaSystem CreateParametersSet( NodeList parametersSetNodeList ) {
        DiffEquaSystem parametersSet = new DiffEquaSystem();

        for ( int i = 0; i < parametersSetNodeList.getLength(); i++ ) {
            Node currentDOMNode = parametersSetNodeList.item( i );
            if ( currentDOMNode.getNodeType() == Node.ELEMENT_NODE ) {
                switch ( currentDOMNode.getNodeName() ) {
                case STEP_TAG_NAME:
                    parametersSet.setStep( Double.parseDouble(
                            currentDOMNode.getAttributes().getNamedItem( VALUE_ATTRIBUTE ).getNodeValue() ) );
                    break;
                case INITIAL_TAG_NAME:
                    parametersSet.setAbscissa( Double.parseDouble(
                            currentDOMNode.getAttributes().getNamedItem( VALUE_ATTRIBUTE ).getNodeValue() ) );
                    break;
                case VARIABLES_VALUES_TAG_NAME:
                    NodeList variableValueNodeList = currentDOMNode.getChildNodes();

                    for ( int j = 0; j < variableValueNodeList.getLength(); j++ ) {
                        Node currentVariableValueNode = variableValueNodeList.item( j );
                        if ( currentVariableValueNode.getNodeType() == Node.ELEMENT_NODE ) {
                            parametersSet.getMapping()
                                    .put( currentVariableValueNode.getAttributes().getNamedItem( NAME_ATTRIBUTE )
                                            .getNodeValue(),
                                            currentVariableValueNode.getAttributes().getNamedItem( VALUE_ATTRIBUTE )
                                                    .getNodeValue() );
                        }
                    }
                    break;
                case INPUT_FUNCTIONS_TAG_NAME:
                    NodeList inputFunctionNodeList = currentDOMNode.getChildNodes();

                    for ( int j = 0; j < inputFunctionNodeList.getLength(); j++ ) {
                        Node currentinputFunctionNode = inputFunctionNodeList.item( j );
                        if ( currentinputFunctionNode.getNodeType() == Node.ELEMENT_NODE ) {
                            parametersSet.getInputFunction()
                                    .put( currentinputFunctionNode.getAttributes().getNamedItem( NAME_ATTRIBUTE )
                                            .getNodeValue(),
                                            currentinputFunctionNode.getAttributes().getNamedItem( PATH_ATTRIBUTE )
                                                    .getNodeValue() );
                        }
                    }
                    break;
                }
            }
        }

        return parametersSet;
    }

    /**
     * Recursively creates an equation tree structure, from the node list given
     * in parameters.
     * 
     * @param DOMnodeList,
     *            the list of nodes
     * @param isLeftChild,
     *            true if the NodeObject that the function have to create is the
     *            left child of its parent node, false otherwise.<br/>
     *            REMARK:
     *            <ul>
     *            <li>The root is considered as the left child of a virtual
     *            super parent node.</li>
     *            <li>If a node has only one child, it is considered as its left
     *            child.</li>
     *            </ul>
     * @return
     * @see fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequation.tree.NodeObject
     */
    private NodeObject CreateNodeObject( NodeList DOMnodeList, boolean isLeftChild ) {
        NodeObject nodeObject = null;
        boolean wait = !isLeftChild;

        // go through the node list
        for ( int i = 0; i < DOMnodeList.getLength(); i++ ) {
            // if the node is an element type node
            if ( DOMnodeList.item( i ).getNodeType() == Node.ELEMENT_NODE ) {
                // if the node is supposed to be the left child, it can set
                // itself right away
                if ( !wait ) {
                    // in case there is another node in the list, it has to be
                    // ignored.
                    wait = !wait;
                    // If current node is a number or a variable, the node sets
                    // itself and the recursion stops.
                    // If it is an operator (unary or binary) the node sets
                    // itself and then its children, recursively.
                    // If the node is function without child, the node sets
                    // itself and the recursion
                    // stops.
                    // If the node is a function with a child, the node sets
                    // itself, then its child, recursively.
                    Node currentDOMNode = DOMnodeList.item( i );
                    if ( currentDOMNode.getNodeName().equals( NUMBER_TAG_NAME ) ) {
                        // setting number node, recursion stops
                        nodeObject = new Number();
                        nodeObject
                                .setValue(
                                        currentDOMNode.getAttributes().getNamedItem( VALUE_ATTRIBUTE ).getNodeValue() );
                    } else if ( currentDOMNode.getNodeName().equals( VARIABLE_TAG_NAME ) ) {
                        // setting variable node, recursion stops
                        nodeObject = new Variable();
                        nodeObject
                                .setValue(
                                        currentDOMNode.getAttributes().getNamedItem( VALUE_ATTRIBUTE ).getNodeValue() );
                    } else if ( currentDOMNode.getNodeName().equals( UNARY_OPERATOR_TAG_NAME ) ) {
                        // setting unary operator
                        nodeObject = new UnaryOperator();
                        nodeObject
                                .setValue(
                                        currentDOMNode.getAttributes().getNamedItem( VALUE_ATTRIBUTE ).getNodeValue() );
                        // setting left child (isLeftChild = true), recursion
                        // continues
                        nodeObject.setLeftChild( CreateNodeObject( currentDOMNode.getChildNodes(), true ) );
                    } else if ( currentDOMNode.getNodeName().equals( FUNCTION_TAG_NAME ) ) {
                        // setting function node
                        nodeObject = new Function();
                        nodeObject.setFunctionRole( EFunctionRole.getFunctionRole(
                                currentDOMNode.getAttributes().getNamedItem( ROLE_ATTRIBUTE ).getNodeValue() ) );
                        nodeObject.setValue(
                                currentDOMNode.getAttributes().getNamedItem( VALUE_ATTRIBUTE ).getNodeValue() );
                        nodeObject.setDeriv( Integer.parseInt(
                                currentDOMNode.getAttributes().getNamedItem( DERIV_ATTRIBUTE ).getNodeValue() ) );

                        // checks if the function has some child (if the node
                        // list is not null and contains a Node.ELEMENT_NODE
                        // type node)
                        if ( hasElementTypeNode( currentDOMNode.getChildNodes() ) )
                            // setting child (left child), recursion continues
                            nodeObject.setLeftChild( CreateNodeObject( currentDOMNode.getChildNodes(), true ) );

                        // if no child to set, recursion stops
                    } else if ( currentDOMNode.getNodeName().equals( BINARY_OPERATOR_TAG_NAME ) ) {
                        // setting binary node
                        nodeObject = new BinaryOperator();
                        nodeObject.setValue(
                                currentDOMNode.getAttributes().getNamedItem( VALUE_ATTRIBUTE ).getNodeValue() );
                        // setting left child, recursion continues on left
                        // branch
                        nodeObject.setLeftChild( CreateNodeObject( currentDOMNode.getChildNodes(), true ) );
                        // setting right child, recursion continues on right
                        // branch
                        nodeObject.setRightChild( CreateNodeObject( currentDOMNode.getChildNodes(), false ) );
                    }
                    // if the node is not the left child, then the list should
                    // contains two element type nodes, one for left child and
                    // the other for right child. The current node needs to wait
                    // the second right child before setting itself.
                } else
                    wait = !wait;
            }
        }

        return nodeObject;
    }

    /**
     * Check if the NodeList in parameter contains a node, that is an instance
     * of the Element class (element type node).
     * 
     * @param nodeList
     * @return true, if the list contains an element type node, false otherwise
     */
    private boolean hasElementTypeNode( NodeList nodeList ) {
        // false if nodeList is null
        if ( nodeList == null )
            return false;

        // go through the list to look for a Node.ELEMENT_NODE type node
        for ( int i = 0; i < nodeList.getLength(); i++ ) {
            if ( nodeList.item( i ).getNodeType() == Node.ELEMENT_NODE )
                return true;
        }

        // if nothing found, then false
        return false;
    }
}
