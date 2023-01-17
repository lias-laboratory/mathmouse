package fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith( Suite.class )
@SuiteClasses( value = {
        fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.nummeth.AllTests.class,
        TestDifferentialEquation2IO.class } )
public class AllTests {

}
