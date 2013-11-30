/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package datomicFacts;

/**
 *
 * @author anantoni
 */
public class MethodDescriptorRef {
    private int id;
    private String value;

    public MethodDescriptorRef(int id, String value ) {
        this.id = id;
        this.value = value;
    }
    
    public int getID() {
        return this.id;
    }
    
    public String getValue() {
        return this.value;
    }
}
