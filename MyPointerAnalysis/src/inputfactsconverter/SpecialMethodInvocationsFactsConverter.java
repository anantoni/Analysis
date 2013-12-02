/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package inputfactsconverter;

import datomicFacts.MethodInvocationRef;
import datomicFacts.MethodSignatureRef;
import datomicFacts.SpecialMethodInvocation;
import datomicFacts.Var;
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
public class SpecialMethodInvocationsFactsConverter extends FactsConverter implements Runnable {
    private ArrayList<MethodInvocationRef> methodInvocationRefFactsList = null;
    private ArrayList<Var> varFactsList = null;
    private ArrayList<MethodSignatureRef> methodSignatureRefFactsList = null;
    private ArrayList<SpecialMethodInvocation> specialMethodInvocationFactsList = null;
    private FactsID id = null;
    private Thread t = null;
    
    public SpecialMethodInvocationsFactsConverter(FactsID id, ArrayList<Var> varFactsList, ArrayList<MethodSignatureRef> methodSignatureRefFactsList, ArrayList<MethodInvocationRef> methodInvocationRefFactsList) {
        this.methodInvocationRefFactsList = methodInvocationRefFactsList;
        this.varFactsList = varFactsList;
        this.methodSignatureRefFactsList = methodSignatureRefFactsList;
        specialMethodInvocationFactsList = new ArrayList<>();
        this.id = id;
    }

    @Override
    public void parseLogicBloxFactsFile() {
        try {
                    
            try (BufferedReader br = new BufferedReader( new FileReader( "../cache/input-facts/SpecialMethodInvocation-Base.facts" ) )) {
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
                            System.out.println( "SpecialMethodInvocation-Base.facts: Could not find match - " + line );
                            System.exit(-1);
                        }
                        MethodInvocationRef invocation = null;
                        Var var = null;

                        for ( MethodInvocationRef invocation1 : methodInvocationRefFactsList ) {
                            if ( invocation1.getCallGraphEdgeSourceRef().getInstructionRef().getInstruction().equals( m.group(1) ) ) {
                                invocation = invocation1;
                                break;
                            }
                        }
                        if ( invocation == null ) { 
                            System.out.println( "SpecialMethodInvocation-Base.facts: Method Invocation Ref not found for: " + m.group(1) );
                            System.exit(-1);
                        }

                        for ( Var var1 : varFactsList ) {
                            if ( var1.getName().equals( m.group(3) ) ) {
                                var = var1;
                                break;
                            }
                        }
                        if ( var == null ) { 
                            System.out.println( "SpecialMethodInvocation-Base.facts: Variable Ref not found for: " + m.group(3) );
                            System.exit(-1);
                        }                        
                        SpecialMethodInvocation specialMethodInvocation = new SpecialMethodInvocation( id.getID(), invocation, var );
                        specialMethodInvocationFactsList.add(specialMethodInvocation);
                    }
                    br.close();  
            }
            
            try (BufferedReader br = new BufferedReader( new FileReader( "../cache/input-facts/SpecialMethodInvocation-In.facts" ) )) {
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
                            System.out.println( "SpecialMethodInvocation-In.facts: Could not find match - " + line );
                            System.exit(-1);
                        }
                        MethodSignatureRef inmethod = null;

                        for ( MethodSignatureRef inmethod1 : methodSignatureRefFactsList ) {
                            if ( inmethod1.getValue().equals( m.group(3) ) ) {
                                inmethod = inmethod1;
                                break;
                            }
                        }
                        if ( inmethod == null ) { 
                            System.out.println( "SpecialMethodInvocation-In.facts: Inmethod Signature Ref not found for: " + m.group(3) );
                            System.exit(-1);
                        }     
                        boolean invocationFound = false;
                        for ( SpecialMethodInvocation invocation : specialMethodInvocationFactsList ) 
                            if ( invocation.getInvocation().getCallGraphEdgeSourceRef().getInstructionRef().getInstruction().equals( m.group(1) ) ) {
                                invocation.setInmethod(inmethod);
                                invocationFound = true;
                            }
                        if ( invocationFound == false ) {
                            System.out.println("SpecialMethodInvocation-In.facts: Invocation not found for: " + m.group(1) );
                            System.exit(-1);
                        }
                    }
                    br.close();  
            }
            
            try (BufferedReader br = new BufferedReader( new FileReader( "../cache/input-facts/SpecialMethodInvocation-Signature.facts" ) )) {
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
                            System.out.println( "SpecialMethodInvocation-Signature.facts: Could not find match - " + line );
                            System.exit(-1);
                        }
                        MethodSignatureRef signature = null;

                        for ( MethodSignatureRef signature1 : methodSignatureRefFactsList ) {
                            if ( signature1.getValue().equals( m.group(3) ) ) {
                                signature = signature1;
                                break;
                            }
                        }
                        if ( signature == null ) { 
                            System.out.println( "SpecialMethodInvocation-Signature.facts: Method Signature not found for: " + m.group(3) );
                            System.exit(-1);
                        }
                        boolean invocationFound = false;
                        for ( SpecialMethodInvocation invocation : specialMethodInvocationFactsList ) 
                            if ( invocation.getInvocation().getCallGraphEdgeSourceRef().getInstructionRef().getInstruction().equals( m.group(1) ) ) {
                                invocation.setSignature(signature);
                                invocationFound = true;
                            }
                        if ( invocationFound == false ) {
                            System.out.println("SpecialMethodInvocation-Signature.facts: Invocation not found for: " + m.group(1) );
                            System.exit(-1);
                        }
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
            try ( PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("../datomic_facts/SpecialMethodInvocation.dtm", false)));) {
                for ( SpecialMethodInvocation key : specialMethodInvocationFactsList ) {
                    writer.println( "{:db/id #db/id[:db.part/user " + key.getID() + "]" );
                    writer.println( " :SpecialMethodInvocation/invocation #db/id[:db.part/user " + key.getInvocation().getID() + "]" );
                    writer.println( " :SpecialMethodInvocation/base #db/id[:db.part/user " + key.getVar().getID() + "]" );
                    writer.println( " :SpecialMethodInvocation/inmethod #db/id[:db.part/user " + key.getInmethod().getID() + "]" );
                    writer.println( " :SpecialMethodInvocation/signature #db/id[:db.part/user " + key.getSignature().getID() + "]}" );
                }
                writer.close();
            }
            System.out.println( "SpecialMethodInvocation-Base facts converted: " + specialMethodInvocationFactsList.size() );
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
        t = new Thread(this, "Child thread: SpecialMethodInvocationsFactsConverter" );
        t.start();
    }
    
    public Thread getThread() {
        return t;
    }
    
}
