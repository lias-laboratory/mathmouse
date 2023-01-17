package fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.tree;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAttribute;

import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.DiffEquaSystem;
import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.FunctionKey;
import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.InputFunctions;
import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.ParametersSet;

/**
 * Classe abstraite représentant un noeud d'une équation
 * 
 * @author albertf
 *
 */
public abstract class NodeObject implements Cloneable, Serializable {
    protected static final int ERREUR           = Integer.MIN_VALUE;

    public static final String PACKAGE_NAME     = "fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.tree";

    /**
     * 
     */
    private static final long  serialVersionUID = -1270155724636007398L;

    protected Long             id;

    public Long getId() {
        return id;
    }

    public void setId( Long id ) {
        this.id = id;
    }

    @XmlAttribute( name = "value", required = true )
    protected String value;

    public String getValue() {
        return value;
    }

    public void setValue( String value ) {
        this.value = value;
    }

    public NodeObject( String value ) {
        super();
        this.value = value;
    }

    public NodeObject() {
    }

    public abstract EFunctionRole getFunctionRole();

    public void setFunctionRole( EFunctionRole role ) {

    }

    public int getDeriv() {
        return ERREUR;
    }

    public void setDeriv( int deriv ) {

    }

    public void setLeftChild( NodeObject child ) {

    }

    public void setRightChild( NodeObject child ) {

    }

    public abstract int getOrder();

    public abstract boolean computable();

    public abstract boolean isLinear( Map<String, String> inputFunction );

    public abstract boolean isLinear2( Map<FunctionKey, String> inputFunction );

    @Override
    public abstract String toString();

    public abstract boolean hasFunction();

    /**
     * @param inputFunction
     * @return True, if some function cannot be mapped to a real mathematical
     *         function of the language
     */
    public abstract boolean hasUnknownFunction( Map<String, String> inputFunction );

    public abstract boolean hasUnknownFunction2( Map<FunctionKey, String> inputFunction );

    public abstract String getUnknownFunction( Map<String, String> inputFunction ) throws Exception;

    public abstract String getUnknownFunction2( InputFunctions inputFunctions ) throws Exception;

    public abstract NodeObject transformEquationWithSystem( DiffEquaSystem diffEquaSystem );

    public abstract NodeObject transformEquationWithSystem( ParametersSet parametersSet );

    public abstract boolean hasChildren();

    /**
     * @param name
     * @param deriv
     * @return True, if the specified function, derived at the specified order
     *         is in the equation
     */
    public abstract boolean hasFunction( String name, int deriv );

    /**
     * 
     * @param name
     * @return True, if the specified function appears in the equation, whatever
     *         its derivation order
     */
    public abstract boolean hasFunction( String name );

    public abstract NodeObject findFunction( int deriv, String name );

    @Override
    public abstract NodeObject clone();

    @Override
    public abstract boolean equals( Object obj );

    public abstract NodeObject getCoefficient( int deriv, String name ) throws Exception;

    public abstract boolean isFunction( int deriv, String name );

    /**
     * Variable différent de x
     * 
     * @return True si l'équation possède des variables (différents de x), false
     *         sinon
     */
    public abstract boolean hasUnknownVariable( Map<String, String> mapping );

    public abstract boolean hasUnknownVariable2( Map<String, Double> mapping );

    public abstract boolean hasVariable( String var );

    public abstract List<String> getVariable();

    public abstract boolean canBeSimplified();

    public abstract NodeObject simplify() throws Exception;

    /**
     * 
     * Returns a copy of the equation tree, where the variables var have been
     * replaced by the element elt.
     * 
     * @param var,
     *            variable to modify
     * @param elt,
     *            element to replace variable with
     * @return
     */
    public abstract NodeObject replaceVar( String var, NodeObject elt );

    public abstract NodeObject replaceFunctionByVar( String func, double var );

    public abstract NodeObject replaceFunctionByVar2( FunctionKey func, double var );

    public abstract NodeObject replaceFunctionByVar( String func, int deriv, double var );

    public abstract NodeObject replaceNode( NodeObject oldNode, NodeObject newNode );

    public abstract NodeObject replaceFunctionByNode( String func, int deriv, NodeObject newNode );

    /**
     * Gives the right child of the current node
     * 
     * @return null if no right child
     */
    public abstract NodeObject getRightPart();

    /**
     * Gives the left child of the current node. If the node has only one unique
     * child, it is considered as the left child.
     * 
     * @return null if no left child (meaning node has no children)
     */
    public abstract NodeObject getLeftPart();

    /**
     * Permet de remplacer la ou les fonctions de l'équation par les composantes
     * d'un vecteur
     * 
     * @param funcname
     *            Nom de la fonction
     * @param v
     *            Vecteur
     * @return
     */
    public abstract NodeObject replaceFunctionByVector( String funcname, Map<Integer, NodeObject> v );
}
