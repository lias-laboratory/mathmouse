package fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.numericalmethod;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Permet de faire le mapping entre les fonctions mathématiques et les fonctions
 * au sein de l'équation Ex : remplacer le String "cos" par la fonction Math.cos
 * 
 * @author albertf
 *
 */
public class MathFunctionMapping {

    private static MathFunctionMapping INSTANCE = new MathFunctionMapping();
    private Map<String, Method>        methods  = new HashMap<String, Method>();

    private MathFunctionMapping() {
        // reflection
        Method[] methodsArray = Math.class.getMethods();
        for ( int i = 0; i < methodsArray.length; i++ ) {
            methods.put( methodsArray[i].getName(), methodsArray[i] );
        }
    }

    public static MathFunctionMapping getINSTANCE() {
        return INSTANCE;
    }

    /*
     * note: possibilité de "surcharger" le mapping Ex: si l'utilisateur utilise
     * le mot "cosinus" ou encore "COS" au lieu de "cos", le mapping avec
     * Math.cos ne se fera pas. A moins de l'ajouter manuellement.
     */
    public void addMethods( String name, Method method ) {
        this.methods.put( name, method );
    }

    public Method getMethod( String name ) {
        return this.methods.get( name );
    }

    public boolean haveMethod( String name ) {
        if ( methods.containsKey( name ) )
            return true;
        else
            return false;
    }
}
