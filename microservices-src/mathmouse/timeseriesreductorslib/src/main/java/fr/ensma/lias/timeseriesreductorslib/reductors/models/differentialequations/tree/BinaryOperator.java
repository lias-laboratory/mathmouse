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
 * Classe représentant un noeud binaire définissant un opérateur binaire (ex: +,
 * -, *, /, etc.)
 * 
 * @author albertf
 *
 */
@XmlAccessorType( XmlAccessType.FIELD )
@XmlType( name = "", propOrder = {
        "value",
        "LeftChild",
        "RightChild"
} )
@XmlRootElement( name = "BinaryOperator" )
public class BinaryOperator extends BinaryNode {

    public BinaryOperator() {
        super();
    }

    private boolean containsOp( BinaryOperatorEnum ops[] ) {
        for ( int i = 0; i < ops.length; i++ ) {
            if ( ops[i].getValue().equals( this.value ) )
                return true;
        }
        return false;
    }

    public BinaryOperator( String value, NodeObject left, NodeObject right ) {
        super( value, left, right );
    }

    @Override
    public int getOrder() {
        return Math.max( left.getOrder(), right.getOrder() );
    }

    @Override
    public boolean isLinear( Map<String, String> inputFunction ) {
        BinaryOperatorEnum ops[] = { BinaryOperatorEnum.DIVI, BinaryOperatorEnum.POW, BinaryOperatorEnum.STAR,
                BinaryOperatorEnum.ROOT };
        if ( this.containsOp( ops ) )
            if ( this.left.hasUnknownFunction( inputFunction ) && this.right.hasUnknownFunction( inputFunction ) )
                return false;
            else if ( ( this.left.hasUnknownFunction( inputFunction )
                    || this.right.hasUnknownFunction( inputFunction ) )
                    && this.value.equals( BinaryOperatorEnum.POW.name() ) )
                return false;
            else
                return true;
        else
            return this.left.isLinear( inputFunction ) && this.right.isLinear( inputFunction );
    }

    @Override
    public boolean isLinear2( Map<FunctionKey, String> inputFunction ) {
        BinaryOperatorEnum ops[] = { BinaryOperatorEnum.DIVI, BinaryOperatorEnum.POW, BinaryOperatorEnum.STAR,
                BinaryOperatorEnum.ROOT };
        if ( this.containsOp( ops ) )
            if ( this.left.hasUnknownFunction2( inputFunction ) && this.right.hasUnknownFunction2( inputFunction ) )
                return false;
            else if ( ( this.left.hasUnknownFunction2( inputFunction )
                    || this.right.hasUnknownFunction2( inputFunction ) )
                    && this.value.equals( BinaryOperatorEnum.POW.name() ) )
                return false;
            else
                return true;
        else
            return this.left.isLinear2( inputFunction ) && this.right.isLinear2( inputFunction );
    }

    @Override
    public String toString() {
        return "(" + this.left + this.value + this.right + ")";
    }

    @Override
    public boolean hasFunction() {
        return left.hasFunction() || right.hasFunction();
    }

    @Override
    public boolean hasUnknownFunction( Map<String, String> inputFunction ) {
        return this.left.hasUnknownFunction( inputFunction ) || this.right.hasUnknownFunction( inputFunction );
    }

    @Override
    public boolean hasUnknownFunction2( Map<FunctionKey, String> inputFunction ) {
        return this.left.hasUnknownFunction2( inputFunction ) || this.right.hasUnknownFunction2( inputFunction );
    }

    @Override
    public String getUnknownFunction( Map<String, String> inputFunction ) throws Exception {
        if ( this.left.getUnknownFunction( inputFunction ) == null )
            return this.right.getUnknownFunction( inputFunction );
        else if ( this.right.getUnknownFunction( inputFunction ) == null )
            return this.left.getUnknownFunction( inputFunction );
        else if ( this.right.getUnknownFunction( inputFunction )
                .equals( this.left.getUnknownFunction( inputFunction ) ) )
            return this.right.getUnknownFunction( inputFunction );
        else
            throw new Exception( "Many unknown functions" );
    }

    @Override
    public String getUnknownFunction2( InputFunctions inputFunctions ) throws Exception {
        if ( this.left.getUnknownFunction2( inputFunctions ) == null )
            return this.right.getUnknownFunction2( inputFunctions );
        else if ( this.right.getUnknownFunction2( inputFunctions ) == null )
            return this.left.getUnknownFunction2( inputFunctions );
        else if ( this.right.getUnknownFunction2( inputFunctions )
                .equals( this.left.getUnknownFunction2( inputFunctions ) ) )
            return this.right.getUnknownFunction2( inputFunctions );
        else
            throw new Exception( "Many unknown functions" );
    }

