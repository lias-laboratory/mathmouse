package fr.ensma.lias.dbgui.gui.views;

import javax.swing.JTabbedPane;

import fr.ensma.lias.dbgui.graphicwrapper.models.TimeSeriesForm;

public class TimeSeriesTabbedView extends JTabbedPane {
    private static final String        FLAT_VIEW_PANE_NAME = "Flat";
    private static final String        FORM_VIEW_PANE_NAME = "Form";

    private String                     filePath;
    private FlatDetailedView           flatView;
    private FormTimeSeriesDetailedView formDetailedView;

    public TimeSeriesTabbedView() {
        super( JTabbedPane.BOTTOM );
        filePath = "";
        flatView = new FlatDetailedView();
        formDetailedView = new FormTimeSeriesDetailedView();
        add( FORM_VIEW_PANE_NAME, formDetailedView );
        add( FLAT_VIEW_PANE_NAME, flatView );
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath( String filePath ) {
        this.filePath = filePath;
    }

    public void setFlatText( String text ) {
        flatView.contentPane.setText( text );
    }

    public void setFormText( TimeSeriesForm form ) {
        formDetailedView.setFormDetailedView( form );
    }
}
