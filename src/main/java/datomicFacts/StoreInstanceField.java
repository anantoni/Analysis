/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package datomicFacts;

/**
 *
 * @author anantoni
 */
public class StoreInstanceField {
    int id;
    Var base = null;
    Var from = null;
    MethodSignatureRef inmethod = null;
    FieldSignatureRef signature = null;
    
    public StoreInstanceField(int id, Var from, Var base, FieldSignatureRef signature,  MethodSignatureRef inmethod ) {
        this.id = id;
        this.base = base;
        this.from = from;
        this.inmethod = inmethod;
        this.signature = signature;
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
    
    public FieldSignatureRef getSignature() {
        return this.signature;
    }
    
}
