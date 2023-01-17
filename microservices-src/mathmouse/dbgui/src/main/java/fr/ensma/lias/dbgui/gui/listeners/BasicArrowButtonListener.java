package fr.ensma.lias.dbgui.gui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import fr.ensma.lias.dbgui.gui.DbUserInterfaceMain;
import fr.ensma.lias.dockermanagerapi.enumerations.EServiceName;
import fr.ensma.lias.javarabbitmqapi.enumerations.EQueueName;

public class BasicArrowButtonListener implements ActionListener {
    private EQueueName queueName;
    private String     message;

    public BasicArrowButtonListener( EServiceName serviceName, boolean upArrow ) {
        message = serviceName.value();
        if ( upArrow )
            queueName = EQueueName.INCREMENTE_SERVICE_REQUEST;
        else
            queueName = EQueueName.DECREMENTE_SERVICE_REQUEST;
    }

    @Override
    public void actionPerformed( ActionEvent e ) {
        try {
            DbUserInterfaceMain.getSender().publish( queueName, message );
        } catch ( Exception e1 ) {
            e1.printStackTrace();
        }
    }

}
