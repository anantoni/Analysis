/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package datomicFacts;

/**
 *
 * @author anantoni
 */
public class AssignCast {
    int id;
    Type type = null;
    MethodSignatureRef inmethod = null;
    VarRef to = null;
    VarRef from = null;
    
    public AssignCast( int id, Type type, VarRef from, VarRef to, MethodSignatureRef inmethod  ) {
        this.id = id;
        this.type = type;
        this.inmethod = inmethod;
        this.to = to;
        this.from = from;
    }
    
    public int getID() {
        return this.id;
    }
    
    public Type getType() {
        return this.type;
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
