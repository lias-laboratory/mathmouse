package fr.ensma.lias.dbcoreapi.models;

import java.sql.ResultSet;
import java.sql.SQLException;

import fr.ensma.lias.dbcoreapi.models.enumerations.EAttributeName;
import fr.ensma.lias.dbcoreapi.models.enumerations.EMathObject;

/**
 * 
 * @author Cyrille Ponchateau (cyrille.ponchateau@ensma.fr)
 *
 */
public class Node extends EquationElement {
    private long        id;
    private EMathObject mathObject;
    private String      name;
    private int         deriv;
    private int         depth;
    private long        parent;
    private long        left;
    private long        right;
    private long        equation;
    private long        idContent;

    public Node() {
        exist = false;
    }

    public Node( long id, EMathObject mathObject, String name, int deriv, int depth, long parent, long left, long right,
            long equation ) {
        this.id = id;
        this.mathObject = mathObject;
        this.name = name;
        this.deriv = deriv;
        this.depth = depth;
        this.parent = parent;
        this.left = left;
        this.right = right;
        this.equation = equation;
        idContent = 0;
        exist = false;
    }

    public Node( long id, EMathObject mathObject, String name, int deriv, int depth, long parent, long left, long right,
            long equation, boolean exist ) {
        this.id = id;
        this.mathObject = mathObject;
        this.name = name;
        this.deriv = deriv;
        this.depth = depth;
        this.parent = parent;
        this.left = left;
        this.right = right;
        this.equation = equation;
        this.exist = exist;
        idContent = 0;
    }

    public Node( long id, EMathObject mathObject, String name, int deriv, int depth, long parent, long left, long right,
            long equation, long idContent, boolean exist ) {
        super();
        this.id = id;
        this.mathObject = mathObject;
        this.name = name;
        this.deriv = deriv;
        this.depth = depth;
        this.parent = parent;
        this.left = left;
        this.right = right;
        this.equation = equation;
        this.idContent = idContent;
        this.exist = exist;
    }

    public Node( ResultSet tuple ) {
        try {
            id = tuple.getLong( EAttributeName.ID_NODE_ATTRIBUTE.value() );
            mathObject = EMathObject.fromValue(
                    tuple.getString( EAttributeName.MATH_OBJECT_ATTRIBUTE.value() ).replaceAll( "\\s+", "" ) );
            name = tuple.getString( EAttributeName.NAME_ATTRIBUTE.value() ).replaceAll( "\\s+", "" );
            deriv = tuple.getInt( EAttributeName.DERIV_ATTRIBUTE.value() );
            parent = tuple.getLong( EAttributeName.ID_PARENT_ATTRIBUTE.value() );
            left = tuple.getLong( EAttributeName.ID_LEFT_ATTRIBUTE.value() );
            right = tuple.getLong( EAttributeName.ID_RIGHT_ATTRIBUTE.value() );
            equation = tuple.getLong( EAttributeName.ID_EQUATION_ATTRIBUTE.value() );
            idContent = 0;
            exist = false;
        } catch ( SQLException e ) {
            e.printStackTrace();
        }

    }

    public long getId() {
        return id;
    }

    public void setId( long id ) {
        this.id = id;
    }

    public EMathObject getMathObject() {
        return mathObject;
    }

    public void setMathObject( EMathObject mathObject ) {
        this.mathObject = mathObject;
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public int getDeriv() {
        return deriv;
    }

    public void setDeriv( int deriv ) {
        this.deriv = deriv;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth( int depth ) {
        this.depth = depth;
    }

    public long getParent() {
        return parent;
    }

    public void setParent( long parent ) {
        this.parent = parent;
    }

    public long getLeft() {
        return left;
    }

    public void setLeft( long left ) {
        this.left = left;
    }

    public long getRight() {
        return right;
    }

    public void setRight( long right ) {
        this.right = right;
    }

    public long getEquation() {
        return equation;
    }

    public void setEquation( long equation ) {
        this.equation = equation;
    }

    public long getIdContent() {
        return idContent;
    }

    public void setIdContent( long idContent ) {
        this.idContent = idContent;
    }

    @Override
    public String toString() {
        return "{equation_id=" + equation + ";node_id=" + id + ";mathObject=" + mathObject + ";name=" + name + ";deriv="
                + deriv + ";depth=" + depth + ";parent=" + parent + ";left=" + left + ";right=" + right
                + ";nodeContentId=" + idContent + "}";
    }

    @Override
    public boolean equals( Object obj ) {
        if ( obj == this )
            return true;
        if ( obj == null )
            return false;
        if ( obj instanceof Node ) {
            Node n = (Node) obj;
            if ( !mathObject.equals( n.mathObject ) )
                return false;
            if ( !name.equals( n.name ) )
                return false;
            if ( deriv != n.deriv )
                return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = prime * result + deriv;
        result = prime * result + ( mathObject == null ? 0 : mathObject.hashCode() );
        result = prime * result + ( name == null ? 0 : name.hashCode() );
        return result;
    }

}
