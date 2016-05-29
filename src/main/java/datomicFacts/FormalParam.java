/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package datomicFacts;

/**
 *
 * @author anantoni
 */
public class FormalParam {
    int id;
    int index;
    MethodSignatureRef method = null;
    Var var = null;
    
    public FormalParam(int id, int index, MethodSignatureRef method, Var var) {
        this.id = id;
        this.index = index;
        this.method = method;
        this.var = var;
    }
    
    public int getID() {
        return this.id;
    }
    
    public int getIndex() {
        return index;
    }
    
    public MethodSignatureRef getMethod() {
        return method;
    }
    
    public Var getVar() {
        return var;
    }
    
}
