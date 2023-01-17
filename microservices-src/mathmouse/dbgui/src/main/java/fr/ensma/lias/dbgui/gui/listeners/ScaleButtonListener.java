package fr.ensma.lias.dbgui.gui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import fr.ensma.lias.dbgui.gui.DbUserInterfaceMain;
import fr.ensma.lias.dbgui.gui.views.editors.JTableArrowsButtonEditor;
import fr.ensma.lias.dockermanagerapi.enumerations.EServiceName;
import fr.ensma.lias.javarabbitmqapi.enumerations.EQueueName;

public class ScaleButtonListener implements ActionListener {
    private EQueueName               queueName;
    private EServiceName             service;
    private JTableArrowsButtonEditor editor;
    private long                     last;
    private long                     now;

    public ScaleButtonListener( boolean up, EServiceName service, JTableArrowsButtonEditor editor ) {
        this.service = service;
        if ( up )
            queueName = EQueueName.INCREMENTE_SERVICE_REQUEST;
        else
            queueName = EQueueName.DECREMENTE_SERVICE_REQUEST;
        this.editor = editor;
        last = 0l;
        now = 2001l;
    }

    @Override
    public void actionPerformed( ActionEvent e ) {
        try {
            now = System.currentTimeMillis();
            if ( Math.abs( now - last ) > 1000 ) {
                System.out.println( getClass() + ", " + queueName + ": button clicked !" );
                DbUserInterfaceMain.getSender().publish( queueName, service.value() );
                editor.fireEditingStopped();
                last = now;
            }
        } catch ( Exception e1 ) {
            e1.printStackTrace();
        }
    }

}
