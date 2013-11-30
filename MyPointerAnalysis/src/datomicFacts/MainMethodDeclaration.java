/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package datomicFacts;

/**
 *
 * @author anantoni
 */
public class MainMethodDeclaration {
    int id;
    MethodSignatureRef method = null;
    
    public MainMethodDeclaration(int id, MethodSignatureRef method) {
        this.id = id;
        this.method = method;
    }
    
    public int getID() {
        return this.id;
    }
    
    public MethodSignatureRef getMethod() {
        return this.method;
    }
    
}
