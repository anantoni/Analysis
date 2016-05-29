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
    Var base = null;
    Var to = null;
    
    public LoadArrayIndex( int id, Var base, Var to, MethodSignatureRef inmethod ) {
        this.id = id;
        this.base = base;
        this.to = to;
        this.inmethod = inmethod;
    }
    
    public int getID() {
        return this.id;
    }
    
    public Var getBase() {
        return this.base;
    }
    
    public Var getTo() {
        return this.to;
    }
    
    public MethodSignatureRef getInmethod() {
        return this.inmethod;
    }
    
}
