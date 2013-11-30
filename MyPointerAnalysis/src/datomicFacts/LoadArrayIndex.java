/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package datomicFacts;

/**
 *
 * @author anantoni
 */
public class LoadArrayIndex {
    int id;
    MethodSignatureRef inmethod = null;
    VarRef base = null;
    VarRef to = null;
    
    public LoadArrayIndex( int id, VarRef base, VarRef to, MethodSignatureRef inmethod ) {
        this.id = id;
        this.base = base;
        this.to = to;
        this.inmethod = inmethod;
    }
    
    public int getID() {
        return this.id;
    }
    
    public VarRef getBase() {
        return this.base;
    }
    
    public VarRef getTo() {
        return this.to;
    }
    
    public MethodSignatureRef getInmethod() {
        return this.inmethod;
    }
    
}
