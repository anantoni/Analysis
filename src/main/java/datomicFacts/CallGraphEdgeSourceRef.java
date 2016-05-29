/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package datomicFacts;

/**
 *
 * @author anantoni
 */
public class CallGraphEdgeSourceRef {
    int id;
    InstructionRef x;
    
    public CallGraphEdgeSourceRef( int id, InstructionRef x ) {
        this.id = id;
        this.x = x;
    }
    
    public int getID() {
        return this.id;
    }
    
    public InstructionRef getInstructionRef() {
        return x;
    }
    
}
