package fr.ensma.lias.dbgui.kernel.observers;

import fr.ensma.lias.dbgui.kernel.models.ServiceModel;

public interface IServicesInstancesObserver {
    void update( ServiceModel service );
}
