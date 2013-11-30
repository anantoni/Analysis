/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package datomicFacts;

/**
 *
 * @author
 * anantoni
 */
public class ArrayType {
    int id;
    ReferenceType x = null;
    
    public ArrayType( int id, ReferenceType x ) {
        this.id = id;
        this.x = x;
    }
    
    public int getID() {
        return this.id;
    }
    
    public ReferenceType getReferenceType() {
        return this.x;
    }
    
}
