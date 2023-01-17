package fr.ensma.lias.timeseriesreductorslib.reductors.segmentors.plr.interpolation;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import fr.ensma.lias.timeseriesreductorslib.reductors.segmentors.plr.interpolation.segalgs.TPLRInterpolateBU;
import fr.ensma.lias.timeseriesreductorslib.reductors.segmentors.plr.interpolation.segalgs.TPLRInterpolateSW;
import fr.ensma.lias.timeseriesreductorslib.reductors.segmentors.plr.interpolation.segalgs.TPLRInterpolateTD;

@RunWith( Suite.class )
@SuiteClasses( value = { TestPLRInterpolateSegment.class, TPLRInterpolateSW.class,
        TPLRInterpolateBU.class, TPLRInterpolateTD.class } )
public class AllPLRInterpolateTest {

}