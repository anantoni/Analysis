/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package datomicFacts;

/**
 *
 * @author anantoni
 */
public class MethodInvocationRef {
    int id;
    CallGraphEdgeSourceRef x;
    
    public MethodInvocationRef( int id, CallGraphEdgeSourceRef x ) {
        this.id = id;
        this.x = x;
    }
    
    public int getID() {
        return this.id;
    }
    
    public CallGraphEdgeSourceRef getCallGraphEdgeSourceRef() {
        return x;
    }
    
}
