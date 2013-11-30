/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package inputfactsconverter;

/**
 *
 * @author anantoni
 */
public class FactsID {
    private int id;
    
    public FactsID( int id ) {
        this.id = id;
    }

    public synchronized int getID() {
        int temp_id = id--;
        return temp_id;
    }

    void printID() {
        System.out.println( this.id );
    }

}
