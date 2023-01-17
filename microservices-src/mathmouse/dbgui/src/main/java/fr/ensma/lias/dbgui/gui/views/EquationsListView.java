package fr.ensma.lias.dbgui.gui.views;

import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import fr.ensma.lias.dbgui.graphicwrapper.models.EquationIDData;
import fr.ensma.lias.dbgui.gui.workers.EquationsListViewRefresher;

public class EquationsListView extends JTree {
    private DefaultMutableTreeNode tree;

    public EquationsListView() {
        super();
        tree = new DefaultMutableTreeNode( "Equations" );
        this.getSelectionModel().setSelectionMode( ListSelectionModel.SINGLE_SELECTION );
        this.setModel( new DefaultTreeModel( tree ) );
    }

    public DefaultMutableTreeNode getTree() {
        return tree;
    }

    public void setTree( DefaultMutableTreeNode tree ) {
        this.tree = tree;
    }

    public void addElement( EquationIDData element ) {
        EquationsListViewRefresher refresher = new EquationsListViewRefresher( this, element );
        refresher.execute();
    }

}
