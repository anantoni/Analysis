/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package datomicFacts;

/**
 *
 * @author anantoni
 */
public class VirtualMethodInvocationBase {
    int id;
    MethodInvocationRef invocation = null;
    VarRef var = null;
    
    public VirtualMethodInvocationBase( int id, MethodInvocationRef invocation, VarRef var) {
        this.id = id;
        this.invocation = invocation;
        this.var = var;
    }
    
    public int getID() {
        return this.id;
    }
    
    public MethodInvocationRef getInvocation() {
        return invocation;
    }
    
    public VarRef getVar() {
        return var;
    }
}
