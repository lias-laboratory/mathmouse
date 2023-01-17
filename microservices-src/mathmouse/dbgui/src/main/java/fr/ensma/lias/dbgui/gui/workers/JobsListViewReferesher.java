package fr.ensma.lias.dbgui.gui.workers;

import java.util.List;

import javax.swing.SwingWorker;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import fr.ensma.lias.dbgui.graphicwrapper.models.JobNode;
import fr.ensma.lias.dbgui.gui.DbUserInterfaceMain;
import fr.ensma.lias.dbgui.gui.views.JobsListView;

public class JobsListViewReferesher extends SwingWorker<Void, DefaultMutableTreeNode> {
    private DefaultMutableTreeNode tree;
    private JobsListView           view;

    public JobsListViewReferesher( JobsListView view ) {
        this.view = view;
        tree = (DefaultMutableTreeNode) view.getTree();
    }

    @Override
    protected Void doInBackground() throws Exception {
        tree.removeAllChildren();

        for ( int i = 0; i < DbUserInterfaceMain.getStartedJobManager().size(); i++ ) {
            tree.add( new DefaultMutableTreeNode(
                    new JobNode( DbUserInterfaceMain.getStartedJobManager().getJob( i ).getJobsName(), i ) ) );
        }

        publish( tree );
        return null;
    }

    @Override
    protected void process( List<DefaultMutableTreeNode> chunks ) {
        for ( DefaultMutableTreeNode itm : chunks ) {
            view.setModel( new DefaultTreeModel( itm ) );
        }

        view.updateUI();
    }
}
