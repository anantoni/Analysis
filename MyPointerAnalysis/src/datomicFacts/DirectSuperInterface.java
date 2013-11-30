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
public class DirectSuperInterface {
    int id;
    Type refClass;
    Type superInterface;
    
    public DirectSuperInterface( int id, Type refClass, Type superInterface ) {
        this.id = id;
        this.refClass = refClass;
        this.superInterface = superInterface;
    }
    
    public int getID() {
        return this.id;
    }
    
    public Type getClassType() {
        return refClass;
    }
    
    public Type getInterfaceType() {
        return superInterface;
    }
}
