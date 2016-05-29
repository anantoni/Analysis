/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package datomicFacts;

/**
 *
 * @author anantoni
 */
public class ActualParam {
    int id;
    int index;
    MethodInvocationRef invocation = null;
    Var var = null;
    
    public ActualParam(int id, int index, MethodInvocationRef invocation, Var var) {
        this.id = id;
        this.index = index;
        this.invocation = invocation;
        this.var = var;
    }
    
    public int getID() {
        return this.id;
    }
    
    public int getIndex() {
        return index;
    }
    
    public MethodInvocationRef getInvocation() {
        return invocation;
    }
    
    public Var getVar() {
        return var;
    }
}