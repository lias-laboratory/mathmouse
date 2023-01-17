package fr.ensma.lias.dbgui.gui.views.renderers;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.table.TableCellRenderer;

import fr.ensma.lias.dbgui.graphicwrapper.models.CellButtonData;

public class JTableArrowsButtonRenderer extends BasicArrowButton implements TableCellRenderer {

    public JTableArrowsButtonRenderer() {
        super( 0 );
        setOpaque( true );
    }

    @Override
    public Component getTableCellRendererComponent( JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column ) {
        if ( value instanceof CellButtonData ) {
            CellButtonData data = (CellButtonData) value;
            this.setDirection( data.getDirection() );
            this.setEnabled( data.isScalable() );
        }
        return this;
    }

}
