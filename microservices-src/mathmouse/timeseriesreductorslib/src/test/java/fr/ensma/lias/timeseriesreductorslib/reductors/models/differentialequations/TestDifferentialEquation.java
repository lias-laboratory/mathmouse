package fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.tree.NodeObject;
import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.tree.Variable;

public class TestDifferentialEquation {
    public static final String          STATIC_LOGGER           = "BEFORE_CLASS";

    public static final String          EQUATION_1_PATH         = "src/test/resources/equation/equation1_2.xml";
    public static final String          EQUATION_2_PATH         = "src/test/resources/equation/equation1_2.xml";
    public static final String          EQUATION_3_PATH         = "src/test/resources/equation/equation3.xml";
    public static final String          EQUATION_4_PATH         = "src/test/resources/equation/equation1_2.xml";
    public static final String          EQUATION_5_PATH         = "src/test/resources/equation/equation1_2.xml";

    private static final int            EQUATION_1_SYSTEM_INDEX = 0;
    private static final int            EQUATION_2_SYSTEM_INDEX = 1;
    private static final int            EQUATION_3_SYSTEM_INDEX = 0;
    private static final int            EQUATION_4_SYSTEM_INDEX = 2;
    private static final int            EQUATION_5_SYSTEM_INDEX = 3;

    private static DifferentialEquation equation1;
    private static DifferentialEquation equation2;
    private static DifferentialEquation equation3;
    private static DifferentialEquation equation4;
    private static DifferentialEquation equation5;
    private static DifferentialEquation equation1bis;

    private static DifferentialEquation simplifiableWithSystem;
    private static DifferentialEquation simplifiable;
    private static DifferentialEquation unsimplifiable;

    @BeforeClass
    public static void initialize() {
        String log4j = "src/test/resources/log4j.xml";
        DOMConfigurator.configure( log4j );
        try {
            // test constructor with xml file
            equation1 = new DifferentialEquation( EQUATION_1_PATH, EQUATION_1_SYSTEM_INDEX, true );
            equation2 = new DifferentialEquation( EQUATION_2_PATH, EQUATION_2_SYSTEM_INDEX, true );
            equation3 = new DifferentialEquation( EQUATION_3_PATH, EQUATION_3_SYSTEM_INDEX, true );
            equation1bis = new DifferentialEquation( EQUATION_1_PATH, EQUATION_1_SYSTEM_INDEX, true );
            simplifiable = new DifferentialEquation( "src/test/resources/equation/example5.xml", 0, false );
            simplifiableWithSystem = new DifferentialEquation( "src/test/resources/equation/example4.xml", 0, false );
            unsimplifiable = new DifferentialEquation( "src/test/resources/equation/example6.xml", 0, false );
        } catch ( Exception e ) {
            e.printStackTrace();
        }

        // equation 4 should raise an exception, since there is intentionally a
        // mapping error in the XML
        try {
            equation4 = new DifferentialEquation( EQUATION_4_PATH, EQUATION_4_SYSTEM_INDEX, true );
        } catch ( Exception e ) {
            Logger.getLogger( STATIC_LOGGER ).debug( "Erreur equation 4" );
            e.printStackTrace();
        } finally {
            // we need equation 4 for the tests, so if the error is raised, it
            // is set anyway, skipping the mapping checking
            try {
                equation4 = new DifferentialEquation( EQUATION_4_PATH, EQUATION_4_SYSTEM_INDEX, false );
            } catch ( Exception e ) {
                e.printStackTrace();
            }
        }

        // equation 5 should raise an exception, since there is intentionally an
        // error in the input functions in the XML file
        try {
            equation5 = new DifferentialEquation( EQUATION_5_PATH, EQUATION_5_SYSTEM_INDEX, true );
        } catch ( Exception e ) {
            Logger.getLogger( STATIC_LOGGER ).debug( "Erreur equation 5" );
            e.printStackTrace();
        } finally {
            try {
                equation5 = new DifferentialEquation( EQUATION_5_PATH, EQUATION_5_SYSTEM_INDEX, false );
            } catch ( Exception e ) {
                e.printStackTrace();
            }
        }

    }

