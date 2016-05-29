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
public class FieldModifier {
    int id;
    FieldSignatureRef field = null;
    ModifierRef modifier = null;
    
    public FieldModifier( int id, FieldSignatureRef field, ModifierRef modifier) {
        this.id = id;
        this.field = field;
        this.modifier = modifier;
    }
    
    public int getID() {
        return this.id;
    }
    
    public FieldSignatureRef getField() {
        return field;
    }
    
    public ModifierRef getModifier() {
        return modifier;
    }
    
}
