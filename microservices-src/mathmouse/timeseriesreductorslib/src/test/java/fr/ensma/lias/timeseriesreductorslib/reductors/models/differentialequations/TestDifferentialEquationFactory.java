package fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestDifferentialEquationFactory {
    private static final String XML_FILE_DESTINATION = "src/test/resources/xmls/test.xml";
    // Note: the XSD file is conformed as an XML file (checked in EditiX), but
    // the defined representation may miss some features. It were made in order
    // to propose an improved representation based on the one proposed by Albert
    // Florent. Also, Florent used a DTD file, which is poor in terms of XML
    // format definition.
    private static final String XSD_FILE_PATH        = "src/test/resources/differential-equation.xsd";

    // equation 1
    private static final String EQUATION_PATH        = "src/test/resources/xmls/equation1.xml";

    // equation 2
    private static final String EQUATION2_PATH       = "src/test/resources/xmls/equation2.xml";

    // equation 3
    private static final String EQUATION3_PATH       = "src/test/resources/xmls/equation3.xml";

    @BeforeClass
    public static void initialize() {
        String log4j = "src/test/resources/log4j.xml";
        DOMConfigurator.configure( log4j );
    }

    @Test
    public void testXMLReaderWithEquation1() {
        // given the equation1.xml file from resources/xmls
        try {
            // when reading equation
            DifferentialEquationFactory factory = DifferentialEquationFactory.getInstance( XSD_FILE_PATH );
            DifferentialEquation equation1 = factory.XMLRead( EQUATION_PATH );
            Logger.getLogger( getClass() ).debug( equation1 );

            // then

            assertNotNull( equation1 );
            assertNotNull( equation1.getEquation() );
            assertNotNull( equation1.getSystem() );

            assertTrue( equation1.getSystem().getStep().equals( 1.0 ) );
            assertTrue( equation1.getSystem().getAbscissa().equals( 0.0 ) );

            assertTrue( equation1.getSystem().getMapping().size() == 2 );
            assertTrue( equation1.getSystem().getMapping().containsKey( "T" ) );
            assertTrue( equation1.getSystem().getMapping().containsKey( "K" ) );
            assertTrue( equation1.getSystem().getMapping().get( "T" ).equals( "25" ) );
            assertTrue( equation1.getSystem().getMapping().get( "K" ).equals( "2.4" ) );

            assertTrue( equation1.getSystem().getInputFunction().size() == 1 );
            assertTrue( equation1.getSystem().getInputFunction().containsKey( "u" ) );
            assertTrue(
                    equation1.getSystem().getInputFunction().get( "u" ).equals( "resources\\input\\outDiffEq.txt" ) );
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

}