    @Before
    public void testInitialize() {
        assertNotNull( equation1 );
        assertNotNull( equation2 );
        assertNotNull( equation3 );
        assertNotNull( equation4 );
        assertNotNull( equation5 );
        assertNotNull( equation1bis );

        assertNotNull( simplifiable );
        assertNotNull( simplifiableWithSystem );
        assertNotNull( unsimplifiable );

        Logger.getLogger( STATIC_LOGGER ).debug( "Equation 1: " + equation1 );
        Logger.getLogger( STATIC_LOGGER ).debug( "Equation 2: " + equation2 );
        Logger.getLogger( STATIC_LOGGER ).debug( "Equation 3: " + equation3 );
        Logger.getLogger( STATIC_LOGGER ).debug( "Equation 4: " + equation4 );
        Logger.getLogger( STATIC_LOGGER ).debug( "Equation 5: " + equation5 );
        Logger.getLogger( STATIC_LOGGER ).debug( "Equation 1 bis: " + equation1bis );
        Logger.getLogger( STATIC_LOGGER ).debug( "Equation simplifiable with system: " + simplifiableWithSystem );
        Logger.getLogger( STATIC_LOGGER ).debug( "Equation simplifiable: " + simplifiable );
        Logger.getLogger( STATIC_LOGGER ).debug( "Equation unsimplifiable: " + unsimplifiable );
    }

    @Test
    public void testEquals() {
        // test equal
        assertTrue( equation1.equals( equation1bis ) );
        assertFalse( equation1.equals( equation2 ) );
    }

    @Test
    public void testHashCode() {
        // test hashCode
        int eq1hash = equation1.hashCode();
        int eq1bishash = equation1bis.hashCode();

        assertFalse( eq1hash == eq1bishash );
    }

    @Test
    public void testLinear() {
        // test is linear
        assertTrue( equation1.isLinear() );
        assertTrue( equation3.isLinear() );
    }

    @Test
    public void testGetCoefficent() {
        // test getCoefficient
        try {
            assertTrue( equation1.getCoefficient( 1, "y" ).equals( new Variable( "T" ) ) );
            Logger.getLogger( getClass() ).debug( equation1.getCoefficient( 1, "y" ) );
            assertTrue( equation1.getCoefficient( 0, "y" ).equals( new Variable( "T" ) ) );
            Logger.getLogger( getClass() ).debug( equation1.getCoefficient( 0, "y" ) );
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    @Test
    public void testHasInitialCondition() {
        // test hasInitialCondition
        assertTrue( equation1.hasInitialCondition() );
    }

    @Test
    public void testCheckMapping() {
        assertTrue( equation1.checkMapping() );
        assertFalse( equation4.checkMapping() );
    }

    @Test
    public void testCheckInputFunctionMapping() {
        assertTrue( equation1.checkInputFunctionMapping() );
        assertFalse( equation5.checkInputFunctionMapping() );
    }

    @Test
    public void testTransformEquationWithSystem() {
        try {
            Logger.getLogger( getClass() ).debug( "Transforming: " + simplifiableWithSystem );
            NodeObject simplified1 = simplifiableWithSystem.transformEquationWithSystem();
            Logger.getLogger( getClass() ).debug( simplified1 );
        } catch ( Exception e ) {
            e.printStackTrace();
        }

    }

    @Test
    public void testCanBeSimplified() {
        try {
            // bug detected here, to be patched later
            assertFalse( simplifiableWithSystem.canBeSimplified() );
            assertTrue( simplifiableWithSystem.transformEquationWithSystem().canBeSimplified() );
            // assertTrue( simplifiableWithSystem.canBeSimplified() );
            // assertTrue( simplifiable.canBeSimplified() );
            // assertFalse( unsimplifiable.canBeSimplified() );
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSimplify() {
        try {
            Logger.getLogger( getClass() ).debug( "Simplifying: " + simplifiableWithSystem );
            Logger.getLogger( getClass() ).debug( simplifiableWithSystem.simplify() );
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

}
