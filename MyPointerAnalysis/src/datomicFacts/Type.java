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
public class Type {
    private String value = null;
    private int id;
    
    public Type( int id, String value ) {
        this.id = id;
        this.value = value;
    }
    
    public String getValue() {
        return this.value;
    }
    
    public int getID() {
        return this.id;
    }
}
