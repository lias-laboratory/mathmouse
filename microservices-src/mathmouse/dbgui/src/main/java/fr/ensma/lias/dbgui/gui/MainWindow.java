package fr.ensma.lias.dbgui.gui;

import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

public class MainWindow extends JFrame {
    private static final String  WINDOW_NAME             = "MathMOuse";
    private static final int     WINDOW_DEFAULT_WIDTH    = 1050;
    private static final int     WINDOW_DEFAULT_HEIGHT   = 930;
    private static final boolean RESIZABLE               = true;

    private static final String  EQUATIONS_PANE_NAME     = "Equations";
    public static final int      EQUATIONS_PANE_INDEX    = 0;
    private static final String  CREATE_JOBS_PANE_NAME   = "Create Jobs";
    public static final int      CREATE_JOBS_PANE_INDEX  = 1;
    private static final String  JOBS_MANAGER_PANE_NAME  = "Jobs Manager";
    public static final int      JOBS_MANAGER_PANE_INDEX = 2;
    private static final String  CONTROL_PANEL_PANE_NAME = "Control Panel";
    public static final int      CONTROL_PANEL_INDEX     = 3;

    private EquationsPane        equationsPane;
    private CreateJobsPane       createJobsPane;
    private JobsManagerPane      jobsManagerPane;
    private ControlPanel         controlPanel;
    private JTabbedPane          tabbedPane;

    public MainWindow() {
        setTitle( WINDOW_NAME );
        setSize( WINDOW_DEFAULT_WIDTH, WINDOW_DEFAULT_HEIGHT );
        setResizable( RESIZABLE );
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        tabbedPane = new JTabbedPane();
        equationsPane = new EquationsPane();
        createJobsPane = new CreateJobsPane();
        jobsManagerPane = new JobsManagerPane();
        controlPanel = new ControlPanel();
        tabbedPane.addTab( EQUATIONS_PANE_NAME, equationsPane );
        tabbedPane.addTab( CREATE_JOBS_PANE_NAME, createJobsPane );
        tabbedPane.addTab( JOBS_MANAGER_PANE_NAME, jobsManagerPane );
        tabbedPane.addTab( CONTROL_PANEL_PANE_NAME, controlPanel );
        // Add the tabbed pane to this panel.
        setLayout( new GridLayout( 1, 1 ) );
        add( tabbedPane );
        setVisible( true );
    }

    public EquationsPane getEquationsPane() {
        return equationsPane;
    }

    public CreateJobsPane getCreateJobsPane() {
        return createJobsPane;
    }

    public JobsManagerPane getJobsManagerPane() {
        return jobsManagerPane;
    }

    public void setSelectedPane( int paneIndex ) {
        tabbedPane.setSelectedIndex( paneIndex );
    }
}
