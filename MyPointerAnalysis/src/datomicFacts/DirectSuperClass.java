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
public class DirectSuperClass {
    int id;
    Type refClass;
    Type superClass;
    
    public DirectSuperClass( int id, Type refClass, Type superClass ) {
        this.id = id;
        this.refClass = refClass;
        this.superClass = superClass;
    }
    
    public int getID() {
        return this.id;
    }
    
    public Type getRefClassType() {
        return refClass;
    }
    
    public Type getSuperClassType() {
        return superClass;
    }
}
