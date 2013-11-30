/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package datomicFacts;

/**
 *
 * @author anantoni
 */
public class LoadPrimStaticField {
    int id;
    MethodSignatureRef inmethod = null;
    FieldSignatureRef sig = null;
    
    public LoadPrimStaticField(int id, FieldSignatureRef sig, MethodSignatureRef inmethod ) {
        this.id = id;
        this.inmethod = inmethod;
        this.sig = sig;
    }
    
    public int getID() {
        return this.id;
    }
    
    public MethodSignatureRef getInmethod() {
        return this.inmethod;
    }
    
    public FieldSignatureRef getSig() {
        return this.sig;
    }
    
}
