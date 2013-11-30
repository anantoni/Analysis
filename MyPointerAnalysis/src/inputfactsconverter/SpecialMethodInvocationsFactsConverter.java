/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package inputfactsconverter;

import datomicFacts.MethodInvocationRef;
import datomicFacts.MethodSignatureRef;
import datomicFacts.SpecialMethodInvocationBase;
import datomicFacts.SpecialMethodInvocationIn;
import datomicFacts.SpecialMethodInvocationSignature;
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
    private ArrayList<SpecialMethodInvocationBase> specialMethodInvocationBaseFactsList = null;
    private ArrayList<SpecialMethodInvocationIn> specialMethodInvocationInFactsList = null;
    private ArrayList<SpecialMethodInvocationSignature> specialMethodInvocationSignatureFactsList = null;
    private FactsID id = null;
    private Thread t = null;
    
    public SpecialMethodInvocationsFactsConverter(FactsID id, ArrayList<Var> varFactsList, ArrayList<MethodSignatureRef> methodSignatureRefFactsList, ArrayList<MethodInvocationRef> methodInvocationRefFactsList) {
        this.methodInvocationRefFactsList = methodInvocationRefFactsList;
        this.varFactsList = varFactsList;
        this.methodSignatureRefFactsList = methodSignatureRefFactsList;
        specialMethodInvocationBaseFactsList = new ArrayList<>();
        specialMethodInvocationInFactsList = new ArrayList<>();
        specialMethodInvocationSignatureFactsList = new ArrayList<>();
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
                        SpecialMethodInvocationBase specialMethodInvocationBase = new SpecialMethodInvocationBase( id.getID(), invocation, var );
                        specialMethodInvocationBaseFactsList.add(specialMethodInvocationBase);
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
                        MethodInvocationRef invocation = null;
                        MethodSignatureRef inmethod = null;

                        for ( MethodInvocationRef invocation1 : methodInvocationRefFactsList ) {
                            if ( invocation1.getCallGraphEdgeSourceRef().getInstructionRef().getInstruction().equals( m.group(1) ) ) {
                                invocation = invocation1;
                                break;
                            }
                        }
                        if ( invocation == null ) { 
                            System.out.println( "SpecialMethodInvocation-In.facts: Method Invocation Ref not found for: " + m.group(1) );
                            System.exit(-1);
                        }

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
                        SpecialMethodInvocationIn specialMethodInvocationIn = new SpecialMethodInvocationIn( id.getID(), invocation, inmethod );
                        specialMethodInvocationInFactsList.add(specialMethodInvocationIn);
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
                        MethodInvocationRef invocation = null;
                        MethodSignatureRef signature = null;

                        for ( MethodInvocationRef invocation1 : methodInvocationRefFactsList ) {
                            if ( invocation1.getCallGraphEdgeSourceRef().getInstructionRef().getInstruction().equals( m.group(1) ) ) {
                                invocation = invocation1;
                                break;
                            }
                        }
                        if ( invocation == null ) { 
                            System.out.println( "SpecialMethodInvocation-Signature.facts: Method Invocation Ref not found for: " + m.group(1) );
                            System.exit(-1);
                        }

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
                        SpecialMethodInvocationSignature specialMethodInvocationSignature = new SpecialMethodInvocationSignature( id.getID(), invocation, signature );
                        specialMethodInvocationSignatureFactsList.add(specialMethodInvocationSignature);
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
            try ( PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("../datomic_facts/SpecialMethodInvocation-Base.dtm", false)));) {
                for ( SpecialMethodInvocationBase key : specialMethodInvocationBaseFactsList ) {
                    writer.println( "{:db/id #db/id[:db.part/user " + key.getID() + "]" );
                    writer.println( " :SpecialMethodInvocation-Base/invocation #db/id[:db.part/user " + key.getInvocation().getID() + "]" );
                    writer.println( " :SpecialMethodInvocation-Base/base #db/id[:db.part/user " + key.getVar().getID() + "]}" );
                }
                writer.close();
            }
            System.out.println( "SpecialMethodInvocation-Base facts converted: " + specialMethodInvocationBaseFactsList.size() );
            
            try ( PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("../datomic_facts/SpecialMethodInvocation-In.dtm", false)));) {
                for ( SpecialMethodInvocationIn key : specialMethodInvocationInFactsList ) {
                    writer.println( "{:db/id #db/id[:db.part/user " + key.getID() + "]" );
                    writer.println( " :SpecialMethodInvocation-In/invocation #db/id[:db.part/user " + key.getInvocation().getID() + "]" );
                    writer.println( " :SpecialMethodInvocation-In/inmethod #db/id[:db.part/user " + key.getInmethod().getID() + "]}" );
                }
                writer.close();
            }
            System.out.println( "SpecialMethodInvocation-In facts converted: " + specialMethodInvocationInFactsList.size() );
            
            try ( PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("../datomic_facts/SpecialMethodInvocation-Signature.dtm", false)));) {
                for ( SpecialMethodInvocationSignature key : specialMethodInvocationSignatureFactsList ) {
                    writer.println( "{:db/id #db/id[:db.part/user " + key.getID() + "]" );
                    writer.println( " :SpecialMethodInvocation-Signature/invocation #db/id[:db.part/user " + key.getInvocation().getID() + "]" );
                    writer.println( " :SpecialMethodInvocation-Signature/signature #db/id[:db.part/user " + key.getSignature().getID() + "]}" );
                }
                writer.close();
            }
            System.out.println( "SpecialMethodInvocation-Signature facts converted: " + specialMethodInvocationSignatureFactsList.size() );
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
