package fr.ensma.lias.dbgui.kernel.observers;

import fr.ensma.lias.dockermanagerapi.enumerations.EServiceName;

public interface IServicesInstancesObservable {

    void addObserver( IServicesInstancesObserver observer );

    void removeObserver( IServicesInstancesObserver observer );

    void removeObserver( int index );

    void notifyChange( EServiceName service );
}
