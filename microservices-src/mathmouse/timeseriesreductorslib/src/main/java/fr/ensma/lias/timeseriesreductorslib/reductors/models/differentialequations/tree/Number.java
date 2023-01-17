package fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.DiffEquaSystem;
import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.FunctionKey;
import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.InputFunctions;
import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.ParametersSet;

/**
 * Classe représentant un nombre
 * 
 * @author albertf
 *
 */
@XmlAccessorType( XmlAccessType.FIELD )
@XmlType( name = "", propOrder = {
        "value"
} )
@XmlRootElement( name = "Number" )
public class Number extends NodeObject {
    double doubleValue;

    public Number() {
        super();
    }

    public Number( String value ) {
        super( value );
        doubleValue = Double.parseDouble( value );
    }

    public Number( double value ) {
        super( String.valueOf( value ) );
        doubleValue = value;
    }

    public double getDoubleValue() {
        return doubleValue;
    }

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public boolean isLinear( Map<String, String> inputFunction ) {
        return true;
    }

    @Override
    public boolean isLinear2( Map<FunctionKey, String> inputFunction ) {
        return true;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean hasFunction() {
        return false;
    }

    @Override
    public boolean hasUnknownFunction( Map<String, String> inputFunction ) {
        return false;
    }

    @Override
    public boolean hasUnknownFunction2( Map<FunctionKey, String> inputFunction ) {
        return false;
    }

    @Override
    public String getUnknownFunction( Map<String, String> inputFunction ) {
        return null;
    }

    @Override
    public String getUnknownFunction2( InputFunctions inputFunctions ) throws Exception {
        return null;
    }

    @Override
    public NodeObject transformEquationWithSystem( DiffEquaSystem diffEquaSystem ) {
        return this.clone();
    }

    @Override
    public NodeObject transformEquationWithSystem( ParametersSet parametersSet ) {
        return this.clone();
    }

    @Override
    public boolean hasChildren() {
        return false;
    }

    @Override
    public boolean hasFunction( String name, int deriv ) {
        return false;
    }

    @Override
    public boolean hasFunction( String name ) {
        return false;
    }

    @Override
    public NodeObject findFunction( int i, String name ) {
        return null;
    }

    public Number clone() {
        return new Number( this.value );
    }

    @Override
    public NodeObject getCoefficient( int deriv, String name ) throws Exception {
        return null;
    }

    @Override
    public boolean isFunction( int deriv, String name ) {
        return false;
    }

    @Override
    public boolean hasUnknownVariable( Map<String, String> mapping ) {
        return false;
    }

    @Override
    public boolean hasUnknownVariable2( Map<String, Double> mapping ) {
        return false;
    }

    @Override
    public boolean hasVariable( String var ) {
        return false;
    }

    @Override
    public List<String> getVariable() {
        return new ArrayList<String>();
    }

    @Override
    public boolean canBeSimplified() {
        return false;
    }

    @Override
    public NodeObject simplify() throws Exception {
        return this.clone();
    }

    @Override
    public NodeObject replaceVar( String var, NodeObject elt ) {
        return this.clone();
    }

    @Override
    public NodeObject replaceFunctionByVar( String func, double var ) {
        return this.clone();
    }

    @Override
    public NodeObject replaceFunctionByVar2( FunctionKey func, double var ) {
        return this.clone();
    }

    @Override
    public boolean equals( Object obj ) {
        if ( !( obj instanceof Number ) )
            return false;
        else if ( !this.value.equals( ( (Number) obj ).getValue() ) )
            return false;
        else
            return true;
    }

    @Override
    public NodeObject replaceFunctionByVar( String func, int deriv, double var ) {
        return this.clone();
    }

    @Override
    public NodeObject replaceNode( NodeObject oldNode, NodeObject newNode ) {
        if ( this.equals( oldNode ) )
            return newNode;
        else
            return this.clone();
    }

    @Override
    public NodeObject getRightPart() {
        return null;
    }

    @Override
    public NodeObject getLeftPart() {
        return null;
    }

    @Override
    public NodeObject replaceFunctionByNode( String func, int deriv,
            NodeObject newNode ) {
        return this.clone();
    }

    @Override
    public NodeObject replaceFunctionByVector( String funcname,
            Map<Integer, NodeObject> v ) {
        return this.clone();
    }

    @Override
    public EFunctionRole getFunctionRole() {
        return null;
    }

    @Override
    public boolean computable() {
        return true;
    }
}