    @Override
    public NodeObject transformEquationWithSystem( DiffEquaSystem diffEquaSystem ) {
        return new BinaryOperator( this.value, left.transformEquationWithSystem( diffEquaSystem ),
                right.transformEquationWithSystem( diffEquaSystem ) );
    }

    @Override
    public NodeObject transformEquationWithSystem( ParametersSet parametersSet ) {
        return new BinaryOperator( this.value, left.transformEquationWithSystem( parametersSet ),
                right.transformEquationWithSystem( parametersSet ) );
    }

    @Override
    public boolean hasChildren() {
        return ( this.left != null ) || ( this.right != null );
    }

    @Override
    public boolean hasFunction( String name, int deriv ) {
        return this.left.hasFunction( name, deriv ) || this.right.hasFunction( name, deriv );
    }

    @Override
    public boolean hasFunction( String name ) {
        return this.left.hasFunction( name ) || this.right.hasFunction( name );
    }

    @Override
    public NodeObject findFunction( int i, String name ) {
        BinaryOperatorEnum ops[] = { BinaryOperatorEnum.DIVI, BinaryOperatorEnum.STAR };
        if ( this.containsOp( ops ) ) {
            if ( this.left.hasFunction( name, i ) || this.right.hasFunction( name, i ) )
                return this.clone();
            else
                return null;
        } else if ( this.left.hasFunction( name, i ) ) {
            if ( !this.left.isFunction( i, name ) )
                return this.left.findFunction( i, name );
            else if ( this.value.equals( "-" ) ) {
                return new UnaryOperator( "+", this.left.clone() );
            } else
                return new UnaryOperator( this.value, this.left.clone() );
        } else if ( this.right.hasFunction( name, i ) ) {
            if ( !this.right.isFunction( i, name ) ) {
                return new UnaryOperator( this.value, this.right.findFunction( i, name ) );
            } else {
                return new UnaryOperator( this.value, this.right.clone() );
            }
        } else
            return null;
    }

    public BinaryOperator clone() {
        return new BinaryOperator( this.value, left.clone(), right.clone() );
    }

    @Override
    public NodeObject getCoefficient( int deriv, String name ) throws Exception {
        BinaryOperatorEnum ops[] = { BinaryOperatorEnum.DIVI, BinaryOperatorEnum.STAR };
        if ( this.right.hasFunction( name, deriv ) )
            if ( this.right.hasChildren() )
                return new BinaryOperator( this.value, this.left.clone(), this.right.getCoefficient( deriv, name ) );
            else if ( this.containsOp( ops ) )
                return this.left.clone();
            else
                return new UnaryOperator( this.value, this.left.clone() );
        else if ( this.left.hasFunction( name, deriv ) )
            if ( this.left.hasChildren() )
                return new BinaryOperator( this.value, this.left.getCoefficient( deriv, name ), this.right.clone() );
            else if ( this.containsOp( ops ) )
                return this.right.clone();
            else
                return new UnaryOperator( this.value, this.right.clone() );
        else
            throw new Exception( "Error : getCoefficient" );

    }

    @Override
    public boolean isFunction( int deriv, String name ) {
        return false;
    }

    @Override
    public boolean hasUnknownVariable( Map<String, String> mapping ) {
        return this.left.hasUnknownVariable( mapping ) || this.right.hasUnknownVariable( mapping );
    }

    @Override
    public boolean hasUnknownVariable2( Map<String, Double> mapping ) {
        return this.left.hasUnknownVariable2( mapping ) || this.right.hasUnknownVariable2( mapping );
    }

    @Override
    public boolean hasVariable( String var ) {
        return this.left.hasVariable( var ) || this.right.hasVariable( var );
    }

    @Override
    public List<String> getVariable() {
        List<String> variables = new ArrayList<String>();
        variables.addAll( this.left.getVariable() );
        variables.addAll( this.right.getVariable() );
        return variables;
    }

