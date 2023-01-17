package fr.ensma.lias.dbgui.gui.views.editors;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.JTextField;

import fr.ensma.lias.dbgui.graphicwrapper.TextFormatter;
import fr.ensma.lias.dbgui.graphicwrapper.models.TimeSeriesForm;
import fr.ensma.lias.dbgui.gui.views.CreateJobsTableView;
import fr.ensma.lias.dbgui.gui.views.TemporaryTimeSeriesTabbedView;
import fr.ensma.lias.dbgui.gui.views.TimeSeriesTabbedView;
import fr.ensma.lias.timeseriesreductorslib.timeseries.TimeSeriesDoubleDouble;
import fr.ensma.lias.timeseriesreductorslib.timeseries.TimeSeriesDoubleDoubleIO;

public class JTableValuesButtonEditor extends JTableButtonEditor {
    private TimeSeriesTabbedView view;

    public JTableValuesButtonEditor() {
        super( new JTextField() );
        view = new TimeSeriesTabbedView();
    }

    @Override
    public Component getTableCellEditorComponent( JTable table, Object value, boolean isSelected, int row,
            int column ) {
        super.getTableCellEditorComponent( table, value, isSelected, row, column );
        String filePath = String.valueOf( table.getValueAt( row, CreateJobsTableView.FILE_COLUMN_INDEX ) );
        view.setFilePath( filePath );
        TimeSeriesDoubleDouble series = TimeSeriesDoubleDoubleIO.readFromFile( filePath );
        view.setFlatText( TextFormatter.writeTimeSeries( series ) );
        view.setFormText( new TimeSeriesForm( filePath, series ) );
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        if ( clicked ) {
            // action to perform when button was clicked
            // example :
            TemporaryTimeSeriesTabbedView tempView = new TemporaryTimeSeriesTabbedView( view );
        }
        clicked = false;
        return new String( label );
    }

}
