package fr.ensma.lias.dbgui.kernel.models;

import fr.ensma.lias.timeseriesreductorslib.comparison.ENotification;

public class Result {
    private String        equationName;
    private String        formula;
    private long          idEquation;
    private ENotification notification;
    private double        avgError;
    private double        minError;
    private double        maxError;
    private double        sdError;

    public Result( String equationName, String formula, long idEquation, ENotification notification, double avgError,
            double minError, double maxError, double sdError ) {
        super();
        this.equationName = equationName;
        this.formula = formula;
        this.idEquation = idEquation;
        this.notification = notification;
        this.avgError = avgError;
        this.minError = minError;
        this.maxError = maxError;
        this.sdError = sdError;
    }

    public String getEquationName() {
        return equationName;
    }

    public void setEquationName( String equationName ) {
        this.equationName = equationName;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula( String formula ) {
        this.formula = formula;
    }

    public long getIdEquation() {
        return idEquation;
    }

    public void setIdEquation( long idEquation ) {
        this.idEquation = idEquation;
    }

    public ENotification getNotification() {
        return notification;
    }

    public void setNotification( ENotification notification ) {
        this.notification = notification;
    }

    public double getAvgError() {
        return avgError;
    }

    public void setAvgError( double avgError ) {
        this.avgError = avgError;
    }

    public double getMinError() {
        return minError;
    }

    public void setMinError( double minError ) {
        this.minError = minError;
    }

    public double getMaxError() {
        return maxError;
    }

    public void setMaxError( double maxError ) {
        this.maxError = maxError;
    }

    public double getSdError() {
        return sdError;
    }

    public void setSdError( double sdError ) {
        this.sdError = sdError;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits( avgError );
        result = prime * result + (int) ( temp ^ ( temp >>> 32 ) );
        result = prime * result + ( ( equationName == null ) ? 0 : equationName.hashCode() );
        result = prime * result + ( ( formula == null ) ? 0 : formula.hashCode() );
        result = prime * result + (int) ( idEquation ^ ( idEquation >>> 32 ) );
        temp = Double.doubleToLongBits( maxError );
        result = prime * result + (int) ( temp ^ ( temp >>> 32 ) );
        temp = Double.doubleToLongBits( minError );
        result = prime * result + (int) ( temp ^ ( temp >>> 32 ) );
        result = prime * result + ( ( notification == null ) ? 0 : notification.hashCode() );
        temp = Double.doubleToLongBits( sdError );
        result = prime * result + (int) ( temp ^ ( temp >>> 32 ) );
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
        Result other = (Result) obj;
        if ( Double.doubleToLongBits( avgError ) != Double.doubleToLongBits( other.avgError ) )
            return false;
        if ( equationName == null ) {
            if ( other.equationName != null )
                return false;
        } else if ( !equationName.equals( other.equationName ) )
            return false;
        if ( formula == null ) {
            if ( other.formula != null )
                return false;
        } else if ( !formula.equals( other.formula ) )
            return false;
        if ( idEquation != other.idEquation )
            return false;
        if ( Double.doubleToLongBits( maxError ) != Double.doubleToLongBits( other.maxError ) )
            return false;
        if ( Double.doubleToLongBits( minError ) != Double.doubleToLongBits( other.minError ) )
            return false;
        if ( notification != other.notification )
            return false;
        if ( Double.doubleToLongBits( sdError ) != Double.doubleToLongBits( other.sdError ) )
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Result [equationName=" + equationName + ", formula=" + formula + ", idEquation=" + idEquation
                + ", notification=" + notification + ", avgError=" + avgError + ", minError=" + minError + ", maxError="
                + maxError + ", sdError=" + sdError + "]";
    }

}
