package fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations;

import static org.junit.Assert.assertTrue;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.junit.BeforeClass;
import org.junit.Test;

import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.tree.BinaryOperator;
import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.tree.EFunctionRole;
import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.tree.Function;
import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.tree.NodeObject;
import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.tree.UnaryOperator;

public class TestTextFileFactory {
    private static final String XML_FILE_DESTINATION           = "src/test/resources/xmls/test.xml";
    // Note: the XSD file is conformed as an XML file (checked in EditiX), but
    // the defined representation may miss some features. It were made in order
    // to propose an improved representation based on the one proposed by Albert
    // Florent. Also, Florent used a DTD file, which is poor in terms of XML
    // format definition.
    private static final String XSD_FILE_PATH                  = "src/test/resources/differential-equation.xsd";

    // equation 1
    private static final String EQUATION_PATH                  = "src/test/resources/equation/equation1_2.xml";
    private static final int    EQUATION_PARAMETERS_SET_INDEX  = 0;
    private static final String OUT1                           = "src/test/resources/xmls/equation1.xml";

    // equation 2
    private static final String EQUATION2_PATH                 = "src/test/resources/equation/equation1_2.xml";
    private static final int    EQUATION2_PARAMETERS_SET_INDEX = 1;
    private static final String OUT2                           = "src/test/resources/xmls/equation2.xml";

    // equation 3
    private static final String EQUATION3_PATH                 = "src/test/resources/equation/equation3.xml";
    private static final int    EQUATION3_PARAMETERS_SET_INDEX = 0;
    private static final String OUT3                           = "src/test/resources/xmls/equation3.xml";

    @BeforeClass
    public static void initialize() {
        String log4j = "src/test/resources/log4j.xml";
        DOMConfigurator.configure( log4j );
    }

