package fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations;

import java.io.Serializable;
import java.util.ArrayList;

public class DifferentialEquations implements Serializable {
    /**
     * 
     */
    private static final long               serialVersionUID = -8681274020172728287L;

    private ArrayList<DifferentialEquation> differentialEquations;

    public DifferentialEquations() {
        differentialEquations = new ArrayList<DifferentialEquation>();
    }

    public void ajouter( DifferentialEquation differentialEquation ) {
        differentialEquations.add( differentialEquation );
    }

    public void vider() {
        differentialEquations.clear();
    }

    public DifferentialEquation get( int index ) {
        return differentialEquations.get( index );
    }

    public int size() {
        return differentialEquations.size();
    }

}
