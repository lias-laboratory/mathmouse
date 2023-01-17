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
import fr.ensma.lias.dbgui.gui.listeners.ValidateButtonListener;

public class TemporaryEquationTabbedView extends JDialog {
    private EquationTabbedView view;
    private String             equationName;
    private JButton            validate;
    private JButton            cancel;

    public TemporaryEquationTabbedView( String equationName, EquationTabbedView view ) {
        setLayout( new BorderLayout() );
        setSize( new Dimension( 600, 400 ) );
        setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
        this.equationName = equationName;
        this.view = view;
        setTitle( equationName );

        validate = new JButton( "Validate" );
        validate.addActionListener( new ValidateButtonListener( view, this ) );
        cancel = new JButton( "Cancel" );
        cancel.addActionListener( new CancelButtonListener( this ) );

        Container container = new Container();
        container.setLayout( new GridLayout( 1, 5 ) );
        container.add( Box.createGlue() );
        container.add( Box.createGlue() );
        container.add( Box.createGlue() );
        container.add( validate );
        container.add( cancel );

        add( view, BorderLayout.CENTER );
        add( container, BorderLayout.SOUTH );
        setVisible( true );
    }

}
