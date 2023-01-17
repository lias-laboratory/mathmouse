package fr.ensma.lias.dbgui.gui.views;

import javax.swing.JTable;
import javax.swing.JTextField;

import fr.ensma.lias.dbgui.graphicwrapper.models.CellButtonData;
import fr.ensma.lias.dbgui.graphicwrapper.models.DockerControlTableModel;
import fr.ensma.lias.dbgui.graphicwrapper.models.GraphicServices;
import fr.ensma.lias.dbgui.gui.views.editors.JTableArrowsButtonEditor;
import fr.ensma.lias.dbgui.gui.views.renderers.JTableArrowsButtonRenderer;
import fr.ensma.lias.dbgui.gui.views.renderers.JTableServicesInstancesRenderer;
import fr.ensma.lias.dbgui.kernel.models.ServiceModel;
import fr.ensma.lias.dbgui.kernel.observers.IServicesInstancesObserver;
import fr.ensma.lias.javarabbitmqapi.enumerations.EQueueName;

public class ControlTableView extends JTable implements IServicesInstancesObserver {
    private DockerControlTableModel model;
    private GraphicServices         gServices;

    public ControlTableView() {
        model = new DockerControlTableModel();

        setModel( model );

        getColumn( DockerControlTableModel.CONTROL_BUTTON_PLUS_COLUMN )
                .setCellRenderer( new JTableArrowsButtonRenderer() );
        getColumn( DockerControlTableModel.CONTROL_BUTTON_PLUS_COLUMN )
                .setCellEditor( new JTableArrowsButtonEditor( new JTextField() ) );
        getColumn( DockerControlTableModel.CONTROL_BUTTON_MINUS_COLUMN )
                .setCellRenderer( new JTableArrowsButtonRenderer() );
        getColumn( DockerControlTableModel.CONTROL_BUTTON_MINUS_COLUMN )
                .setCellEditor( new JTableArrowsButtonEditor( new JTextField() ) );
        getColumn( DockerControlTableModel.MAIN_FIELD_COLUMN ).setCellRenderer( new JTableServicesInstancesRenderer() );

        setRowHeight( 80 );

        gServices = new GraphicServices( this );

        for ( int i = 0; i < gServices.getServicesTable().length; i++ ) {
            // Object[] data = { gServices.getServicesTable()[i].getServiceName().value(),
            // new CellButtonData( gServices.getServicesTable()[i].getServiceName(),
            // EQueueName.INCREMENTE_SERVICE_REQUEST ),
            // new CellButtonData( gServices.getServicesTable()[i].getServiceName(),
            // EQueueName.DECREMENTE_SERVICE_REQUEST ),
            // gServices.getServicesTable()[i].getInstances() };
            Object[] data = { "test", "test", "test", "test" };
            model.addRow( data );
        }
    }

    @Override
    public void repaint() {
        super.repaint();

        if ( gServices != null )
            for ( int i = gServices.getServicesTable().length - 1; i >= 0; i-- ) {
                model.removeRow( i );
            }

        if ( gServices != null )
            for ( int i = 0; i < gServices.getServicesTable().length; i++ ) {
                Object[] data = { gServices.getServicesTable()[i].getServiceName().value(),
                        new CellButtonData( gServices.getServicesTable()[i].getServiceName(),
                                EQueueName.DECREMENTE_SERVICE_REQUEST ),
                        new CellButtonData( gServices.getServicesTable()[i].getServiceName(),
                                EQueueName.INCREMENTE_SERVICE_REQUEST ),
                        gServices.getServicesTable()[i].getInstances() };
                model.addRow( data );
            }
    }

    @Override
    public void update( ServiceModel service ) {
        this.updateUI();
    }
}
