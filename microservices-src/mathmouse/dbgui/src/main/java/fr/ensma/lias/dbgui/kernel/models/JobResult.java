package fr.ensma.lias.dbgui.kernel.models;

import java.util.ArrayList;
import java.util.List;

public class JobResult {
    private int          nbProcessedEquations;
    private int          nbExpectedEquations;
    private List<Result> results;

    public JobResult() {
        super();
        results = new ArrayList<Result>();
        nbProcessedEquations = 0;
    }

    public int getNbProcessedEquations() {
        return nbProcessedEquations;
    }

    public void setNbProcessedEquations( int nbProcessedEquations ) {
        this.nbProcessedEquations = nbProcessedEquations;
    }

    public int getNbExpectedEquations() {
        return nbExpectedEquations;
    }

    public void setNbExpectedEquations( int nbExpectedEquations ) {
        this.nbExpectedEquations = nbExpectedEquations;
    }

    public List<Result> getResults() {
        return results;
    }

    public void incrementeProcessedEquations() {
        nbProcessedEquations++;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + nbExpectedEquations;
        result = prime * result + nbProcessedEquations;
        result = prime * result + ( ( results == null ) ? 0 : results.hashCode() );
        return result;
    }

    @Override
    public boolean equals( Object obj ) {
        if ( this == obj )
            return true;
        if ( obj == null )
            return false;
        if ( getClass() != obj.getClass() )
            return false;
        JobResult other = (JobResult) obj;
        if ( nbExpectedEquations != other.nbExpectedEquations )
            return false;
        if ( nbProcessedEquations != other.nbProcessedEquations )
            return false;
        if ( results == null ) {
            if ( other.results != null )
                return false;
        } else if ( !results.equals( other.results ) )
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "JobResult [nbProcessedEquations=" + nbProcessedEquations + ", nbExpectedEquations="
                + nbExpectedEquations + ", results=" + results + "]";
    }

}