    @Test
    public void testXMLWriterWithEquation1() {
        // given the equation 1 example taken from the equation1_2.xml file
        try {
            DifferentialEquation equation1 = new DifferentialEquation( EQUATION_PATH, EQUATION_PARAMETERS_SET_INDEX,
                    true );
            // the patch is made to tag the Functions y and u as input and
            // output, since the new XSD specification requires that information
            // and they are now attributes of the Function.java class. The said
            // attribute is called functionRole, the different roles are listed
            // in the EFunctionRole enumeration, see
            // fr.ensma.lias.timeseriesreductorslib.reductors.models.differentielequations.tree.EFunctionRole.
            patchEquation( equation1.getEquation() );
            Logger.getLogger( getClass() ).debug( equation1 );
            TextFileFactory textFileFactory = TextFileFactory.getInstance( XSD_FILE_PATH );
            textFileFactory.XMLWrite( XML_FILE_DESTINATION, equation1 );
            // then: when performing the writing of the XML file, it is
            // validated with the XSD, if the file is not conformed, it raises
            // an exception. Therefore, if no exception is catched during the
            // execution, the written file is correct to the XSD specification.
            // Therefore, the file is correctly written. It does not makes sure
            // that the file will be correctly read or that the XSD
            // specification needs some improvement though.
            // For the reading, it will be checked when the reading function
            // will be written.
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    @Test
    public void testXMLWriterWithEquation2WithoutPatching() {
        // given the equation 2 example taken form the equation1_2.xml file
        try {
            DifferentialEquation equation2 = new DifferentialEquation( EQUATION2_PATH, EQUATION2_PARAMETERS_SET_INDEX,
                    true );
            // the patch is made to tag the Functions y and u as input and
            // output, since the new XSD specification requires that information
            // and they are now attributes of the Function.java class. The said
            // attribute is called functionRole, the different roles are listed
            // in the EFunctionRole enumeration, see
            // fr.ensma.lias.timeseriesreductorslib.reductors.models.differentielequations.tree.EFunctionRole.
            // patchEquation( equation1.getEquation() );
            TextFileFactory textFileFactory = TextFileFactory.getInstance( XSD_FILE_PATH );
            textFileFactory.XMLWrite( XML_FILE_DESTINATION, equation2 );
            // then: when performing the writing of the XML file, it is
            // validated with the XSD, if the file is not conformed, it raises
            // an exception. Therefore, if no exception is catched during the
            // execution, the written file is correct to the XSD specification.
            // Therefore, the file is correctly written. It does not makes sure
            // that the file will be correctly read or that the XSD
            // specification needs some improvement though.
            // For the reading, it will be checked when the reading function
            // will be written.
        } catch ( Exception e ) {
            // EFunctionRole is not initialized, therefore a
            // NullPointerException is met when trying to read it, to write it
            // in XML file
            assertTrue( e instanceof NullPointerException );
        }
    }

    @Test
    public void testXMLWriterWithEquation2WithPatching() {
        // given the equation 2 example taken form the equation1_2.xml file
        try {
            DifferentialEquation equation2 = new DifferentialEquation( EQUATION2_PATH, EQUATION2_PARAMETERS_SET_INDEX,
                    true );
            // the patch is made to tag the Functions y and u as input and
            // output, since the new XSD specification requires that information
            // and they are now attributes of the Function.java class. The said
            // attribute is called functionRole, the different roles are listed
            // in the EFunctionRole enumeration, see
            // fr.ensma.lias.timeseriesreductorslib.reductors.models.differentielequations.tree.EFunctionRole.
            patchEquation( equation2.getEquation() );
            TextFileFactory textFileFactory = TextFileFactory.getInstance( XSD_FILE_PATH );
            textFileFactory.XMLWrite( XML_FILE_DESTINATION, equation2 );
            // then: when performing the writing of the XML file, it is
            // validated with the XSD, if the file is not conformed, it raises
            // an exception. Therefore, if no exception is catched during the
            // execution, the written file is correct to the XSD specification.
            // Therefore, the file is correctly written. It does not makes sure
            // that the file will be correctly read or that the XSD
            // specification needs some improvement though.
            // For the reading, it will be checked when the reading function
            // will be written.
        } catch ( Exception e ) {

        }
    }

    @Test
    public void export() {
        // this function is meant to export the 3 main equations example
        try {
            // getting equation
            DifferentialEquation equation1 = new DifferentialEquation( EQUATION_PATH, EQUATION_PARAMETERS_SET_INDEX,
                    true );
            DifferentialEquation equation2 = new DifferentialEquation( EQUATION2_PATH, EQUATION2_PARAMETERS_SET_INDEX,
                    true );
            DifferentialEquation equation3 = new DifferentialEquation( EQUATION3_PATH, EQUATION_PARAMETERS_SET_INDEX,
                    true );

            // patching
            patchEquation( equation1.getEquation() );
            patchEquation( equation2.getEquation() );
            patchEquation( equation3.getEquation() );

            TextFileFactory textFileFactory = TextFileFactory.getInstance( XSD_FILE_PATH );

            // writing
            textFileFactory.XMLWrite( OUT1, equation1 );
            textFileFactory.XMLWrite( OUT2, equation2 );
            textFileFactory.XMLWrite( OUT3, equation3 );
        } catch ( Exception e ) {
            e.printStackTrace();
        }

    }

    private void patchEquation( NodeObject node ) {
        if ( node instanceof Function ) {
            if ( node.getValue().equals( "u" ) )
                node.setFunctionRole( EFunctionRole.EQUATION_INPUT );
            else if ( node.getValue().equals( "y" ) )
                node.setFunctionRole( EFunctionRole.EQUATION_OUTPUT );
            if ( node.hasChildren() )
                patchEquation( node.getLeftPart() );
        } else if ( node instanceof BinaryOperator ) {
            patchEquation( node.getLeftPart() );
            patchEquation( node.getRightPart() );
        } else if ( node instanceof UnaryOperator ) {
            patchEquation( node.getLeftPart() );
        }
    }

}
