package fr.ensma.lias.dbgui.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Enumeration;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.log4j.Logger;

import fr.ensma.lias.dbgui.graphicwrapper.models.EquationIDData;
import fr.ensma.lias.dbgui.gui.listeners.UploadButtonListener;
import fr.ensma.lias.dbgui.gui.views.EquationTabbedView;
import fr.ensma.lias.dbgui.gui.views.EquationsListView;
import fr.ensma.lias.dbgui.gui.views.GroupFormView;
import fr.ensma.lias.javarabbitmqapi.enumerations.EQueueName;

public class EquationsPane extends AbstractPane implements TreeSelectionListener {
    private JButton            uploadButton;
    private JButton            downloadButton;

    private EquationsListView  equationsListView;
    private EquationTabbedView equationTabbedView;
    private GroupFormView      groupFormView;

    public EquationsPane() {
        setLayout( new BorderLayout() );

        equationsListView = new EquationsListView();
        equationsListView.addTreeSelectionListener( this );
        JScrollPane equationsNamesScrollPane = new JScrollPane( equationsListView );
        equationsNamesScrollPane.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS );
        equationsNamesScrollPane.setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS );
        equationsNamesScrollPane.setVisible( true );
        equationsNamesScrollPane.setMaximumSize( new Dimension( Math.round( (float) this.getWidth() / 10.0f ),
                this.getHeight() ) );

        // builds flow layout button container
        Container buttonContainer = new Container();
        buttonContainer.setLayout( new GridLayout( 1, 4 ) );
        uploadButton = new JButton( "Upload" );
        uploadButton.addActionListener( new UploadButtonListener( this ) );
        buttonContainer.add( uploadButton );
        buttonContainer.add( Box.createGlue() );
        buttonContainer.add( Box.createGlue() );
        buttonContainer.add( Box.createGlue() );

        // text view for equations displaying
        equationTabbedView = new EquationTabbedView();

        // builds principal view container
        add( equationsNamesScrollPane, BorderLayout.WEST );
        add( buttonContainer, BorderLayout.NORTH );
        add( equationTabbedView, BorderLayout.CENTER );
    }

    public EquationsListView getEquationsListView() {
        return equationsListView;
    }

    public EquationTabbedView getEquationTabbedView() {
        return equationTabbedView;
    }

    @Override
    public void valueChanged( TreeSelectionEvent e ) {
        if ( e.getPath().getPathCount() == 3 ) {
            Logger.getLogger( getClass() ).debug( e.getPath() );
            DefaultMutableTreeNode groupNode = null;
            DefaultMutableTreeNode currentNode;
            Enumeration<DefaultMutableTreeNode> children = equationsListView.getTree().children();
            while ( children.hasMoreElements() && groupNode == null ) {
                currentNode = children.nextElement();
                if ( ( (String) currentNode.getUserObject() ).equals( e.getPath().getPathComponent( 1 ).toString() ) )
                    groupNode = currentNode;
            }
            Logger.getLogger( getClass() ).debug( groupNode.getUserObject() );

            EquationIDData selectedItem = null;
            children = groupNode.children();
            while ( children.hasMoreElements() && selectedItem == null ) {
                currentNode = children.nextElement();
                if ( ( (EquationIDData) currentNode.getUserObject() ).getName()
                        .equals( e.getPath().getPathComponent( 2 ).toString() ) ) {
                    selectedItem = (EquationIDData) currentNode.getUserObject();
                }
            }

            try {
                DbUserInterfaceMain.getSender().publish( EQueueName.GET_EQUATIONS_REQUEST_QUEUE_NAME.value(),
                        String.valueOf( selectedItem.getId() ) );
            } catch ( Exception e1 ) {
                e1.printStackTrace();
            }
        } else if ( e.getPath().getPathCount() == 2 ) {
            groupFormView = new GroupFormView();
            remove( equationsListView );
            revalidate();
            repaint();
        }
    }
}
