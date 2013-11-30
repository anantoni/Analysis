/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package analysis;

import clojure.core$_;
import java.util.*;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PushbackReader;

import datomic.Connection;
import datomic.Database;
import static datomic.Util.*;
import static datomic.Peer.*;
import datomic.Util;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author anantoni
 */
public class RecursiveFixPoint {
    Connection conn = null;
    
    public RecursiveFixPoint( Connection conn ) {
        this.conn = conn;
    }
    
    public void reachFixPoint() {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter("../cache/analysis/results.txt", "UTF-8");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(RecursiveFixPoint.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(-1);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(RecursiveFixPoint.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(-1);
        }
        Object rules = null;
        Reader rulesReader;
        try {
            rulesReader = new FileReader("resources/analysis.edn");
            rules = Util.readAll(rulesReader).get(0);
        } catch (FileNotFoundException ex) {
            System.out.println(ex.toString());
            System.exit(-1);
        }

        System.out.println(rules);
        Collection<List<Object>> results;

        results = q( "[:find ?varValue ?z " +
                     ":in $ % " +
                     ":where (VarPointsTo ?heap ?var)" +
                     "[?var :VarRef/value ?varValue]" +
                     "[?heap :HeapAllocationRef/x ?x]" +
                     "[?x :CallGraphEdgeSourceRef/x ?y]" +
                     "[?y :InstructionRef/x ?z]]",
                     conn.db(), 
                     rules  ); 
        System.out.println("VarPointsTo: " + results.size() );
        //writer.println("VarPointsTo: " + results.size() );
        //writer.flush();
        
        for ( Object result : results ) {
            //System.out.println( ((List)result).get(1) + ", " + ((List)result).get(0) );
            writer.println(((List) result).get(1) + ", " + ((List) result).get(0));
        }
        writer.flush();
        
        results = q( "[:find ?heapbase ?heap " +
                     ":in $ % " +
                     ":where (ArrayIndexPointsTo ?heapbase ?heap) ]" , 
                     conn.db(), 
                     rules  ); 
        System.out.println("ArrayIndexPointsTo: " + results.size() );
        writer.println("ArrayIndexPointsTo: " + results.size() );
        writer.flush();
        
        results = q( "[:find ?varValue ?z " +
                     ":in $ % " +
                     ":where (VarPointsTo ?heap ?var)" +
                     "[?var :VarRef/value ?varValue]" +
                     "[?heap :HeapAllocationRef/x ?x]" +
                     "[?x :CallGraphEdgeSourceRef/x ?y]" +
                     "[?y :InstructionRef/x ?z]]",
                     conn.db(), 
                     rules  ); 
        System.out.println("VarPointsTo: " + results.size() );
        writer.println("VarPointsTo: " + results.size() );
        writer.flush();
        
        for ( Object result : results ) {
            System.out.println( ((List)result).get(1) + ", " + ((List)result).get(0) );
            writer.println(((List) result).get(1) + ", " + ((List) result).get(0));
        }
        writer.flush();
        
        
        
        results = q( "[:find ?fieldsig ?heap " +
                     ":in $ % " +
                     ":where (StaticFieldPointsTo ?fieldsig ?heap ) ]" , 
                     conn.db(), 
                     rules  ); 
        System.out.println("StaticFieldPointsTo: " + results.size() );
        writer.println("StaticFieldPointsTo: " + results.size() );
        writer.flush();
        results = q( "[:find ?heapbase ?fieldsig ?heap " +
                     ":in $ % " +
                     ":where (InstanceFieldPointsTo ?heapbase ?fieldsig ?heap) ]" , 
                     conn.db(), 
                     rules  ); 
        System.out.println("InstanceFieldPointsTo: " + results.size() );
        writer.println("InstanceFieldPointsTo: " + results.size() );
        writer.flush();
        
        
        
        
        
        
        
        results = q( "[:find ?z ?value " +
                     ":in $ % " +
                     ":where (CallGraphEdge ?invocation ?tomethod)" +
                     "[?invocation :MethodInvocationRef/x ?x]" +
                     "[?x :CallGraphEdgeSourceRef/x ?y]" +
                     "[?y :InstructionRef/x ?z]" +
                     "[?tomethod :MethodSignatureRef/value ?value]]", 
                     conn.db(), 
                     rules  ); 
        System.out.println("CallGraphEdge: " + results.size() );
        writer.println("CallGraphEdge: " + results.size() );
        writer.flush();
        
        
        
            
        for ( Object result :results ) {
            System.out.println( ((List)result).get(0) + ", " + ((List)result).get(1) );
            writer.println( ((List)result).get(0) + ", " + ((List)result).get(1) );
        }
        writer.flush();
        
        results = q( "[:find ?method " +
                     ":in $ % " +
                     ":where (Reachable ?method) ]" , 
                     conn.db(), 
                     rules  ); 
        System.out.println("Reachable: " + results.size() );
        writer.println("Reachable: " + results.size() );
        writer.flush();
        
        
        results = q( "[:find ?type ?from ?to " +
                     ":in $ % " +
                     ":where (Assign ?type ?from ?to) ]" , 
                     conn.db(), 
                     rules  ); 
        System.out.println("Assign: " + results.size() );
        writer.println("Assign: " + results.size() );
        writer.flush();
        writer.close();
    }   
}
