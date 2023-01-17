package fr.ensma.lias.dbgui.gui.listeners;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.ensma.lias.dbgui.graphicwrapper.TextFormatter;
import fr.ensma.lias.dbgui.graphicwrapper.models.EquationForm;
import fr.ensma.lias.dbgui.gui.EquationsPane;
import fr.ensma.lias.dbgui.gui.views.EquationTabbedView;
import fr.ensma.lias.dbgui.gui.views.TemporaryEquationTabbedView;
import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.DifferentialEquation2;
import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.DifferentialEquation2IO;

public class UploadButtonListener extends AbstractOpenFileButtonListener {
    private Logger logger;

    public UploadButtonListener( EquationsPane pane ) {
        super( pane );
        logger = LoggerFactory.getLogger( getClass() );
    }

    @Override
    public void actionPerformed( ActionEvent e ) {
        logger.debug( "upload button clicked..." );
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory( new File( System.getProperty( "user.home" ) ) );
        FileNameExtensionFilter xmlFilter = new FileNameExtensionFilter( "xml files (*.xml)", "xml" );
        fileChooser.setFileFilter( xmlFilter );
        int result = fileChooser.showOpenDialog( pane );
        if ( result == JFileChooser.APPROVE_OPTION ) {
            boolean localFile = true;
            File selectedFile = fileChooser.getSelectedFile();
            logger.debug( "Selected file: " + selectedFile.getAbsolutePath() );
            EquationTabbedView view = new EquationTabbedView();
            DifferentialEquation2 equation = DifferentialEquation2IO
                    .XMLRead20FromFile( TextFormatter.XSD_FILE_PATH, selectedFile.getAbsolutePath() );
            view.setFilePath( selectedFile.getAbsolutePath() );
            view.setFlatText( TextFormatter.writeFlatDifferentialEquation( equation ) );
            view.setXMLText( TextFormatter.xmlContent( selectedFile ) );
            view.setFormText( new EquationForm( equation ) );
            TemporaryEquationTabbedView temporaryView = new TemporaryEquationTabbedView( equation.getName(), view );
        }
    }

}
