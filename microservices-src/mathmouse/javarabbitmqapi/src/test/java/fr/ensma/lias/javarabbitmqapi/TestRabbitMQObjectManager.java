package fr.ensma.lias.javarabbitmqapi;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestRabbitMQObjectManager {
    private static final String           VALUE = "Hello World !";

    private RabbitMQObjectManager<String> obj;

    @Before
    public void initialize() {
        obj = new RabbitMQObjectManager<String>();
    }

    @Test
    public void test() {
        byte[] auxBytes = obj.toBytes( VALUE );
        String result = obj.fromBytes( auxBytes );
        Assert.assertEquals( VALUE, result );
    }

}
