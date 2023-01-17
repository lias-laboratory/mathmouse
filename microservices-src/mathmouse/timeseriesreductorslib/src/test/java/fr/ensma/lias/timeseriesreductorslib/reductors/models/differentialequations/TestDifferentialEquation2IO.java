package fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations;

import static org.junit.Assert.assertTrue;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestDifferentialEquation2IO {
    private static final String          XSD_1_0_FILE_PATH         = "src/test/resources/differential-equation-1.0.xsd";
    private static final String          XSD_2_0_FILE_PATH         = "src/test/resources/differential-equation-2.0.xsd";
    private static final String          EQUATION_FILE_PATH        = "src/test/resources/xmls2.0/equation1.xml";
    private static final String          DESTINATION_XML_FILE_PATH = "src/test/resources/xmls2.0/test.xml";

    private static DifferentialEquation2 equation;

    @BeforeClass
    public static void initialize() {
        BasicConfigurator.configure();

        DifferentialEquation2IO.setSchema( XSD_1_0_FILE_PATH );
        equation = DifferentialEquation2IO.XMLRead20FromFile( XSD_2_0_FILE_PATH, EQUATION_FILE_PATH );
    }

    @Test
    public void testXMLRead() {
        Logger.getLogger( getClass() ).debug( equation );
        assertTrue( equation.getEquation().toString().equals( "(((T*y')+y)=(K*u))" ) );
        assertTrue( equation.getParametersSet().getInitialValues().size() == 1 );
        assertTrue( equation.getParametersSet().getInitialValues().keySet().contains( new FunctionKey( "y", 0 ) ) );
        assertTrue( Math
                .abs( equation.getParametersSet().getInitialValues().get( new FunctionKey( "y", 0 ) ) ) <= new Double(
                        10e-20 ) );
        assertTrue( equation.getParametersSet().getMapping().size() == 2 );
        assertTrue( equation.getParametersSet().getMapping().containsKey( "T" ) );
        assertTrue( Math.abs( 25.0 - equation.getParametersSet().getMapping().get( "T" ) ) <= new Double( 10e-20 ) );
        assertTrue( equation.getParametersSet().getMapping().containsKey( "K" ) );
        assertTrue( Math.abs( 2.4 - equation.getParametersSet().getMapping().get( "K" ) ) <= new Double( 10e-20 ) );
        assertTrue( equation.getParametersSet().getInputFunctions().size() == 1 );
        assertTrue( equation.getParametersSet().getInputFunctions().containsKey( new FunctionKey( "u", 0 ) ) );
    }

    @Test
    public void testXMLWrite() {
        DifferentialEquation2IO.setSchema( XSD_2_0_FILE_PATH );
        DifferentialEquation2IO.XMLWrite20InFile( equation, DESTINATION_XML_FILE_PATH );
    }

}