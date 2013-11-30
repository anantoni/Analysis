/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package inputfactsconverter;

import datomicFacts.MethodInvocationRef;
import datomicFacts.MethodSignatureRef;
import datomicFacts.StaticMethodInvocation;
import datomicFacts.StaticMethodInvocationIn;
import datomicFacts.StaticMethodInvocationSignature;
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
public class StaticMethodInvocationsFactsConverter extends FactsConverter implements Runnable {
    private ArrayList<MethodInvocationRef> methodInvocationRefFactsList = null;
    private ArrayList<MethodSignatureRef> methodSignatureRefFactsList = null;
    private ArrayList<StaticMethodInvocation> staticMethodInvocationFactsList = null;
    private ArrayList<StaticMethodInvocationIn> staticMethodInvocationInFactsList = null;
    private ArrayList<StaticMethodInvocationSignature> staticMethodInvocationSignatureFactsList = null;
    private FactsID id = null;
    private Thread t = null;
    
    public StaticMethodInvocationsFactsConverter(FactsID id, ArrayList<MethodSignatureRef> methodSignatureRefFactsList, ArrayList<MethodInvocationRef> methodInvocationRefFactsList) {
        this.methodInvocationRefFactsList = methodInvocationRefFactsList;
        this.methodSignatureRefFactsList = methodSignatureRefFactsList;
        this.id = id;
        staticMethodInvocationFactsList = new ArrayList<>();
        staticMethodInvocationInFactsList = new ArrayList<>();
        staticMethodInvocationSignatureFactsList = new ArrayList<>();
    }

    @Override
    public void parseLogicBloxFactsFile() {
        try {
                    
            try (BufferedReader br = new BufferedReader( new FileReader( "../cache/input-facts/StaticMethodInvocation.facts" ) )) {
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
                            System.out.println( "StaticMethodInvocation.facts: Could not find match - " + line );
                            System.exit(-1);
                        }
                        
                        MethodInvocationRef invocation = null;
                        MethodSignatureRef signature = null;
                        MethodSignatureRef inmethod = null;
                        
                        for ( MethodInvocationRef invocation1 : methodInvocationRefFactsList ) {
                            if ( invocation1.getCallGraphEdgeSourceRef().getInstructionRef().getInstruction().equals( m.group(1) ) ) {
                                invocation = invocation1;
                                break;
                            }
                        }
                        if ( invocation == null ) { 
                            System.out.println( "StaticMethodInvocation.facts: Method Invocation Ref not found for: " + m.group(1) );
                            System.exit(-1);
                        }
                        
                        for ( MethodSignatureRef signature1 : methodSignatureRefFactsList ) {
                            if ( signature1.getValue().equals( m.group(3) ) ) {
                                signature = signature1;
                                break;
                            }
                        }
                        if ( signature == null ) { 
                            System.out.println( "StaticMethodInvocation.facts: Signature Signature Ref not found for: " + m.group(3) );
                            System.exit(-1);
                        }

                        for ( MethodSignatureRef inmethod1 : methodSignatureRefFactsList ) {
                            if ( inmethod1.getValue().equals( m.group(5) ) ) {
                                inmethod = inmethod1;
                                break;
                            }
                        }
                        if ( inmethod == null ) { 
                            System.out.println( "StaticMethodInvocation.facts: Inmethod Signature Ref not found for: " + m.group(5) );
                            System.exit(-1);
                        }
                        
                        StaticMethodInvocation staticMethodInvocation = new StaticMethodInvocation( id.getID(), invocation, signature, inmethod );
                        staticMethodInvocationFactsList.add(staticMethodInvocation);
                        
                    }
                    br.close();  
            }
            
            try (BufferedReader br = new BufferedReader( new FileReader( "../cache/input-facts/StaticMethodInvocation-In.facts" ) )) {
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
                            System.out.println( "StaticMethodInvocation-In.facts: Could not find match - " + line );
                            System.exit(-1);
                        }
                        MethodInvocationRef invocation = null;
                        MethodSignatureRef inmethod = null;

                        for ( MethodInvocationRef invocation1 : methodInvocationRefFactsList ) {
                            if ( invocation1.getCallGraphEdgeSourceRef().getInstructionRef().getInstruction().equals( m.group(1) ) ) {
                                invocation = invocation1;
                                break;
                            }
                        }
                        if ( invocation == null ) { 
                            System.out.println( "StaticMethodInvocation-In.facts: Method Invocation Ref not found for: " + m.group(1) );
                            System.exit(-1);
                        }

