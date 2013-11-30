/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package datomicFacts;

/**
 *
 * @author anantoni
 */
public class ThisVar {
    int id;
    MethodSignatureRef method = null;
    Var var = null;
    
    public ThisVar( int id, MethodSignatureRef method, Var var ) {
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
    
    public Var getVar() {
        return var;
    }
    
}
