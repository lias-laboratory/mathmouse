package fr.ensma.lias.dbgui.gui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import fr.ensma.lias.dbgui.gui.DbUserInterfaceMain;
import fr.ensma.lias.dbgui.gui.views.CreateJobsTableView;

public class DeleteJobModelButtonListener implements ActionListener {
    private CreateJobsTableView view;

    public DeleteJobModelButtonListener( CreateJobsTableView createJobsTableView ) {
        this.view = createJobsTableView;
    }

    @Override
    public void actionPerformed( ActionEvent e ) {
        List<Integer> indexesToRemove = new ArrayList<Integer>();
        for ( int i = 0; i < view.getRowCount(); i++ ) {
            // if the line were selected, the corresponding job will be started
            if ( Boolean.valueOf( view.getValueAt( i, CreateJobsTableView.SELECT_COLUMN_INDEX ).toString() ) ) {
                DbUserInterfaceMain.getJobCreator().removeJobModel( i );
            }
        }
    }

}
