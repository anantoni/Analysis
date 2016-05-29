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
public class ArrayFactsConverter implements FactsConverter, Runnable {
    private List<ArrayType> arrayTypeFactsList = null;
    private List<Type> typeFactsList = null;
    private List<MethodSignatureRef> methodSignatureRefFactsList = null;
    private List<Var> varFactsList = null;
    private List<LoadArrayIndex> loadArrayIndexFactsList = null;
    private List<StoreArrayIndex> storeArrayIndexFactsList = null;
    private List<ComponentType> componentTypeFactsList = null;
    private FactsID id = null;
    private Thread t = null;
    
    public ArrayFactsConverter(FactsID id, List<MethodSignatureRef> methodSignatureRefFactsList, List<Var> varFactsList, List<Type> typeFactsList, List<ArrayType> arrayTypeFactsList) {
        this.arrayTypeFactsList = arrayTypeFactsList;
        this.typeFactsList = typeFactsList;
        this.methodSignatureRefFactsList = methodSignatureRefFactsList;
        this.varFactsList = varFactsList;
        this.id = id;
        loadArrayIndexFactsList = new ArrayList<>();
        storeArrayIndexFactsList = new ArrayList<>();
        componentTypeFactsList = new ArrayList<>();
    }

    public void setTypeFactsList( ArrayList<Type> typeFactsList ) {
        this.typeFactsList = typeFactsList;
    }
    
    public void setvarFactsList( ArrayList<Var> varFactsList ) {
        this.varFactsList = varFactsList;
    }
    
    public void setMethodSignatureRefFactsList( ArrayList<MethodSignatureRef> methodSignatureRefFactsList ) {
        this.methodSignatureRefFactsList = methodSignatureRefFactsList;
    }
    
    public void setArraTypeFactsList( ArrayList<ArrayType> arrayTypeFactsList ) {
        this.arrayTypeFactsList = arrayTypeFactsList;
    }
    
    @Override
    public void parseLogicBloxFactsFile() {
        try {
            try (BufferedReader br = new BufferedReader( new FileReader( "cache/input-facts/LoadArrayIndex.facts" ) )) {
                String line;
                while ((line = br.readLine()) != null) {
                    line = line.trim();
                    String pattern = "(.*)(,\\s)(.*)(,\\s)(<.*>)";
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
                        System.out.println( "LoadArrayIndex.facts: Could not find match - " + line );
                        System.exit(-1);
                    }
                    Var base = null;
                    MethodSignatureRef inmethod = null;
                    Var to = null;

                    for ( MethodSignatureRef inmethod1 : methodSignatureRefFactsList ) {
                        if ( inmethod1.getValue().equals( m.group(5) ) ) {
                            inmethod = inmethod1;
                            break;
                        }
                    }
                    if ( inmethod == null ) { 
                        System.out.println( "LoadArrayIndex.facts: Method Signature Ref not found for: " + m.group(5) );
                        System.exit(-1);
                    }

                    for ( Var to1 : varFactsList ) {
                        if ( to1.getName().equals( m.group(3) ) ) {
                            to = to1;
                            break;
                        }
                    }
                    if ( to == null ) { 
                        System.out.println( "LoadArrayIndex.facts: Variable Ref to not found for: " + m.group(3) );
                        System.exit(-1);
                    }

                    
                    for ( Var base1 : varFactsList ) {
                        if ( base1.getName().equals( m.group(1) ) ) {
                            base = base1;
                            break;
                        }
                    }
                    if ( base == null ) { 
                        System.out.println( "LoadArrayIndex.facts: Variable Ref base not found for: " + m.group(1) );
                        System.exit(-1);
                    }

                    LoadArrayIndex loadArrayIndex = new LoadArrayIndex(id.getID(), base, to, inmethod);
                    loadArrayIndexFactsList.add(loadArrayIndex);
                }
                br.close();  
            }
            
            try (BufferedReader br = new BufferedReader( new FileReader( "cache/input-facts/StoreArrayIndex.facts" ) )) {
                String line;
                while ((line = br.readLine()) != null) {
                    line = line.trim();
                    String pattern = "(.*)(,\\s)(.*)(,\\s)(<.*>)";
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
                        System.out.println( "StoreArrayIndex.facts: Could not find match - " + line );
                        System.exit(-1);
                    }
                    Var base = null;
                    MethodSignatureRef inmethod = null;
                    Var from = null;

                    for ( MethodSignatureRef inmethod1 : methodSignatureRefFactsList ) {
                        if ( inmethod1.getValue().equals( m.group(5) ) ) {
                            inmethod = inmethod1;
                            break;
                        }
                    }
                    if ( inmethod == null ) { 
                        System.out.println( "StoreArrayIndex.facts: Method Signature Ref not found for: " + m.group(5) );
                        System.exit(-1);
                    }

                    for ( Var from1 : varFactsList ) {
                        if ( from1.getName().equals( m.group(1) ) ) {
                            from = from1;
                            break;
                        }
                    }
                    if ( from == null ) { 
                        System.out.println( "StoreArrayIndex.facts: Variable Ref from not found for: " + m.group(1) );
                        System.exit(-1);
                    }

                    for ( Var base1 : varFactsList ) {
                        if ( base1.getName().equals( m.group(3) ) ) {
                            base = base1;
                            break;
                        }
                    }
                    if ( base == null ) { 
                        System.out.println( "StoreArrayIndex.facts: Variable Ref base not found for: " + m.group(3) );
                        System.exit(-1);
                    }
                    StoreArrayIndex storeArrayIndex = new StoreArrayIndex(id.getID(), from, base, inmethod);
                    storeArrayIndexFactsList.add(storeArrayIndex);
                }
                br.close();  
            }
            
