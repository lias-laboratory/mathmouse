package fr.ensma.lias.dbcoreapi.io;

/**
 * Classes made to read or write XML (or other formatted) files have some common
 * properties listed here.
 * 
 * @author cyrille ponchateau (cyrille.ponchateau@ensma.fr)
 *
 */
public abstract class FileIOFactory {

    /**
     * PMML tag names
     */
    protected static final String PMML_ROOT_TAG_NAME             = "PMML";
    protected static final String PMML_HEADER_TAG_NAME           = "Header";
    protected static final String PMML_APPLICATION_TAG_NAME      = "Application";
    protected static final String PMML_DATA_DICTIONNARY_TAG_NAME = "DataDictionary";
    protected static final String PMML_DATA_FIELD_TAG_NAME       = "DataField";

    /**
     * PMML attributes names
     */
    protected static final String PMML_VERSION_ATTRIBUTE         = "version";
    protected static final String PMML_XMLNS_ATTRIBUTE           = "xmlns";
    protected static final String PMML_NAME_ATTRIBUTE            = "name";
    protected static final String PMML_COPYRIGHT_ATTRIBUTE       = "copyright";

    /**
     * PMML data type names
     */
    protected static final String PMML_CONTINUOUS_DATA_TYPE      = "continuous";

}
