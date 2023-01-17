package fr.ensma.lias.dbgui.gui;

import java.awt.BorderLayout;

import javax.swing.JScrollPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import fr.ensma.lias.dbgui.graphicwrapper.models.JobNode;
import fr.ensma.lias.dbgui.gui.views.ControlTableView;
import fr.ensma.lias.dbgui.gui.views.JobsListView;
import fr.ensma.lias.dbgui.kernel.models.ServicesList;

public class ControlPanel extends AbstractPane implements TreeSelectionListener {

    private ControlTableView controlView;
    private JobsListView     jobsListView;

    public ControlPanel() {
        super();

        controlView = new ControlTableView();
        JScrollPane controlViewScrollPane = new JScrollPane( controlView );

        jobsListView = new JobsListView();
        jobsListView.addTreeSelectionListener( this );
        JScrollPane jobsListViewScrollPane = new JScrollPane( jobsListView );
        jobsListViewScrollPane.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS );
        jobsListViewScrollPane.setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS );
        jobsListViewScrollPane.setVisible( true );

        add( controlViewScrollPane, BorderLayout.CENTER );
        add( jobsListViewScrollPane, BorderLayout.EAST );
    }

    @Override
    public void valueChanged( TreeSelectionEvent e ) {
        if ( e.getPath().getPathCount() == 2 ) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.getPath().getLastPathComponent();
            JobNode jobNode = (JobNode) node.getUserObject();
            ServicesList.getInstances()
                    .setCurrentlyObservedJobIndex( jobNode.getJobIndex() );
        }
    }
}
