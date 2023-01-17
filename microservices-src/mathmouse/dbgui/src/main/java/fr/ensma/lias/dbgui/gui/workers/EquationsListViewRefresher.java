package fr.ensma.lias.dbgui.gui.workers;

import java.util.Enumeration;
import java.util.List;

import javax.swing.SwingWorker;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.ensma.lias.dbgui.graphicwrapper.models.EquationIDData;
import fr.ensma.lias.dbgui.gui.views.EquationsListView;

public class EquationsListViewRefresher extends SwingWorker<Void, DefaultMutableTreeNode> {
    private DefaultMutableTreeNode tree;
    private EquationsListView      view;
    private EquationIDData         data;
    private Logger                 logger;

    public EquationsListViewRefresher( EquationsListView view, EquationIDData data ) {
        this.view = view;
        this.data = data;
        tree = (DefaultMutableTreeNode) view.getTree();
        logger = LoggerFactory.getLogger( getClass() );
    }

    @Override
    protected Void doInBackground() throws Exception {
        Enumeration<DefaultMutableTreeNode> children = tree.children();

        DefaultMutableTreeNode groupNode = null;
        boolean groupFound = false;
        while ( children.hasMoreElements() && !groupFound ) {
            groupNode = children.nextElement();

            if ( ( (String) groupNode.getUserObject() ).equals( data.getGroup() ) ) {
                groupFound = true;
            }
        }

        if ( groupFound ) {
            groupNode.add( new DefaultMutableTreeNode( data ) );
            logger.debug( "Added node " + ( (DefaultMutableTreeNode) groupNode.getLastChild() ).getUserObject()
                    + " to existing group " + (String) groupNode.getUserObject() );
        } else {
            groupNode = new DefaultMutableTreeNode( data.getGroup() );
            groupNode.add( new DefaultMutableTreeNode( data ) );
            tree.add( groupNode );
            logger.debug( "Added new group " + (String) groupNode.getUserObject() + " and added node "
                    + ( (DefaultMutableTreeNode) groupNode.getLastChild() ).getUserObject() );
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
