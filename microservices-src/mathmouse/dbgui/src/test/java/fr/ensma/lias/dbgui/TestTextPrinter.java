package fr.ensma.lias.dbgui;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import fr.ensma.lias.dbgui.graphicwrapper.TextFormatter;
import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.DifferentialEquation2;
import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.FunctionKey;
import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.ParametersSet;
import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.tree.Variable;
import fr.ensma.lias.timeseriesreductorslib.timeseries.TimeSeriesDoubleDouble;
import fr.ensma.lias.timeseriesreductorslib.timeseries.interpolation.EInterpolationFunction;

public class TestTextPrinter {
    private DifferentialEquation2 equation;

    @Before
    public void initialize() {
        equation = new DifferentialEquation2();
        equation.setEquation( new Variable( "pi and phy" ) );
        equation.setId( 1l );
        TimeSeriesDoubleDouble series = new TimeSeriesDoubleDouble();
        series.setInterpolationFunction( EInterpolationFunction.STEP_INTERPOLATION );
        series.put( 1.0, 10.0 );
        Map<FunctionKey, TimeSeriesDoubleDouble> inputseries = new HashMap<FunctionKey, TimeSeriesDoubleDouble>();
        inputseries.put( new FunctionKey( "u", 0 ), series );
        equation.setInputFunctions( inputseries );
        equation.setName( "test equation" );
        ParametersSet paramsSet = new ParametersSet();
        Map<FunctionKey, Double> initialvalues = new HashMap<FunctionKey, Double>();
        initialvalues.put( new FunctionKey( "y", 0 ), 0.0 );
        paramsSet.setInitialValues( initialvalues );
        Map<String, Double> variablesvalues = new HashMap<String, Double>();
        variablesvalues.put( "pi", 3.14159265 );
        variablesvalues.put( "phy", 1.6180339887 );
        paramsSet.setMapping( variablesvalues );
        equation.setParametersSet( paramsSet );
    }

    @Test
    public void test() {
        String flatEq = TextFormatter.writeFlatDifferentialEquation( equation );
        Assert.assertNotNull( flatEq );
        // Assert.assertEquals( flatEq,
        // "Equation 1 : test equation\nFormula : pi and phy\nValues of the variables : \n\tphy =
        // 1.6180339887\n\tpi = 3.14159265\nInitial conditions : \n\ttime origin = 1.0\n\ty(0) =
        // 0.0\n\tInput functions values : \n\t\tu(0): to interpolate with none\n\t\t\t1.0 ->
        // 10.0\n" );
        System.out.println( TextFormatter.writeFlatDifferentialEquation( equation ) );
    }
}
