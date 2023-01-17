package fr.ensma.lias.dbgui.gui.views.renderers;

import java.awt.Component;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class JTableServicesInstancesRenderer extends JComponent implements TableCellRenderer {

    public JTableServicesInstancesRenderer() {
        setOpaque( true );
    }

    @Override
    public Component getTableCellRendererComponent( JTable arg0, Object value, boolean arg2, boolean arg3, int arg4,
            int arg5 ) {
        removeAll();
        if ( value instanceof ArrayList ) {
            ArrayList<JButton> buttons = (ArrayList<JButton>) value;
            setLayout( new GridLayout( 1, buttons.size(), 10, 5 ) );
            for ( JButton button : buttons ) {
                add( button );
            }
        }
        return this;
    }

}
