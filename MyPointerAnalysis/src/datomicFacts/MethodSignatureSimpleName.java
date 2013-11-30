/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package datomicFacts;

/**
 *
 * @author anantoni
 */
public class MethodSignatureSimpleName {
    private int id;
    private MethodSignatureRef ref = null;
    private SimpleNameRef simplename = null;
    
    public MethodSignatureSimpleName( int id, MethodSignatureRef ref, SimpleNameRef simplename ) {
        this.id = id;
        this.ref = ref;
        this.simplename = simplename;
    }
    
    public int getID() {
        return this.id;
    }
    
    public MethodSignatureRef getMethodSignatureRef() {
        return ref;
    }
    
    public SimpleNameRef getSimpleName() {
        return simplename;
    }
    
}