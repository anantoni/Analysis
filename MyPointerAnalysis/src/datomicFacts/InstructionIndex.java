/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package datomicFacts;

/**
 *
 * @author anantoni
 */
public class InstructionIndex {
    int id;
    InstructionRef instruction = null;
    int index = -1;
    
    public InstructionIndex( int id, InstructionRef instruction, int index ) {
        this.id = id;
        this.index = index;
        this.instruction = instruction;
    }
    
    public int getID() {
        return this.id;
    }
    
    public InstructionRef getInstruction() {
        return this.instruction;
    }
    
    public int getIndex() {
        return this.index;
    }
    
    
}
