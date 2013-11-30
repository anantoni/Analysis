/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package inputfactsconverter;

import datomicFacts.MethodDeclaration;
import datomicFacts.MethodDeclarationException;
import datomicFacts.MethodDescriptorRef;
import datomicFacts.MethodModifier;
import datomicFacts.MethodSignatureDescriptor;
import datomicFacts.MethodSignatureRef;
import datomicFacts.MethodSignatureSimpleName;
import datomicFacts.MethodSignatureType;
import datomicFacts.ModifierRef;
import datomicFacts.SimpleNameRef;
import datomicFacts.Type;
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
public class MethodDeclarationsAndSignaturesFactsConverter extends FactsConverter implements Runnable {
    private ArrayList<Type> typeFactsList = null;
    private ArrayList<SimpleNameRef> simpleNameRefFactsList = null;
    private ArrayList<MethodSignatureRef> methodSignatureRefFactsList = null;
    private ArrayList<ModifierRef> modifierRefFactsList = null;
    private ArrayList<MethodDescriptorRef> methodDescriptorRefFactsList = null;
    private ArrayList<MethodDeclaration> methodDeclarationFactsList = null;
    private ArrayList<MethodDeclarationException> methodDeclarationExceptionFactsList = null;
    private ArrayList<MethodSignatureType> methodSignatureTypeFactsList = null;
    private ArrayList<MethodSignatureSimpleName> methodSignatureSimpleNameFactsList = null;
    private ArrayList<MethodSignatureDescriptor> methodSignatureDescriptorFactsList = null;
    private ArrayList<MethodModifier> methodModifierFactsList = null;
    private FactsID id = null;
    Thread t = null;
    
    public MethodDeclarationsAndSignaturesFactsConverter( FactsID id, ArrayList<Type> typeFactsList, ArrayList<SimpleNameRef> simpleNameRefFactsList, ArrayList<MethodSignatureRef> methodSignatureRefFactsList, ArrayList<ModifierRef> modifierRefFactsList ) {
        this.typeFactsList = typeFactsList;
        this.simpleNameRefFactsList = simpleNameRefFactsList;
        this.methodSignatureRefFactsList = methodSignatureRefFactsList;
        this.modifierRefFactsList = modifierRefFactsList;
        this.id = id;
        methodDeclarationFactsList = new ArrayList();
        methodDescriptorRefFactsList = new ArrayList<>();
        methodDeclarationExceptionFactsList = new ArrayList<>();
        methodSignatureTypeFactsList = new ArrayList<>();
        methodSignatureSimpleNameFactsList = new ArrayList<>();
        methodSignatureDescriptorFactsList = new ArrayList<>();
        methodModifierFactsList = new ArrayList<>();
    }
    
