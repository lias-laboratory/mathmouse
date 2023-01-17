package fr.ensma.lias.dbgui.gui.listeners;

import java.awt.event.ActionListener;

import fr.ensma.lias.javarabbitmqapi.enumerations.EQueueName;

public abstract class AbstractSendFileButtonListener implements ActionListener {
    protected String     logMessage;
    protected EQueueName queueName;

    public AbstractSendFileButtonListener() {
    }

}
