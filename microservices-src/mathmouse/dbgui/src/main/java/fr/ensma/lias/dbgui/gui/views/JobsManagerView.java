package fr.ensma.lias.dbgui.gui.views;

import javax.swing.JTable;
import javax.swing.JTextField;

import fr.ensma.lias.dbgui.graphicwrapper.models.StartedJobTableModel;
import fr.ensma.lias.dbgui.gui.views.editors.JTableDetailedButtonEditor;
import fr.ensma.lias.dbgui.gui.views.editors.JTableProgressBarEditor;
import fr.ensma.lias.dbgui.gui.views.renderers.JTableButtonRenderer;
import fr.ensma.lias.dbgui.gui.views.renderers.JTableProgressBarCellRenderer;
import fr.ensma.lias.dbgui.kernel.models.JobModel;
import fr.ensma.lias.dbgui.kernel.observers.IJobManagerObserver;

public class JobsManagerView extends JTable implements IJobManagerObserver
{
    private static final String  DETAILS_BUTTON_VALUE = "Details";

    private StartedJobTableModel model;

    public JobsManagerView()
    {
        model = new StartedJobTableModel();
        setModel( model );

        getColumn( StartedJobTableModel.PROGRESS_COLUMN ).setCellRenderer( new JTableProgressBarCellRenderer() );
        getColumn( StartedJobTableModel.PROGRESS_COLUMN )
                .setCellEditor( new JTableProgressBarEditor( new JTextField() ) );

        getColumn( StartedJobTableModel.DETAILS_COLUMN ).setCellRenderer( new JTableButtonRenderer() );
        getColumn( StartedJobTableModel.DETAILS_COLUMN ).setCellEditor( new JTableDetailedButtonEditor() );
    }

    @Override
    public void additionUpdate( JobModel object )
    {
        Object[] newRow = { object.getJobsName(), object.getStatus().value(), 0.0f, DETAILS_BUTTON_VALUE };
        model.insertRow( model.getRowCount(), newRow );
        updateUI();
    }

    @Override
    public void removalUpdate( int index )
    {
        model.removeRow( index );
        updateUI();
    }

    @Override
    public void progressUpdate( int index, JobModel job )
    {
        model.setValueAt( job, index, StartedJobTableModel.PROGRESS_COLUMN_INDEX );
    }

    @Override
    public void addResult()
    {
        // TODO Auto-generated method stub

    }

}
