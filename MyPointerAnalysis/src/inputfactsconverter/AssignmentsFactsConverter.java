/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package inputfactsconverter;

import datomicFacts.AssignCast;
import datomicFacts.AssignHeapAllocation;
import datomicFacts.AssignLocal;
import datomicFacts.AssignReturnValue;
import datomicFacts.HeapAllocationRef;
import datomicFacts.MethodInvocationRef;
import datomicFacts.MethodSignatureRef;
import datomicFacts.Type;
import datomicFacts.VarRef;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author anantoni
 */
public class AssignmentsFactsConverter extends FactsConverter implements Runnable {
    private ArrayList<HeapAllocationRef> heapAllocationRefFactsList = null;
    private ArrayList<MethodSignatureRef> methodSignatureRefFactsList = null;
    private ArrayList<VarRef> varRefFactsList = null;
    private ArrayList<MethodInvocationRef> methodInvocationRefFactsList = null;
    private ArrayList<AssignHeapAllocation> assignHeapAllocationFactsList = null;
    private ArrayList<AssignReturnValue> assignReturnValueFactsList = null;
    private ArrayList<AssignLocal> assignLocalFactsList = null;
    private ArrayList<AssignCast> assignCastFactsList = null;
    private ArrayList<Type> typeFactsList = null;
    private FactsID id = null;
    private Thread t = null;
    
    public AssignmentsFactsConverter( FactsID id, ArrayList<HeapAllocationRef> heapAllocationRefFactsList, ArrayList<VarRef> varRefFactsList, ArrayList<MethodInvocationRef> methodInvocationRefFactsList, ArrayList<MethodSignatureRef> methodSignatureRefFactsList, ArrayList<Type> typeFactsList ) {
        this.heapAllocationRefFactsList = heapAllocationRefFactsList;
        this.methodInvocationRefFactsList = methodInvocationRefFactsList;
        this.methodSignatureRefFactsList = methodSignatureRefFactsList;
        this.varRefFactsList = varRefFactsList;
        this.typeFactsList = typeFactsList;
        this.id = id;
        assignCastFactsList = new ArrayList<>();
        assignHeapAllocationFactsList = new ArrayList<>();
        assignLocalFactsList = new ArrayList<>();
        assignReturnValueFactsList = new ArrayList<>();
    }

    @Override
    void parseLogicBloxFactsFile() {
        try {
            try (BufferedReader br = new BufferedReader( new FileReader( "../cache/input-facts/AssignHeapAllocation.facts" ) )) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        line = line.trim();
                        String pattern = "(.*)(,\\s)(.*)(,\\s)(.*)";
                        // Create a Pattern object
                        Pattern r = Pattern.compile(pattern);

                        // Now create matcher object.
                        Matcher m = r.matcher(line);
                        if ( m.find() ) {
                            if ( m.groupCount() != 5 ) {
                                System.out.println( "Invalid number of groups matched" );
                                System.exit(-1);
                            }
                        } 
                        else {
                            System.out.println( "AssignHeapAllocation.facts: Could not find match - " + line );
                            System.exit(-1);
                        }
                        HeapAllocationRef heap = null;
                        MethodSignatureRef inmethod = null;
                        VarRef to = null;

                        for ( MethodSignatureRef inmethod1 : methodSignatureRefFactsList ) {
                            if ( inmethod1.getValue().equals( m.group(5) ) ) {
                                inmethod = inmethod1;
                                break;
                            }
                        }
                        if ( inmethod == null ) { 
                            System.out.println( "AssignHeapAllocation.facts: Method Signature Ref not found for: " + m.group(5) );
                            System.exit(-1);
                        }

                        for ( VarRef to1 : varRefFactsList ) {
                            if ( to1.getValue().equals( m.group(3) ) ) {
                                to = to1;
                                break;
                            }
                        }
                        if ( to == null ) { 
                            System.out.println( "AssignHeapAllocation.facts: Variable Ref not found for: " + m.group(3) );
                            System.exit(-1);
                        }
                        
                        for ( HeapAllocationRef heap1 : heapAllocationRefFactsList ) {
                            if ( heap1.getCallGraphEdgeSourceRef().getInstructionRef().getInstruction().equals( m.group(1) ) ) {
                                heap = heap1;
                                break;
                            }
                        }
                        if ( heap == null ) { 
                            System.out.println( "AssignHeapAllocation.facts: Heap Allocation Ref not found for: " + m.group(1) );
                            System.exit(-1);
                        }
                        
                        AssignHeapAllocation assignHeapAllocation = new AssignHeapAllocation( id.getID(), heap, inmethod, to );
                        assignHeapAllocationFactsList.add(assignHeapAllocation);
                    }
                    br.close();  
            }
            
