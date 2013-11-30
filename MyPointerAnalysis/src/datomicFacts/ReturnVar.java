/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package datomicFacts;

/**
 *
 * @author anantoni
 */
public class ReturnVar {
    int id;
    MethodSignatureRef method = null;
    VarRef var = null;
    
    public ReturnVar( int id, MethodSignatureRef method, VarRef var ) {
        this.id = id;
        this.method = method;
        this.var = var;
    }
    
    public int getID() {
        return this.id;
    }
    
    public MethodSignatureRef getMethod() {
        return method;
    }
    
    public VarRef getVar() {
        return var;
    }
}
