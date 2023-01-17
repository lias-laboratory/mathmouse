package fr.ensma.lias.dbgui.gui;

import java.awt.BorderLayout;
import java.awt.Container;

public abstract class AbstractPane extends Container {

    public AbstractPane() {
        setLayout( new BorderLayout() );
    }
}
