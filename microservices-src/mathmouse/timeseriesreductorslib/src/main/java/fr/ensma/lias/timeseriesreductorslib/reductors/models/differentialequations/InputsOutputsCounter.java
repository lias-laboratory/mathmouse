package fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.tree.BinaryOperator;
import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.tree.Function;
import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.tree.NodeObject;
import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.tree.UnaryOperator;

/**
 * Object dedicated to input and output functions counting. (Note: may evolve to
 * input and output functions extractor and output uniqueness checker).
 * 
 * @author cyrille ponchateau (ponchateau@ensma.fr)
 *
 */
public class InputsOutputsCounter {
    int               inputsCount;
    int               outputsCount;
    List<FunctionKey> outputs;
    List<FunctionKey> inputs;

    /**
     * Object is built directly from an equation
     * 
     * @param equation
     */
    public InputsOutputsCounter( DifferentialEquation2 equation ) {
        countInputsAndOutputs( equation );
    }

    /**
     * Private function that does the counting of inputs and outputs
     * 
     * @param equation
     */
    private void countInputsAndOutputs( DifferentialEquation2 equation ) {
        inputs = new ArrayList<FunctionKey>();
        outputs = new ArrayList<FunctionKey>();

        // gets the list of all the functions in the equation
        Map<FunctionKey, Integer> functionsList = new HashMap<FunctionKey, Integer>();
        getFunctionsList( equation.getEquation(), functionsList );

        // sorts them out into inputs and outputs, using the input functions
        // list of the parameters set. If the function is in the input functions
        // list, it is an input, otherwise it is an output.
        for ( FunctionKey key : functionsList.keySet() ) {
            if ( equation.getParametersSet().getInputFunctions().containsKey( key ) )
                inputs.add( key );
            else
                outputs.add( key );
        }

        // the input and outputs functions numbers are the sizes of each list
        inputsCount = inputs.size();
        outputsCount = outputs.size();
    }

    /**
     * Recursively gets the list of all the functions (inputs and outputs mixed)
     * 
     * @param node
     * @param functionsList
     */
    private void getFunctionsList( NodeObject node, Map<FunctionKey, Integer> functionsList ) {
        if ( node instanceof Function ) {
            // if the node is a function, it is added to the functions list
            functionsList.put( new FunctionKey( node.getValue(), node.getDeriv() ), 1 );
            if ( node.getLeftPart() != null )
                // if the node has a child, the recursion goes further, the
                // function stops otherwise
                getFunctionsList( node.getLeftPart(), functionsList );
        } else if ( node instanceof UnaryOperator ) {
            // if the node is a unary operator, the recursion goes on the only
            // child of the node
            getFunctionsList( node.getLeftPart(), functionsList );
        } else if ( node instanceof BinaryOperator ) {
            // if the node is a binary operator, the recursion goes further on
            // each children
            getFunctionsList( node.getLeftPart(), functionsList );
            getFunctionsList( node.getRightPart(), functionsList );
        }
        // in case the node is a number or a variable, there nothing to do, the
        // recursion stops
    }

    public int getInputsCount() {
        return inputsCount;
    }

    public int getOutputsCount() {
        return outputsCount;
    }

    public List<FunctionKey> getOutputs() {
        return outputs;
    }

    public List<FunctionKey> getInputs() {
        return inputs;
    }

}
