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
public class FieldSignature {
    int id;
    FieldSignatureRef ref = null;
    Type declaringClass = null;
    SimpleNameRef simplename = null;
    Type type = null;
    
    public FieldSignature( int id, FieldSignatureRef ref, Type declaringClass, SimpleNameRef simplename, Type type ) {
        this.id = id;
        this.ref = ref;
        this.declaringClass = declaringClass;
        this.simplename = simplename;
        this.type = type;
    }
    
    public int getID() {
        return this.id;
    }
    
    public FieldSignatureRef getFieldSignatureRef() {
        return this.ref;
    }
    
    public Type getDeclaringClass() {
        return this.declaringClass;
    }
    
    public SimpleNameRef getSimpleNameRef() {
        return this.simplename;
    }
    
    public Type getType() {
        return this.type;
    }
}
