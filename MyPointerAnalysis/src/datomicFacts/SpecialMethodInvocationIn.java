/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package datomicFacts;

/**
 *
 * @author anantoni
 */
public class SpecialMethodInvocationIn {
    int id;
    MethodInvocationRef invocation = null;
    MethodSignatureRef inmethod = null;
    
    public SpecialMethodInvocationIn( int id, MethodInvocationRef invocation, MethodSignatureRef inmethod ) {
        this.id = id;
        this.invocation = invocation;
        this.inmethod = inmethod;
    }
    
    public int getID() {
        return this.id;
    }
    
    public MethodInvocationRef getInvocation() {
        return this.invocation;
    }
    
    public MethodSignatureRef getInmethod() {
        return this.inmethod;
    }
}
