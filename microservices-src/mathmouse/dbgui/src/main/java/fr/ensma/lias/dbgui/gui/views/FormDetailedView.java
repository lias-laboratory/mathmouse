package fr.ensma.lias.dbgui.gui.views;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import fr.ensma.lias.dbgui.graphicwrapper.models.EquationForm;
import fr.ensma.lias.dbgui.graphicwrapper.models.InputFunctionForm;

public class FormDetailedView extends AbstractDetailedView {
    private static final String NAME_LABEL                 = "Name : ";
    private static final String GROUP_LABEL                = "Group : ";
    private static final String FORMULA_LABEL              = "Formula : ";
    private static final String VARIABLES_VALUES_LABEL     = "Variables values : ";
    private static final String INITIAL_CONDITIONS_LABEL   = "Initial Conditions : ";
    private static final String TIME_ORIGIN_LABEL          = "Time Origin : ";
    private static final String INITIAL_VALUES_LABEL       = "Initial values : ";
    private static final String DERIVATION_ORDER_LABEL     = "Derivation order : ";
    private static final String VALUE_LABEL                = "Value : ";
    private static final String VALUES_LABEL               = "Values : ";
    private static final String INPUT_FUNCTIONS_LABEL      = "Input functions : ";
    private static final String INTERPOLATION_METHOD_LABEL = "Interpolation method : ";
    private static final String TIME_LABEL                 = "Time : ";

    private JPanel              panel;
    private JTextField          nameField;
    private JTextField          groupField;
    private JTextField          formulaField;
    private JTextField          variablesValuesField;
    private JTextField          timeOriginField;
    private List<JTextField>    initialValuesDerivationOrderFields;
    private List<JTextField>    initialValuesFields;
    private List<JTextField>    functionNameFields;
    private List<JTextField>    functionDerivationOrderFields;
    private List<JTextField>    interpolationMethodFields;
    private List<List<String>>  timeKeysLists;
    private List<List<String>>  valuesLists;

    private JLabel              nameLabel;
    private JLabel              formulaLabel;
    private JLabel              variablesValuesLabel;
    private JLabel              initialConditionsLabel;
    private JLabel              timeOriginLabel;
    private JLabel              initialValuesLabel;
    private JLabel              inputFunctionsLabel;

    public FormDetailedView() {
        remove( contentPaneScrollPane );
        setLayout( new BorderLayout() );

        nameLabel = new JLabel( NAME_LABEL );
        formulaLabel = new JLabel( FORMULA_LABEL );
        variablesValuesLabel = new JLabel( VARIABLES_VALUES_LABEL );
        initialConditionsLabel = new JLabel( INITIAL_CONDITIONS_LABEL );
        timeOriginLabel = new JLabel( TIME_ORIGIN_LABEL );
        initialValuesLabel = new JLabel( INITIAL_VALUES_LABEL );
        inputFunctionsLabel = new JLabel( INPUT_FUNCTIONS_LABEL );

        nameField = new JTextField();
        formulaField = new JTextField();
        variablesValuesField = new JTextField();
        timeOriginField = new JTextField();

        initialValuesDerivationOrderFields = new ArrayList<JTextField>();
        initialValuesFields = new ArrayList<JTextField>();
        functionNameFields = new ArrayList<JTextField>();
        functionDerivationOrderFields = new ArrayList<JTextField>();
        interpolationMethodFields = new ArrayList<JTextField>();
        timeKeysLists = new ArrayList<List<String>>();
        valuesLists = new ArrayList<List<String>>();

        panel = new JPanel();
        panel.setLayout( new GridLayout( 0, 4 ) );
        // line 1 : name
        panel.add( nameLabel );
        panel.add( nameField );
        panel.add( Box.createGlue() );
        panel.add( Box.createGlue() );
        // line 2 : group
        // panel.add( groupLabel );
        // panel.add( groupField );
        panel.add( Box.createGlue() );
        panel.add( Box.createGlue() );
        // line 2 : formula
        panel.add( formulaLabel );
        panel.add( formulaField );
        panel.add( Box.createGlue() );
        panel.add( Box.createGlue() );
        // line 3 : variables values
        panel.add( variablesValuesLabel );
        panel.add( variablesValuesField );
        panel.add( Box.createGlue() );
        panel.add( Box.createGlue() );
        // line 4 : initial conditions section
        panel.add( initialConditionsLabel );
        panel.add( Box.createGlue() );
        panel.add( Box.createGlue() );
        panel.add( Box.createGlue() );
        // line 5 : time origin
        panel.add( timeOriginLabel );
        panel.add( timeOriginField );
        panel.add( Box.createGlue() );
        panel.add( Box.createGlue() );
        // line 6 : initial values subsection
        panel.add( initialValuesLabel );
        panel.add( Box.createGlue() );
        panel.add( Box.createGlue() );
        panel.add( Box.createGlue() );

        // initial values lines
        for ( int i = 0; i < initialValuesFields.size(); i++ ) {
            panel.add( new JLabel( DERIVATION_ORDER_LABEL ) );
            panel.add( initialValuesDerivationOrderFields.get( i ) );
            panel.add( new JLabel( VALUE_LABEL ) );
            panel.add( initialValuesFields.get( i ) );
        }

        // input functions section
        panel.add( inputFunctionsLabel );
        panel.add( Box.createGlue() );
        panel.add( Box.createGlue() );
        panel.add( Box.createGlue() );

        // input functions
        for ( int i = 0; i < functionNameFields.size(); i++ ) {
            panel.add( new JLabel( NAME_LABEL ) );
            panel.add( functionNameFields.get( i ) );
            panel.add( new JLabel( DERIVATION_ORDER_LABEL ) );
            panel.add( functionDerivationOrderFields.get( i ) );
            panel.add( new JLabel( INTERPOLATION_METHOD_LABEL ) );
            panel.add( interpolationMethodFields.get( i ) );
            panel.add( Box.createGlue() );
            panel.add( Box.createGlue() );
            panel.add( new JLabel( VALUES_LABEL ) );
            panel.add( Box.createGlue() );
            panel.add( Box.createGlue() );
            panel.add( Box.createGlue() );

            for ( int j = 0; j < timeKeysLists.get( i ).size(); j++ ) {
                panel.add( new JLabel( TIME_LABEL ) );
                panel.add( new JTextField( timeKeysLists.get( i ).get( j ) ) );
                panel.add( new JLabel( VALUE_LABEL ) );
                panel.add( new JTextField( valuesLists.get( i ).get( j ) ) );
            }
        }

        add( panel, BorderLayout.NORTH );
    }

