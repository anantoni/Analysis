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
public class HeapAllocationType {
    HeapAllocationRef heapAllocationRef = null;
    Type type = null;
    int id = 0;
    
    public HeapAllocationType( int id, HeapAllocationRef har, Type t ) {
        this.id = id;
        heapAllocationRef = har;
        type = t;
    }
    
    public int getID() {
        return this.id;
    }
    
    public Type getType() {
        return type;
    }
    
    public HeapAllocationRef getHeapAllocationRef() {
        return heapAllocationRef;
    }
}
