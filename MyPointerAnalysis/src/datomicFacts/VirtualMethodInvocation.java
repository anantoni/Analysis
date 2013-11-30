/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package datomicFacts;

/**
 *
 * @author anantoni
 */
public class VirtualMethodInvocation {
    int id;
    MethodInvocationRef invocation = null;
    MethodSignatureRef signature = null;
    MethodSignatureRef inmethod = null;
    
    public VirtualMethodInvocation( int id, MethodInvocationRef invocation, MethodSignatureRef signature, MethodSignatureRef inmethod ) {
        this.id = id;
        this.invocation = invocation;
        this.signature = signature;
        this.inmethod = inmethod;
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
    
    public MethodSignatureRef getInmethod() {
        return this.inmethod;
    }
    
}
