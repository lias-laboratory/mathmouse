package fr.ensma.lias.dbgui.gui.views;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import fr.ensma.lias.dbgui.graphicwrapper.models.TimeSeriesForm;

public class FormTimeSeriesDetailedView extends AbstractDetailedView {
    private static final String FILE_PATH_LABEL            = "File Path : ";
    private static final String VALUE_LABEL                = "Value : ";
    private static final String VALUES_LABEL               = "Values : ";
    private static final String INTERPOLATION_METHOD_LABEL = "Interpolation method : ";
    private static final String TIME_LABEL                 = "Time : ";

    private JPanel              panel;
    private String              filePath;
    private String              interpolationMethod;
    private Map<String, String> values;

    private JLabel              filePathLabel;
    private JLabel              interpolationMethodLabel;
    private JLabel              valuesLabel;
    private JLabel              valueLabel;
    private JLabel              timeLabel;

    public FormTimeSeriesDetailedView() {
        remove( contentPaneScrollPane );
        setLayout( new BorderLayout() );

        filePathLabel = new JLabel( FILE_PATH_LABEL );
        interpolationMethodLabel = new JLabel( INTERPOLATION_METHOD_LABEL );
        valuesLabel = new JLabel( VALUES_LABEL );
        valueLabel = new JLabel( VALUE_LABEL );
        timeLabel = new JLabel( TIME_LABEL );

        filePath = new String();
        interpolationMethod = new String();
        values = new HashMap<String, String>();

        panel = new JPanel();
        panel.setLayout( new GridLayout( 0, 4 ) );
        // line 1 : name
        panel.add( filePathLabel );
        panel.add( new JTextField( filePath ) );
        panel.add( Box.createGlue() );
        panel.add( Box.createGlue() );
        // line 2 : formula
        panel.add( interpolationMethodLabel );
        panel.add( new JTextField( interpolationMethod ) );
        panel.add( Box.createGlue() );
        panel.add( Box.createGlue() );
        // line 3 : variables values
        panel.add( valuesLabel );
        panel.add( Box.createGlue() );
        panel.add( Box.createGlue() );
        panel.add( Box.createGlue() );

        // values lines
        for ( String key : values.keySet() ) {
            panel.add( new JLabel( TIME_LABEL ) );
            panel.add( new JTextField( key ) );
            panel.add( new JLabel( VALUE_LABEL ) );
            panel.add( new JTextField( values.get( key ) ) );
        }

        JScrollPane panelScrollPane = new JScrollPane( panel );
        panelScrollPane.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS );
        panelScrollPane.setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS );
        panelScrollPane.setVisible( true );
        add( panelScrollPane, BorderLayout.NORTH );
    }

    public void setFormDetailedView( TimeSeriesForm form ) {
        filePath = form.getFilePath();
        interpolationMethod = form.getInterpolationMethod();
        values = form.getValues();
        repaint();
    }

    @Override
    public void repaint() {
        panel.removeAll();
        // line 1 : name
        panel.add( filePathLabel );
        panel.add( new JTextField( filePath ) );
        panel.add( Box.createGlue() );
        panel.add( Box.createGlue() );
        // line 2 : formula
        panel.add( interpolationMethodLabel );
        panel.add( new JTextField( interpolationMethod ) );
        panel.add( Box.createGlue() );
        panel.add( Box.createGlue() );
        // line 3 : variables values
        panel.add( valuesLabel );
        panel.add( Box.createGlue() );
        panel.add( Box.createGlue() );
        panel.add( Box.createGlue() );

        // values lines
        for ( String key : values.keySet() ) {
            panel.add( new JLabel( TIME_LABEL ) );
            panel.add( new JTextField( key ) );
            panel.add( new JLabel( VALUE_LABEL ) );
            panel.add( new JTextField( values.get( key ) ) );
        }
        super.repaint();
    }
}
