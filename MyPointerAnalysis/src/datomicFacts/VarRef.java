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
public class VarRef {
    private String value = null;
    private int id;
    
    public VarRef(  int id, String v ) {
        this.id = id;
        value = v;

    }
    
    public String getValue() {
        return value;
    }
    
    public int getID() {
        return this.id;
    }
}
