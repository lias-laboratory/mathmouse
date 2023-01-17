package fr.ensma.lias.dbgui.gui.views;

import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import fr.ensma.lias.dbgui.gui.DbUserInterfaceMain;
import fr.ensma.lias.dbgui.gui.workers.JobsListViewReferesher;
import fr.ensma.lias.dbgui.kernel.models.JobModel;
import fr.ensma.lias.dbgui.kernel.observers.IJobManagerObserver;

public class JobsListView extends JTree implements IJobManagerObserver {
    private DefaultMutableTreeNode tree;

    public JobsListView() {
        super();
        tree = new DefaultMutableTreeNode( "Jobs List" );
        this.getSelectionModel().setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
        this.setModel( new DefaultTreeModel( tree ) );
        DbUserInterfaceMain.getStartedJobManager().addObserver( this );
    }

    public DefaultMutableTreeNode getTree() {
        return tree;
    }

    public void setTree( DefaultMutableTreeNode tree ) {
        this.tree = tree;
    }

    @Override
    public void additionUpdate( JobModel object ) {
        JobsListViewReferesher refresher = new JobsListViewReferesher( this );
        refresher.execute();
    }

    @Override
    public void removalUpdate( int index ) {
        JobsListViewReferesher refresher = new JobsListViewReferesher( this );
        refresher.execute();
    }

    @Override
    public void progressUpdate( int index, JobModel element ) {
        JobsListViewReferesher refresher = new JobsListViewReferesher( this );
        refresher.execute();
    }

    @Override
    public void addResult() {
        // TODO Auto-generated method stub

    }
}
