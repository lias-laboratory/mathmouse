package fr.ensma.lias.dbgui.kernel.models;

import java.util.ArrayList;
import java.util.Vector;

import fr.ensma.lias.dbgui.kernel.observers.IJobManagerObservable;
import fr.ensma.lias.dbgui.kernel.observers.IJobManagerObserver;

public abstract class AbstractJobManager implements IJobManagerObservable
{
    protected ArrayList<IJobManagerObserver> observers;
    protected Vector<JobModel>               jobsList;
    protected AbstractJobManager             jobManager = null;

    protected AbstractJobManager()
    {
        observers = new ArrayList<IJobManagerObserver>();
        jobsList = new Vector<JobModel>();
    }

    @Override
    public void addObserver( IJobManagerObserver observer )
    {
        observers.add( observer );
    }

    public void addJobModel( JobModel jobModel )
    {
        jobsList.add( jobModel );
        for ( IJobManagerObserver obs : observers )
        {
            obs.additionUpdate( jobModel );
        }
    }

    public void removeJobModel( int index )
    {
        jobsList.remove( index );
        for ( IJobManagerObserver obs : observers )
        {
            obs.removalUpdate( index );
        }
    }

    public void removeJobModel( JobModel job )
    {
        jobsList.remove( job );
    }

    public JobModel getJobByHash( int hash )
    {
        boolean found = false;
        JobModel desiredJob = null;
        int i = 0;

        while ( i < jobsList.size() && !found )
        {
            if ( jobsList.get( i ).getHash() == hash )
            {
                found = true;
                desiredJob = jobsList.get( i );
            } else
            {
                i++;
            }
        }

        return desiredJob;
    }

    public int getJobIndex( JobModel job )
    {
        return jobsList.indexOf( job );
    }

    public JobModel getJob( int index )
    {
        if ( jobsList.size() > index )
        {
            return jobsList.get( index );
        } else
        {
            return null;
        }
    }

    public int size()
    {
        return jobsList.size();
    }

    public boolean contains( int hash )
    {
        boolean found = false;

        int i = 0;
        while ( i < jobsList.size() && !found )
        {
            if ( jobsList.get( i ).getHash() == hash )
            {
                found = true;
            } else
            {
                i++;
            }
        }

        return found;
    }

    public void setNbExpectedEquations( int hash, int nb )
    {
        getJobByHash( hash ).getJobResult().setNbExpectedEquations( nb );
    }

    public void incrementeProcessedEquations( int hash )
    {
        getJobByHash( hash ).getJobResult().incrementeProcessedEquations();
        getJobByHash( hash ).incrementeComparaison();
    }

    public void addJobResult( int hash, Result result )
    {
        getJobByHash( hash ).getJobResult().getResults().add( result );
        for ( IJobManagerObserver obs : observers )
        {
            obs.addResult();
        }
    }

}
