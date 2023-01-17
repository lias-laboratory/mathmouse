package fr.ensma.lias.timeseriesreductorslib.reductors.models.differentialequations.tree;

import javax.xml.bind.annotation.XmlElement;

/**
 * Classe abstraite représentant un noeud binaire
 * 
 * @author albertf
 *
 */
public abstract class BinaryNode extends NodeObject {

    @XmlElement( name = "LeftChild", required = true )
    protected NodeObject left  = null;
    @XmlElement( name = "RightChild", required = true )
    protected NodeObject right = null;

    public BinaryNode( String value ) {
        super( value );
    }

    public BinaryNode( String value, NodeObject left, NodeObject right ) {
        super( value );
        this.left = left;
        this.right = right;
    }

    public BinaryNode() {
        super();
    }

    public NodeObject getLeft() {
        return left;
    }

    public void setLeft( NodeObject left ) {
        this.left = left;
    }

    public NodeObject getRight() {
        return right;
    }

    public void setRight( NodeObject right ) {
        this.right = right;
    }

    @Override
    public void setLeftChild( NodeObject child ) {
        left = child;
    }

    @Override
    public void setRightChild( NodeObject child ) {
        right = child;
    }

}
