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
public class ComponentType {
    int id;
    ArrayType arrayType = null;
    Type componentType = null;
    
    public ComponentType( int id, ArrayType arrayType, Type componentType ) {
        this.id = id;
        this.arrayType = arrayType;
        this.componentType = componentType;
    }
    
    public int getID() {
        return this.id;
    }
    
    public ArrayType getArrayType() {
        return this.arrayType;
    }
    
    public Type getComponentType() {
        return this.componentType;
    }
}
