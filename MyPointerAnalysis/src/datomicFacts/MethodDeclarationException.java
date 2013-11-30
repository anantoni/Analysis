/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package datomicFacts;

/**
 *
 * @author anantoni
 */
public class MethodDeclarationException {
    private int id;
    private MethodSignatureRef method;
    private Type exceptionType;
    
    public MethodDeclarationException( int id, MethodSignatureRef method, Type exceptionType ) {
        this.id = id;
        this.method = method;
        this.exceptionType = exceptionType;
    }
    
    public int getID() {
        return this.id;
    }

    public MethodSignatureRef getMethod() {
        return method;
    }
    
    public Type getExceptionType() {
        return exceptionType;
    }
    
    
}
