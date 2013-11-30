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
public class VarDeclaringMethod {
    int id;
    VarRef varRef;
    MethodSignatureRef methodSignatureRef;
    
    public VarDeclaringMethod( int id, VarRef varRef, MethodSignatureRef methodSignatureRef ) {
        this.id = id;
        this.varRef = varRef;
        this.methodSignatureRef = methodSignatureRef;
    }
    
    public int getID() {
        return this.id;
    }
    
    public VarRef getVarRef() {
        return varRef;
    }
    
    public MethodSignatureRef getMethodSignatureRef() {
        return methodSignatureRef;
    }
}
