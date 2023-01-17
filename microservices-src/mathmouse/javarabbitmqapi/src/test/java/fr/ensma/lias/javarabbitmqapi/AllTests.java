package fr.ensma.lias.javarabbitmqapi;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith( Suite.class )
@SuiteClasses( value = { TestRabbitMQObjectManager.class } )
public class AllTests {

}
