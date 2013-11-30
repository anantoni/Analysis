/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package datomicFacts;

/**
 *
 * @author anantoni
 */
public class AssignContextInsensitiveHeapAllocation {
    int id;
    HeapAllocationRef heap = null;
    Var var = null;
    MethodSignatureRef inmethod = null;
    
    public AssignContextInsensitiveHeapAllocation( int id, HeapAllocationRef heap, Var var, MethodSignatureRef inmethod ) {
        this.id = id;
        this.heap = heap;
        this.var = var;
        this.inmethod = inmethod;
    }
    
    public int getID() {
        return this.id;
    }
    
    public HeapAllocationRef getHeap() {
        return this.heap;
    }
    
    public Var getVar() {
        return this.var;
    }
    
    public MethodSignatureRef getInmethod() {
        return this.inmethod;
    }
}
