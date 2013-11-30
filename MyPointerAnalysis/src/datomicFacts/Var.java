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
public class Var {
    private String name = null;
    private MethodSignatureRef method = null;
    private Type type = null;
    private int id;
    
    public Var(  int id, String name, Type type ) {
        this.id = id;
        this.name = name;
        this.type = type;
    }
    
    public String getName() {
        return name;
    }
    
    public int getID() {
        return this.id;
    }
    
    public void setDeclaringMethod( MethodSignatureRef method ) {
        this.method = method;
    }
    
    public MethodSignatureRef getDeclaringMethod() {
        return method;
    }
    
    public Type getType() {
        return type;
    }
}
