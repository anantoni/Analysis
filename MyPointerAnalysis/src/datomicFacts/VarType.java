/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package datomicFacts;

/**
 *
 * @author
 * anantoni
 */
public class VarType {
    int id;
    VarRef varRef;
    Type type;
    
    public VarType( int id, VarRef varRef, Type type ) {
        this.id = id;
        this.varRef = varRef;
        this.type = type;
    }
    
    public int getID() {
        return this.id;
    }
    
    public VarRef getVarRef() {
        return varRef;
    }
    
    public Type getType() {
        return type;
    }
}