    public ArrayList<MethodDescriptorRef> getMethodDescriptorRefFactsList() {
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

                        MethodDeclaration methodDeclaration = new MethodDeclaration( id.getID(), signature, ref );
                        methodDeclarationFactsList.add( methodDeclaration );
                        
                    }
                    br.close();  
            }
            
            try (BufferedReader br = new BufferedReader( new FileReader( "../cache/input-facts/MethodDeclaration-Exception.facts" ) )) {
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
                        MethodSignatureRef method = null;
                        Type exceptionType = null;

                        for ( MethodSignatureRef methodSignatureRef : methodSignatureRefFactsList ) {
                            if ( methodSignatureRef.getValue().equals( m.group(3) ) ) {
                                method = methodSignatureRef;
                                break;
                            }
                        }
                        if ( method == null ) { 
                            System.out.println( "Method Signature Ref not found for: " + m.group(3) );
                            System.exit(-1);
                        }

                        for ( Type type: typeFactsList ) {
                            if ( type.getValue().equals( m.group(1) ) ) {
                                exceptionType = type;
                                break;
                            }
                        }
                        if ( exceptionType == null ) { 
                            System.out.println( "Exception type not found for: " + m.group(1) );
                            System.exit(-1);
                        }

                        MethodDeclarationException methodDeclarationException = new MethodDeclarationException( id.getID(), method , exceptionType );
                        methodDeclarationExceptionFactsList.add( methodDeclarationException );
                        
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
                    MethodSignatureRef signature = null;
                    Type type = null;

                    for ( MethodSignatureRef methodSignatureRef : methodSignatureRefFactsList ) {
                        if ( methodSignatureRef.getValue().equals( m.group(1) ) ) {
                            signature = methodSignatureRef;
                            break;
                        }
                    }
                    if ( signature == null ) { 
                        System.out.println( "MethodSignature-Type.facts: Method signature not found for: " + m.group(1) );
                        System.exit(-1);
                    }

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

                    MethodSignatureType methodSignatureType = new MethodSignatureType( id.getID(), signature, type );
                    methodSignatureTypeFactsList.add(methodSignatureType);

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
                    MethodSignatureRef signature = null;
                    SimpleNameRef simplename = null;

                    for ( MethodSignatureRef methodSignatureRef : methodSignatureRefFactsList ) {
                        if ( methodSignatureRef.getValue().equals( m.group(1) ) ) {
                            signature = methodSignatureRef;
                            break;
                        }
                    }
                    if ( signature == null ) { 
                        System.out.println( "MethodSignature-SimpleName.facts: Method signature not found for: " + m.group(1) );
                        System.exit(-1);
                    }

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

                    MethodSignatureSimpleName methodSignatureSimpleName = new MethodSignatureSimpleName( id.getID(), signature, simplename );
                    methodSignatureSimpleNameFactsList.add(methodSignatureSimpleName);

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
                    MethodSignatureRef signature = null;
                    MethodDescriptorRef descriptor = null;

                    for ( MethodSignatureRef methodSignatureRef : methodSignatureRefFactsList ) {
                        if ( methodSignatureRef.getValue().equals( m.group(1) ) ) {
                            signature = methodSignatureRef;
                            break;
                        }
                    }
                    if ( signature == null ) { 
                        System.out.println( "MethodSignature-Descriptor.facts: Method signature not found for: " + m.group(1) );
                        System.exit(-1);
                    }

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

                    MethodSignatureDescriptor methodSignatureDescriptor = new MethodSignatureDescriptor( id.getID(), signature, descriptor );
                    methodSignatureDescriptorFactsList.add(methodSignatureDescriptor);

                }
                br.close();  
            }
            
            try (BufferedReader br = new BufferedReader( new FileReader( "../cache/input-facts/MethodModifier.facts" ) )) {
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
                    MethodSignatureRef method = null;
                    ModifierRef mod = null;

                    for ( MethodSignatureRef methodSignatureRef : methodSignatureRefFactsList ) {
                        if ( methodSignatureRef.getValue().equals( m.group(3) ) ) {
                            method = methodSignatureRef;
                            break;
                        }
                    }
                    if ( method == null ) { 
                        System.out.println( "MethodModifier.facts: Method signature not found for: " + m.group(3) );
                        System.exit(-1);
                    }

                    for ( ModifierRef mod1 : modifierRefFactsList ) {
                        if ( mod1.getValue().equals( m.group(1) ) ) {
                            mod = mod1;
                            break;
                        }
                    }
                    if ( mod == null ) { 
                        System.out.println( "MethodModifier.facts: Method modifier not found for: " + m.group(1) );
                        System.exit(-1);
                    }

                    MethodModifier methodModifier = new MethodModifier( id.getID(), method, mod );
                    methodModifierFactsList.add(methodModifier);
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
            try ( PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("../datomic_facts/MethodDescriptorRef.dtm", false)));) {
                for ( MethodDescriptorRef key : methodDescriptorRefFactsList ) {
                    writer.println( "{:db/id #db/id[:db.part/user " + key.getID() + "]" );
                    writer.println( " :MethodDescriptorRef/value \"" + key.getValue() + "\"}");
                }
                writer.close();
            }
            System.out.println( "MethodDescriptorRef facts converted: " + methodDescriptorRefFactsList.size() );
            
            try ( PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("../datomic_facts/MethodDeclaration.dtm", false)));) {
                for ( MethodDeclaration key : methodDeclarationFactsList ) {
                    writer.println( "{:db/id #db/id[:db.part/user " + key.getID() + "]" );
                    writer.println( " :MethodDeclaration/signature #db/id[:db.part/user " + key.getSignature().getID() + "]");
                    writer.println( " :MethodDeclaration/ref #db/id[:db.part/user " + key.getRef().getID() + "]}");
                }
                writer.close();
            }
            System.out.println( "MethodDeclaration facts converted: " + methodDeclarationFactsList.size() );
            
            try ( PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("../datomic_facts/MethodDeclaration-Exception.dtm", false)));) {
                for ( MethodDeclarationException key : methodDeclarationExceptionFactsList ) {
                    writer.println( "{:db/id #db/id[:db.part/user " + key.getID() + "]" );
                    writer.println( " :MethodDeclaration-Exception/method #db/id[:db.part/user " + key.getMethod().getID() + "]");
                    writer.println( " :MethodDeclaration-Exception/exceptionType #db/id[:db.part/user " + key.getExceptionType().getID() + "]}");
                }
                writer.close();
            }
            System.out.println( "MethodDeclaration-Exception facts converted: " + methodDeclarationExceptionFactsList.size() );
            
            try ( PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("../datomic_facts/MethodSignature-Type.dtm", false)));) {
                for ( MethodSignatureType key : methodSignatureTypeFactsList ) {
                    writer.println( "{:db/id #db/id[:db.part/user " + key.getID() + "]" );
                    writer.println( " :MethodSignature-Type/signature #db/id[:db.part/user " + key.getMethodSignatureRef().getID() + "]");
                    writer.println( " :MethodSignature-Type/type #db/id[:db.part/user " + key.getType().getID() + "]}");
                }
                writer.close();
            }
            System.out.println( "MethodSignature-Type facts converted: " + methodSignatureTypeFactsList.size() );

            
            try ( PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("../datomic_facts/MethodSignature-SimpleName.dtm", false)));) {
                for ( MethodSignatureSimpleName key : methodSignatureSimpleNameFactsList ) {
                    writer.println( "{:db/id #db/id[:db.part/user " + key.getID() + "]" );
                    writer.println( " :MethodSignature-SimpleName/signature #db/id[:db.part/user " + key.getMethodSignatureRef().getID() + "]");
                    writer.println( " :MethodSignature-SimpleName/simplename #db/id[:db.part/user " + key.getSimpleName().getID() + "]}");
                }
                writer.close();
            }
            System.out.println( "MethodSignature-SimpleName facts converted: " + methodSignatureSimpleNameFactsList.size() );

            try ( PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("../datomic_facts/MethodSignature-Descriptor.dtm", false)));) {
                for ( MethodSignatureDescriptor key : methodSignatureDescriptorFactsList ) {
                    writer.println( "{:db/id #db/id[:db.part/user " + key.getID() + "]" );
                    writer.println( " :MethodSignature-Descriptor/signature #db/id[:db.part/user " + key.getMethodSignatureRef().getID() + "]");
                    writer.println( " :MethodSignature-Descriptor/descriptor #db/id[:db.part/user " + key.getDescriptor().getID() + "]}");    
                }
                writer.close();
            }
            System.out.println( "MethodSignature-Descriptor facts converted: " + methodSignatureDescriptorFactsList.size() );     
            
            try ( PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("../datomic_facts/MethodModifier.dtm", false)));) {
                for ( MethodModifier key : methodModifierFactsList ) {
                    writer.println( "{:db/id #db/id[:db.part/user " + key.getID() + "]" );
                    writer.println( " :MethodModifier/method #db/id[:db.part/user " + key.getMethod().getID() + "]");
                    writer.println( " :MethodModifier/mod #db/id[:db.part/user " + key.getModifier().getID() + "]}");    
                }
                writer.close();
            }
            System.out.println( "MethodModifier facts converted: " + methodModifierFactsList.size() ); 
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
