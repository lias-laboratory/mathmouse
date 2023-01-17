package fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations;

import java.io.File;

import javax.xml.XMLConstants;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

/**
 * Classes made to read or write XML (or other formatted) files have some common
 * properties listed here.
 * 
 * @author cyrille ponchateau (cyrille.ponchateau@ensma.fr)
 *
 */
public abstract class FileIO {

    /**
     * XML Schema
     */
    protected static Schema schema;

    /**
     * Sets the XML Schema from the path of the .xsd file.
     * 
     * @param XSDFilePath
     */
    public static void setSchema( String XSDFilePath ) {
        SchemaFactory factory = SchemaFactory.newInstance( XMLConstants.W3C_XML_SCHEMA_NS_URI );
        try {
            schema = factory.newSchema( new File( XSDFilePath ) );
        } catch ( SAXException e ) {
            e.printStackTrace();
        }
    }

    public static Schema getSchema() {
        return schema;
    }

    public static void setSchema( Schema newschema ) {
        schema = newschema;
    }

}
