/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package datomicFacts;

/**
 *
 * @author anantoni
 */
public class LoadStaticField {
    int id;
    Var to = null;
    MethodSignatureRef inmethod = null;
    FieldSignatureRef sig = null;
    
    public LoadStaticField(int id, FieldSignatureRef sig, Var to, MethodSignatureRef inmethod ) {
        this.id = id;
        this.to = to;
        this.inmethod = inmethod;
        this.sig = sig;
    }
    
    public int getID() {
        return this.id;
    }
    
    public Var getTo() {
        return this.to;
    }
    
    public MethodSignatureRef getInmethod() {
        return this.inmethod;
    }
    
    public FieldSignatureRef getSig() {
        return this.sig;
    }
    
}
