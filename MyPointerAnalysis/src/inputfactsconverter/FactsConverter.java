/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package inputfactsconverter;

/**
 *
 * @author
 * anantoni
 */
public abstract class FactsConverter {    
    
    abstract void parseLogicBloxFactsFile();
    
    abstract void createDatomicFactsFile();
    
}
