package fr.ensma.lias.dbgui.graphicwrapper.models;

import javax.swing.table.DefaultTableModel;

import fr.ensma.lias.dbgui.kernel.models.JobModel;

public class StartedJobTableModel extends DefaultTableModel {
    public static final String    JOB_NAMES_COLUMN        = "Jobs Name";
    public static final String    STATUS_COLUMN           = "Status";
    public static final String    PROGRESS_COLUMN         = "Progress";
    public static final String    DETAILS_COLUMN          = "Details";

    public static final int       JOBS_NAMES_COLUMN_INDEX = 0;
    public static final int       STATUS_COLUMN_INDEX     = 1;
    public static final int       PROGRESS_COLUMN_INDEX   = 2;
    public static final int       DETAILS_COLUMN_INDEX    = 3;

    private static final String[] HEADER                  = { JOB_NAMES_COLUMN, STATUS_COLUMN, PROGRESS_COLUMN,
            DETAILS_COLUMN };

    public StartedJobTableModel() {
        setColumnIdentifiers( HEADER );
    }

    @Override
    public void setValueAt( Object aValue, int row, int column ) {
        if ( aValue instanceof JobModel ) {
            JobModel job = (JobModel) aValue;
            super.setValueAt( job.getStatus(), row, STATUS_COLUMN_INDEX );
            super.setValueAt( job.getProgress(), row, PROGRESS_COLUMN_INDEX );
            fireTableCellUpdated( row, column );
        }

    }
}
