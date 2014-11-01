/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

/**
 *
 * @author
 * anantoni
 */

import analysis.MyAnalysis;
import analysis.RecursiveFixPoint;
import java.util.List;
import java.util.Scanner;

import java.io.Reader;
import java.io.FileReader;

import datomic.Connection;
import datomic.Peer;
import datomic.Util;
import inputfactsconverter.InputFactsConverter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.ExecutionException;

public class MyPointerAnalysis {

    public static void main(String[] args) {

	try {
            System.out.println( Thread.currentThread().getContextClassLoader().getResources("data_readers.clj") );
            // Comment the following line in order to not reproduce the .dtm files
            //InputFactsConverter inputFactsConverter = new InputFactsConverter();
            
	    System.out.println("Creating and connecting to database...");
         
            //String uri = "datomic:free://localhost:4334/analysis";
            String uri = "datomic:mem://analysis";
	    Peer.createDatabase(uri);
	    Connection conn = Peer.connect(uri);

	    System.out.println("Parsing schema dtm file and running transaction...");

	    Reader schema_rdr = new FileReader("../schema_and_seed_data/schema.dtm");
	    List schema_tx = (List) Util.readAll(schema_rdr).get(0);
	    Object txResult = conn.transact(schema_tx).get();
	    System.out.println(txResult);
            
            insertInputFacts(conn);
            System.out.println( "Seed data inserted" );
	    
            
            MyAnalysis myAnalysis = new MyAnalysis(conn);
            myAnalysis.runAnalysis();           
//            RecursiveFixPoint recursiveFixPoint = new RecursiveFixPoint(conn);
//            recursiveFixPoint.reachFixPoint();
            
            Collection results = Peer.q("[:find ?varValue ?z :where " +
                                        "[?varPointsTo :VarPointsTo/heap ?heap]" +
                                        "[?varPointsTo :VarPointsTo/var ?var]" +
                                        "[?var :Var/name ?varValue]" +
                                        "[?heap :HeapAllocationRef/x ?x]" +
                                        "[?x :CallGraphEdgeSourceRef/x ?y]" +
                                        "[?y :InstructionRef/x ?z]]",
                                        conn.db());
	    System.out.println("var points to: \t" + results.size());
//            for ( Object result : results ) {
//                System.out.println(((List) result).get(1) + ", " + ((List) result).get(0));
//            }
//            results = Peer.q("[:find ?value :where "+
//                            "[?reachable :Reachable/method ?method]" +
//                            "[?method :MethodSignatureRef/value ?value]]",
//                            conn.db());
//            for ( Object result : results ) {
//                System.out.println(result);
//            }
            results = Peer.q( "[:find ?reachable :where [?reachable :Reachable/method]]", conn.db());
	    System.out.println("reachable methods: \t" + results.size());
            results = Peer.q("[:find ?z ?value :where" +
                             "[?callGraphEdge :CallGraphEdge/invocation ?invocation]" +
                             "[?callGraphEdge :CallGraphEdge/tomethod ?tomethod]" +
                             "[?invocation :MethodInvocationRef/x ?x]" +
                             "[?y :InstructionRef/x ?z]" +
                             "[?x :CallGraphEdgeSourceRef/x ?y]" +
                             "[?tomethod :MethodSignatureRef/value ?value]]", 
                             conn.db());
//            for ( Object result : results ) {
//                System.out.println(((List) result).get(0) + ", " + ((List) result).get(1));
//            }
            System.out.println("call graph edge: \t" + results.size()); 
            results = Peer.q("[:find ?arrayIndexPointsTo :where [?arrayIndexPointsTo :ArrayIndexPointsTo/baseheap]]", conn.db());
            System.out.println("array index points to: \t" + results.size());
            
            results = Peer.q("[:find ?instanceFieldPointsTo :where [?instanceFieldPointsTo :InstanceFieldPointsTo/fieldsig]]", conn.db());
            System.out.println("instance field points to: \t" + results.size());

            results = Peer.q("[:find ?staticFieldPointsTo :where [?staticFieldPointsTo :StaticFieldPointsTo/fieldsig]]", conn.db());
	    System.out.println("static field points to: \t" + results.size());
            
            results = Peer.q("[:find ?assign :where [?assign :Assign/to]]", conn.db());
            System.out.println("assigns: \t" +results.size());
            
//            System.out.println("heap allocations: \t" + results.size());
//            results = Peer.q("[:find ?instruction :where [?instruction :InstructionRef/x]]", conn.db());
//	    System.out.println("instructions: \t" + results.size());
//            results = Peer.q("[:find ?type :where [?type :Type/x]]", conn.db());
//	    System.out.println("types: \t" + results.size());
//            results = Peer.q("[:find ?arrayType :where [?arrayType :ArrayType/x]]", conn.db());
//	    System.out.println("array type: \t" + results.size());
//            results = Peer.q("[:find ?var :where [?var :VarRef/value]]", conn.db());
//	    System.out.println("variables: \t" + results.size());
//            results = Peer.q("[:find ?methodSimpleName :where [?methodSimpleName :MethodSignature-SimpleName/signature]]", conn.db());
//	    System.out.println("method signature - simplename: \t" + results.size());
//            results = Peer.q("[:find ?methodType :where [?methodType :MethodSignature-Type/signature]]", conn.db());
//	    System.out.println("method signature - type: \t" + results.size());
//            results = Peer.q("[:find ?methodDescriptor :where [?methodDescriptor :MethodSignature-Descriptor/signature]]", conn.db());
//	    System.out.println("method signature - descriptor: \t" + results.size());
            Peer.shutdown(true);
                        
	}
	catch (IOException | InterruptedException | ExecutionException e) {
            e.printStackTrace();
            System.out.println( e.toString() ); 
            System.exit(-1);
	}
    }
    
    private static final Scanner scanner = new Scanner(System.in);

    private static void pause() {
        if (System.getProperty("NOPAUSE") == null) {
	        System.out.println("\nPress enter to continue...");
	        scanner.nextLine();
        }
    }
    
    public static void insertInputFacts( Connection conn ) throws FileNotFoundException, InterruptedException, ExecutionException {
        System.out.println("Parsing seed data dtm file and running transaction...");
        List data_tx;
        Reader data_rdr;
        Object txResult;
        
        File dir = new File( "../datomic_facts");
        File[] listOfFiles = dir.listFiles(); 
        Arrays.sort(listOfFiles);
        
        try ( PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("../schema_and_seed_data/seed-data.dtm", false)));) {
            writer.println("[");
            int counter = 0;
            
            for (int i = 0; i < listOfFiles.length; i++) {
                
                if (listOfFiles[i].isFile()) {
                    System.out.println( "Adding: " + listOfFiles[i].getName() + " #" + counter );
                    try (BufferedReader br = new BufferedReader( new FileReader( listOfFiles[i] ) ) ) {
                        String line;

                        while ( ( line = br.readLine()) != null ) {
                            writer.println(line);
                        }
                        br.close();
                    }
                    catch( Exception ex) {
                       System.out.println( ex.toString() ); 
                    }
                    writer.println("");
                }
                counter++;
            }
            writer.println("]");
            writer.close();
        }
        catch ( Exception ex ) {
            System.out.println( ex.toString() ); 
            System.exit(-1);
        }
        
        data_rdr = new FileReader("../schema_and_seed_data/seed-data.dtm");
        data_tx = (List) Util.readAll(data_rdr).get(0);
	txResult = conn.transact(data_tx).get();
        conn.gcStorage(new Date());
        
        System.out.println( "input facts imported" );
                
    }
}
