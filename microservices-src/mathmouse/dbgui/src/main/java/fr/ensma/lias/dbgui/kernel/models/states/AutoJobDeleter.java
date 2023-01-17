package fr.ensma.lias.dbgui.kernel.models.states;

import javax.swing.SwingWorker;

import fr.ensma.lias.dbgui.gui.DbUserInterfaceMain;
import fr.ensma.lias.dbgui.kernel.models.JobModel;

public class AutoJobDeleter extends SwingWorker<Void, Void>
{
    private JobModel job;

    public AutoJobDeleter( JobModel job )
    {
        this.job = job;
    }

    @Override
    protected Void doInBackground() throws Exception
    {
        Thread.sleep( 5000 );
        return null;
    }

    @Override
    protected void done()
    {
        int index = DbUserInterfaceMain.getStartedJobManager().getJobIndex( job );
        DbUserInterfaceMain.getStartedJobManager().removeJobModel( job );
        DbUserInterfaceMain.getStartedJobManager().fireJobRemoval( index );
    }

}