    public void setFormDetailedView( EquationForm form ) {
        nameField.setText( form.getName() );
        formulaField.setText( form.getFormula() );
        variablesValuesField.setText( form.getVariablesValues().toString() );
        timeOriginField.setText( form.getTimeOrigin() );

        initialValuesDerivationOrderFields.clear();
        initialValuesFields.clear();
        for ( String key : form.getInitialValues().keySet() ) {
            initialValuesDerivationOrderFields.add( new JTextField( key ) );
            initialValuesFields.add( new JTextField( form.getInitialValues().get( key ) ) );
        }

        functionNameFields.clear();
        functionDerivationOrderFields.clear();
        interpolationMethodFields.clear();
        timeKeysLists.clear();
        valuesLists.clear();
        for ( InputFunctionForm inForm : form.getInputFunctionsForms() ) {
            functionNameFields.add( new JTextField( inForm.getName() ) );
            functionDerivationOrderFields.add( new JTextField( inForm.getDerivationOrder() ) );
            interpolationMethodFields.add( new JTextField( inForm.getInterpolationMethod() ) );
            ArrayList<String> timeKeys = new ArrayList<String>();
            ArrayList<String> values = new ArrayList<String>();
            for ( String key : inForm.getValues().keySet() ) {
                timeKeys.add( key );
                values.add( inForm.getValues().get( key ) );
            }
            timeKeysLists.add( timeKeys );
            valuesLists.add( values );
        }
        repaint();
    }

    @Override
    public void repaint() {
        panel.removeAll();
        // line 1 : name
        panel.add( nameLabel );
        panel.add( nameField );
        panel.add( Box.createGlue() );
        panel.add( Box.createGlue() );
        // line 2 : formula
        panel.add( formulaLabel );
        panel.add( formulaField );
        panel.add( Box.createGlue() );
        panel.add( Box.createGlue() );
        // line 3 : variables values
        panel.add( variablesValuesLabel );
        panel.add( variablesValuesField );
        panel.add( Box.createGlue() );
        panel.add( Box.createGlue() );
        // line 4 : initial conditions section
        panel.add( initialConditionsLabel );
        panel.add( Box.createGlue() );
        panel.add( Box.createGlue() );
        panel.add( Box.createGlue() );
        // line 5 : time origin
        panel.add( timeOriginLabel );
        panel.add( timeOriginField );
        panel.add( Box.createGlue() );
        panel.add( Box.createGlue() );
        // line 6 : initial values subsection
        panel.add( initialValuesLabel );
        panel.add( Box.createGlue() );
        panel.add( Box.createGlue() );
        panel.add( Box.createGlue() );

        // initial values lines
        for ( int i = 0; i < initialValuesFields.size(); i++ ) {
            panel.add( new JLabel( DERIVATION_ORDER_LABEL ) );
            panel.add( initialValuesDerivationOrderFields.get( i ) );
            panel.add( new JLabel( VALUE_LABEL ) );
            panel.add( initialValuesFields.get( i ) );
        }

        // input functions section
        panel.add( inputFunctionsLabel );
        panel.add( Box.createGlue() );
        panel.add( Box.createGlue() );
        panel.add( Box.createGlue() );

        // input functions
        for ( int i = 0; i < functionNameFields.size(); i++ ) {
            panel.add( new JLabel( NAME_LABEL ) );
            panel.add( functionNameFields.get( i ) );
            panel.add( new JLabel( DERIVATION_ORDER_LABEL ) );
            panel.add( functionDerivationOrderFields.get( i ) );
            panel.add( new JLabel( INTERPOLATION_METHOD_LABEL ) );
            panel.add( interpolationMethodFields.get( i ) );
            panel.add( Box.createGlue() );
            panel.add( Box.createGlue() );
            panel.add( new JLabel( VALUES_LABEL ) );
            panel.add( Box.createGlue() );
            panel.add( Box.createGlue() );
            panel.add( Box.createGlue() );

            for ( int j = 0; j < timeKeysLists.get( i ).size(); j++ ) {
                panel.add( new JLabel( TIME_LABEL ) );
                panel.add( new JTextField( timeKeysLists.get( i ).get( j ) ) );
                panel.add( new JLabel( VALUE_LABEL ) );
                panel.add( new JTextField( valuesLists.get( i ).get( j ) ) );
            }
        }
        super.repaint();
    }
}