            try (BufferedReader br = new BufferedReader( new FileReader( "cache/input-facts/ComponentType.facts" ) )) {
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
                        System.out.println( "ComponentType.facts: Could not find match - " + line );
                        System.exit(-1);
                    }
                    ArrayType arrayType = null;
                    Type cType = null;

                    for ( ArrayType arrayType1 : arrayTypeFactsList ) {
                        if ( arrayType1.getReferenceType().getType().getValue().equals( m.group(1) ) ) {
                            arrayType = arrayType1;
                            break;
                        }
                    }
                    if ( arrayType == null ) { 
                        System.out.println( "ComponentType.facts: Array type not found for: " + m.group(1) );
                        System.exit(-1);
                    }

                    for ( Type componentType1 : typeFactsList ) {
                        if ( componentType1.getValue().equals( m.group(3) ) ) {
                            cType = componentType1;
                            break;
                        }
                    }
                    if ( cType == null ) { 
                        System.out.println( "ComponentType.facts: Component type not found for: " + m.group(3) );
                        System.exit(-1);
                    }
                    ComponentType componentType = new ComponentType(id.getID(), arrayType, cType );
                    componentTypeFactsList.add(componentType);
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
            try ( PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("datomic_facts/LoadArrayIndex.dtm", false)));) {
                for ( LoadArrayIndex key : loadArrayIndexFactsList ) {
                    writer.println( "{:db/id #db/id[:db.part/user " + key.getID() + "]" );
                    writer.println( " :LoadArrayIndex/base #db/id[:db.part/user " + key.getBase().getID() + "]" );
                    writer.println( " :LoadArrayIndex/to #db/id[:db.part/user " + key.getTo().getID() + "]" );
                    writer.println( " :LoadArrayIndex/inmethod #db/id[:db.part/user " + key.getInmethod().getID() + "]}" );
                }
                writer.close();
            }
            System.out.println( "LoadArrayIndex facts converted: " + loadArrayIndexFactsList.size() );
            
            try ( PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("datomic_facts/StoreArrayIndex.dtm", false)));) {
                for ( StoreArrayIndex key : storeArrayIndexFactsList ) {
                    writer.println( "{:db/id #db/id[:db.part/user " + key.getID() + "]" );
                    writer.println( " :StoreArrayIndex/from #db/id[:db.part/user " + key.getFrom().getID() + "]" );
                    writer.println( " :StoreArrayIndex/base #db/id[:db.part/user " + key.getBase().getID() + "]" );
                    writer.println( " :StoreArrayIndex/inmethod #db/id[:db.part/user " + key.getInmethod().getID() + "]}" );
                }
                writer.close();
            }
            System.out.println( "StoreArrayIndex facts converted: " + storeArrayIndexFactsList.size() );
            
            try ( PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("datomic_facts/ComponentType.dtm", false)));) {
                for ( ComponentType key : componentTypeFactsList ) {
                    writer.println( "{:db/id #db/id[:db.part/user " + key.getID() + "]" );
                    writer.println( " :ComponentType/arrayType #db/id[:db.part/user " + key.getArrayType().getReferenceType().getType().getID() + "]" );
                    writer.println( " :ComponentType/componentType #db/id[:db.part/user " + key.getComponentType().getID() + "]}" );
                }
                writer.close();
            }
            System.out.println( "ComponentType facts converted: " + componentTypeFactsList.size() );
            
            
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
        t = new Thread(this, "Child thread: ArrayFactsConverter" );
        t.start();
    } 
    
    public Thread getThread() {
        return this.t;
    }
}
