package fr.ensma.lias.dbgui.kernel.models;

import java.util.ArrayList;
import java.util.List;

import org.testcontainers.shaded.org.glassfish.hk2.utilities.reflection.Logger;

import fr.ensma.lias.dockermanagerapi.enumerations.EServiceName;

/**
 * Represents a service. Allows to represent the service architecture of the application.
 * 
 * @author Cyrille Ponchateau (cyrille.ponchateau@ensma.fr)
 *
 */
public class ServiceModel {
    public static final int MIN = 1;
    /**
     * Name of the service represented by the node
     */
    private EServiceName    serviceName;
    /**
     * Number of instances of the service
     */
    private int             numberOfInstances;
    /**
     * True, if the service is scalable
     */
    private boolean         scalable;
    /**
     * Equal to numberOfInstances, if service is not scalable. Less or equal to numeberOfInstances
     * otherwise. It is the number of instances of the service which are currently doing an
     * operation, meaning the other instances are paused.
     */
    private int             numberOfCurrentJobs;

    public ServiceModel() {
        serviceName = null;
        numberOfInstances = 0;
        scalable = false;
        numberOfCurrentJobs = 0;
    }

    public ServiceModel( EServiceName serviceName, int numberOfInstances, boolean scalable, int workingInstances ) {
        super();
        this.serviceName = serviceName;
        this.numberOfInstances = numberOfInstances;
        this.scalable = scalable;
        numberOfCurrentJobs = 0;
    }

    public void setNumberOfCurrentJobs( int numberOfCurrentJobs ) {
        this.numberOfCurrentJobs = numberOfCurrentJobs;
    }

    public List<Integer> getCurrentJobsNumbersList() {
        ArrayList<Integer> list = new ArrayList<Integer>();

        for ( int i = 0; i < numberOfInstances; i++ ) {
            list.add( 0 );
        }

        if ( numberOfCurrentJobs >= 0 ) {
            if ( numberOfCurrentJobs <= numberOfInstances ) {
                for ( int i = 0; i < numberOfCurrentJobs; i++ ) {
                    list.set( i, 1 );
                }
            } else {
                int mod = numberOfCurrentJobs % numberOfInstances;
                int n = ( numberOfCurrentJobs - mod ) / numberOfInstances;

                for ( int i = 0; i < numberOfInstances; i++ ) {
                    list.set( i, n );
                }

                for ( int i = 0; i < mod; i++ ) {
                    list.set( i, list.get( i ) + 1 );
                }
            }
        }

        return list;
    }

    public int getNumberOfInstances() {
        return numberOfInstances;
    }

    public void setNumberOfInstances( int numberOfInstances ) {
        if ( numberOfInstances < MIN ) {
            Logger.getLogger().debug( "number of instances cannot be set to less than " + MIN
                    + ". Has been automatically reset to " + MIN + "." );
            numberOfInstances = MIN;
        }
        this.numberOfInstances = numberOfInstances;
    }

    public boolean isScalable() {
        return scalable;
    }

    public boolean isDisabled() {
        return ( numberOfInstances < MIN );
    }

    public void setScalable( boolean scalable ) {
        this.scalable = scalable;
    }

    public EServiceName getServiceName() {
        return serviceName;
    }

    public void addInstances() {
        numberOfInstances++;
    }

    public void removeInstances() {
        if ( numberOfInstances > MIN ) {
            numberOfInstances--;
        }
    }
}
