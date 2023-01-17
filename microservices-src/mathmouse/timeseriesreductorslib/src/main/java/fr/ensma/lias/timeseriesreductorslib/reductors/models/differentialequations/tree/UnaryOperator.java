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
 * Classe représentant un opérateur unaire
 * 
 * @author albertf
 *
 */
@XmlAccessorType( XmlAccessType.FIELD )
@XmlType( name = "", propOrder = {
        "value",
        "LeftChild"
} )
@XmlRootElement( name = "UnaryOperator" )
public class UnaryOperator extends UnaryNode {

    public UnaryOperator() {
        super();
    }

    protected boolean containsOp( UnaryOperatorEnum[] ops ) {
        for ( int i = 0; i < ops.length; i++ ) {
            if ( ops[i].getValue().equals( this.value ) )
                return true;
        }
        return false;
    }

    public UnaryOperator( String value, NodeObject child ) {
        super( value, child );
    }

    @Override
    public int getOrder() {
        return this.child.getOrder();
    }

    @Override
    public boolean isLinear( Map<String, String> inputFunction ) {
        UnaryOperatorEnum ops[] = { UnaryOperatorEnum.ROOT, UnaryOperatorEnum.FACTO };
        if ( this.containsOp( ops ) )
            if ( this.child.hasUnknownFunction( inputFunction ) ) {
                return false;
            }
        return true;
    }

    @Override
    public boolean isLinear2( Map<FunctionKey, String> inputFunction ) {
        UnaryOperatorEnum ops[] = { UnaryOperatorEnum.ROOT, UnaryOperatorEnum.FACTO };
        if ( this.containsOp( ops ) )
            if ( this.child.hasUnknownFunction2( inputFunction ) ) {
                return false;
            }
        return true;
    }

    @Override
    public String toString() {
        return this.value + "(" + this.child + ")";
    }

    @Override
    public boolean hasFunction() {
        return child.hasFunction();
    }

    @Override
    public boolean hasUnknownFunction( Map<String, String> inputFunction ) {
        return this.child.hasUnknownFunction( inputFunction );
    }

    @Override
    public boolean hasUnknownFunction2( Map<FunctionKey, String> inputFunction ) {
        return this.child.hasUnknownFunction2( inputFunction );
    }

    @Override
    public String getUnknownFunction( Map<String, String> inputFunction ) throws Exception {
        return this.child.getUnknownFunction( inputFunction );
    }

    @Override
    public String getUnknownFunction2( InputFunctions inputFunctions ) throws Exception {
        return this.child.getUnknownFunction2( inputFunctions );
    }

    @Override
    public NodeObject transformEquationWithSystem( DiffEquaSystem diffEquaSystem ) {
        return new UnaryOperator( this.value, child.transformEquationWithSystem( diffEquaSystem ) );
    }

    @Override
    public NodeObject transformEquationWithSystem( ParametersSet parametersSet ) {
        return new UnaryOperator( this.value, child.transformEquationWithSystem( parametersSet ) );
    }

    @Override
    public boolean hasChildren() {
        return this.child != null;
    }

    @Override
    public boolean hasFunction( String name, int deriv ) {
        return this.child.hasFunction( name, deriv );
    }

    @Override
    public boolean hasFunction( String name ) {
        return this.child.hasFunction( name );
    }

    @Override
    public NodeObject findFunction( int i, String name ) {
        return this.child.findFunction( i, name );
    }

    public UnaryOperator clone() {
        if ( this.hasChildren() )
            return new UnaryOperator( this.value, this.child.clone() );
        else
            return new UnaryOperator( this.value, null );
    }

    @Override
    public NodeObject getCoefficient( int deriv, String name ) throws Exception {
        if ( this.child.hasChildren() )
            return new UnaryOperator( this.value, this.child.getCoefficient( deriv, name ) );
        else if ( this.child.isFunction( deriv, name ) )
            return new UnaryOperator( this.value, null );
        else
            return null;
    }

