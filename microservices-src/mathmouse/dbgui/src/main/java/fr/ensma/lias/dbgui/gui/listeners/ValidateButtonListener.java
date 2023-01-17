package fr.ensma.lias.dbgui.gui.listeners;

import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.ensma.lias.dbgui.gui.DbUserInterfaceMain;
import fr.ensma.lias.dbgui.gui.views.EquationTabbedView;
import fr.ensma.lias.dbgui.gui.views.TemporaryEquationTabbedView;
import fr.ensma.lias.javarabbitmqapi.enumerations.EQueueName;

public class ValidateButtonListener extends AbstractSendFileButtonListener {
    private EquationTabbedView          view;
    private TemporaryEquationTabbedView dialog;
    private Logger                      logger;

    public ValidateButtonListener( EquationTabbedView view, TemporaryEquationTabbedView dialog ) {
        super();
        this.view = view;
        this.dialog = dialog;
        queueName = EQueueName.PUT_EQUATION_REQUEST_QUEUE_NAME;
        logMessage = "file were not found, skipped upload instruction...";
        logger = LoggerFactory.getLogger( getClass() );
    }

    @Override
    public void actionPerformed( ActionEvent e ) {
        logger.debug( "Upload operation validated by the user." );
        BufferedReader bf = null;
        try {
            bf = new BufferedReader( new FileReader( new File( view.getFilePath() ) ) );
            String xmlString = bf.readLine();
            logger.debug( "Sending xml content via rabbitmq..." );
            DbUserInterfaceMain.getSender().publish( queueName.value(), xmlString );
            logger.debug( "Sending xml content via rabbitmq... OK." );
        } catch ( Exception e1 ) {
            e1.printStackTrace();
        } finally {
            try {
                if ( bf != null )
                    bf.close();
            } catch ( IOException e1 ) {
                e1.printStackTrace();
            }
            dialog.dispose();
        }
    }

}
