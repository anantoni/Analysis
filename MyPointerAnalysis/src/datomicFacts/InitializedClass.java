/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package datomicFacts;

/**
 *
 * @author anantoni
 */
public class InitializedClass {
    int id;
    Type classOrInterface = null;
    
    public InitializedClass( int id, Type classOrInterface ) {
        this.id = id;
        this.classOrInterface = classOrInterface;
    }
    
    public int getID() {
        return this.id;
    }
    
    public Type getClassOrInterface() {
        return this.classOrInterface;
    }
    
}
