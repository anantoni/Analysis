/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package datomicFacts;

/**
 *
 * @author anantoni
 */
public class MethodSignatureDescriptor {
    private int id;
    private MethodSignatureRef ref = null;
    private MethodDescriptorRef descriptor = null;
    
    public MethodSignatureDescriptor( int id, MethodSignatureRef ref, MethodDescriptorRef descriptor ) {
        this.id = id;
        this.ref = ref;
        this.descriptor = descriptor;
    }
    
    public int getID() {
        return this.id;
    }
    
    public MethodSignatureRef getMethodSignatureRef() {
        return this.ref;
    }
    
    public MethodDescriptorRef getDescriptor() {
        return this.descriptor;
    }
}
