package fr.ensma.lias.dbgui.gui.views.renderers;

import java.awt.Component;

import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class JTableProgressBarCellRenderer extends JProgressBar implements TableCellRenderer {

    public JTableProgressBarCellRenderer() {
        setMinimum( 0 );
        setMaximum( 100 );
        setValue( 0 );
        setStringPainted( true );
    }

    @Override
    public Component getTableCellRendererComponent( JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column ) {
        if ( value instanceof Float )
            setValue( Math.round( (Float) value ) );
        return this;
    }

}
