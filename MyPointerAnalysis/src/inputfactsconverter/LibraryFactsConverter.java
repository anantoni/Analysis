/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package inputfactsconverter;

import datomicFacts.AssignAuxiliaryHeapAllocation;
import datomicFacts.AssignCompatible;
import datomicFacts.AssignContextInsensitiveHeapAllocation;
import datomicFacts.AssignNormalHeapAllocation;
import datomicFacts.ClassInitializer;
import datomicFacts.HeapAllocationRef;
import datomicFacts.ImplicitReachable;
import datomicFacts.InitializedClass;
import datomicFacts.MainMethodDeclaration;
import datomicFacts.MethodDescriptorRef;
import datomicFacts.MethodLookup;
import datomicFacts.MethodSignatureRef;
import datomicFacts.SimpleNameRef;
import datomicFacts.Type;
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
public class LibraryFactsConverter extends FactsConverter implements Runnable {
    FactsID id = null;
    private ArrayList<Type> typeFactsList = null;
    private ArrayList<MethodSignatureRef> methodSignatureRefFactsList = null;
    private ArrayList<InitializedClass> initializedClassFactsList = null;
    private ArrayList<ClassInitializer> classInitializerFactsList = null;
    private ArrayList<MainMethodDeclaration> mainMethodDeclarationFactsList = null;
    private ArrayList<ImplicitReachable> implicitReachabeFactsList = null;
    private ArrayList<AssignCompatible> assignCompatibleFactsList = null;
    private ArrayList<MethodLookup> methodLookupFactsList = null;
    private ArrayList<SimpleNameRef> simpleNameRefFactsList = null;
    private ArrayList<MethodDescriptorRef> methodDescriptorRefFactsList = null;
    private ArrayList<HeapAllocationRef> heapAllocationRefFactsList = null;
    private ArrayList<Var> varFactsList = null;
    private ArrayList<AssignNormalHeapAllocation> assignNormalHeapAllocationFactsList = null;
    private ArrayList<AssignAuxiliaryHeapAllocation> assignAuxiliaryHeapAllocationFactsList = null;
    private ArrayList<AssignContextInsensitiveHeapAllocation> assignContextInsensitiveHeapAllocationFactsList = null;
    Thread t = null;
    
    public LibraryFactsConverter( FactsID id, ArrayList<Type> typeFactsList, ArrayList<MethodSignatureRef> methodSignatureRefFactsList, 
                                    ArrayList<MethodDescriptorRef> methodDescriptorRefFactsList, ArrayList<SimpleNameRef> simplenNameRefFactsList,
                                    ArrayList<HeapAllocationRef> heapAllocationRefFactsList, ArrayList<Var> varFactsList ) {
        this.id = id;
        this.typeFactsList = typeFactsList;
        this.methodSignatureRefFactsList = methodSignatureRefFactsList;
        this.initializedClassFactsList = new ArrayList<>();
        this.mainMethodDeclarationFactsList = new ArrayList<>();
        this.classInitializerFactsList = new ArrayList<>();
        this.implicitReachabeFactsList = new ArrayList<>();
        this.assignCompatibleFactsList = new ArrayList<>();
        this.methodLookupFactsList = new ArrayList<>();
        this.simpleNameRefFactsList = simplenNameRefFactsList;
        this.methodDescriptorRefFactsList = methodDescriptorRefFactsList;
        this.varFactsList = varFactsList;
        this.heapAllocationRefFactsList = heapAllocationRefFactsList;
        this.assignNormalHeapAllocationFactsList = new ArrayList<>();
        this.assignAuxiliaryHeapAllocationFactsList = new ArrayList<>();
        this.assignContextInsensitiveHeapAllocationFactsList = new ArrayList<>();
        
    }

    @Override
    void parseLogicBloxFactsFile() {
        try {
                    
            try (BufferedReader br = new BufferedReader( new FileReader( "../cache/input-facts/InitializedClass.facts" ) )) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        line = line.trim();
                      
                        Type type = null;

                        for ( Type type1 : typeFactsList ) {
                            if ( type1.getValue().equals( line ) ) {
                                type = type1;
                                break;
                            }
                        }
                        if ( type == null ) { 
                            System.out.println( "InitializedClass.facts: Type not found for: " + line );
                            System.exit(-1);
                        }

                        InitializedClass initializedClass = new InitializedClass(id.getID(), type);
                        initializedClassFactsList.add(initializedClass);
                        
                    }
                    br.close();  
            }
            
