/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package inputfactsconverter;

import datomicFacts.FieldSignatureRef;
import datomicFacts.LoadInstanceField;
import datomicFacts.LoadPrimStaticField;
import datomicFacts.LoadStaticField;
import datomicFacts.MethodSignatureRef;
import datomicFacts.StoreInstanceField;
import datomicFacts.StorePrimStaticField;
import datomicFacts.StoreStaticField;
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
public class FieldsFactsConverter extends FactsConverter implements Runnable {
    private ArrayList<FieldSignatureRef> fieldSignatureRefFactsList = null;
    private ArrayList<MethodSignatureRef> methodSignatureRefFactsList = null;
    private ArrayList<Var> varRefFactsList = null;
    private ArrayList<LoadInstanceField> loadInstanceFieldFactsList = null;
    private ArrayList<LoadStaticField> loadStaticFieldFactsList = null;
    private ArrayList<LoadPrimStaticField> loadPrimStaticFieldFactsList = null;
    private ArrayList<StoreInstanceField> storeInstanceFieldFactsList = null;
    private ArrayList<StoreStaticField> storeStaticFieldFactsList = null;
    private ArrayList<StorePrimStaticField> storePrimStaticFieldFactsList = null;
    private FactsID id = null;
    private Thread t = null;
    
    public FieldsFactsConverter(FactsID id, ArrayList<Var> varRefFactsList, ArrayList<MethodSignatureRef> methodSignatureRefFactsList, ArrayList<FieldSignatureRef> fieldSignatureRefFactsList) {
        this.fieldSignatureRefFactsList = fieldSignatureRefFactsList;
        this.methodSignatureRefFactsList = methodSignatureRefFactsList;
        this.varRefFactsList = varRefFactsList;
        loadInstanceFieldFactsList = new ArrayList<>();
        loadStaticFieldFactsList = new ArrayList<>();
        loadPrimStaticFieldFactsList = new ArrayList<>();
        storeInstanceFieldFactsList = new ArrayList<>();
        storeStaticFieldFactsList  = new ArrayList<>();
        storePrimStaticFieldFactsList  = new ArrayList<>();
        this.id = id;
    }    
    
    @Override
    void parseLogicBloxFactsFile() {
        try {
            try (BufferedReader br = new BufferedReader( new FileReader( "../cache/input-facts/LoadInstanceField.facts" ) )) {
                String line;
                while ((line = br.readLine()) != null) {
                    line = line.trim();
                    String pattern = "(.*)(,\\s)(<.*>)(,\\s)(.*)(,\\s)(<.*>)";
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
                        System.out.println( "LoadInstanceField.facts: Could not find match - " + line );
                        System.exit(-1);
                    }
                    FieldSignatureRef sig = null;
                    Var base = null;
                    MethodSignatureRef inmethod = null;
                    Var to = null;

                    for ( MethodSignatureRef inmethod1 : methodSignatureRefFactsList ) {
                        if ( inmethod1.getValue().equals( m.group(7) ) ) {
                            inmethod = inmethod1;
                            break;
                        }
                    }
                    if ( inmethod == null ) { 
                        System.out.println( "LoadInstanceField.facts: Method Signature Ref not found for: " + m.group(7) );
                        System.exit(-1);
                    }

                    for ( Var to1 : varRefFactsList ) {
                        if ( to1.getName().equals( m.group(5) ) ) {
                            to = to1;
                            break;
                        }
                    }
                    if ( to == null ) { 
                        System.out.println( "LoadInstanceField.facts: Variable Ref to not found for: " + m.group(5) );
                        System.exit(-1);
                    }

                    for ( Var base1 : varRefFactsList ) {
                        if ( base1.getName().equals( m.group(1) ) ) {
                            base = base1;
                            break;
                        }
                    }
                    if ( base == null ) { 
                        System.out.println( "LoadInstanceField.facts: Variable Ref base not found for: " + m.group(1) );
                        System.exit(-1);
                    }

                    for ( FieldSignatureRef sig1 : fieldSignatureRefFactsList ) {
                        if ( sig1.getValue().equals( m.group(3) ) ) {
                            sig = sig1;
                            break;
                        }
                    }
                    if ( sig == null ) { 
                        System.out.println( "LoadInstanceField.facts: Field Signature Ref not found for: " + m.group(3) );
                        System.exit(-1);
                    }

                    LoadInstanceField loadInstanceField = new LoadInstanceField(id.getID(), base, sig, to, inmethod);
                    loadInstanceFieldFactsList.add(loadInstanceField);
                }
                br.close();  
            }
            
            try (BufferedReader br = new BufferedReader( new FileReader( "../cache/input-facts/LoadStaticField.facts" ) )) {
                String line;
                while ((line = br.readLine()) != null) {
                    line = line.trim();
                    String pattern = "(<.*>)(,\\s)(.*)(,\\s)(<.*>)";
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
                        System.out.println( "LoadStaticField.facts: Could not find match - " + line );
                        System.exit(-1);
                    }
                    FieldSignatureRef sig = null;
                    MethodSignatureRef inmethod = null;
                    Var to = null;

                    for ( MethodSignatureRef inmethod1 : methodSignatureRefFactsList ) {
                        if ( inmethod1.getValue().equals( m.group(5) ) ) {
                            inmethod = inmethod1;
                            break;
                        }
                    }
                    if ( inmethod == null ) { 
                        System.out.println( "LoadStaticField.facts: Method Signature Ref not found for: " + m.group(5) );
                        System.exit(-1);
                    }

                    for ( Var to1 : varRefFactsList ) {
                        if ( to1.getName().equals( m.group(3) ) ) {
                            to = to1;
                            break;
                        }
                    }
                    if ( to == null ) { 
                        System.out.println( "LoadStaticField.facts: Variable Ref to not found for: " + m.group(3) );
                        System.exit(-1);
                    }

                    for ( FieldSignatureRef sig1 : fieldSignatureRefFactsList ) {
                        if ( sig1.getValue().equals( m.group(1) ) ) {
                            sig = sig1;
                            break;
                        }
                    }
                    if ( sig == null ) { 
                        System.out.println( "LoadInstanceField.facts: Field Signature Ref found for: " + m.group(1) );
                        System.exit(-1);
                    }

                    LoadStaticField loadStaticField = new LoadStaticField(id.getID(), sig, to, inmethod);
                    loadStaticFieldFactsList.add(loadStaticField);
                }
                br.close();  
            }
            
