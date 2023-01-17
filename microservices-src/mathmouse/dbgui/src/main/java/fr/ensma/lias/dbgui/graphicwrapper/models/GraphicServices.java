package fr.ensma.lias.dbgui.graphicwrapper.models;

import fr.ensma.lias.dbgui.gui.views.ControlTableView;
import fr.ensma.lias.dbgui.kernel.models.ServiceModel;
import fr.ensma.lias.dbgui.kernel.models.ServicesList;
import fr.ensma.lias.dbgui.kernel.observers.IServicesInstancesObserver;
import fr.ensma.lias.dockermanagerapi.enumerations.EServiceName;

public class GraphicServices implements IServicesInstancesObserver {
    private static final int NUMBER_OF_SERVICES               = 5;
    private static final int SERVICE_IHM_INDEX                = 0;
    private static final int SERVICE_COMPARATOR_MANAGER_INDEX = 1;
    private static final int SERVICE_COMPARATOR_INDEX         = 2;
    private static final int SERVICE_GENERATOR_INDEX          = 3;
    private static final int SERVICE_DB_CORE_INDEX            = 4;
    private static final int ERROR                            = -1;

    private GraphicService[] servicesTable;
    private ControlTableView view;

    public GraphicServices( ControlTableView view ) {
        this.view = view;
        servicesTable = new GraphicService[NUMBER_OF_SERVICES];
        for ( EServiceName key : ServicesList.getInstances().keySet() ) {
            setService( ServicesList.getInstances().get( key ) );
        }
        ServicesList.getInstances().addObserver( this );
    }

    public GraphicService[] getServicesTable() {
        return servicesTable;
    }

    public void setService( ServiceModel serviceModel ) {
        int serviceIndex;
        switch ( serviceModel.getServiceName() ) {
        case IHM:
            serviceIndex = SERVICE_IHM_INDEX;
            break;
        case COMPARATOR:
            serviceIndex = SERVICE_COMPARATOR_INDEX;
            break;
        case COMPARATOR_MANAGER:
            serviceIndex = SERVICE_COMPARATOR_MANAGER_INDEX;
            break;
        case GENERATOR:
            serviceIndex = SERVICE_GENERATOR_INDEX;
            break;
        case DB_CORE:
            serviceIndex = SERVICE_DB_CORE_INDEX;
            break;
        default:
            serviceIndex = ERROR;
            break;
        }

        if ( serviceIndex != ERROR ) {
            servicesTable[serviceIndex] = new GraphicService();
            servicesTable[serviceIndex].setServices( serviceModel );
        }
    }

    private void setServices() {
        for ( EServiceName service : ServicesList.getInstances().keySet() ) {
            setService( ServicesList.getInstances().get( service ) );
        }
    }

    @Override
    public void update( ServiceModel service ) {
        setServices();
        view.repaint();
    }

}
