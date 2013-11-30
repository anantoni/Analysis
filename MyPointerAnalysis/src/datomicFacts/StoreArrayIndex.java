/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package datomicFacts;

/**
 *
 * @author anantoni
 */
public class StoreArrayIndex {
    int id;
    MethodSignatureRef inmethod = null;
    Var base = null;
    Var from = null;
    
    public StoreArrayIndex( int id, Var from, Var base, MethodSignatureRef inmethod ) {
        this.id = id;
        this.base = base;
        this.from = from;
        this.inmethod = inmethod;
    }
    
    public int getID() {
        return this.id;
    }
    
    public Var getBase() {
        return this.base;
    }
    
    public Var getFrom() {
        return this.from;
    }
    
    public MethodSignatureRef getInmethod() {
        return this.inmethod;
    }
    
}
