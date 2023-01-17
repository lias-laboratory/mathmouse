package fr.ensma.lias.dbcoreapi.io;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith( Suite.class )
@SuiteClasses( value = { TestCSVFileFactory.class, TestXMLFileFactory.class } )
public class AllTests {

}
