package fr.ensma.lias.dbgui.gui;

import java.awt.BorderLayout;

import javax.swing.JScrollPane;

import fr.ensma.lias.dbgui.gui.views.JobsManagerView;

public class JobsManagerPane extends AbstractPane {
    private JobsManagerView jobsManagerView;

    public JobsManagerPane() {
        super();

        jobsManagerView = new JobsManagerView();
        JScrollPane jobsManagerViewScrollPane = new JScrollPane( jobsManagerView );

        add( jobsManagerViewScrollPane, BorderLayout.CENTER );
    }

    public JobsManagerView getJobsManagerView() {
        return jobsManagerView;
    }

}
