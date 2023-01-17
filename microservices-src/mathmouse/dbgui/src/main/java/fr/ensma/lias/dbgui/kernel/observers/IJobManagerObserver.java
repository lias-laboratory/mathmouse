package fr.ensma.lias.dbgui.kernel.observers;

import fr.ensma.lias.dbgui.kernel.models.JobModel;

public interface IJobManagerObserver
{
    void additionUpdate( JobModel object );

    void removalUpdate( int index );

    void progressUpdate( int index, JobModel element );

    void addResult();
}
