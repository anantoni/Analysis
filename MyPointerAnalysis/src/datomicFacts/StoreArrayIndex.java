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
    VarRef base = null;
    VarRef from = null;
    
    public StoreArrayIndex( int id, VarRef from, VarRef base, MethodSignatureRef inmethod ) {
        this.id = id;
        this.base = base;
        this.from = from;
        this.inmethod = inmethod;
    }
    
    public int getID() {
        return this.id;
    }
    
    public VarRef getBase() {
        return this.base;
    }
    
    public VarRef getFrom() {
        return this.from;
    }
    
    public MethodSignatureRef getInmethod() {
        return this.inmethod;
    }
    
}
