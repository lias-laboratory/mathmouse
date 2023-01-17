package fr.ensma.lias.dbgui.gui.views.editors;

import java.awt.Component;

import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.plaf.basic.BasicArrowButton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.ensma.lias.dbgui.graphicwrapper.models.CellButtonData;
import fr.ensma.lias.dbgui.gui.DbUserInterfaceMain;

public class JTableArrowsButtonEditor extends DefaultCellEditor {
    protected BasicArrowButton button;
    protected boolean          clicked;
    private Logger             logger;

    public JTableArrowsButtonEditor( JTextField textField ) {
        super( textField );
        button = new BasicArrowButton( SwingConstants.SOUTH );
        clicked = false;
        logger = LoggerFactory.getLogger( getClass() );
    }

    @Override
    public Component getTableCellEditorComponent( JTable table, Object value, boolean isSelected, int row,
            int column ) {
        if ( value instanceof CellButtonData ) {
            CellButtonData data = (CellButtonData) value;
            button.setEnabled( data.isScalable() );
            button.setDirection( data.getDirection() );
            if ( data.isScalable() ) {
                try {
                    DbUserInterfaceMain.getSender().publish( data.getQueueName(), data.getServiceName().value() );
                } catch ( Exception e ) {
                    e.printStackTrace();
                }
            }
        }
        clicked = false;
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        return new String( "clicked" );
    }

    @Override
    public boolean stopCellEditing() {
        return super.stopCellEditing();
    }

    @Override
    public void fireEditingStopped() {
        super.fireEditingStopped();
    }

}
