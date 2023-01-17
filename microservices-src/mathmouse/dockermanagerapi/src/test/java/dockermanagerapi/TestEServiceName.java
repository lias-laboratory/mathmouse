package dockermanagerapi;

import org.junit.Test;

import fr.ensma.lias.dockermanagerapi.enumerations.EServiceName;
import junit.framework.Assert;

public class TestEServiceName {

    @Test
    public void value() {
        EServiceName service = EServiceName.COMPARATOR;
        Assert.assertTrue( service.value().equals( "comparator" ) );
    }
}
