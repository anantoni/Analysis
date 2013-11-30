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
public class Method {
    private int id;
    private MethodSignatureRef signature = null;
    private SimpleNameRef simplename = null;
    private MethodDescriptorRef descriptor = null;
    private Type type = null;
    private MethodSignatureRef declaration = null;

    public Method( int id, MethodSignatureRef signature, MethodSignatureRef declaration  ) {
        this.id = id;
        this.signature = signature;
        this.declaration = declaration;
        
    }
    
    public MethodSignatureRef getSignature() {
        return signature;
    }
    
    public Type getType() {
        return type;
    }
    
    public SimpleNameRef getSimpleName() {
        return simplename;
    }
    
    public MethodDescriptorRef getDescriptor() {
        return this.descriptor;
    }
    
    public int getID() {
        return this.id;
    }
    
    public void setSimpleName(SimpleNameRef simplename) {
        this.simplename = simplename;
    }
    
    public void setDescriptor(MethodDescriptorRef descriptor) {
        this.descriptor = descriptor;
    }
    
    public void setType(Type type) {
        this.type = type;
    }
    
    public MethodSignatureRef getDeclaration() {
        return this.declaration;
    }
}
