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
public class PrimitiveType {
    Type x = null;
    int id;
    
    public PrimitiveType( int id, Type x ) {
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
