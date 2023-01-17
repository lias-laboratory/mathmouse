package fr.ensma.lias.dbgui.gui.views;

import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class GroupFormView extends AbstractDetailedView {
    private static final String GROUP_LABEL         = "Group : ";
    private static final String CONTENT_LABEL       = "Content : ";
    private static final String EQUATION_NAME_LABEL = "Equation name : ";
    private static final String FORMULA_LABEL       = "Formula : ";

    private JTextField          groupField;
    private Map<String, String> formulas;
    private JLabel              fieldLabel;
    private JLabel              contentLabel;

    public GroupFormView() {
        setLayout( new GridLayout( 0, 4 ) );

        groupField = new JTextField();
        formulas = new HashMap<String, String>();

        fieldLabel = new JLabel( GROUP_LABEL );
        contentLabel = new JLabel( CONTENT_LABEL );

        add( fieldLabel );
        add( groupField );
        add( Box.createGlue() );
        add( Box.createGlue() );
        add( contentLabel );
        add( Box.createGlue() );
        add( Box.createGlue() );
        add( Box.createGlue() );

        for ( String name : formulas.keySet() ) {
            JLabel nameLabel = new JLabel( EQUATION_NAME_LABEL );
            JTextField nameField = new JTextField( name );
            JLabel formulaLabel = new JLabel( FORMULA_LABEL );
            JTextField formulaField = new JTextField( formulas.get( name ) );
            add( nameLabel );
            add( nameField );
            add( formulaLabel );
            add( formulaField );
        }

        setVisible( true );
    }

    public void addEquation( String name, String formula ) {
        formulas.put( name, formula );
        add( new JLabel( EQUATION_NAME_LABEL ) );
        add( new JTextField( name ) );
        add( new JLabel( FORMULA_LABEL ) );
        add( new JTextField( formula ) );
        revalidate();
        repaint();
    }

}
