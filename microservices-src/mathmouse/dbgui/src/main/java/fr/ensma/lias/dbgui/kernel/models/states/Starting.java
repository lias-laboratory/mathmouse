package fr.ensma.lias.dbgui.kernel.models.states;

import fr.ensma.lias.dbgui.graphicwrapper.enumerations.EStatus;
import fr.ensma.lias.dbgui.kernel.models.JobModel;
import fr.ensma.lias.dockermanagerapi.enumerations.EServiceName;

public class Starting implements IJobState {
    private JobModel job;

    public Starting( JobModel job ) {
        this.job = job;
        job.setStatus( EStatus.STARTING );
    }

    @Override
    public void t_progress() {
        job.getServices().add( EServiceName.COMPARATOR_MANAGER );
        job.getServices().add( EServiceName.GENERATOR );
        job.setState( new Requesting( job ) );
    }

}
