package fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.tree;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Classe abstraite représentant un node unaire
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
public abstract class UnaryNode extends NodeObject {

    @XmlElement( name = "LeftChild", required = true )
    protected NodeObject child = null;

    public UnaryNode( String value ) {
        super( value );
    }

    public UnaryNode( String value, NodeObject child ) {
        super( value );
        this.child = child;
    }

    public UnaryNode() {
        super();
    }

    @Override
    public NodeObject getLeftPart() {
        return child;
    }

    public void setChild( NodeObject child ) {
        this.child = child;
    }

    @Override
    public void setLeftChild( NodeObject child ) {
        child = child;
    }

}
