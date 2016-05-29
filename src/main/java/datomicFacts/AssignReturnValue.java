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
    Var to = null;
    
    public AssignReturnValue( int id, MethodInvocationRef invocation, Var to ) {
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
    
    public Var getTo() {
        return to;
    }
}
