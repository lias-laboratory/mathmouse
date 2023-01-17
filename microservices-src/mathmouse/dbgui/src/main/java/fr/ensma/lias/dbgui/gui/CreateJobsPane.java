package fr.ensma.lias.dbgui.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JScrollPane;

import fr.ensma.lias.dbgui.gui.listeners.OpenButtonListener;
import fr.ensma.lias.dbgui.gui.listeners.StartJobButtonListener;
import fr.ensma.lias.dbgui.gui.views.CreateJobsTableView;

/**
 * 
 * @author ponchatc
 *
 */
public class CreateJobsPane extends AbstractPane {
    private JButton             compareButton;
    private JButton             openButton;
    private JButton             deleteButton;
    private CreateJobsTableView createJobsTableView;

    public CreateJobsPane() {
        super();

        createJobsTableView = new CreateJobsTableView();
        JScrollPane jobsTableScrollPane = new JScrollPane( createJobsTableView );

        // builds flow layout button container
        Container buttonContainer = new Container();
        buttonContainer.setLayout( new GridLayout( 1, 4 ) );
        openButton = new JButton( "Open" );
        openButton.addActionListener( new OpenButtonListener( this ) );
        compareButton = new JButton( "Start" );
        compareButton.addActionListener( new StartJobButtonListener( createJobsTableView ) );
        deleteButton = new JButton( "Delete" );
        buttonContainer.add( openButton );
        buttonContainer.add( compareButton );
        buttonContainer.add( deleteButton );
        buttonContainer.add( Box.createGlue() );

        add( jobsTableScrollPane, BorderLayout.CENTER );
        add( buttonContainer, BorderLayout.NORTH );
    }

    public CreateJobsTableView getCreateJobsTableView() {
        return createJobsTableView;
    }

}
