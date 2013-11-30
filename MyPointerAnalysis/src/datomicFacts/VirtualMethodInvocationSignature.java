/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package datomicFacts;

/**
 *
 * @author anantoni
 */
public class VirtualMethodInvocationSignature {
    int id;
    MethodInvocationRef invocation = null;
    MethodSignatureRef signature = null;
    
    public VirtualMethodInvocationSignature( int id, MethodInvocationRef invocation, MethodSignatureRef signature ) {
        this.id = id;
        this.invocation = invocation;
        this.signature = signature;
    }
    
    public int getID() {
        return this.id;
    }
    
    public MethodInvocationRef getInvocation() {
        return invocation;
    }
    
    public MethodSignatureRef getSignature() {
        return signature;
    }
}
