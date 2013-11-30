/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package datomicFacts;

/**
 *
 * @author anantoni
 */
public class StoreStaticField {
    int id;
    VarRef from = null;
    MethodSignatureRef inmethod = null;
    FieldSignatureRef signature = null;
    
    public StoreStaticField(int id, VarRef from, FieldSignatureRef signature,  MethodSignatureRef inmethod ) {
        this.id = id;
        this.from = from;
        this.inmethod = inmethod;
        this.signature = signature;
    }
    
    public int getID() {
        return this.id;
    }
    
    public VarRef getFrom() {
        return this.from;
    }
    
    public MethodSignatureRef getInmethod() {
        return this.inmethod;
    }
    
    public FieldSignatureRef getSignature() {
        return this.signature;
    }
    
}
