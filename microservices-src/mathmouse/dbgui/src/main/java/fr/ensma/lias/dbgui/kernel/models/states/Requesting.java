package fr.ensma.lias.dbgui.kernel.models.states;

import fr.ensma.lias.dbgui.graphicwrapper.enumerations.EStatus;
import fr.ensma.lias.dbgui.kernel.models.JobModel;
import fr.ensma.lias.dockermanagerapi.enumerations.EServiceName;

public class Requesting implements IJobState {
    private JobModel job;

    public Requesting( JobModel job ) {
        this.job = job;
        job.setStatus( EStatus.WAITING );
    }

    @Override
    public void t_progress() {
        job.getServices().clear();
        job.getServices().add( EServiceName.COMPARATOR_MANAGER );
        job.getServices().add( EServiceName.GENERATOR );
        job.getServices().add( EServiceName.DB_CORE );
        job.setState( new Answered( job ) );
    }

}
