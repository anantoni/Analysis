/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package datomicFacts;

/**
 *
 * @author anantoni
 */
public class MethodSignatureRef {
    private int id;
    private String value = null;

    public MethodSignatureRef( int id, String value  ) {
        this.id = id;
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
    
    public int getID() {
        return this.id;
    }
}