    @Override
    public boolean canBeSimplified() {
        BinaryOperatorEnum ops[] = { BinaryOperatorEnum.PLUS, BinaryOperatorEnum.MINUS };
        UnaryOperatorEnum opsAdd[] = { UnaryOperatorEnum.PLUS, UnaryOperatorEnum.MINUS };
        if ( this.left instanceof Number && this.right instanceof Number )
            return true;
        else if ( this.right instanceof Number && Double.parseDouble( this.right.value ) == 1.0 )
            return true;
        else if ( this.right instanceof UnaryOperator && this.containsOp( ops )
                && ( (UnaryOperator) this.right ).containsOp( opsAdd ) )
            return true;
        else
            return this.left.canBeSimplified() || this.right.canBeSimplified();
    }

    @Override
    public NodeObject simplify() throws Exception {
        BinaryOperatorEnum ops[] = { BinaryOperatorEnum.PLUS, BinaryOperatorEnum.MINUS };
        UnaryOperatorEnum opsAdd[] = { UnaryOperatorEnum.PLUS, UnaryOperatorEnum.MINUS };
        if ( this.left instanceof Number && this.right instanceof Number ) {
            double val1 = Double.parseDouble( left.getValue() );
            double val2 = Double.parseDouble( right.getValue() );
            return new Number( new Double( BinaryOperatorEnum.compute( val1, val2, this.value ) ).toString() );
        } else if ( this.right instanceof Number && Double.parseDouble( this.right.value ) == 1.0 ) {
            return this.left.clone();
        } else if ( this.right instanceof UnaryOperator && this.containsOp( ops )
                && ( (UnaryOperator) this.right ).containsOp( opsAdd ) )
            return this.simplifyAdd();
        else
            return new BinaryOperator( this.value, this.left.simplify(), this.right.simplify() );
    }

    private NodeObject simplifyAdd() {
        if ( this.value.equals( this.right ) && this.value.equals( "+" ) )
            return new BinaryOperator( "+", this.left.clone(), ( (UnaryOperator) this.right ).getLeftPart().clone() );
        else if ( this.value.equals( this.right ) && this.value.equals( "-" ) )
            return new BinaryOperator( "+", this.left.clone(), ( (UnaryOperator) this.right ).getLeftPart().clone() );
        else
            return new BinaryOperator( "-", this.left.clone(), ( (UnaryOperator) this.right ).getLeftPart().clone() );

    }

    @Override
    public NodeObject replaceVar( String var, NodeObject elt ) {
        return new BinaryOperator( this.value, this.left.replaceVar( var, elt ), this.right.replaceVar( var, elt ) );
    }

    @Override
    public NodeObject replaceFunctionByVar( String func, double var ) {
        return new BinaryOperator( this.value, this.left.replaceFunctionByVar( func, var ),
                this.right.replaceFunctionByVar( func, var ) );
    }

    @Override
    public NodeObject replaceFunctionByVar2( FunctionKey func, double var ) {
        return new BinaryOperator( this.value, this.left.replaceFunctionByVar2( func, var ),
                this.right.replaceFunctionByVar2( func, var ) );
    }

    @Override
    public boolean equals( Object obj ) {
        if ( obj.getClass() != this.getClass() )
            return false;
        else if ( !( obj instanceof BinaryOperator ) )
            return false;
        else if ( !( (BinaryOperator) obj ).getValue().equals( this.value ) )
            return false;
        else if ( !( (BinaryOperator) obj ).getLeft().equals( this.left ) )
            return false;
        else if ( !( (BinaryOperator) obj ).getRight().equals( this.right ) )
            return false;
        else
            return true;
    }

    @Override
    public NodeObject replaceFunctionByVar( String func, int deriv, double var ) {
        return new BinaryOperator( this.value, this.left.replaceFunctionByVar( func, deriv, var ),
                this.right.replaceFunctionByVar( func, deriv, var ) );
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
        return right;
    }

    @Override
    public NodeObject getLeftPart() {
        return left;
    }

    @Override
    public NodeObject replaceFunctionByNode( String func, int deriv,
            NodeObject newNode ) {
        return new BinaryOperator( this.value, this.left.replaceFunctionByNode( func, deriv, newNode ),
                this.right.replaceFunctionByNode( func, deriv, newNode ) );
    }

    @Override
    public NodeObject replaceFunctionByVector( String funcname,
            Map<Integer, NodeObject> v ) {
        return new BinaryOperator( this.value, left.replaceFunctionByVector( funcname, v ),
                right.replaceFunctionByVector( funcname, v ) );
    }

    @Override
    public EFunctionRole getFunctionRole() {
        return null;
    }

    @Override
    public boolean computable() {
        return this.left.computable() && this.right.computable();
    }

}
