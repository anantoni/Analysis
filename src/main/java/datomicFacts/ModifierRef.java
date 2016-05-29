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
public class ModifierRef {
    int id;
    String value;
    
    public ModifierRef( int id, String v ) {
        this.id = id;
        this.value = v;
    }
    
    public int getID() {
        return this.id;
    }
    
    public String getValue() {
        return value;
    }
}
