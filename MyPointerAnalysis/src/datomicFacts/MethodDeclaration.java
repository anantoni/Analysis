/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package datomicFacts;

/**
 *
 * @author anantoni
 */
public class MethodDeclaration {
    private int id;
    private MethodSignatureRef signature;
    private MethodSignatureRef ref;

    public MethodDeclaration(int id, MethodSignatureRef signature, MethodSignatureRef ref ) {
        this.id = id;
        this.signature = signature;
        this.ref = ref;
    }
    
    public int getID() {
        return this.id;
    }
    
    public MethodSignatureRef getSignature() {
        return this.signature;
    }
    
    public MethodSignatureRef getRef() {
        return this.ref;
    }
}
