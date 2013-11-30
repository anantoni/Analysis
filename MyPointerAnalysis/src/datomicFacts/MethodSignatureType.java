/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package datomicFacts;

/**
 *
 * @author anantoni
 */
public class MethodSignatureType {
    private int id;
    private MethodSignatureRef ref = null;
    private Type type = null;
    private SimpleNameRef simplename = null;
    private MethodDescriptorRef descriptor = null;
    
    public MethodSignatureType( int id, MethodSignatureRef ref, Type type ) {
        this.id = id;
        this.ref = ref;
        this.type = type;
    }
    
    public int getID() {
        return this.id;
    }
    
    public MethodSignatureRef getMethodSignatureRef() {
        return ref;
    }
    
    public Type getType() {
        return type;
    }
    
}
