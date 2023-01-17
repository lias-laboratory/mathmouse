package fr.ensma.lias.comparator.comparatorservice;

import org.apache.log4j.BasicConfigurator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import fr.ensma.lias.javarabbitmqapi.RabbitMQBasicSender;

public class ActionsTest {
    @Mock
    private RabbitMQBasicSender sender;

    @Rule
    public MockitoRule          mockitoRule = MockitoJUnit.rule();

    @Before
    public void initialize() {
        BasicConfigurator.configure();
        Actions.initialize( sender );
    }

    @Test
    public void testExecuteComparison() {
        Assert.assertTrue( true );
    }
}
