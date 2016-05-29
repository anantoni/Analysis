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
public class HeapAllocationRef {
    private CallGraphEdgeSourceRef x = null;
    private int id;
    
    public HeapAllocationRef( int id, CallGraphEdgeSourceRef x ) {
        this.x = x;
        this.id = id;
    }
    
    public int getID() {
        return this.id;
    }

    public CallGraphEdgeSourceRef getCallGraphEdgeSourceRef() {
        return this.x;
    }
}