                        for ( MethodSignatureRef inmethod1 : methodSignatureRefFactsList ) {
                            if ( inmethod1.getValue().equals( m.group(3) ) ) {
                                inmethod = inmethod1;
                                break;
                            }
                        }
                        if ( inmethod == null ) { 
                            System.out.println( "StaticMethodInvocation-In.facts: Inmethod Signature Ref not found for: " + m.group(3) );
                            System.exit(-1);
                        }
                        
                        StaticMethodInvocationIn staticMethodInvocationIn = new StaticMethodInvocationIn( id.getID(), invocation, inmethod );
                        staticMethodInvocationInFactsList.add(staticMethodInvocationIn);
                        
                    }
                    br.close();  
            }
            
            try (BufferedReader br = new BufferedReader( new FileReader( "../cache/input-facts/StaticMethodInvocation-Signature.facts" ) )) {
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
                            System.out.println( "StaticMethodInvocation-Signature.facts: Could not find match - " + line );
                            System.exit(-1);
                        }
                        MethodInvocationRef invocation = null;
                        MethodSignatureRef signature = null;

                        for ( MethodInvocationRef invocation1 : methodInvocationRefFactsList ) {
                            if ( invocation1.getCallGraphEdgeSourceRef().getInstructionRef().getInstruction().equals( m.group(1) ) ) {
                                invocation = invocation1;
                                break;
                            }
                        }
                        if ( invocation == null ) { 
                            System.out.println( "StaticMethodInvocation-Signature.facts: Method Invocation Ref not found for: " + m.group(1) );
                            System.exit(-1);
                        }

                        for ( MethodSignatureRef signature1 : methodSignatureRefFactsList ) {
                            if ( signature1.getValue().equals( m.group(3) ) ) {
                                signature = signature1;
                                break;
                            }
                        }
                        if ( signature == null ) { 
                            System.out.println( "StaticMethodInvocation-Signature.facts: Method Signature not found for: " + m.group(3) );
                            System.exit(-1);
                        }
                        
                        StaticMethodInvocationSignature staticMethodInvocationSignature = new StaticMethodInvocationSignature( id.getID(), invocation, signature );
                        staticMethodInvocationSignatureFactsList.add(staticMethodInvocationSignature);
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
            try ( PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("../datomic_facts/StaticMethodInvocation.dtm", false)));) {
                for ( StaticMethodInvocation key : staticMethodInvocationFactsList ) {
                    writer.println( "{:db/id #db/id[:db.part/user " + key.getID() + "]" );
                    writer.println( " :StaticMethodInvocation/invocation #db/id[:db.part/user " + key.getInvocation().getID() + "]" );
                    writer.println( " :StaticMethodInvocation/signature #db/id[:db.part/user " + key.getSignature().getID() + "]" );
                    writer.println( " :StaticMethodInvocation/inmethod #db/id[:db.part/user " + key.getInmethod().getID() + "]}" );
                }
                writer.close();
            }
            System.out.println( "StaticMethodInvocation facts converted: " + staticMethodInvocationFactsList.size() );
            
            try ( PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("../datomic_facts/StaticMethodInvocation-In.dtm", false)));) {
                for ( StaticMethodInvocationIn key : staticMethodInvocationInFactsList ) {
                    writer.println( "{:db/id #db/id[:db.part/user " + key.getID() + "]" );
                    writer.println( " :StaticMethodInvocation-In/invocation #db/id[:db.part/user " + key.getInvocation().getID() + "]" );
                    writer.println( " :StaticMethodInvocation-In/inmethod #db/id[:db.part/user " + key.getInmethod().getID() + "]}" );
                }
                writer.close();
            }
            System.out.println( "StaticMethodInvocation-In facts converted: " + staticMethodInvocationInFactsList.size() );
            
            try ( PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("../datomic_facts/StaticMethodInvocation-Signature.dtm", false)));) {
                for ( StaticMethodInvocationSignature key : staticMethodInvocationSignatureFactsList ) {
                    writer.println( "{:db/id #db/id[:db.part/user " + key.getID() + "]" );
                    writer.println( " :StaticMethodInvocation-Signature/invocation #db/id[:db.part/user " + key.getInvocation().getID() + "]" );
                    writer.println( " :StaticMethodInvocation-Signature/signature #db/id[:db.part/user " + key.getSignature().getID() + "]}" );
                }
                writer.close();
            }
            System.out.println( "StaticMethodInvocation-Signature facts converted: " + staticMethodInvocationSignatureFactsList.size() );
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
        t = new Thread(this, "Child thread: StaticMethodInvocationsFactsConverter" );
        t.start();
    }
     
    public Thread getThread() {
        return t;
    } 
    
}
