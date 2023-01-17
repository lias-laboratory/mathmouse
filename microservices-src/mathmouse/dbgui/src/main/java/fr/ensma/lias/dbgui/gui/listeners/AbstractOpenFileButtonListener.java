package fr.ensma.lias.dbgui.gui.listeners;

import java.awt.event.ActionListener;

import fr.ensma.lias.dbgui.gui.AbstractPane;

public abstract class AbstractOpenFileButtonListener implements ActionListener {
    protected AbstractPane pane;

    public AbstractOpenFileButtonListener( AbstractPane pane ) {
        this.pane = pane;
    }

}
