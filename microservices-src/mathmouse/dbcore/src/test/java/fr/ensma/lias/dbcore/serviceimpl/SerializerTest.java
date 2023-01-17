package fr.ensma.lias.dbcore.serviceimpl;

import org.junit.Test;

import junit.framework.Assert;

public class SerializerTest {

    @Test
    public void serialize() {
        String serial = Serializer.serialize( 120l, "name", "group" );
        Assert.assertTrue( serial.equals( "id;name;group_name\n120;name;group\n" ) );
    }
}
