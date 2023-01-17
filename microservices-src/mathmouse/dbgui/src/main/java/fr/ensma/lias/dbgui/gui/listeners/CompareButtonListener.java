package fr.ensma.lias.dbgui.gui.listeners;

import java.awt.event.ActionEvent;

import fr.ensma.lias.javarabbitmqapi.enumerations.EQueueName;

public class CompareButtonListener extends AbstractSendFileButtonListener {

    public CompareButtonListener() {
        super();
        logMessage = "file were not found, compare request were not sent...";
        queueName = EQueueName.COMPARISON_REQUEST_QUEUE_NAME;
    }

    @Override
    public void actionPerformed( ActionEvent arg0 ) {

    }

}
