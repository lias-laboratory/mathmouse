package fr.ensma.lias.dbgui.kernel.models;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.ensma.lias.dbgui.graphicwrapper.enumerations.EStatus;
import fr.ensma.lias.dbgui.kernel.models.states.IJobState;
import fr.ensma.lias.dbgui.kernel.models.states.Queueing;
import fr.ensma.lias.dockermanagerapi.enumerations.EServiceName;

public class JobModel {
    private Logger                  logger;
    private int                     hash;
    private String                  filePath;
    private String                  jobsName;
    private EStatus                 status;
    private JobResult               jobResult;
    private float                   progress;
    private int                     Ngeneration;
    private int                     Ncomparison;
    private int                     ngeneration;
    private int                     ncomparison;
    private ArrayList<EServiceName> services;
    private IJobState               state;

    public JobModel() {
        filePath = null;
        jobsName = null;
        status = EStatus.CREATED;
        jobResult = new JobResult();
        progress = 0.0f;
        Ngeneration = 0;
        Ncomparison = 0;
        ngeneration = 0;
        ncomparison = 0;
        services = new ArrayList<EServiceName>();
        state = new Queueing( this );
        logger = LoggerFactory.getLogger( this.getClass() );
    }

    public JobModel( String filePath, String jobsName ) {
        this.filePath = filePath;
        this.jobsName = jobsName;
        jobResult = new JobResult();
        status = EStatus.CREATED;
        progress = 0.0f;
        Ngeneration = 0;
        Ncomparison = 0;
        ngeneration = 0;
        ncomparison = 0;
        services = new ArrayList<EServiceName>();
        state = new Queueing( this );
        logger = LoggerFactory.getLogger( this.getClass() );
    }

    public int getHash() {
        return hash;
    }

    public void setHash( int hash ) {
        this.hash = hash;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath( String filePath ) {
        this.filePath = filePath;
    }

    public String getJobsName() {
        return jobsName;
    }

    public void setJobsName( String jobsName ) {
        this.jobsName = jobsName;
    }

    public EStatus getStatus() {
        return status;
    }

    public void setStatus( EStatus status ) {
        this.status = status;
    }

    public void setState( IJobState state ) {
        this.state = state;
    }

    public ArrayList<EServiceName> getServices() {
        return services;
    }

    public float getProgress() {
        logger.debug( "progress: " + progress );
        return progress;
    }

    public JobResult getJobResult() {
        return jobResult;
    }

    public void setNgeneration( int ngeneration ) {
        Ngeneration = ngeneration;
    }

    public void setNcomparison( int ncomparison ) {
        Ncomparison = ncomparison;
    }

    public int getRemainingGenerationJobsNumber() {
        return Ngeneration - ngeneration;
    }

    public int getRemainingComparisonJobsNumber() {
        return Ncomparison - ncomparison;
    }

    public void incrementeProgress( float pop ) {
        progress = progress + pop;
        logger.debug( "progress: " + progress );
    }

    public void incrementeGeneration() {
        ngeneration++;
        progress = progress + 60.0f / Ngeneration;
    }

    public void incrementeComparaison() {
        ncomparison++;
        progress = progress + 30.0f / Ncomparison;
    }

    public boolean isOver() {
        logger.debug( "progress: " + progress );
        logger.debug( "Ncomparaison=" + Ncomparison + ", ncomparaison=" + ncomparison );
        logger.debug( "Ngeneration=" + Ngeneration + ", ngeneration=" + ngeneration );
        if ( Ncomparison == ncomparison && Ngeneration == ngeneration ) {
            logger.debug( "comparison job over." );
            progress = 100.0f;
            return true;
        }
        return false;
    }

    public void progress() {
        state.t_progress();
    }

}
