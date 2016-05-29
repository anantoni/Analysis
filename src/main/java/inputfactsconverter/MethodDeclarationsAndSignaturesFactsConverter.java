/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package inputfactsconverter;

import datomicFacts.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author anantoni
 */
public class MethodDeclarationsAndSignaturesFactsConverter implements FactsConverter, Runnable {
    private List<Type> typeFactsList = null;
    private List<SimpleNameRef> simpleNameRefFactsList = null;
    private List<MethodSignatureRef> methodSignatureRefFactsList = null;
    private List<Method> methodFactsList = null;
    private List<MethodDescriptorRef> methodDescriptorRefFactsList = null;
    private FactsID id = null;
    Thread t = null;
    
    public MethodDeclarationsAndSignaturesFactsConverter( FactsID id, List<Type> typeFactsList, List<SimpleNameRef> simpleNameRefFactsList, List<MethodSignatureRef> methodSignatureRefFactsList, List<ModifierRef> modifierRefFactsList ) {
        this.typeFactsList = typeFactsList;
        this.simpleNameRefFactsList = simpleNameRefFactsList;
        this.methodSignatureRefFactsList = methodSignatureRefFactsList;
        this.id = id;
        this.methodFactsList = new ArrayList<>();
        this.methodDescriptorRefFactsList = new ArrayList<>();
    }
    
    public List<MethodDescriptorRef> getMethodDescriptorRefFactsList() {
        return this.methodDescriptorRefFactsList;
    }
    
    @Override
    public void parseLogicBloxFactsFile() {
        try {
            try (BufferedReader br = new BufferedReader( new FileReader( "../cache/input-facts/MethodDescriptorRef.facts" ) )) {
                String line;
                while ((line = br.readLine()) != null) {
                    line = line.trim();
                    MethodDescriptorRef methodDescriptorRef = new MethodDescriptorRef(id.getID(), line );
                    methodDescriptorRefFactsList.add( methodDescriptorRef );

                }
                br.close();
            }
                    
            try (BufferedReader br = new BufferedReader( new FileReader( "../cache/input-facts/MethodDeclaration.facts" ) )) {
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
                            System.out.println( "Could not find match" );
                            System.exit(-1);
                        }
                        MethodSignatureRef signature = null;
                        MethodSignatureRef ref = null;

                        for ( MethodSignatureRef methodSignatureRef1 : methodSignatureRefFactsList ) {
                            if ( methodSignatureRef1.getValue().equals( m.group(1) ) ) {
                                signature = methodSignatureRef1;
                                break;
                            }
                        }
                        if ( signature == null ) { 
                            System.out.println( "Method Signature Ref not found for: " + m.group(1) );
                            System.exit(-1);
                        }

                        for ( MethodSignatureRef methodSignatureRef2 : methodSignatureRefFactsList ) {
                            if ( methodSignatureRef2.getValue().equals( m.group(3) ) ) {
                                ref = methodSignatureRef2;
                                break;
                            }
                        }
                        if ( ref == null ) { 
                            System.out.println( "Method Signature Ref not found for: " + m.group(3) );
                            System.exit(-1);
                        }

                        Method method = new Method( id.getID(), signature, ref );
                        methodFactsList.add( method );
                        
                    }
                    br.close();  
            }
            
            try (BufferedReader br = new BufferedReader( new FileReader( "../cache/input-facts/MethodSignature-Type.facts" ) )) {
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
                        System.out.println( "Could not find match" );
                        System.exit(-1);
                    }
                    Type type = null;

