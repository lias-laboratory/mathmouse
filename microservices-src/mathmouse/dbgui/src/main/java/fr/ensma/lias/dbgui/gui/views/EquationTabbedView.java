package fr.ensma.lias.dbgui.gui.views;

import javax.swing.JTabbedPane;

import fr.ensma.lias.dbgui.graphicwrapper.models.EquationForm;

public class EquationTabbedView extends JTabbedPane {
    private static final String FLAT_VIEW_PANE_NAME = "Flat";
    private static final String XML_VIEW_PANE_NAME  = "XML";
    private static final String FORM_VIEW_PANE_NAME = "Form";

    private String              filePath;
    private FlatDetailedView    flatView;
    private XMLDetailedView     xmlDetailedView;
    private FormDetailedView    formDetailedView;

    public EquationTabbedView() {
        super( JTabbedPane.BOTTOM );
        filePath = "";
        flatView = new FlatDetailedView();
        xmlDetailedView = new XMLDetailedView();
        formDetailedView = new FormDetailedView();
        add( FORM_VIEW_PANE_NAME, formDetailedView );
        add( FLAT_VIEW_PANE_NAME, flatView );
        add( XML_VIEW_PANE_NAME, xmlDetailedView );
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

    public void setXMLText( String text ) {
        xmlDetailedView.contentPane.setText( text );
    }

    public void setFormText( EquationForm equationForm ) {
        formDetailedView.setFormDetailedView( equationForm );
    }

}
