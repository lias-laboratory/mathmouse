package fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.DiffEquaSystem;
import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.FunctionKey;
import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.InputFunctions;
import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.ParametersSet;
import fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.numericalmethod.MathFunctionMapping;;

/**
 * Classe représentant une fonction (forces, etc.)
 * 
 * @author albertf
 *
 */
@XmlAccessorType( XmlAccessType.FIELD )
@XmlType( name = "", propOrder = {
        "value",
        "deriv",
        "role",
        "LeftChild"
} )
@XmlRootElement( name = "Function" )
public class Function extends UnaryNode {

    public Function() {
        super();
    }

    @XmlAttribute( name = "deriv", required = true )
    private int           deriv = 0;

    @XmlAttribute( name = "role", required = true )
    private EFunctionRole functionRole;

    public Function( String value, int deriv, NodeObject child, EFunctionRole role ) {
        super( value, child );
        this.deriv = deriv;
    }

    public Function( String value, int deriv, NodeObject child ) {
        super( value, child );
        this.deriv = deriv;
    }

    public Function( String value, NodeObject child ) {
        super( value, child );
    }

    public Function( String value, NodeObject child, EFunctionRole role ) {
        super( value, child );
    }

    public Function( String value, int nbDeriv ) {
        super( value );
        this.deriv = nbDeriv;
    }

    public Function( String value, int nbDeriv, EFunctionRole role ) {
        super( value );
        this.deriv = nbDeriv;
    }

    @Override
    public int getDeriv() {
        return deriv;
    }

    @Override
    public void setDeriv( int deriv ) {
        this.deriv = deriv;
    }

    @Override
    public int getOrder() {
        if ( this.child != null )
            return Math.max( deriv, this.child.getOrder() );
        else
            return deriv;
    }

    @Override
    public boolean isLinear( Map<String, String> inputFunction ) {
        if ( this.child != null && this.child.hasUnknownFunction( inputFunction ) )
            return false;
        else
            return true;
    }

    @Override
    public boolean isLinear2( Map<FunctionKey, String> inputFunction ) {
        if ( this.child != null && this.child.hasUnknownFunction2( inputFunction ) )
            return false;
        else
            return true;
    }

    @Override
    public String toString() {
        String prime = "";
        for ( int i = 0; i < deriv; i++ )
            prime += "'";
        if ( this.child != null )
            return this.value + prime + "(" + this.child + ")";
        else
            return this.value + prime;
    }

    @Override
    public boolean hasFunction() {
        return true;
    }

    @Override
    public boolean hasUnknownFunction( Map<String, String> inputFunction ) {
        MathFunctionMapping map = MathFunctionMapping.getINSTANCE();
        if ( map.haveMethod( this.value ) || ( inputFunction != null && inputFunction.containsKey( this.value ) ) )
            if ( this.child != null )
                return false || this.child.hasUnknownFunction( inputFunction );
            else
                return false;
        else
            return true;
    }

    @Override
    public boolean hasUnknownFunction2( Map<FunctionKey, String> inputFunction ) {
        MathFunctionMapping map = MathFunctionMapping.getINSTANCE();
        if ( map.haveMethod( this.value ) || ( inputFunction != null && inputFunction.containsKey( this.value ) ) )
            if ( this.child != null )
                return false || this.child.hasUnknownFunction2( inputFunction );
            else
                return false;
        else
            return true;
    }

    @Override
    public String getUnknownFunction( Map<String, String> inputFunction ) throws Exception {
        MathFunctionMapping map = MathFunctionMapping.getINSTANCE();
        if ( map.haveMethod( this.value ) || ( inputFunction != null && inputFunction.containsKey( this.value ) ) ) {
            if ( this.child == null || this.child.getUnknownFunction( inputFunction ) == null )
                return null;
            else
                return this.child.getUnknownFunction( inputFunction );
        } else {
            if ( this.child == null || this.child.getUnknownFunction( inputFunction ) == null )
                return this.value;
            else if ( this.child.getUnknownFunction( inputFunction ).equals( this.value ) )
                return this.value;
            else
                throw new Exception( "Many unknown functions" );
        }
    }

    @Override
    public String getUnknownFunction2( InputFunctions inputFunctions ) throws Exception {
        MathFunctionMapping map = MathFunctionMapping.getINSTANCE();
        if ( map.haveMethod( this.value )
                || ( inputFunctions != null && inputFunctions.containsFunctionName( this.value ) ) ) {
            if ( this.child == null || this.child.getUnknownFunction2( inputFunctions ) == null )
                return null;
            else
                return this.child.getUnknownFunction2( inputFunctions );
        } else {
            if ( this.child == null || this.child.getUnknownFunction2( inputFunctions ) == null )
                return this.value;
            else if ( this.child.getUnknownFunction2( inputFunctions ).equals( this.value ) )
                return this.value;
            else
                throw new Exception( "Many unknown functions" );
        }
    }

    @Override
    public NodeObject transformEquationWithSystem( DiffEquaSystem diffEquaSystem ) {
        if ( this.child != null )
            return new Function( this.value, this.deriv, child.transformEquationWithSystem( diffEquaSystem ) );
        else
            return this.clone();
    }

    @Override
    public NodeObject transformEquationWithSystem( ParametersSet parametersSet ) {
        if ( this.child != null )
            return new Function( this.value, this.deriv, child.transformEquationWithSystem( parametersSet ) );
        else
            return this.clone();
    }

