package fr.ensma.lias.dbgui.kernel.models.states;

import fr.ensma.lias.dbgui.graphicwrapper.enumerations.EStatus;
import fr.ensma.lias.dbgui.kernel.models.JobModel;
import fr.ensma.lias.dockermanagerapi.enumerations.EServiceName;

public class Queueing implements IJobState {
    private JobModel job;

    public Queueing( JobModel job ) {
        this.job = job;
        job.setStatus( EStatus.WAITING );
    }

    @Override
    public void t_progress() {
        job.getServices().clear();
        job.getServices().add( EServiceName.COMPARATOR_MANAGER );
        job.setState( new Starting( job ) );
    }

}