            try (BufferedReader br = new BufferedReader( new FileReader( "../cache/input-facts/LoadPrimStaticField.facts" ) )) {
                String line;
                while ((line = br.readLine()) != null) {
                    line = line.trim();
                    String pattern = "(<.*>)(,\\s)(<.*>)";
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
                        System.out.println( "LoadPrimStaticField.facts: Could not find match - " + line );
                        System.exit(-1);
                    }
                    FieldSignatureRef sig = null;
                    MethodSignatureRef inmethod = null;

                    for ( MethodSignatureRef inmethod1 : methodSignatureRefFactsList ) {
                        if ( inmethod1.getValue().equals( m.group(3) ) ) {
                            inmethod = inmethod1;
                            break;
                        }
                    }
                    if ( inmethod == null ) { 
                        System.out.println( "LoadPrimStaticField.facts: Method Signature Ref not found for: " + m.group(3) );
                        System.exit(-1);
                    }

                    for ( FieldSignatureRef sig1 : fieldSignatureRefFactsList ) {
                        if ( sig1.getValue().equals( m.group(1) ) ) {
                            sig = sig1;
                            break;
                        }
                    }
                    if ( sig == null ) { 
                        System.out.println( "LoadPrimStaticField.facts: Field Signature Ref found for: " + m.group(1) );
                        System.exit(-1);
                    }

                    LoadPrimStaticField loadPrimStaticField = new LoadPrimStaticField(id.getID(), sig, inmethod);
                    loadPrimStaticFieldFactsList.add(loadPrimStaticField);
                }
                br.close();  
            }
            
            try (BufferedReader br = new BufferedReader( new FileReader( "../cache/input-facts/StoreInstanceField.facts" ) )) {
                String line;
                while ((line = br.readLine()) != null) {
                    line = line.trim();
                    String pattern = "(.*)(,\\s)(.*)(,\\s)(<.*>)(,\\s)(<.*>)";
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
                        System.out.println( "StoreInstanceField.facts: Could not find match - " + line );
                        System.exit(-1);
                    }
                    FieldSignatureRef sig = null;
                    Var base = null;
                    MethodSignatureRef inmethod = null;
                    Var from = null;

                    for ( MethodSignatureRef inmethod1 : methodSignatureRefFactsList ) {
                        if ( inmethod1.getValue().equals( m.group(7) ) ) {
                            inmethod = inmethod1;
                            break;
                        }
                    }
                    if ( inmethod == null ) { 
                        System.out.println( "StoreInstanceField.facts: Method Signature Ref inmethod not found for: " + m.group(7) );
                        System.exit(-1);
                    }

                    for ( Var from1 : varRefFactsList ) {
                        if ( from1.getName().equals( m.group(1) ) ) {
                            from = from1;
                            break;
                        }
                    }
                    if ( from == null ) { 
                        System.out.println( "StoreInstanceField.facts: Variable Ref from not found for: " + m.group(1) );
                        System.exit(-1);
                    }

                    for ( Var base1 : varRefFactsList ) {
                        if ( base1.getName().equals( m.group(3) ) ) {
                            base = base1;
                            break;
                        }
                    }
                    if ( base == null ) { 
                        System.out.println( "StoreInstanceField.facts: Variable Ref base not found for: " + m.group(3) );
                        System.exit(-1);
                    }

                    for ( FieldSignatureRef sig1 : fieldSignatureRefFactsList ) {
                        if ( sig1.getValue().equals( m.group(5) ) ) {
                            sig = sig1;
                            break;
                        }
                    }
                    if ( sig == null ) { 
                        System.out.println( "StoreInstanceField.facts: Variable Ref base not found for: " + m.group(5) );
                        System.exit(-1);
                    }

                    StoreInstanceField storeInstanceField = new StoreInstanceField(id.getID(), from, base, sig, inmethod);
                    storeInstanceFieldFactsList.add(storeInstanceField);
                }
                br.close();  
            }
            
            try (BufferedReader br = new BufferedReader( new FileReader( "../cache/input-facts/StoreStaticField.facts" ) )) {
                String line;
                while ((line = br.readLine()) != null) {
                    line = line.trim();
                    String pattern = "(.*)(,\\s)(<.*>)(,\\s)(<.*>)";
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
                        System.out.println( "StoreStaticField.facts: Could not find match - " + line );
                        System.exit(-1);
                    }
                    FieldSignatureRef sig = null;
                    MethodSignatureRef inmethod = null;
                    Var from = null;

                    for ( MethodSignatureRef inmethod1 : methodSignatureRefFactsList ) {
                        if ( inmethod1.getValue().equals( m.group(5) ) ) {
                            inmethod = inmethod1;
                            break;
                        }
                    }
                    if ( inmethod == null ) { 
                        System.out.println( "StoreStaticField.facts: Method Signature Ref not found for: " + m.group(5) );
                        System.exit(-1);
                    }

                    for ( Var from1 : varRefFactsList ) {
                        if ( from1.getName().equals( m.group(1) ) ) {
                            from = from1;
                            break;
                        }
                    }
                    if ( from == null ) { 
                        System.out.println( "StoreStaticField.facts: Variable Ref to not found for: " + m.group(1) );
                        System.exit(-1);
                    }

                    for ( FieldSignatureRef sig1 : fieldSignatureRefFactsList ) {
                        if ( sig1.getValue().equals( m.group(3) ) ) {
                            sig = sig1;
                            break;
                        }
                    }
                    if ( sig == null ) { 
                        System.out.println( "StoreInstanceField.facts: Field Signature Ref found for: " + m.group(3) );
                        System.exit(-1);
                    }

                    StoreStaticField storeStaticField = new StoreStaticField(id.getID(), from, sig, inmethod);
                    storeStaticFieldFactsList.add(storeStaticField);
                }
                br.close();  
            }
            
            try (BufferedReader br = new BufferedReader( new FileReader( "../cache/input-facts/StorePrimStaticField.facts" ) )) {
                String line;
                while ((line = br.readLine()) != null) {
                    line = line.trim();
                    String pattern = "(<.*>)(,\\s)(<.*>)";
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
                        System.out.println( "StorePrimStaticField.facts: Could not find match - " + line );
                        System.exit(-1);
                    }
                    FieldSignatureRef sig = null;
                    MethodSignatureRef inmethod = null;

                    for ( MethodSignatureRef inmethod1 : methodSignatureRefFactsList ) {
                        if ( inmethod1.getValue().equals( m.group(3) ) ) {
                            inmethod = inmethod1;
                            break;
                        }
                    }
                    if ( inmethod == null ) { 
                        System.out.println( "StorePrimStaticField.facts: Method Signature Ref not found for: " + m.group(3) );
                        System.exit(-1);
                    }

                    for ( FieldSignatureRef sig1 : fieldSignatureRefFactsList ) {
                        if ( sig1.getValue().equals( m.group(1) ) ) {
                            sig = sig1;
                            break;
                        }
                    }
                    if ( sig == null ) { 
                        System.out.println( "StoreInstanceField.facts: Field Signature Ref found for: " + m.group(1) );
                        System.exit(-1);
                    }

                    StorePrimStaticField storePrimStaticField = new StorePrimStaticField(id.getID(), sig, inmethod);
                    storePrimStaticFieldFactsList.add(storePrimStaticField);
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
            try ( PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("../datomic_facts/LoadInstanceField.dtm", false)));) {
                for ( LoadInstanceField key : loadInstanceFieldFactsList ) {
                    writer.println( "{:db/id #db/id[:db.part/user " + key.getID() + "]" );
                    writer.println( " :LoadInstanceField/base #db/id[:db.part/user " + key.getBase().getID() + "]" );
                    writer.println( " :LoadInstanceField/sig #db/id[:db.part/user " + key.getSig().getID() + "]" );
                    writer.println( " :LoadInstanceField/to #db/id[:db.part/user " + key.getTo().getID() + "]" );
                    writer.println( " :LoadInstanceField/inmethod #db/id[:db.part/user " + key.getInmethod().getID() + "]}" );
                }
                writer.close();
            }
            System.out.println( "LoadInstanceField facts converted: " + loadInstanceFieldFactsList.size() );
            
            try ( PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("../datomic_facts/LoadStaticField.dtm", false)));) {
                for ( LoadStaticField key : loadStaticFieldFactsList ) {
                    writer.println( "{:db/id #db/id[:db.part/user " + key.getID() + "]" );
                    writer.println( " :LoadStaticField/sig #db/id[:db.part/user " + key.getSig().getID() + "]" );
                    writer.println( " :LoadStaticField/to #db/id[:db.part/user " + key.getTo().getID() + "]" );
                    writer.println( " :LoadStaticField/inmethod #db/id[:db.part/user " + key.getInmethod().getID() + "]}" );
                }
                writer.close();
            }
            System.out.println( "LoadStaticField facts converted: " + loadStaticFieldFactsList.size() );
            
            try ( PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("../datomic_facts/LoadPrimStaticField.dtm", false)));) {
                for ( LoadPrimStaticField key : loadPrimStaticFieldFactsList ) {
                    writer.println( "{:db/id #db/id[:db.part/user " + key.getID() + "]" );
                    writer.println( " :LoadPrimStaticField/sig #db/id[:db.part/user " + key.getSig().getID() + "]" );
                    writer.println( " :LoadPrimStaticField/inmethod #db/id[:db.part/user " + key.getInmethod().getID() + "]}" );
                }
                writer.close();
            }
            System.out.println( "LoadPrimStaticField facts converted: " + loadPrimStaticFieldFactsList.size() );
            
            try ( PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("../datomic_facts/StoreInstanceField.dtm", false)));) {
                for ( StoreInstanceField key : storeInstanceFieldFactsList ) {
                    writer.println( "{:db/id #db/id[:db.part/user " + key.getID() + "]" );
                    writer.println( " :StoreInstanceField/from #db/id[:db.part/user " + key.getFrom().getID() + "]" );
                    writer.println( " :StoreInstanceField/base #db/id[:db.part/user " + key.getBase().getID() + "]" );
                    writer.println( " :StoreInstanceField/signature #db/id[:db.part/user " + key.getSignature().getID() + "]" );
                    writer.println( " :StoreInstanceField/inmethod #db/id[:db.part/user " + key.getInmethod().getID() + "]}" );
                }
                writer.close();
            }
            System.out.println( "StoreInstanceField facts converted: " + storeInstanceFieldFactsList.size() );
            
            try ( PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("../datomic_facts/StoreStaticField.dtm", false)));) {
                for ( StoreStaticField key : storeStaticFieldFactsList ) {
                    writer.println( "{:db/id #db/id[:db.part/user " + key.getID() + "]" );
                    writer.println( " :StoreStaticField/signature #db/id[:db.part/user " + key.getSignature().getID() + "]" );
                    writer.println( " :StoreStaticField/from #db/id[:db.part/user " + key.getFrom().getID() + "]" );
                    writer.println( " :StoreStaticField/inmethod #db/id[:db.part/user " + key.getInmethod().getID() + "]}" );
                }
                writer.close();
            }
            System.out.println( "StoreStaticField facts converted: " + storeStaticFieldFactsList.size() );
            
            try ( PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("../datomic_facts/StorePrimStaticField.dtm", false)));) {
                for ( StorePrimStaticField key : storePrimStaticFieldFactsList ) {
                    writer.println( "{:db/id #db/id[:db.part/user " + key.getID() + "]" );
                    writer.println( " :StorePrimStaticField/signature #db/id[:db.part/user " + key.getSignature().getID() + "]" );
                    writer.println( " :StorePrimStaticField/inmethod #db/id[:db.part/user " + key.getInmethod().getID() + "]}" );
                }
                writer.close();
            }
            System.out.println( "StorePrimStaticField facts converted: " + storePrimStaticFieldFactsList.size() );
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
        t = new Thread(this, "Child thread: FieldFactsConverter" );
        t.start();
    }
    
    public Thread getThread() {
        return t;
    }
}
