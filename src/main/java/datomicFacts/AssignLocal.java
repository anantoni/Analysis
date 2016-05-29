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
    Var to = null;
    Var from = null;
    
    public AssignLocal( int id, Var from, Var to, MethodSignatureRef inmethod  ) {
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
    
    public Var getTo() {
        return to;
    }
    
     public Var getFrom() {
        return from;
    }
}
