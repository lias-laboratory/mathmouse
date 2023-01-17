package fr.ensma.lias.dbgui.gui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CancelButtonListener implements ActionListener {
    private JDialog dialog;
    private Logger  logger;

    public CancelButtonListener( JDialog dialog ) {
        this.dialog = dialog;
        logger = LoggerFactory.getLogger( getClass() );
    }

    @Override
    public void actionPerformed( ActionEvent e ) {
        logger.debug( "Upload operation canceled by the user." );
        dialog.dispose();
    }

}
