package fr.ensma.lias.dbgui.gui.listeners;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.ensma.lias.dbgui.gui.CreateJobsPane;
import fr.ensma.lias.dbgui.gui.DbUserInterfaceMain;
import fr.ensma.lias.dbgui.kernel.models.JobModel;

public class OpenButtonListener extends AbstractOpenFileButtonListener {
    private Logger logger;

    public OpenButtonListener( CreateJobsPane pane ) {
        super( pane );
        logger = LoggerFactory.getLogger( getClass() );
    }

    @Override
    public void actionPerformed( ActionEvent e ) {
        logger.debug( "open file button clicked..." );
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory( new File( System.getProperty( "user.home" ) ) );
        FileNameExtensionFilter xmlFilter = new FileNameExtensionFilter( "csv files (*.csv)", "csv" );
        fileChooser.setFileFilter( xmlFilter );
        int result = fileChooser.showOpenDialog( pane );
        if ( result == JFileChooser.APPROVE_OPTION ) {
            boolean localFile = true;
            File selectedFile = fileChooser.getSelectedFile();
            logger.debug( "Selected file: " + selectedFile.getAbsolutePath() );
            JobModel jobModel = new JobModel( selectedFile.getAbsolutePath(), selectedFile.getName() );
            DbUserInterfaceMain.getJobCreator().addJobModel( jobModel );
        }
    }

}
