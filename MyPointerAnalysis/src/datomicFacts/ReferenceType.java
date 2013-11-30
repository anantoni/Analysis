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
public class ReferenceType {
    
    Type x = null;
    int id;
    
    public ReferenceType( int id, Type x ) {
        this.id = id;
        this.x = x;
    }
    
    public int getID() {
        return this.id;
    }
    
    public Type getType() {
        return this.x;
    }
}
