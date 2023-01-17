package fr.ensma.lias.dbgui.graphicwrapper.models;

public class JobNode {
    private String jobName;
    private int    jobIndex;

    public JobNode( String jobName, int jobIndex ) {
        super();
        this.jobName = jobName;
        this.jobIndex = jobIndex;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName( String jobName ) {
        this.jobName = jobName;
    }

    public int getJobIndex() {
        return jobIndex;
    }

    public void setJobIndex( int jobIndex ) {
        this.jobIndex = jobIndex;
    }

    @Override
    public String toString() {
        return jobName;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + jobIndex;
        result = prime * result + ( ( jobName == null ) ? 0 : jobName.hashCode() );
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
        JobNode other = (JobNode) obj;
        if ( jobIndex != other.jobIndex )
            return false;
        if ( jobName == null ) {
            if ( other.jobName != null )
                return false;
        } else if ( !jobName.equals( other.jobName ) )
            return false;
        return true;
    }

}
