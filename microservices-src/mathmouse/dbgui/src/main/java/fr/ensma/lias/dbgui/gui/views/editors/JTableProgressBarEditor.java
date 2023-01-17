package fr.ensma.lias.dbgui.gui.views.editors;

import java.awt.Component;

import javax.swing.DefaultCellEditor;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class JTableProgressBarEditor extends DefaultCellEditor {
    // why protect one and make the others private ?
    protected JProgressBar progressBar; // why button needed
    private int            value;
    private Boolean        clicked;
    private int            row;

    public JTableProgressBarEditor( JTextField txtField ) {
        super( txtField );
        progressBar = new JProgressBar();
        progressBar.setStringPainted( true );

        // listener to react on click events
        progressBar.addChangeListener( new ChangeListener() {

            @Override
            public void stateChanged( ChangeEvent e ) {
                fireEditingStopped();
            }
        } );

    }

    @Override
    public Component getTableCellEditorComponent( JTable table, Object value, boolean isSelected, int row,
            int column ) {
        // the string object is set as the label
        if ( value instanceof Double )
            this.value = (int) Math.round( ( (double) value ) );
        // then the button is declared
        // why clicked ?
        clicked = true;
        this.row = row;
        return progressBar;
    }

    @Override
    public Object getCellEditorValue() {
        return new Double( value );
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
