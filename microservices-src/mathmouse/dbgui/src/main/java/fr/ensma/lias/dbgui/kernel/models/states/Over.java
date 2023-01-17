package fr.ensma.lias.dbgui.kernel.models.states;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.ensma.lias.dbgui.graphicwrapper.enumerations.EStatus;
import fr.ensma.lias.dbgui.gui.DbUserInterfaceMain;
import fr.ensma.lias.dbgui.kernel.models.JobModel;

public class Over implements IJobState
{
    private JobModel job;
    private Logger   logger;

    public Over( JobModel job )
    {
        this.job = job;
        job.setStatus( EStatus.OVER );
        DbUserInterfaceMain.end = System.currentTimeMillis();
        long time = DbUserInterfaceMain.end - DbUserInterfaceMain.start;
        logger = LoggerFactory.getLogger( getClass() );
        logger.debug( "Elapsed time : " + time / 1000 + " s." );
        // triggers auto deletion of the job, the job will be automatically deleted after 5 seconds.
        ( new AutoJobDeleter( job ) ).execute();
    }

    @Override
    public void t_progress()
    {
        logger.debug( "no more transition available." );
    }

}