            try (BufferedReader br = new BufferedReader( new FileReader( "../cache/input-facts/AssignReturnValue.facts" ) )) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        line = line.trim();
                        String pattern = "(.*)(,\\s)(.*)";
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
                            System.out.println( "AssignReturnValue.facts: Could not find match - " + line );
                            System.exit(-1);
                        }
                        MethodInvocationRef invocation = null;
                        VarRef to = null;

                        for ( MethodInvocationRef invocation1 : methodInvocationRefFactsList ) {
                            if ( invocation1.getCallGraphEdgeSourceRef().getInstructionRef().getInstruction().equals( m.group(1) ) ) {
                                invocation = invocation1;
                                break;
                            }
                        }
                        if ( invocation == null ) { 
                            System.out.println( "AssignReturnValue.facts: Method Invocation Ref not found for: " + m.group(1) );
                            System.exit(-1);
                        }

                        for ( VarRef to1 : varRefFactsList ) {
                            if ( to1.getValue().equals( m.group(3) ) ) {
                                to = to1;
                                break;
                            }
                        }
                        if ( to == null ) { 
                            System.out.println( "AssignReturnValue.facts: VarRef not found for: " + m.group(3) );
                            System.exit(-1);
                        }
                        
                        AssignReturnValue assignReturnValue = new AssignReturnValue( id.getID(), invocation, to );
                        assignReturnValueFactsList.add(assignReturnValue);
                        
                    }
                    br.close();  
            }
            
            try (BufferedReader br = new BufferedReader( new FileReader( "../cache/input-facts/AssignLocal.facts" ) )) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        line = line.trim();
                        String pattern = "(.*)(,\\s)(.*)(,\\s)(.*)";
                        // Create a Pattern object
                        Pattern r = Pattern.compile(pattern);

                        // Now create matcher object.
                        Matcher m = r.matcher(line);
                        if ( m.find() ) {
                            if ( m.groupCount() != 5 ) {
                                System.out.println( "Invalid number of groups matched" );
                                System.exit(-1);
                            }
                        } 
                        else {
                            System.out.println( "AssignLocal.facts: Could not find match - " + line );
                            System.exit(-1);
                        }
                        VarRef from = null;
                        VarRef to = null;
                        MethodSignatureRef inmethod = null;
                        
                        for ( MethodSignatureRef inmethod1 : methodSignatureRefFactsList ) {
                            if ( inmethod1.getValue().equals( m.group(5) ) ) {
                                inmethod = inmethod1;
                                break;
                            }
                        }
                        if ( inmethod == null ) { 
                            System.out.println( "AssignLocal.facts: Method Signature Ref not found for: " + m.group(5) );
                            System.exit(-1);
                        }

                        for ( VarRef to1 : varRefFactsList ) {
                            if ( to1.getValue().equals( m.group(3) ) ) {
                                to = to1;
                                break;
                            }
                        }
                        if ( to == null ) { 
                            System.out.println( "AssignLocal.facts: Variable Ref not found for: " + m.group(3) );
                            System.exit(-1);
                        }
                        
                        for ( VarRef from1 : varRefFactsList ) {
                            if ( from1.getValue().equals( m.group(1) ) ) {
                                from = from1;
                                break;
                            }
                        }
                        if ( from == null ) { 
                            System.out.println( "AssignLocal.facts: Variable Ref not found for: " + m.group(1) );
                            System.exit(-1);
                        }
                        
                        AssignLocal assignLocal = new AssignLocal( id.getID(), from, to, inmethod );
                        assignLocalFactsList.add(assignLocal);
                    }
                    br.close();  
            }            
            
            try (BufferedReader br = new BufferedReader( new FileReader( "../cache/input-facts/AssignCast.facts" ) )) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        line = line.trim();
                        String pattern = "(.*)(,\\s)(.*)(,\\s)(.*)(,\\s)(.*)";
                        // Create a Pattern object
                        Pattern r = Pattern.compile(pattern);

                        // Now create matcher object.
                        Matcher m = r.matcher(line);
                        if ( m.find() ) {
                            if ( m.groupCount() != 7 ) {
                                System.out.println( "Invalid number of groups matched" );
                                System.exit(-1);
                            }
                        } 
                        else {
                            System.out.println( "AssignCast.facts: Could not find match - " + line );
                            System.exit(-1);
                        }
                        Type type = null;
                        VarRef from = null;
                        VarRef to = null;
                        MethodSignatureRef inmethod = null;
                        
                        for ( Type type1 : typeFactsList ) {
                            if ( type1.getValue().equals( m.group(1) ) ) {
                                type = type1;
                                break;
                            }
                        }
                        
                        if ( type == null ) { 
                            System.out.println( "AssignCast.facts: Type not found for: " + m.group(1) );
                            System.exit(-1);
                        }
                        
                        for ( MethodSignatureRef inmethod1 : methodSignatureRefFactsList ) {
                            if ( inmethod1.getValue().equals( m.group(7) ) ) {
                                inmethod = inmethod1;
                                break;
                            }
                        }
                        if ( inmethod == null ) { 
                            System.out.println( "AssignCast.facts: Method Signature Ref not found for: " + m.group(7) );
                            System.exit(-1);
                        }

                        for ( VarRef to1 : varRefFactsList ) {
                            if ( to1.getValue().equals( m.group(5) ) ) {
                                to = to1;
                                break;
                            }
                        }
                        if ( to == null ) { 
                            System.out.println( "AssignCast.facts: Variable Ref #2 not found for: " + m.group(5) );
                            System.exit(-1);
                        }
                        
                        for ( VarRef from1 : varRefFactsList ) {
                            if ( from1.getValue().equals( m.group(3) ) ) {
                                from = from1;
                                break;
                            }
                        }
                        if ( from == null ) { 
                            System.out.println( "AssignCast.facts: Variable Ref #1 not found for: " + m.group(3) );
                            System.exit(-1);
                        }
                        
                        AssignCast assignCast = new AssignCast( id.getID(), type, from, to, inmethod );
                        assignCastFactsList.add(assignCast);
                    }
                    br.close();  
            }                         
        }
        catch( IOException ex) {
            System.out.println( ex.toString() );
            System.exit(-1);

        }

    }

    @Override
    void createDatomicFactsFile() {
        try {
            try ( PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("../datomic_facts/AssignHeapAllocation.dtm", false)));) {
                for ( AssignHeapAllocation key : assignHeapAllocationFactsList ) {
                    writer.println( "{:db/id #db/id[:db.part/user " + key.getID() + "]" );
                    writer.println( " :AssignHeapAllocation/heap #db/id[:db.part/user " + key.getHeap().getID() + "]");
                    writer.println( " :AssignHeapAllocation/inmethod #db/id[:db.part/user " + key.getInmethod().getID() + "]" );
                    writer.println( " :AssignHeapAllocation/to #db/id[:db.part/user " + key.getTo().getID() + "]}" );
                }
                writer.close();
            }
            System.out.println( "AssignHeapAllocation facts converted: " + assignHeapAllocationFactsList.size() );
            
            try ( PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("../datomic_facts/AssignReturnValue.dtm", false)));) {
                for ( AssignReturnValue key : assignReturnValueFactsList ) {
                    writer.println( "{:db/id #db/id[:db.part/user " + key.getID() + "]" );
                    writer.println( " :AssignReturnValue/invocation #db/id[:db.part/user " + key.getInvocation().getID() + "]" );
                    writer.println( " :AssignReturnValue/to #db/id[:db.part/user " + key.getTo().getID() + "]}" );
                }
                writer.close();
            }
            System.out.println( "AssignReturnValue facts converted: " + assignReturnValueFactsList.size() );
            
            try ( PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("../datomic_facts/AssignLocal.dtm", false)));) {
                for ( AssignLocal key : assignLocalFactsList ) {
                    writer.println( "{:db/id #db/id[:db.part/user " + key.getID() + "]" );
                    writer.println( " :AssignLocal/from #db/id[:db.part/user " + key.getFrom().getID() + "]" );
                    writer.println( " :AssignLocal/to #db/id[:db.part/user " + key.getTo().getID() + "]" );
                    writer.println( " :AssignLocal/inmethod #db/id[:db.part/user " + key.getInmethod().getID() + "]}" );
                }
                writer.close();
            }
            System.out.println( "AssignLocal facts converted: " + assignLocalFactsList.size() );
            
            try ( PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("../datomic_facts/AssignCast.dtm", false)));) {
                for ( AssignCast key : assignCastFactsList ) {
                    writer.println( "{:db/id #db/id[:db.part/user " + key.getID() + "]" );
                    writer.println( " :AssignCast/type #db/id[:db.part/user " + key.getType().getID() + "]" );
                    writer.println( " :AssignCast/from #db/id[:db.part/user " + key.getFrom().getID() + "]" );
                    writer.println( " :AssignCast/to #db/id[:db.part/user " + key.getTo().getID() + "]" );
                    writer.println( " :AssignCast/inmethod #db/id[:db.part/user " + key.getInmethod().getID() + "]}" );
                }
                writer.close();
            }
            System.out.println( "AssignCast facts converted: " + assignCastFactsList.size() );
        }        
        catch ( Exception ex ) {
            System.out.println( ex.toString() ); 
            System.exit(-1);
        }
    }

    @Override
    public void run() {
        this.parseLogicBloxFactsFile();
        this.createDatomicFactsFile();
    }
    
    public void startThread() {
        t = new Thread(this, "Child thread: AssignmentFactsConverter" );
        t.start();
    }
    
    public Thread getThread() {
        return t;
    }
    
}
