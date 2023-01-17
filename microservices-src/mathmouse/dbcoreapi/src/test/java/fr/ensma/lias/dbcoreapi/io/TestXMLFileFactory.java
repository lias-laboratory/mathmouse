package fr.ensma.lias.dbcoreapi.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import fr.ensma.lias.dbcoreapi.models.FlatDifferentialEquation;

public class TestXMLFileFactory {
    private static final String             XSD_FILE_PATH  = "src/test/resources/differential-equation-2.0.xsd";
    private static final String             TEST_FILE_PATH = "src/test/resources/xml/test.xml";

    private static String                   xmlString;
    private static XMLFileFactory           factory;
    private static FlatDifferentialEquation flatEquation;

    @Before
    public void initialize() {
        BasicConfigurator.configure();

        factory = XMLFileFactory.getInstance( XSD_FILE_PATH );
        xmlString = new String();
        try {
            File file = new File( TEST_FILE_PATH );
            BufferedReader bf = new BufferedReader( new FileReader( file ) );
            xmlString = xmlString + bf.readLine();
            Logger.getLogger( getClass() ).debug( xmlString );
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }

    @Test
    public void testXMLRead() {
        try {
            flatEquation = factory.XMLread( xmlString, new ArrayList<Long>(),
                    new ArrayList<Long>(), new ArrayList<Long>(), new ArrayList<Long>(), new ArrayList<Long>(),
                    new ArrayList<Long>() );
            Assert.assertEquals( "equation1", flatEquation.getName() );
            Assert.assertEquals( "First Order", flatEquation.getGroup() );
            Assert.assertEquals( 1l, flatEquation.getId() );
            Assert.assertEquals( 1, flatEquation.getOrder() );

            // inputs checking
            Assert.assertEquals( 1, flatEquation.getInputs().size() );
            Long keyinput = 1l;
            Assert.assertEquals( 1l, flatEquation.getInputs().get( keyinput ).getId() );
            Assert.assertEquals( "u", flatEquation.getInputs().get( keyinput ).getName() );
            Assert.assertEquals( 0, flatEquation.getInputs().get( keyinput ).getDeriv() );
            Assert.assertEquals( "1-1-u-0", flatEquation.getInputs().get( keyinput ).getSerialKey() );
            Assert.assertEquals( "none", flatEquation.getInputs().get( keyinput ).getInterpolationFunction().value() );

            // initial values checking
            Assert.assertEquals( 1, flatEquation.getInitialValues().size() );
            Long keyinitial = 1l;
            Assert.assertEquals( 1l, flatEquation.getInitialValues().get( keyinitial ).getId() );
            Assert.assertEquals( "y", flatEquation.getInitialValues().get( keyinitial ).getName() );
            Assert.assertEquals( 0, flatEquation.getInitialValues().get( keyinitial ).getDeriv() );
            Assert.assertTrue(
                    Math.abs( 0.0d - flatEquation.getInitialValues().get( keyinitial ).getInitialValue() ) < 10e-8 );

            // variables values checking
            Assert.assertEquals( 2, flatEquation.getVariables().size() );
            Long keyvariables = 1l;
            Assert.assertEquals( 1l, flatEquation.getVariables().get( keyvariables ).getId() );
            Assert.assertEquals( "T", flatEquation.getVariables().get( keyvariables ).getName() );
            Assert.assertTrue( Math.abs( 25.0d - flatEquation.getVariables().get( keyvariables ).getValue() ) < 10e-8 );
            keyvariables = 2l;
            Assert.assertEquals( 2l, flatEquation.getVariables().get( keyvariables ).getId() );
            Assert.assertEquals( "K", flatEquation.getVariables().get( keyvariables ).getName() );
            Assert.assertTrue( Math.abs( 2.4d - flatEquation.getVariables().get( keyvariables ).getValue() ) < 10e-8 );

            // nodes checking
            Assert.assertEquals( 9, flatEquation.getFormula().size() );
            for ( Long key : flatEquation.getFormula().keySet() ) {
                Assert.assertEquals( flatEquation.getId(), flatEquation.getFormula().get( key ).getEquation() );
            }
        } catch ( ParserConfigurationException | SAXException | IOException e ) {
            e.printStackTrace();
        }
    }

    @Test
    public void testXMLWrite() {
        String writtenString = factory.XMLWrite( flatEquation );
        Logger.getLogger( getClass() ).debug( writtenString );
        // Assert.assertEquals( xmlString, writtenString );
    }
}
