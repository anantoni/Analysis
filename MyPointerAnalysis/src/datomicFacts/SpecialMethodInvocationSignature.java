/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package datomicFacts;

/**
 *
 * @author anantoni
 */
public class SpecialMethodInvocationSignature {
    int id;
    MethodInvocationRef invocation = null;
    MethodSignatureRef signature = null;
    
    public SpecialMethodInvocationSignature( int id, MethodInvocationRef invocation, MethodSignatureRef signature ) {
        this.id = id;
        this.invocation = invocation;
        this.signature = signature;
    }
    
    public int getID() {
        return this.id;
        
    }
    
    public MethodInvocationRef getInvocation() {
        return this.invocation;
    }
    
    public MethodSignatureRef getSignature() {
        return this.signature;
    }
}
