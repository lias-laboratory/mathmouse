package fr.ensma.lias.dbgui.kernel.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import fr.ensma.lias.dbgui.gui.DbUserInterfaceMain;
import fr.ensma.lias.dbgui.kernel.observers.IJobManagerObserver;
import fr.ensma.lias.dbgui.kernel.observers.IServicesInstancesObservable;
import fr.ensma.lias.dbgui.kernel.observers.IServicesInstancesObserver;
import fr.ensma.lias.dockermanagerapi.enumerations.EServiceName;

public class ServicesList implements IServicesInstancesObservable, IJobManagerObserver {

    private List<IServicesInstancesObserver>    observers;
    private HashMap<EServiceName, ServiceModel> services;
    private int                                 currentlyObservedJobIndex;

    private static ServicesList                 self;

    private ServicesList() {
        services = new HashMap<EServiceName, ServiceModel>();
        observers = new ArrayList<IServicesInstancesObserver>();
        services.put( EServiceName.IHM, new ServiceModel( EServiceName.IHM, 1, false, 1 ) );
        services.put( EServiceName.COMPARATOR, new ServiceModel( EServiceName.COMPARATOR, 1, true, 1 ) );
        services.put( EServiceName.COMPARATOR_MANAGER,
                new ServiceModel( EServiceName.COMPARATOR_MANAGER, 1, false, 1 ) );
        services.put( EServiceName.GENERATOR, new ServiceModel( EServiceName.GENERATOR, 1, true, 1 ) );
        services.put( EServiceName.DB_CORE, new ServiceModel( EServiceName.DB_CORE, 1, false, 1 ) );
        currentlyObservedJobIndex = 0;
        DbUserInterfaceMain.getStartedJobManager().addObserver( this );
    }

    public static ServicesList getInstances() {
        if ( self == null )
            self = new ServicesList();
        return self;
    }

    public void setServicesInstances( String serviceName, int instances ) {
        services.get( EServiceName.fromValue( serviceName ) ).setNumberOfInstances( instances );
        notifyChange( EServiceName.fromValue( serviceName ) );
    }

    public ServiceModel getService( EServiceName service ) {
        return services.get( service );
    }

    public int getCurrentlyObservedJobIndex() {
        return currentlyObservedJobIndex;
    }

    public void setCurrentlyObservedJobIndex( int currentlyObservedJobIndex ) {
        this.currentlyObservedJobIndex = currentlyObservedJobIndex;
    }

    public void putService( ServiceModel service ) {
        services.put( service.getServiceName(), service );
        notifyChange( service.getServiceName() );
    }

    @Override
    public void addObserver( IServicesInstancesObserver observer ) {
        observers.add( observer );
    }

    @Override
    public void removeObserver( IServicesInstancesObserver observer ) {
        observers.remove( observer );
    }

    @Override
    public void removeObserver( int index ) {
        observers.remove( index );
    }

    @Override
    public void notifyChange( EServiceName service ) {
        if ( observers != null && !observers.isEmpty() )
            for ( IServicesInstancesObserver observer : observers ) {
                observer.update( services.get( service ) );
            }
    }

    public Set<EServiceName> keySet() {
        return services.keySet();
    }

    public ServiceModel get( EServiceName key ) {
        return services.get( key );
    }

    @Override
    public void additionUpdate( JobModel object ) {
    }

    @Override
    public void removalUpdate( int index ) {
    }

    @Override
    public void progressUpdate( int index, JobModel element ) {
        if ( index == currentlyObservedJobIndex ) {
            for ( EServiceName key : services.keySet() ) {
                services.get( key ).setNumberOfCurrentJobs( 0 );
            }

            for ( EServiceName service : element.getServices() ) {
                if ( service.equals( EServiceName.COMPARATOR ) ) {
                    services.get( service ).setNumberOfCurrentJobs( element.getRemainingComparisonJobsNumber() );
                } else if ( service.equals( EServiceName.GENERATOR ) ) {
                    services.get( service ).setNumberOfCurrentJobs( element.getRemainingGenerationJobsNumber() );
                } else {
                    services.get( service ).setNumberOfCurrentJobs( 1 );
                }
                notifyChange( service );
            }
        }
    }

    @Override
    public void addResult() {
        // TODO Auto-generated method stub

    }
}