    @Override
    public boolean isFunction( int deriv, String name ) {
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
            return this.hasVariable( var );
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
        UnaryOperatorEnum ops[] = { UnaryOperatorEnum.ROOT, UnaryOperatorEnum.FACTO, UnaryOperatorEnum.MINUS,
                UnaryOperatorEnum.PLUS };
        UnaryOperatorEnum opsAdd[] = { UnaryOperatorEnum.MINUS, UnaryOperatorEnum.PLUS };
        if ( this.hasChildren() ) {
            if ( this.containsOp( ops ) && this.child instanceof Number )
                return true;
            else if ( this.containsOp( opsAdd ) && this.child instanceof UnaryOperator
                    && ( (UnaryOperator) this.child ).containsOp( opsAdd ) )
                return true;
            else
                return this.child.canBeSimplified();
        } else
            return false;
    }

    @Override
    public NodeObject simplify() throws NumberFormatException, Exception {
        UnaryOperatorEnum ops[] = { UnaryOperatorEnum.ROOT, UnaryOperatorEnum.FACTO, UnaryOperatorEnum.MINUS,
                UnaryOperatorEnum.PLUS };
        UnaryOperatorEnum opsAdd[] = { UnaryOperatorEnum.MINUS, UnaryOperatorEnum.PLUS };
        if ( this.hasChildren() ) {
            if ( this.containsOp( ops ) && this.child instanceof Number )
                return new Number(
                        new Double( UnaryOperatorEnum.compute( Double.parseDouble( this.child.value ), this.value ) )
                                .toString() );
            else if ( this.containsOp( opsAdd ) && this.child instanceof UnaryOperator
                    && ( (UnaryOperator) this.child ).containsOp( opsAdd ) )
                return this.simplifyAdd();
            else
                return new UnaryOperator( this.value, this.child.simplify() );
        } else if ( this.value.equals( UnaryOperatorEnum.MINUS.getValue() ) )
            return new Number( "-1" );
        else if ( this.value.equals( UnaryOperatorEnum.PLUS.getValue() ) ) {
            return new Number( "1" );
        } else
            return this.clone();
    }

    private NodeObject simplifyAdd() {
        if ( this.value.equals( "+" ) && this.value.equals( this.child.getValue() ) )
            return this.child.clone();
        else if ( this.value.equals( "-" ) && this.value.equals( this.child.getValue() ) )
            return new UnaryOperator( "+", ( (UnaryOperator) this.child ).getLeftPart() );
        else
            return new UnaryOperator( "-", ( (UnaryOperator) this.child ).getLeftPart() );
    }

    @Override
    public NodeObject replaceVar( String var, NodeObject elt ) {
        if ( !this.hasChildren() )
            return this.clone();
        else
            return new UnaryOperator( this.value, this.child.replaceVar( var, elt ) );
    }

    @Override
    public NodeObject replaceFunctionByVar( String func, double var ) {
        if ( !this.hasChildren() )
            return this.clone();
        else
            return new UnaryOperator( this.value, this.child.replaceFunctionByVar( func, var ) );
    }

    @Override
    public NodeObject replaceFunctionByVar2( FunctionKey func, double var ) {
        if ( !this.hasChildren() )
            return this.clone();
        else
            return new UnaryOperator( this.value, this.child.replaceFunctionByVar2( func, var ) );
    }

    @Override
    public boolean equals( Object obj ) {
        if ( !( obj instanceof UnaryOperator ) )
            return false;
        else if ( !( (UnaryOperator) obj ).getValue().equals( this.value ) )
            return false;
        else if ( !( this.child == null && ( (UnaryOperator) obj ).getLeftPart() == null ) )
            return false;
        else if ( !this.child.equals( ( (UnaryOperator) obj ).getLeftPart() ) )
            return false;
        else
            return true;
    }

    @Override
    public NodeObject replaceFunctionByVar( String func, int deriv, double var ) {
        if ( !this.hasChildren() )
            return this.clone();
        else
            return new UnaryOperator( this.value, this.child.replaceFunctionByVar( func, deriv, var ) );
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
        if ( !this.hasChildren() )
            return this.clone();
        else
            return new UnaryOperator( this.value, this.child.replaceFunctionByNode( func, deriv, newNode ) );
    }

    @Override
    public NodeObject replaceFunctionByVector( String funcname,
            Map<Integer, NodeObject> v ) {
        if ( this.hasChildren() )
            return new UnaryOperator( this.value, child.replaceFunctionByVector( funcname, v ) );
        return this.clone();
    }

    @Override
    public EFunctionRole getFunctionRole() {
        return null;
    }

    @Override
    public boolean computable() {
        return this.child.computable();
    }
}
