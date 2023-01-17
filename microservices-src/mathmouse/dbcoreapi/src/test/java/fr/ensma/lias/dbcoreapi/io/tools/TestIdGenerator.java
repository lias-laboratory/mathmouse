package fr.ensma.lias.dbcoreapi.io.tools;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestIdGenerator {
    private ArrayList<Long> ids;

    private IdGenerator     generator;

    @Before
    public void initialize() {
        ids = new ArrayList<Long>();
        ids.add( 2l );
        ids.add( 3l );
        ids.add( 5l );

        generator = new IdGenerator( ids );
    }

    @Test
    public void testGetValidId() {
        Long newlong = generator.getValidId();
        Assert.assertTrue( 1l == newlong );
        newlong = generator.getValidId();
        Assert.assertTrue( 4l == newlong );
        newlong = generator.getValidId();
        Assert.assertTrue( 6l == newlong );
    }
}
