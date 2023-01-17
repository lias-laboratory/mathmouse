package fr.ensma.lias.dbgui.graphicwrapper.models;

import javax.swing.table.DefaultTableModel;

// have to use custom model
public class CreateJobsTableModel extends DefaultTableModel {
    @Override
    public Class<?> getColumnClass( int columnIndex ) {
        switch ( columnIndex ) {
        case 4:
            return Boolean.class;
        default:
            return String.class;
        }
    }
}
