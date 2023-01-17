package fr.ensma.lias.dbgui.graphicwrapper.models;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;

import fr.ensma.lias.dbgui.kernel.models.ServiceModel;
import fr.ensma.lias.dockermanagerapi.enumerations.EServiceName;

public class GraphicService {
    private static final Color WORKING_SERVICE  = Color.GREEN;
    private static final Color INACTIVE_SERVICE = Color.ORANGE;
    private static final Color DISABLED_SERVICE = Color.GRAY;

    /**
     * Name of the service represented by the node
     */
    private EServiceName       serviceName;
    private String             label;
    private List<JButton>      instances;
    private List<String>       numberOfJobsWaitingList;
    private boolean            scalable;

    public GraphicService() {
        serviceName = null;
        instances = new ArrayList<JButton>();
        numberOfJobsWaitingList = new ArrayList<String>();
    }

    public boolean isScalable() {
        return scalable;
    }

    public void setScalable( boolean scalable ) {
        this.scalable = scalable;
    }

    public EServiceName getServiceName() {
        return serviceName;
    }

    public void setServiceName( EServiceName serviceName ) {
        this.serviceName = serviceName;
    }

    public String getLabel() {
        return label;
    }

    public List<JButton> getInstances() {
        return instances;
    }

    public void setInstances( ArrayList<JButton> instances ) {
        this.instances = instances;
    }

    public void setServices( ServiceModel serviceModel ) {
        serviceName = serviceModel.getServiceName();
        scalable = serviceModel.isScalable();
        instances.clear();

        for ( int n : serviceModel.getCurrentJobsNumbersList() ) {
            numberOfJobsWaitingList.add( String.valueOf( n ) );
        }

        switch ( serviceModel.getServiceName() ) {
        case IHM:
            label = "IHM";
            break;
        case COMPARATOR:
            label = "C";
            break;
        case COMPARATOR_MANAGER:
            label = "CM";
            break;
        case DB_CORE:
            label = "BD";
            break;
        case GENERATOR:
            label = "G";
            break;
        default:
            label = "";
            break;
        }

        for ( int i = 0; i < serviceModel.getNumberOfInstances(); i++ ) {
            JButton b = new JButton();
            b.setText( label + ":" + numberOfJobsWaitingList.get( i ) );
            b.setEnabled( false );
            if ( numberOfJobsWaitingList.get( i ).equals( "0" ) )
                b.setBackground( INACTIVE_SERVICE );
            else
                b.setBackground( WORKING_SERVICE );
            instances.add( b );
        }

    }
}
