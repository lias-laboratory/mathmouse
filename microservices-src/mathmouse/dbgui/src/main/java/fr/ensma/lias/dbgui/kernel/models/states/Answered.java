package fr.ensma.lias.dbgui.kernel.models.states;

import fr.ensma.lias.dbgui.graphicwrapper.enumerations.EStatus;
import fr.ensma.lias.dbgui.kernel.models.JobModel;
import fr.ensma.lias.dockermanagerapi.enumerations.EServiceName;

public class Answered implements IJobState {
    private JobModel job;

    public Answered( JobModel job ) {
        this.job = job;
        job.setStatus( EStatus.IN_PROGRESS );
    }

    @Override
    public void t_progress() {
        job.getServices().clear();
        job.getServices().add( EServiceName.IHM );
        job.getServices().add( EServiceName.COMPARATOR_MANAGER );
        job.getServices().add( EServiceName.COMPARATOR );
        job.getServices().add( EServiceName.GENERATOR );
        job.setState( new Working( job ) );
    }

}
