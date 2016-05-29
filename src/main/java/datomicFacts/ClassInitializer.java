/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package datomicFacts;

/**
 *
 * @author anantoni
 */
public class ClassInitializer {
    int id;
    MethodSignatureRef method = null;
    Type type = null;
    
    public ClassInitializer(int id , Type type, MethodSignatureRef method ) {
        this.id = id;
        this.type = type;
        this.method = method;
    }
    
    public int getID() {
        return this.id;
    }
    
    public Type getType() {
        return this.type;
    }
    
    public MethodSignatureRef getMethod() {
        return this.method;
    }
}
