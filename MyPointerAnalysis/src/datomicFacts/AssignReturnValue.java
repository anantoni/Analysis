/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package datomicFacts;

/**
 *
 * @author anantoni
 */
public class AssignReturnValue {
    int id;
    MethodInvocationRef invocation = null;
    VarRef to = null;
    
    public AssignReturnValue( int id, MethodInvocationRef invocation, VarRef to ) {
        this.id = id;
        this.invocation = invocation;
        this.to = to;
    }
    
    public int getID() {
        return this.id;
    }
    
    public MethodInvocationRef getInvocation() {
        return invocation;
    }
    
    public VarRef getTo() {
        return to;
    }
}
