/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package datomicFacts;

/**
 *
 * @author anantoni
 */
public class AssignLocal {
    int id;
    MethodSignatureRef inmethod = null;
    VarRef to = null;
    VarRef from = null;
    
    public AssignLocal( int id, VarRef from, VarRef to, MethodSignatureRef inmethod  ) {
        this.id = id;
        this.inmethod = inmethod;
        this.to = to;
        this.from = from;
    }
    
    public int getID() {
        return this.id;
    }
    
    public MethodSignatureRef getInmethod() {
        return inmethod;
    }
    
    public VarRef getTo() {
        return to;
    }
    
     public VarRef getFrom() {
        return from;
    }
}
