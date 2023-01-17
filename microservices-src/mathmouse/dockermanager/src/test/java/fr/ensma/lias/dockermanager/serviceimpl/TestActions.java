package fr.ensma.lias.dockermanager.serviceimpl;

import org.apache.log4j.BasicConfigurator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import fr.ensma.lias.javarabbitmqapi.RabbitMQBasicSender;

public class TestActions {
    @Mock
    private RabbitMQBasicSender sender;

    @Rule
    public MockitoRule          mockitoRule = MockitoJUnit.rule();

    @Before
    public void initialize() {
        BasicConfigurator.configure();
        Actions.getInstance().setSender( sender );
    }

    @Test
    public void test() {
        Assert.assertTrue( true );
    }

    /*
     * @Test public void testScale() { resetDocker(); EServiceName serviceName =
     * EServiceName.DB_CORE; // starting services Actions.getInstance().startAllServices();
     * Assert.assertEquals( 1, Actions.getInstance().instancesOfService2( serviceName ) ); //
     * scaling service db_api Actions.getInstance().scale( serviceName, 4 ); Assert.assertEquals( 4,
     * Actions.getInstance().instancesOfService2( serviceName ) ); resetDocker(); Mockito.verify(
     * sender ).publish( EQueueName.INSTANCES_OF_SERVICE_UPDATE.value(), serviceName.value() +
     * ESpecialCharacter.SEPARATOR.value() + 4 ); }
     * 
     * @Test public void testStartAllServices() { resetDocker();
     * Actions.getInstance().startAllServices(); Assert.assertEquals( 1,
     * Actions.getInstance().instancesOfService2( EServiceName.RABBITMQ ) ); Assert.assertEquals( 1,
     * Actions.getInstance().instancesOfService2( EServiceName.MMW_DB ) ); Assert.assertEquals( 1,
     * Actions.getInstance().instancesOfService2( EServiceName.DB_CORE ) ); resetDocker(); }
     * 
     * private void resetDocker() { String[] commands = { "./docker-clear.sh" }; BufferedReader
     * reader = null; try { String command = ""; for ( String item : commands ) { command = command
     * + " " + item; } Logger.getLogger( getClass() ).debug( "executing : " + command );
     * ProcessBuilder pb = new ProcessBuilder( commands ); pb.inheritIO(); Process proc =
     * pb.start();
     * 
     * InputStream is = proc.getInputStream();
     * 
     * reader = new BufferedReader( new InputStreamReader( is ) );
     * 
     * proc.waitFor(); } catch ( Exception e ) { e.printStackTrace(); } finally { if ( reader !=
     * null ) try { reader.close(); } catch ( IOException e ) { e.printStackTrace(); } } }
     */
}
