/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package datomicFacts;

/**
 *
 * @author anantoni
 */
public class MethodLookup {
    int id;
    Type type = null;
    SimpleNameRef simplename = null;
    MethodDescriptorRef descriptor = null;
    MethodSignatureRef method = null;
    
    public MethodLookup( int id, Type type, SimpleNameRef simplename, MethodDescriptorRef descriptor, MethodSignatureRef method ) {
        this.id = id;
        this.type = type;
        this.simplename = simplename;
        this.descriptor = descriptor;
        this.method = method;
    }
    
    public int getID() {
        return this.id;
    }
    
    public Type getType() {
        return this.type;
    }
    
    public SimpleNameRef getSimpleName() {
        return this.simplename;
    }
    
    public MethodSignatureRef getMethod() {
        return this.method;
    }

    public MethodDescriptorRef getDescriptor() {
        return this.descriptor;
    }
    
}
