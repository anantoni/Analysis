/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package inputfactsconverter;

import datomicFacts.CallGraphEdgeSourceRef;
import datomicFacts.HeapAllocationRef;
import datomicFacts.HeapAllocationType;
import datomicFacts.Type;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author
 * anantoni
 */
public class HeapAllocationFactsConverter implements FactsConverter, Runnable {
    private List<HeapAllocationRef> heapAllocationRefFactsList = null;
    private List<HeapAllocationType> heapAllocationTypeFactsList = null;
    private List<Type> typeFactsList = null;
    private List<CallGraphEdgeSourceRef> callGraphEdgeSourceRefFactsList = null;
    FactsID id = null;
    Thread t = null;
    
    public HeapAllocationFactsConverter(FactsID id, List<Type> typeFactsList, List<CallGraphEdgeSourceRef> callGraphEdgeSourceRefFactsList) {
        super();
        heapAllocationRefFactsList = new ArrayList<>();
        heapAllocationTypeFactsList = new ArrayList<>();
        this.typeFactsList = typeFactsList;
        this.callGraphEdgeSourceRefFactsList = callGraphEdgeSourceRefFactsList;
        this.id = id;
    }    
    
    @Override
    public void parseLogicBloxFactsFile() {
        try {
            try (BufferedReader br = new BufferedReader( new FileReader( "cache/input-facts/HeapAllocation-Type.facts" ) )) {
                String line;                
             
                while ((line = br.readLine()) != null) {
                    line = line.trim();
                    String pattern = "(.*)(,\\s)(.+)";
                    // Create a Pattern object
                    Pattern r = Pattern.compile(pattern);

                    // Now create matcher object.
                    Matcher m = r.matcher(line);
                    if ( m.find() ) {
                        if ( m.groupCount() != 3 ) {
                            System.out.println( "Invalid number of groups matched" );
                            System.exit(-1);
                        }
                    } 
                    else {
                        System.out.println( "Could not find match" );
                        System.exit(-1);
                    }
                    Type type = null;
                    CallGraphEdgeSourceRef callGraphEdgeSourceRef = null;
                    
                    
                    for ( Type type1 : typeFactsList ) {
                        if ( type1.getValue().equals(m.group(3) ) ) {
                            type = type1;
                            break;
                        }
                    }
                    
                    if ( type == null ) {
                        System.out.println("HeapAllocation-Type.facts: Type not found for: " + m.group(3) ); 
                        System.exit(-1);
                    }
                    
                    for ( CallGraphEdgeSourceRef callGraphEdgeSourceRef1 : callGraphEdgeSourceRefFactsList ) {
                        if ( callGraphEdgeSourceRef1.getInstructionRef().getInstruction().equals(m.group(1) ) ) {
                            callGraphEdgeSourceRef = callGraphEdgeSourceRef1;
                            break;
                        }
                    }
                    
                    if ( callGraphEdgeSourceRef == null ) {
                        System.out.println("HeapAllocation-Type.facts: CallGraphEdgeSourceRef not found for: " + m.group(1) ); 
                        System.exit(-1);
                    }
                    
                    HeapAllocationRef heapAllocationRef = new HeapAllocationRef( id.getID(), callGraphEdgeSourceRef ); 
                    heapAllocationRefFactsList.add(heapAllocationRef);
                    HeapAllocationType heapAllocationType= new HeapAllocationType(id.getID(), heapAllocationRef, type);
                    heapAllocationTypeFactsList.add(heapAllocationType);
                
                }
                br.close();
            }
        }
        catch( Exception ex) {
            System.out.println( ex.toString() ); 
        }
    }
    
    @Override
    public void createDatomicFactsFile() {
        try {
            try ( PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("datomic_facts/HeapAllocationRef.dtm", false))); ) {
                for ( HeapAllocationRef key : heapAllocationRefFactsList ) {
                    writer.println( "{:db/id #db/id[:db.part/user " + key.getID() + "]" );
                    writer.println( " :HeapAllocationRef/x #db/id[:db.part/user " + key.getCallGraphEdgeSourceRef().getID() + "]}"); 
                }
                writer.close();
            }
            System.out.println( "HeapAllocationRef facts converted: " + heapAllocationRefFactsList.size() );

            
            try ( PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("datomic_facts/HeapAllocation-Type.dtm", false))); ) {

                for ( HeapAllocationType key: heapAllocationTypeFactsList ) {
                    writer.println( "{:db/id #db/id[:db.part/user " + key.getID() + "]" );
                    writer.println( " :HeapAllocation-Type/heap #db/id[:db.part/user " + key.getHeapAllocationRef().getID() + "]");
                    writer.println( " :HeapAllocation-Type/type #db/id[:db.part/user " + key.getType().getID() + "]}"); 
                }
                writer.close();
            }
            System.out.println( "HeapAllocation-Type facts converted: " + heapAllocationTypeFactsList.size() );
        }
        catch ( Exception ex ) {
            System.out.println( ex.toString() ); 
            System.exit(-1);
        }
    }
    
    public List<HeapAllocationRef> getHeapAllocationRefFactsList() {
        return this.heapAllocationRefFactsList;
    }

    @Override
    public void run() {
        this.parseLogicBloxFactsFile();
        this.createDatomicFactsFile();
    }
    
    public void startThread() {
        t = new Thread(this, "Child thread: HeapAllocationFactsConverter" );
        t.start();
    } 
    
    public Thread getThread() {
        return t;
    }
}
