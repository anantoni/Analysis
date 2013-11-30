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
public class ApplicationClass {
    int id;
    Type ref;
    
    public ApplicationClass( int id , Type ref ) {
        this.id = id;
        this.ref = ref;
    }
    
    public int getID() {
        return this.id;
    }
    
    public Type getType() {
        return ref;
    }
    
    
}
