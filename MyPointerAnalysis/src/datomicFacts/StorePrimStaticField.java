/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package datomicFacts;

/**
 *
 * @author anantoni
 */
public class StorePrimStaticField {
    int id;
    MethodSignatureRef inmethod = null;
    FieldSignatureRef signature = null;
    
    public StorePrimStaticField(int id, FieldSignatureRef signature,  MethodSignatureRef inmethod ) {
        this.id = id;
        this.inmethod = inmethod;
        this.signature = signature;
    }
    
    public int getID() {
        return this.id;
    }
    
    public MethodSignatureRef getInmethod() {
        return this.inmethod;
    }
    
    public FieldSignatureRef getSignature() {
        return this.signature;
    }
    
}