    @Override
    public boolean hasChildren() {
        return this.child != null;
    }

    @Override
    public boolean hasFunction( String name, int deriv ) {
        if ( this.value.equals( name ) && this.deriv == deriv )
            return true;
        else
            return false;
    }

    @Override
    public boolean hasFunction( String name ) {
        if ( this.value.equals( name ) )
            return true;
        else
            return false;
    }

    @Override
    public NodeObject findFunction( int i, String name ) {
        if ( this.deriv == i && this.value.equals( name ) )
            return this.clone();
        return null;
    }

    public Function clone() {
        if ( this.child != null )
            return new Function( this.value, this.deriv, this.child.clone() );
        else
            return new Function( this.value, this.deriv );
    }

    @Override
    public NodeObject getCoefficient( int deriv, String name ) throws Exception {
        return null;
    }

    @Override
    public boolean isFunction( int deriv, String name ) {
        if ( this.deriv == deriv && this.value.equals( name ) )
            return true;
        return false;
    }

    @Override
    public boolean hasUnknownVariable( Map<String, String> mapping ) {
        if ( !this.hasChildren() )
            return false;
        else
            return this.child.hasUnknownVariable( mapping );
    }

    @Override
    public boolean hasUnknownVariable2( Map<String, Double> mapping ) {
        if ( !this.hasChildren() )
            return false;
        else
            return this.child.hasUnknownVariable2( mapping );
    }

    @Override
    public boolean hasVariable( String var ) {
        if ( this.hasChildren() )
            return this.child.hasVariable( var );
        return false;
    }

    @Override
    public List<String> getVariable() {
        if ( this.hasChildren() )
            return this.child.getVariable();
        return new ArrayList<String>();
    }

    @Override
    public boolean canBeSimplified() {
        if ( this.hasChildren() && this.child instanceof Number )
            return true;
        else if ( this.hasChildren() )
            return this.child.canBeSimplified();
        else
            return false;
    }

    @Override
    public NodeObject simplify() throws Exception {
        if ( this.hasChildren() )
            if ( this.child instanceof Number && MathFunctionMapping.getINSTANCE().haveMethod( this.value ) )
                return new Number( MathFunctionMapping.getINSTANCE().getMethod( this.value )
                        .invoke( null, Double.parseDouble( this.child.getValue() ) ).toString() );
            else
                return new Function( this.value, this.child.simplify() );
        else
            return this.clone();
    }

    @Override
    public NodeObject replaceVar( String var, NodeObject elt ) {
        if ( this.hasChildren() )
            return new Function( this.value, this.child.replaceVar( var, elt ) );
        else
            return this.clone();
    }

    @Override
    public NodeObject replaceFunctionByVar( String func, double var ) {
        if ( this.value.equals( func ) )
            return new Number( new Double( var ).toString() );
        else if ( this.hasChildren() )
            return new Function( this.value, this.deriv, this.child.replaceFunctionByVar( func, var ) );
        else
            return this.clone();
    }

    @Override
    public NodeObject replaceFunctionByVar2( FunctionKey func, double var ) {
        if ( this.value.equals( func.getName() ) && this.deriv == func.getDeriv() )
            return new Number( new Double( var ).toString() );
        else if ( this.hasChildren() )
            return new Function( this.value, this.deriv, this.child.replaceFunctionByVar2( func, var ) );
        else
            return this.clone();
    }

    @Override
    public boolean equals( Object obj ) {
        if ( !( obj instanceof Function ) )
            return false;
        else if ( !( (Function) obj ).getValue().equals( this.value ) )
            return false;
        else if ( ( (Function) obj ).getDeriv() != this.deriv )
            return false;
        else if ( this.child == null && ( (Function) obj ).getLeftPart() != null )
            return false;
        else if ( this.child != null && ( (Function) obj ).getLeftPart() == null )
            return false;
        else if ( this.child != null && ( (Function) obj ).getLeftPart() != null )
            if ( !this.child.equals( ( (Function) obj ).getLeftPart() ) )
                return false;
        return true;
    }

    @Override
    public NodeObject replaceFunctionByVar( String func, int deriv, double var ) {
        if ( this.value.equals( func ) && this.deriv == deriv )
            return new Number( new Double( var ).toString() );
        else if ( this.hasChildren() )
            return new Function( this.value, this.deriv, this.child.replaceFunctionByVar( func, deriv, var ) );
        else
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
        if ( this.value.equals( func ) && this.deriv == deriv )
            return newNode.clone();
        else if ( this.hasChildren() )
            return new Function( this.value, this.deriv, this.child.replaceFunctionByNode( func, deriv, newNode ) );
        else
            return this.clone();
    }

    @Override
    public NodeObject replaceFunctionByVector( String funcname,
            Map<Integer, NodeObject> v ) {
        if ( this.value.equals( funcname ) )
            if ( v.get( this.deriv + 1 ) != null )
                return v.get( this.deriv + 1 ).clone();
        if ( this.hasChildren() )
            return new Function( this.value, this.deriv, this.child.replaceFunctionByVector( funcname, v ) );
        else
            return this.clone();
    }

    @Override
    public EFunctionRole getFunctionRole() {
        return functionRole;
    }

    @Override
    public void setFunctionRole( EFunctionRole role ) {
        functionRole = role;
    }

    @Override
    public boolean computable() {
        return false;
    }

}
