package fr.ensma.lias.dbgui.gui.views;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;

import fr.ensma.lias.dbgui.gui.listeners.CancelButtonListener;

public class TemporaryTimeSeriesTabbedView extends JDialog {
    private TimeSeriesTabbedView view;
    private JButton              close;

    public TemporaryTimeSeriesTabbedView( TimeSeriesTabbedView view ) {
        setLayout( new BorderLayout() );
        setSize( new Dimension( 600, 400 ) );
        setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
        this.view = view;
        setTitle( "Time Series Values" );

        close = new JButton( "Close" );
        // close and cancel buttons just close the current temporary window,
        // without executing anything more
        close.addActionListener( new CancelButtonListener( this ) );

        Container container = new Container();
        container.setLayout( new GridLayout( 1, 5 ) );
        container.add( Box.createGlue() );
        container.add( Box.createGlue() );
        container.add( Box.createGlue() );
        container.add( close );

        add( view, BorderLayout.CENTER );
        add( container, BorderLayout.SOUTH );
        setVisible( true );
    }

}
