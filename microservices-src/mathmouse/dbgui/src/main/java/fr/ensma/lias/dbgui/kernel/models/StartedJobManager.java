package fr.ensma.lias.dbgui.kernel.models;

import fr.ensma.lias.dbgui.kernel.observers.IJobManagerObserver;
import fr.ensma.lias.dockermanagerapi.enumerations.EServiceName;

public class StartedJobManager extends AbstractJobManager
{
    private static final int SERVICE_NAME_INDEX = 0;
    private static final int HASH_INDEX         = 1;
    private static final int EXPECTED_INDEX     = 2;

    public StartedJobManager()
    {
        super();
    }

    public void updateJob( String[] data )
    {
        EServiceName service = EServiceName.fromValue( data[SERVICE_NAME_INDEX] );
        int hash = Integer.parseInt( data[HASH_INDEX] );
        JobModel job = getJobByHash( hash );
        switch ( service )
        {
        case GENERATOR:
            if ( data.length == 2 )
            {
                job.incrementeProgress( 5.0f );
            } else if ( data.length > 2 )
            {
                job.incrementeGeneration();
            }
            break;
        case DB_CORE:
            int expected = Integer.parseInt( data[EXPECTED_INDEX] );
            job.setNcomparison( expected );
            job.setNgeneration( expected );
            job.incrementeProgress( 5.0f );
            break;
        default:
            break;
        }
        job.progress();
        fireJobProgress( job );
    }

    private void fireJobProgress( JobModel job )
    {
        for ( IJobManagerObserver observer : observers )
        {
            observer.progressUpdate( jobsList.indexOf( job ), job );
        }
    }

    public void fireJobProgress( int hash )
    {
        for ( IJobManagerObserver observer : observers )
        {
            JobModel job = getJobByHash( hash );
            job.progress();
            observer.progressUpdate( jobsList.indexOf( job ), job );
        }
    }

    public void fireJobRemoval( int index )
    {
        for ( IJobManagerObserver observer : observers )
        {
            observer.removalUpdate( index );
        }
    }

}