            try (BufferedReader br = new BufferedReader( new FileReader( "../cache/input-facts/ClassInitializer.facts" ) )) {
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
                        Type type = null;
                        
                        for ( Type type1 : typeFactsList ) {
                            if ( type1.getValue().equals( m.group(1) ) ) {
                                type = type1;
                                break;
                            }
                        }
                        if ( type == null ) { 
                            System.out.println( "ClassInitializer.facts: Type not found for: " + m.group(1) );
                            System.exit(-1);
                        }
                        
                        for ( MethodSignatureRef method1 : methodSignatureRefFactsList ) {
                            if ( method1.getValue().equals( m.group(3) ) ) {
                                method = method1;
                                break;
                            }
                        }
                        if ( method == null ) { 
                            System.out.println( "ClassInitializer.facts: Method Signature Ref not found for: " + m.group(3) );
                            System.exit(-1);
                        }

                        ClassInitializer classInitializer = new ClassInitializer(id.getID(), type, method);
                        classInitializerFactsList.add(classInitializer);
                    }
                    br.close();  
            }
            
            try (BufferedReader br = new BufferedReader( new FileReader( "../cache/input-facts/AssignCompatible.facts" ) )) {
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
                        
                        Type target = null;
                        Type source = null;
                        
                        for ( Type target1 : typeFactsList ) {
                            if ( target1.getValue().equals( m.group(1) ) ) {
                                target = target1;
                                break;
                            }
                        }
                        if ( target == null ) { 
                            System.out.println( "AssignCompatible.facts: Type not found for: " + m.group(1) );
                            System.exit(-1);
                        }
                        
                        for ( Type target1 : typeFactsList ) {
                            if ( target1.getValue().equals( m.group(3) ) ) {
                                source = target1;
                                break;
                            }
                        }
                        if ( source == null ) { 
                            System.out.println( "AssignCompatible.facts: Type not found for: " + m.group(3) );
                            System.exit(-1);
                        }

                        AssignCompatible assignCompatible = new AssignCompatible( id.getID(), target, source );
                        assignCompatibleFactsList.add(assignCompatible);
                        
                    }
                    br.close();  
            }
            
            try (BufferedReader br = new BufferedReader( new FileReader( "../cache/input-facts/MethodLookup.facts" ) )) {
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
                            System.out.println( "Could not find match" );
                            System.exit(-1);
                        }
                        
                        SimpleNameRef simplename = null;
                        MethodDescriptorRef descriptor = null;
                        Type type = null;
                        MethodSignatureRef method = null;
                        
                        for ( SimpleNameRef simplename1 : simpleNameRefFactsList) {
                            if ( simplename1.getValue().equals( m.group(1) ) ) {
                                simplename = simplename1;
                                break;
                            }
                        }
                        if ( simplename == null ) { 
                            System.out.println( "MethodLookup.facts: Type not found for: " + m.group(1) );
                            System.exit(-1);
                        }
                        
                        for ( MethodDescriptorRef descriptor1 : methodDescriptorRefFactsList ) {
                            if ( descriptor1.getValue().equals( m.group(3) ) ) {
                                descriptor = descriptor1;
                                break;
                            }
                        }
                        if ( descriptor == null ) { 
                            System.out.println( "MethodLookup.facts: Descriptor not found for: " + m.group(3) );
                            System.exit(-1);
                        }
                        
                        for ( Type type1 : typeFactsList ) {
                            if ( type1.getValue().equals( m.group(5) ) ) {
                                type = type1;
                                break;
                            }
                        }
                        if ( type == null ) { 
                            System.out.println( "MethodLookup: Type not found for: " + m.group(5) );
                            System.exit(-1);
                        }
                        
                        for ( MethodSignatureRef method1 : methodSignatureRefFactsList ) {
                            if ( method1.getValue().equals( m.group(7) ) ) {
                                method = method1;
                                break;
                            }
                        }
                        if ( method == null ) { 
                            System.out.println( "MethodLookup: Method Signature not found for: " + m.group(7) );
                            System.exit(-1);
                        }

                        MethodLookup methodLookup = new MethodLookup(id.getID(), type, simplename, descriptor, method);
                        methodLookupFactsList.add(methodLookup);
                    }
                    br.close();  
            }
            
            try (BufferedReader br = new BufferedReader( new FileReader( "../cache/input-facts/AssignNormalHeapAllocation.facts" ) )) {
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
                            System.out.println( "Could not find match" );
                            System.exit(-1);
                        }
                        
                        HeapAllocationRef heap = null;
                        Var var = null;
                        MethodSignatureRef inmethod = null;
                        
                        
                        for ( HeapAllocationRef heap1 : heapAllocationRefFactsList ) {
                            if ( heap1.getCallGraphEdgeSourceRef().getInstructionRef().getInstruction().equals( m.group(1) ) ) {
                                heap = heap1;
                                break;
                            }
                        }
                        if ( heap == null ) { 
                            System.out.println( "AssignNormalHeapAllocation.facts: Heap not found for: " + m.group(1) );
                            System.exit(-1);
                        }
                        
                        for ( Var var1 : varFactsList ) {
                            if ( var1.getName().equals( m.group(3) ) ) {
                                var = var1;
                                break;
                            }
                        }
                        if ( var == null ) { 
                            System.out.println( "AssignNormalHeapAllocation.facts: Var not found for: " + m.group(3) );
                            System.exit(-1);
                        }
                        
                        
                        for ( MethodSignatureRef inmethod1 : methodSignatureRefFactsList ) {
                            if ( inmethod1.getValue().equals( m.group(5) ) ) {
                                inmethod = inmethod1;
                                break;
                            }
                        }
                        if ( inmethod == null ) { 
                            System.out.println( "AssignNormalHeapAllocation.facts: Method Signature not found for: " + m.group(5) );
                            System.exit(-1);
                        }
                        AssignNormalHeapAllocation assignNormalHeapAllocation = new AssignNormalHeapAllocation(id.getID(), heap, var, inmethod);
                        assignNormalHeapAllocationFactsList.add(assignNormalHeapAllocation);
                    }
                    br.close();  
            }
            
            try (BufferedReader br = new BufferedReader( new FileReader( "../cache/input-facts/AssignAuxiliaryHeapAllocation.facts" ) )) {
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
                            System.out.println( "Could not find match" );
                            System.exit(-1);
                        }
                        
                        HeapAllocationRef heap = null;
                        Var var = null;
                        MethodSignatureRef inmethod = null;
                        
                        
                        for ( HeapAllocationRef heap1 : heapAllocationRefFactsList ) {
                            if ( heap1.getCallGraphEdgeSourceRef().getInstructionRef().getInstruction().equals( m.group(1) ) ) {
                                heap = heap1;
                                break;
                            }
                        }
                        if ( heap == null ) { 
                            System.out.println( "AssignAuxiliaryHeapAllocation.facts: Heap not found for: " + m.group(1) );
                            System.exit(-1);
                        }
                        
                        for ( Var var1 : varFactsList ) {
                            if ( var1.getName().equals( m.group(3) ) ) {
                                var = var1;
                                break;
                            }
                        }
                        if ( var == null ) { 
                            System.out.println( "AssignAuxiliaryHeapAllocation.facts: Var not found for: " + m.group(3) );
                            System.exit(-1);
                        }
                        
                        
                        for ( MethodSignatureRef inmethod1 : methodSignatureRefFactsList ) {
                            if ( inmethod1.getValue().equals( m.group(5) ) ) {
                                inmethod = inmethod1;
                                break;
                            }
                        }
                        if ( inmethod == null ) { 
                            System.out.println( "AssignAuxiliaryHeapAllocation.facts: Method Signature not found for: " + m.group(5) );
                            System.exit(-1);
                        }
                        AssignAuxiliaryHeapAllocation assignAuxiliaryHeapAllocation = new AssignAuxiliaryHeapAllocation(id.getID(), heap, var, inmethod);
                        assignAuxiliaryHeapAllocationFactsList.add(assignAuxiliaryHeapAllocation);
                    }
                    br.close();  
            }
            
            try (BufferedReader br = new BufferedReader( new FileReader( "../cache/input-facts/AssignContextInsensitiveHeapAllocation.facts" ) )) {
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
                            System.out.println( "Could not find match" );
                            System.exit(-1);
                        }
                        
                        HeapAllocationRef heap = null;
                        Var var = null;
                        MethodSignatureRef inmethod = null;
                        
                        for ( HeapAllocationRef heap1 : heapAllocationRefFactsList ) {
                            if ( heap1.getCallGraphEdgeSourceRef().getInstructionRef().getInstruction().equals( m.group(1) ) ) {
                                heap = heap1;
                                break;
                            }
                        }
                        if ( heap == null ) { 
                            System.out.println( "AssignContextInsensitive.facts: Heap not found for: " + m.group(1) );
                            System.exit(-1);
                        }
                        
                        for ( Var var1 : varFactsList ) {
                            if ( var1.getName().equals( m.group(3) ) ) {
                                var = var1;
                                break;
                            }
                        }
                        if ( var == null ) { 
                            System.out.println( "AssignContextInsensitive.facts: Var not found for: " + m.group(3) );
                            System.exit(-1);
                        }
                        
                        for ( MethodSignatureRef inmethod1 : methodSignatureRefFactsList ) {
                            if ( inmethod1.getValue().equals( m.group(5) ) ) {
                                inmethod = inmethod1;
                                break;
                            }
                        }
                        if ( inmethod == null ) { 
                            System.out.println( "AssignContextInsensitive.facts: Method Signature not found for: " + m.group(5) );
                            System.exit(-1);
                        }
                        AssignContextInsensitiveHeapAllocation assignContextInsensitiveHeapAllocation = new AssignContextInsensitiveHeapAllocation(id.getID(), heap, var, inmethod);
                        assignContextInsensitiveHeapAllocationFactsList.add(assignContextInsensitiveHeapAllocation);
                    }
                    br.close();  
            }
            
            try (BufferedReader br = new BufferedReader( new FileReader( "../cache/input-facts/MainMethodDeclaration.facts" ) )) {
                String line;
                while ((line = br.readLine()) != null) {
                    line = line.trim();
                    MethodSignatureRef method= null;

                    for ( MethodSignatureRef method1 : methodSignatureRefFactsList ) {
                        if ( method1.getValue().equals( line ) ) {
                            method = method1;
                            break;
                        }
                    }
                    if ( method == null ) { 
                        System.out.println( "MainMethodDeclarations.facts: Method signature not found for: " + line );
                        System.exit(-1);
                    }

                    MainMethodDeclaration mainMethodDeclaration = new MainMethodDeclaration( id.getID(), method );
                    mainMethodDeclarationFactsList.add(mainMethodDeclaration);

                }
                br.close();  
            }
            
            try (BufferedReader br = new BufferedReader( new FileReader( "../cache/input-facts/ImplicitReachable.facts" ) )) {
                String line;
                while ((line = br.readLine()) != null) {
                    line = line.trim();
                    MethodSignatureRef method= null;

                    for ( MethodSignatureRef method1 : methodSignatureRefFactsList ) {
                        if ( method1.getValue().equals( line ) ) {
                            method = method1;
                            break;
                        }
                    }
                    if ( method == null ) { 
                        System.out.println( "MainMethodDeclarations.facts: Method signature not found for: " + line );
                        System.exit(-1);
                    }

                    ImplicitReachable implicitReachable = new ImplicitReachable(id.getID(), method);
                    implicitReachabeFactsList.add(implicitReachable);
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
            try ( PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("../datomic_facts/ClassInitializer.dtm", false)));) {
                for ( ClassInitializer key : classInitializerFactsList ) {
                    writer.println( "{:db/id #db/id[:db.part/user " + key.getID() + "]" );
                    writer.println( " :ClassInitializer/type #db/id[:db.part/user " + key.getType().getID() +"]" );
                    writer.println( " :ClassInitializer/method #db/id[:db.part/user " + key.getMethod().getID() + "]}" );

                }
                writer.close();
            }
            System.out.println( "ClassInitializer facts converted: " + classInitializerFactsList.size() );
            
            try ( PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("../datomic_facts/InitializedClass.dtm", false)));) {
                for ( InitializedClass key : initializedClassFactsList ) {
                    writer.println( "{:db/id #db/id[:db.part/user " + key.getID() + "]" );
                    writer.println( " :InitializedClass/classOrInterface #db/id[:db.part/user " + key.getClassOrInterface().getID() +"]}" );
                }
                writer.close();
            }
            System.out.println( "InitializedClass facts converted: " + initializedClassFactsList.size() );
            
            try ( PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("../datomic_facts/ImplicitReachable.dtm", false)));) {
                for ( ImplicitReachable key : implicitReachabeFactsList ) {
                    writer.println( "{:db/id #db/id[:db.part/user " + key.getID() + "]" );
                    writer.println( " :ImplicitReachable/sig #db/id[:db.part/user " + key.getMethod().getID() + "]}" );
                }
                writer.close();
            }
            System.out.println( "ImplicitReachable facts converted: " + implicitReachabeFactsList.size() );
            
            try ( PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("../datomic_facts/MainMethodDeclaration.dtm", false)));) {
                for ( MainMethodDeclaration key : mainMethodDeclarationFactsList ) {
                    writer.println( "{:db/id #db/id[:db.part/user " + key.getID() + "]" );
                    writer.println( " :MainMethodDeclaration/method #db/id[:db.part/user " + key.getMethod().getID() + "]}" );
                }
                writer.close();
            }
            System.out.println( "MainMethodDeclaration facts converted: " + mainMethodDeclarationFactsList.size() );
            
            try ( PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("../datomic_facts/AssignCompatible.dtm", false)));) {
                for ( AssignCompatible key : assignCompatibleFactsList ) {
                    writer.println( "{:db/id #db/id[:db.part/user " + key.getID() + "]" );
                    writer.println( " :AssignCompatible/target #db/id[:db.part/user " + key.getTarget().getID() + "]" );
                    writer.println( " :AssignCompatible/source #db/id[:db.part/user " + key.getSource().getID() + "]}" );
                }
                writer.close();
            }
            System.out.println( "AssignCompatible facts converted: " + assignCompatibleFactsList.size() );
            
            try ( PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("../datomic_facts/MethodLookup.dtm", false)));) {
                for ( MethodLookup key : methodLookupFactsList ) {
                    writer.println( "{:db/id #db/id[:db.part/user " + key.getID() + "]" );
                    writer.println( " :MethodLookup/type #db/id[:db.part/user " + key.getType().getID() + "]" );
                    writer.println( " :MethodLookup/simplename #db/id[:db.part/user " + key.getSimpleName().getID() + "]" );
                    writer.println( " :MethodLookup/descriptor #db/id[:db.part/user " + key.getDescriptor().getID() + "]" );
                    writer.println( " :MethodLookup/method #db/id[:db.part/user " + key.getMethod().getID() + "]}" );
                }
                writer.close();
            }
            System.out.println( "MethodLookup facts converted: " + methodLookupFactsList.size() );
            
            try ( PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("../datomic_facts/AssignNormalHeapAllocation.dtm", false)));) {
                for ( AssignNormalHeapAllocation key : assignNormalHeapAllocationFactsList ) {
                    writer.println( "{:db/id #db/id[:db.part/user " + key.getID() + "]" );
                    writer.println( " :AssignNormalHeapAllocation/heap #db/id[:db.part/user " + key.getHeap().getID() + "]" );
                    writer.println( " :AssignNormalHeapAllocation/var #db/id[:db.part/user " + key.getVar().getID() + "]" );
                    writer.println( " :AssignNormalHeapAllocation/inmethod #db/id[:db.part/user " + key.getInmethod().getID() + "]}" );
                }
                writer.close();
            }
            System.out.println( "AssignNormalHeapAllocation facts converted: " + assignNormalHeapAllocationFactsList.size() );
            
            try ( PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("../datomic_facts/AssignAuxiliaryHeapAllocation.dtm", false)));) {
                for ( AssignAuxiliaryHeapAllocation key : assignAuxiliaryHeapAllocationFactsList ) {
                    writer.println( "{:db/id #db/id[:db.part/user " + key.getID() + "]" );
                    writer.println( " :AssignAuxiliaryHeapAllocation/heap #db/id[:db.part/user " + key.getHeap().getID() + "]" );
                    writer.println( " :AssignAuxiliaryHeapAllocation/var #db/id[:db.part/user " + key.getVar().getID() + "]" );
                    writer.println( " :AssignAuxiliaryHeapAllocation/inmethod #db/id[:db.part/user " + key.getInmethod().getID() + "]}" );
                }
                writer.close();
            }
            System.out.println( "AssignAuxiliaryHeapAllocation facts converted: " + assignAuxiliaryHeapAllocationFactsList.size() );
            
            try ( PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("../datomic_facts/AssignContextInsensitiveHeapAllocation.dtm", false)));) {
                for ( AssignContextInsensitiveHeapAllocation key : assignContextInsensitiveHeapAllocationFactsList ) {
                    writer.println( "{:db/id #db/id[:db.part/user " + key.getID() + "]" );
                    writer.println( " :AssignContextInsensitiveHeapAllocation/heap #db/id[:db.part/user " + key.getHeap().getID() + "]" );
                    writer.println( " :AssignContextInsensitiveHeapAllocation/var #db/id[:db.part/user " + key.getVar().getID() + "]" );
                    writer.println( " :AssignContextInsensitiveHeapAllocation/inmethod #db/id[:db.part/user " + key.getInmethod().getID() + "]}" );
                }
                writer.close();
            }
            System.out.println( "AssignContextInsensitiveHeapAllocation facts converted: " + assignContextInsensitiveHeapAllocationFactsList.size() );
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
        t = new Thread(this, "Child thread: LibraryFactsConverter" );
        t.start();
    } 
     
    public Thread getThread() {
        return t;
    } 
    
    
}
