/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package datomicFacts;

/**
 *
 * @author anantoni
 */
public class AssignHeapAllocation {
    int id;
    HeapAllocationRef heap = null;
    MethodSignatureRef inmethod = null;
    Var to = null;
    
    public AssignHeapAllocation( int id, HeapAllocationRef heap, MethodSignatureRef inmethod, Var to ) {
        this.id = id;
        this.heap = heap;
        this.inmethod = inmethod;
        this.to = to;
    }
    
    public int getID() {
        return this.id;
    }
    
    public HeapAllocationRef getHeap() {
        return heap;
    }
    
    public MethodSignatureRef getInmethod() {
        return inmethod;
    }
    
    public Var getTo() {
        return to;
    }
}
