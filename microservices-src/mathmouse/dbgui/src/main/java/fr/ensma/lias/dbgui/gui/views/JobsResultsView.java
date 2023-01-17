package fr.ensma.lias.dbgui.gui.views;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import fr.ensma.lias.timeseriesreductorslib.comparison.ENotification;

public class JobsResultsView extends JDialog {
    private static final String WINDOW_NAME                = "Results";

    private static final String ACCEPTED_MODELS_PANE_NAME  = "Accepted Models";
    public static final int     ACCEPTED_MODELS_INDEX      = 0;
    private static final String UNDETERMINED_PANE_NAME     = "Undertermined Models";
    public static final int     UNDETERMINED_PANE_INDEX    = 1;
    private static final String REJECTED_MODELS_PANE_NAME  = "Rejected Models";
    public static final int     REJECTED_MODELS_PANE_INDEX = 2;

    private JobsResultPanel     acceptedPanel;
    private JobsResultPanel     undeterminedPanel;
    private JobsResultPanel     rejectedPanel;
    private JTabbedPane         tabbedPane;

    public JobsResultsView() {
        setTitle( WINDOW_NAME );
        setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
        setSize( 800, 500 );

        acceptedPanel = new JobsResultPanel( ENotification.MODEL_ACCEPTED );
        JScrollPane acceptedPanelScrollPane = new JScrollPane( acceptedPanel );
        acceptedPanelScrollPane.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS );
        acceptedPanelScrollPane.setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS );
        undeterminedPanel = new JobsResultPanel( ENotification.UNDETERMINED );
        JScrollPane undeterminedPanelScrollPane = new JScrollPane( undeterminedPanel );
        undeterminedPanelScrollPane.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS );
        undeterminedPanelScrollPane.setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS );
        rejectedPanel = new JobsResultPanel( ENotification.MODEL_REJECTED );
        JScrollPane rejectedPanelScrollPane = new JScrollPane( rejectedPanel );
        rejectedPanelScrollPane.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS );
        rejectedPanelScrollPane.setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS );

        tabbedPane = new JTabbedPane();
        tabbedPane.addTab( ACCEPTED_MODELS_PANE_NAME, acceptedPanelScrollPane );
        tabbedPane.addTab( UNDETERMINED_PANE_NAME, undeterminedPanelScrollPane );
        tabbedPane.addTab( REJECTED_MODELS_PANE_NAME, rejectedPanelScrollPane );

        add( tabbedPane );
        setVisible( true );
    }

    public void setRow( int row ) {
        acceptedPanel.setRow( row );
        undeterminedPanel.setRow( row );
        rejectedPanel.setRow( row );
    }
}
