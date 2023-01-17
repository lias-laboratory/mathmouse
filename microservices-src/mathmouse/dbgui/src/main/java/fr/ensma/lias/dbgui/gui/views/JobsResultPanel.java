package fr.ensma.lias.dbgui.gui.views;

import java.awt.Color;

import javax.swing.JTextArea;

import fr.ensma.lias.dbgui.graphicwrapper.TextFormatter;
import fr.ensma.lias.dbgui.gui.DbUserInterfaceMain;
import fr.ensma.lias.dbgui.kernel.models.JobModel;
import fr.ensma.lias.dbgui.kernel.models.Result;
import fr.ensma.lias.dbgui.kernel.observers.IJobManagerObserver;
import fr.ensma.lias.timeseriesreductorslib.comparison.ENotification;

public class JobsResultPanel extends JTextArea implements IJobManagerObserver
{
    public ENotification filter;
    public int           row;

    public JobsResultPanel( ENotification filter )
    {
        super();
        setBackground( Color.WHITE );
        setSize( 500, 600 );
        this.filter = filter;
        DbUserInterfaceMain.getStartedJobManager().addObserver( this );
    }

    public void setRow( int row )
    {
        this.row = row;
        setText();
    }

    public void setText()
    {
        int size = DbUserInterfaceMain.getStartedJobManager().size();
        JobModel job = DbUserInterfaceMain.getStartedJobManager().getJob( row );
        String text = "";

        for ( Result result : job.getJobResult().getResults() )
        {
            if ( result.getNotification().equals( filter ) )
            {
                text = text + TextFormatter.writeResultDetail( result );
            }
        }

        setText( text );
    }

    @Override
    public void additionUpdate( JobModel object )
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void removalUpdate( int index )
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void progressUpdate( int index, JobModel element )
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void addResult()
    {
        this.setText();
    }

}
