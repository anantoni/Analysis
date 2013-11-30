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
public class MainClass {
    int id;
    ClassType ref;
    
    public MainClass( int id , ClassType ref ) {
        this.id = id;
        this.ref = ref;
    }
    
    public int getID() {
        return this.id;
    }
    
    public ClassType getClassType() {
        return ref;
    }
    
}
