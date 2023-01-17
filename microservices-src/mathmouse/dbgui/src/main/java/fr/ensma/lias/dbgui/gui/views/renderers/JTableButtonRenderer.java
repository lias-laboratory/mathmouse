package fr.ensma.lias.dbgui.gui.views.renderers;

import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

// why should I extends JButton ?
// The class extends JButton, in order to return itself as a button, in the method getTableCellRendererComponent.
// It allows to put a simple string (the button label) in the row vector and the button will build itself.
// No need to declare a button for each row, the table is "smart" enough to make it, which make the new row adding more simple.
public class JTableButtonRenderer extends JButton implements TableCellRenderer {

    public JTableButtonRenderer() {
        // does not seem to change anything when this line is taken off, but all
        // tutorials I have seen used this. Without any explanation tough.
        setOpaque( true );
    }

    @Override
    public Component getTableCellRendererComponent( JTable table, Object obj, boolean isSelected, boolean hasFocus,
            int row, int column ) {
        setText( ( obj == null ) ? "" : obj.toString() );
        return this;
    }
}
