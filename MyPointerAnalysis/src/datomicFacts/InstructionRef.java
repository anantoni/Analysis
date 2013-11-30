/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package datomicFacts;

/**
 *
 * @author anantoni
 */
public class InstructionRef {
    private int id;    
    private String instruction = null;
    
    public InstructionRef( int id, String instruction ) {
        this.id = id;
        this.instruction = instruction;
    }
    
    public int getID() {
        return this.id;
    }
    
    public String getInstruction() {
        return this.instruction;
    }
}
