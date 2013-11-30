/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package datomicFacts;

/**
 *
 * @author anantoni
 */
public class SpecialMethodInvocation {
    int id;
    MethodInvocationRef invocation = null;
    MethodSignatureRef inmethod = null;
    MethodSignatureRef signature = null;
    Var var = null;
    
    public SpecialMethodInvocation( int id, MethodInvocationRef invocation, Var var) {
        this.id = id;
        this.invocation = invocation;
        this.var = var;
    }
    
    public int getID() {
        return this.id;
    }
    
    public MethodInvocationRef getInvocation() {
        return this.invocation;
    }
    
    public void setInmethod(MethodSignatureRef inmethod) {
        this.inmethod = inmethod;
    }

    public void setSignature(MethodSignatureRef signature) {
        this.signature = signature;
    }
    public MethodSignatureRef getInmethod() {
        return this.inmethod;
    }
    
    public MethodSignatureRef getSignature() {
        return this.signature;
    }
    
    public Var getVar() {
        return this.var;
    }
}
