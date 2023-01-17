package fr.ensma.lias.dbgui.gui.views.editors;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.JTextField;

import fr.ensma.lias.dbgui.gui.views.JobsResultsView;

public class JTableDetailedButtonEditor extends JTableButtonEditor {
    private int row;

    public JTableDetailedButtonEditor() {
        super( new JTextField() );

    }

    @Override
    public Component getTableCellEditorComponent( JTable table, Object value, boolean isSelected, int row,
            int column ) {
        super.getTableCellEditorComponent( table, value, isSelected, row, column );
        this.row = row;
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        if ( clicked ) {
            // action to perform when button was clicked
            // example :
            JobsResultsView view = new JobsResultsView();
            view.setRow( row );
        }
        clicked = false;
        return new String( label );
    }

}
