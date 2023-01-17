package fr.ensma.lias.dbgui.gui.views;

import javax.swing.JTable;
import javax.swing.JTextField;

import fr.ensma.lias.dbgui.graphicwrapper.models.CreateJobsTableModel;
import fr.ensma.lias.dbgui.gui.views.editors.JTableButtonEditor;
import fr.ensma.lias.dbgui.gui.views.editors.JTableValuesButtonEditor;
import fr.ensma.lias.dbgui.gui.views.renderers.JTableButtonRenderer;
import fr.ensma.lias.dbgui.kernel.models.JobModel;
import fr.ensma.lias.dbgui.kernel.observers.IJobManagerObserver;

public class CreateJobsTableView extends JTable implements IJobManagerObserver {
    private static final String   JOB_NAMES_COLUMN        = "Jobs Names";
    private static final String   FILE_COLUMN             = "Files";
    private static final String   PARAMETERS_COLUMN       = "Parameters";
    private static final String   VALUES_COLUMN           = "Values";
    private static final String   SELECT_COLUMN           = "Select";
    public static final int       JOB_NAMES_COLUMN_INDEX  = 0;
    public static final int       FILE_COLUMN_INDEX       = 1;
    public static final int       PARAMETERS_COLUMN_INDEX = 2;
    public static final int       VALUES_COLUMN_INDEX     = 3;
    public static final int       SELECT_COLUMN_INDEX     = 4;
    private static final String[] COLUMNS                 = { JOB_NAMES_COLUMN, FILE_COLUMN, PARAMETERS_COLUMN,
            VALUES_COLUMN, SELECT_COLUMN };
    private static final String   CONFIGURE_BUTTON        = "Configure";
    private static final String   VALUES_BUTTON           = "Values";

    private CreateJobsTableModel  model;

    public CreateJobsTableView() {
        model = new CreateJobsTableModel();
        model.setColumnIdentifiers( COLUMNS );
        setModel( model );

        getColumn( PARAMETERS_COLUMN ).setCellRenderer( new JTableButtonRenderer() );
        getColumn( PARAMETERS_COLUMN ).setCellEditor( new JTableButtonEditor( new JTextField() ) );

        getColumn( VALUES_COLUMN ).setCellRenderer( new JTableButtonRenderer() );
        getColumn( VALUES_COLUMN ).setCellEditor( new JTableValuesButtonEditor() );

        setVisible( true );
    }

    public void addJobModel( JobModel jobModel ) {
        Object[] newRow = { jobModel.getJobsName(), jobModel.getFilePath(), CONFIGURE_BUTTON, VALUES_BUTTON, false };
        model.insertRow( model.getRowCount(), newRow );
    }

    public void removeJobModel( int index ) {
        model.removeRow( index );
    }

    public JobModel getJobModel( int index ) {
        return new JobModel( (String) model.getValueAt( index, FILE_COLUMN_INDEX ),
                (String) model.getValueAt( index, JOB_NAMES_COLUMN_INDEX ) );
    }

    @Override
    public void additionUpdate( JobModel object ) {
        if ( object instanceof JobModel )
            addJobModel( (JobModel) object );
        updateUI();
    }

    @Override
    public void removalUpdate( int index ) {
        removeJobModel( index );
        updateUI();
    }

    @Override
    public void progressUpdate( int index, JobModel element ) {

    }

    @Override
    public void addResult() {
        // TODO Auto-generated method stub

    }

}
