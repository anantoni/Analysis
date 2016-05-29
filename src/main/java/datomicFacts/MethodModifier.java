/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package datomicFacts;

/**
 *
 * @author anantoni
 */
public class MethodModifier {
    private int id;
    private MethodSignatureRef method=null;
    private ModifierRef mod=null;
    
    public MethodModifier( int id, MethodSignatureRef method, ModifierRef mod ) {
        this.id = id;
        this.method = method;
        this.mod = mod;
    }
    
    public int getID() {
        return this.id;
    }
    
    public MethodSignatureRef getMethod() {
        return method;
    }
    
    public ModifierRef getModifier() {
        return mod;
    }
    
}
