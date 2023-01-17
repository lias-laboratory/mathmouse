package fr.ensma.lias.dbgui.kernel.models.states;

import fr.ensma.lias.dbgui.graphicwrapper.enumerations.EStatus;
import fr.ensma.lias.dbgui.gui.DbUserInterfaceMain;
import fr.ensma.lias.dbgui.kernel.models.JobModel;
import fr.ensma.lias.dockermanagerapi.enumerations.EServiceName;
import fr.ensma.lias.javarabbitmqapi.enumerations.EQueueName;

public class Working implements IJobState
{
    private JobModel job;

    public Working( JobModel job )
    {
        this.job = job;
        job.setStatus( EStatus.IN_PROGRESS );
    }

    @Override
    public void t_progress()
    {
        job.getServices().clear();
        job.getServices().add( EServiceName.IHM );
        if ( job.isOver() )
        {
            job.setState( new Over( job ) );
            DbUserInterfaceMain.end = System.currentTimeMillis();
            try
            {
                DbUserInterfaceMain.getSender().publish( EQueueName.JOB_OVER, String.valueOf( job.getHash() ) );
            } catch ( Exception e )
            {
                e.printStackTrace();
            }
        }
    }

}
