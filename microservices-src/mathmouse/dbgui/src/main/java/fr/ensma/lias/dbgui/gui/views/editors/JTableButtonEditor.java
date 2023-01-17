package fr.ensma.lias.dbgui.gui.views.editors;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;

public class JTableButtonEditor extends DefaultCellEditor {
    protected JButton button;
    protected String  label;
    protected Boolean clicked;

    public JTableButtonEditor( JTextField txtField ) {
        super( txtField );
        button = new JButton();
        button.setOpaque( true );

        // listener to react on click events
        button.addActionListener( new ActionListener() {

            @Override
            public void actionPerformed( ActionEvent e ) {
                fireEditingStopped();
            }
        } );
    }

    @Override
    public Component getTableCellEditorComponent( JTable table, Object value, boolean isSelected, int row,
            int column ) {
        // the string object is set as the label
        label = ( ( value == null ) ? "" : value.toString() );
        // then the button is declared
        button.setText( label );
        // why clicked ?
        clicked = true;
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        if ( clicked ) {
            // action to perform when button was clicked
            // example :
            JOptionPane.showMessageDialog( button, label + " clicked" );
        }
        clicked = false;
        return new String( label );
    }

    @Override
    public boolean stopCellEditing() {
        clicked = false;
        return super.stopCellEditing();
    }

    @Override
    protected void fireEditingStopped() {
        super.fireEditingStopped();
    }
}
