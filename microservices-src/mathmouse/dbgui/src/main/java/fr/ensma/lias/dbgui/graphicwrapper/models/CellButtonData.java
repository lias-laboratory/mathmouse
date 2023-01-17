package fr.ensma.lias.dbgui.graphicwrapper.models;

import javax.swing.SwingConstants;

import fr.ensma.lias.dockermanagerapi.enumerations.EServiceName;
import fr.ensma.lias.javarabbitmqapi.enumerations.EQueueName;

public class CellButtonData {
    private EServiceName serviceName;
    private EQueueName   queueName;
    private int          direction;
    private boolean      scalable;

    public CellButtonData( EServiceName serviceName, EQueueName queueName ) {
        this.serviceName = serviceName;
        this.queueName = queueName;
        switch ( queueName ) {
        case DECREMENTE_SERVICE_REQUEST:
            direction = SwingConstants.WEST;
            break;
        case INCREMENTE_SERVICE_REQUEST:
            direction = SwingConstants.EAST;
            break;
        default:
            break;
        }

        switch ( serviceName ) {
        case COMPARATOR:
            scalable = true;
            break;
        case GENERATOR:
            scalable = true;
            break;
        default:
            scalable = false;
            break;
        }
    }

    public EServiceName getServiceName() {
        return serviceName;
    }

    public EQueueName getQueueName() {
        return queueName;
    }

    public int getDirection() {
        return direction;
    }

    public boolean isScalable() {
        return scalable;
    }

}
