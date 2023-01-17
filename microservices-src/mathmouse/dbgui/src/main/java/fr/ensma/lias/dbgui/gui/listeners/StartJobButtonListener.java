package fr.ensma.lias.dbgui.gui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.ensma.lias.dbgui.gui.DbUserInterfaceMain;
import fr.ensma.lias.dbgui.gui.views.CreateJobsTableView;
import fr.ensma.lias.javarabbitmqapi.enumerations.EQueueName;
import fr.ensma.lias.timeseriesreductorslib.timeseries.TimeSeriesDoubleDouble;
import fr.ensma.lias.timeseriesreductorslib.timeseries.TimeSeriesDoubleDoubleIO;

public class StartJobButtonListener implements ActionListener {
    private CreateJobsTableView view;
    private Logger              logger;

    public StartJobButtonListener( CreateJobsTableView createJobsTableView ) {
        this.view = createJobsTableView;
        logger = LoggerFactory.getLogger( getClass() );
    }

    @Override
    public void actionPerformed( ActionEvent e ) {
        List<Integer> indexesToRemove = new ArrayList<Integer>();
        logger.debug( "job comparison is starting..." );
        DbUserInterfaceMain.start = System.currentTimeMillis();
        for ( int i = 0; i < view.getRowCount(); i++ ) {
            // if the line were selected, the corresponding job will be started
            if ( Boolean.valueOf( view.getValueAt( i, CreateJobsTableView.SELECT_COLUMN_INDEX ).toString() ) ) {
                // the associated series values are read
                TimeSeriesDoubleDouble ts = TimeSeriesDoubleDoubleIO
                        .readFromFile( DbUserInterfaceMain.getJobCreator().getJob( i ).getFilePath() );
                // the hash code is computed from the values
                int hash = ts.hashCode();
                // the job hash attribute is updated to the newly computed hash
                // code
                DbUserInterfaceMain.getJobCreator().getJob( i ).setHash( hash );
                // change status to waiting
                DbUserInterfaceMain.getJobCreator().getJob( i ).progress();
                try {
                    // the comparison request is sent via rabbitmq, having the
                    // series values and hash code in parameters
                    DbUserInterfaceMain.getSender().publish( EQueueName.COMPARISON_REQUEST_QUEUE_NAME.value(),
                            hash + "\n" + TimeSeriesDoubleDoubleIO.write( ts ) );
                    // the index of the newly started job is added to the list
                    // of indexes to remove
                    indexesToRemove.add( i );
                    // the newly started job is moved to the started jobs list
                    DbUserInterfaceMain.getStartedJobManager()
                            .addJobModel( DbUserInterfaceMain.getJobCreator().getJob( i ) );
                } catch ( Exception e1 ) {
                    e1.printStackTrace();
                }
            }
        }

        // the indexes to remove is sorted in ascending order, in order to
        // remove from the end of the list to the top, to avoid inconsistencies
        // due to element upward shifting while deleting.
        Collections.sort( indexesToRemove, Collections.reverseOrder() );

        // removal of the newly created jobs
        for ( int index : indexesToRemove ) {
            DbUserInterfaceMain.getJobCreator().removeJobModel( index );
        }

    }
}
