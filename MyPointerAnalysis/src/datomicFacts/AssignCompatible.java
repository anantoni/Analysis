/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package datomicFacts;

/**
 *
 * @author anantoni
 */
public class AssignCompatible {
    int id;
    Type target = null;
    Type source = null;
    
    public AssignCompatible( int id, Type target, Type source ) {
        this.id = id;
        this.target = target;
        this.source = source;
    }
    
    public int getID() {
        return this.id;
    }
    
    public Type getTarget() {
        return this.target;
    }
    
    public Type getSource() {
        return this.source;
    }
    
}