                    for ( Type type1 : typeFactsList ) {
                        if ( type1.getValue().equals( m.group(3) ) ) {
                            type = type1;
                            break;
                        }
                    }
                    if ( type == null ) { 
                        System.out.println( "MethodSignature-Type.facts: Method type not found for: " + m.group(3) );
                        System.exit(-1);
                    }
                    boolean methodFound = false;
                    for ( Method method : methodFactsList ) 
                        if ( method.getSignature().getValue().equals( m.group(1) ) ) {
                            method.setType(type);
                            methodFound = true;
                        }
                    if ( methodFound == false ) {
                        System.out.println("MethodSignature-Type.facts: Method not found for: " + m.group(1) );
                        System.exit(-1);
                    }
                }
                br.close();  
            }
            
            try (BufferedReader br = new BufferedReader( new FileReader( "../cache/input-facts/MethodSignature-SimpleName.facts" ) )) {
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
                        System.out.println( "Could not find match" );
                        System.exit(-1);
                    }
                    SimpleNameRef simplename = null;

                    for ( SimpleNameRef simplename1 : simpleNameRefFactsList ) {
                        if ( simplename1.getValue().equals( m.group(3) ) ) {
                            simplename = simplename1;
                            break;
                        }
                    }
                    if ( simplename == null ) { 
                        System.out.println( "MethodSignature-Simplename.facts: Method simplename not found for: " + m.group(3) );
                        System.exit(-1);
                    }
                    boolean methodFound = false;
                    for ( Method method : methodFactsList ) 
                        if ( method.getSignature().getValue().equals( m.group(1) ) ) {
                            method.setSimpleName(simplename);
                            methodFound = true;
                        }
                    if ( methodFound == false ) {
                        System.out.println("MethodSignature-Simplename.facts: Method not found for: " + m.group(1) );
                        System.exit(-1);
                    }
                }
                br.close();  
            }
            
            try (BufferedReader br = new BufferedReader( new FileReader( "../cache/input-facts/MethodSignature-Descriptor.facts" ) )) {
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
                        System.out.println( "Could not find match" );
                        System.exit(-1);
                    }
                    MethodDescriptorRef descriptor = null;

                    for ( MethodDescriptorRef descriptor1 : methodDescriptorRefFactsList ) {
                        if ( descriptor1.getValue().equals( m.group(3) ) ) {
                            descriptor = descriptor1;
                            break;
                        }
                    }
                    if ( descriptor == null ) { 
                        System.out.println( "MethodSignature-Descriptor.facts: Method descriptor not found for: " + m.group(3) );
                        System.exit(-1);
                    }
                    boolean methodFound = false;
                    for ( Method method : methodFactsList ) 
                        if ( method.getSignature().getValue().equals( m.group(1) ) ) {
                            method.setDescriptor(descriptor);
                            methodFound = true;
                        }
                    if ( methodFound == false ) {
                        System.out.println("MethodSignature-Descriptor.facts: Method not found for: " + m.group(1) );
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
    public void createDatomicFactsFile() {
        try {
            try ( PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("../datomic_facts/MethodDescriptorRef.dtm", false)));) {
                for ( MethodDescriptorRef key : methodDescriptorRefFactsList ) {
                    writer.println( "{:db/id #db/id[:db.part/user " + key.getID() + "]" );
                    writer.println( " :MethodDescriptorRef/value \"" + key.getValue() + "\"}");
                }
                writer.close();
            }
            System.out.println( "MethodDescriptorRef facts converted: " + methodDescriptorRefFactsList.size() );
            
            try ( PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("../datomic_facts/Method.dtm", false)));) {
                for ( Method key : methodFactsList ) {
                    writer.println( "{:db/id #db/id[:db.part/user " + key.getID() + "]" );
                    writer.println( " :Method/signature #db/id[:db.part/user " + key.getSignature().getID() + "]");
                    writer.println( " :Method/simplename #db/id[:db.part/user " + key.getSimpleName().getID() + "]");
                    writer.println( " :Method/type #db/id[:db.part/user " + key.getType().getID() + "]");
                    writer.println( " :Method/descriptor #db/id[:db.part/user " + key.getDescriptor().getID() + "]");    
                    writer.println( " :Method/declaration #db/id[:db.part/user " + key.getDeclaration().getID() + "]}");
                }
                writer.close();
            }
            System.out.println( "Method facts converted: " + methodFactsList.size() );
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
        t = new Thread(this, "Child thread: MethodDeclarationsAndSignaturesFactsConverter" );
        t.start();
    } 
     
    public Thread getThread() {
        return t;
    } 
}
