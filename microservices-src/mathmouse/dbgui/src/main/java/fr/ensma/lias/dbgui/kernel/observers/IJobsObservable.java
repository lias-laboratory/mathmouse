package fr.ensma.lias.dbgui.kernel.observers;

public interface IJobsObservable {
    void addObserver( IJobsObserver observer );

    void notifyChange();
}
