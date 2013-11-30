/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package inputfactsconverter;

import datomicFacts.FieldModifier;
import datomicFacts.FieldSignature;
import datomicFacts.FieldSignatureRef;
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
 * @author
 * anantoni
 */
public class FieldDeclarationsFactsConverter extends FactsConverter implements Runnable {
        private ArrayList<Type> typeFactsList;
        private ArrayList<FieldSignatureRef> fieldSignatureRefFactsList;
        private ArrayList<SimpleNameRef> simpleNameRefFactsList;
        private ArrayList<ModifierRef> modifierRefFactsList;
        private ArrayList<FieldSignature> fieldSignatureFactsList;
        private ArrayList<FieldModifier> fieldModifierFactsList;
        private FactsID id = null;
        private Thread t = null;
        
        public FieldDeclarationsFactsConverter( FactsID id, ArrayList<Type> typeFactsList ) {
            this.typeFactsList = typeFactsList;
            fieldSignatureRefFactsList = new ArrayList<>();
            simpleNameRefFactsList = new ArrayList<>();
            modifierRefFactsList = new ArrayList<>();
            fieldSignatureFactsList = new ArrayList<>();
            fieldModifierFactsList = new ArrayList<>();
            this.id = id;
        }
        
        public ArrayList<SimpleNameRef> getSimpleNameRefFactsList() {
            return this.simpleNameRefFactsList;
        }
        
        public ArrayList<FieldSignatureRef> getFieldSignatureRefFactsList() {
            return this.fieldSignatureRefFactsList;
        }
        
        public ArrayList<FieldModifier> getFieldModifierFactsList() {
            return this.fieldModifierFactsList;
        }
        
        public ArrayList<ModifierRef> getModifierRefFactsList() {
            return this.modifierRefFactsList;
        }
        
        @Override
        public void parseLogicBloxFactsFile() {

            try {
//                try (BufferedReader br = new BufferedReader( new FileReader( "../cache/input-facts/FieldSignatureRef.facts" ) )) {
//                    String line;
//                    while ((line = br.readLine()) != null) {
//                        line = line.trim();
//                        FieldSignatureRef fieldSignatureRef = new FieldSignatureRef(id.getID(), line );
//                        fieldSignatureRefFactsList.add( fieldSignatureRef );
//                    }
//                    br.close();
//                }
                
                try (BufferedReader br = new BufferedReader( new FileReader( "../cache/input-facts/SimpleNameRef.facts" ) )) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        line = line.trim();
                        SimpleNameRef simpleNameRef = new SimpleNameRef(id.getID(), line );
                        simpleNameRefFactsList.add( simpleNameRef );
                    }
                    br.close();
                }
                
                try (BufferedReader br = new BufferedReader( new FileReader( "../cache/input-facts/ModifierRef.facts" ) )) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        line = line.trim();
                        ModifierRef modifierRef = new ModifierRef(id.getID(), line );
                        modifierRefFactsList.add( modifierRef );
                    }
                    br.close();
                }
                
                try (BufferedReader br = new BufferedReader( new FileReader( "../cache/input-facts/FieldSignature.facts" ) )) {
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
                        String fieldSignature = null;
                        Type declaringClass = null;
                        SimpleNameRef simpleNameRef = null;
                        Type type = null;

                        fieldSignature = m.group(1);

                        for ( Type t1 : typeFactsList ) {
                            if ( t1.getValue().equals( m.group(3) ) ) {
                                declaringClass = t1;
                                break;
                            }
                        }
                        if ( declaringClass == null ) { 
                            System.out.println( "Declaring Class not found for: " + m.group(3) );
                            System.exit(-1);
                        }

                        for ( SimpleNameRef simpleName: simpleNameRefFactsList ) {
                            if ( simpleName.getValue().equals( m.group(5) ) ) {
                                simpleNameRef = simpleName;
                                break;
                            }
                        }
                        if ( simpleNameRef == null ) { 
                            System.out.println( "Simple Name not found for: " + m.group(5) );
                            System.exit(-1);
                        }

                        for ( Type t2 : typeFactsList ) {
                            if ( t2.getValue().equals( m.group(7) ) ) {
                                type = t2;
                                break;
                            }
                        }
                        if ( type == null ) { 
                            System.out.println( "Type not found for: " + m.group(7) );
                            System.exit(-1);
                        }

                        FieldSignature fiedlSignature = new FieldSignature( id.getID(), fieldSignature, declaringClass, simpleNameRef, type );
                        fieldSignatureFactsList.add(fiedlSignature);
                    }
                    br.close();
                }
                
//                try (BufferedReader br = new BufferedReader( new FileReader( "../cache/input-facts/FieldModifier.facts" ) )) {
//                    String line;
//                    while ((line = br.readLine()) != null) {
//                        line = line.trim();
//                        String pattern = "(.*)(,\\s)(<.*>)";
//                        // Create a Pattern object
//                        Pattern r = Pattern.compile(pattern);
//
//                        // Now create matcher object.
//                        Matcher m = r.matcher(line);
//                        if ( m.find() ) {
//                            if ( m.groupCount() != 3 ) {
//                                System.out.println( "Invalid number of groups matched" );
//                                System.exit(-1);
//                            }
//                        } 
//                        else {
//                            System.out.println( "Could not find match" );
//                            System.exit(-1);
//                        }
//                        FieldSignatureRef field = null;
//                        ModifierRef modifier = null;
//
//                        for ( FieldSignatureRef fieldSignatureRef : fieldSignatureRefFactsList ) {
//                            if ( fieldSignatureRef.getValue().equals( m.group(3) ) ) {
//                                field = fieldSignatureRef;
//                                break;
//                            }
//                        }
//                        if ( field == null ) { 
//                            System.out.println( "Field Signature Ref not found for: " + m.group(3) );
//                            System.exit(-1);
//                        }
//
//                        for ( ModifierRef mod : modifierRefFactsList ) {
//                            if (mod.getValue().equals( m.group(1) ) ) {
//                                modifier = mod;
//                                break;
//                            }
//                        }
//                        if ( modifier == null ) { 
//                            System.out.println( "Modifier Ref not found for: " + m.group(1) );
//                            System.exit(-1);
//                        }
//
//                        FieldModifier fieldModifier = new FieldModifier( id.getID(), field, modifier );
//                        fieldModifierFactsList.add(fieldModifier);
//                    }
//                    br.close();  
//                } 
            }
            catch( IOException ex) {
                System.out.println( ex.toString() );
                System.exit(-1);
            }
        }

    @Override
    void createDatomicFactsFile() {
        try {
//            try ( PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("../datomic_facts/FieldSignatureRef.dtm", false)));) {
//                for ( FieldSignatureRef key : fieldSignatureRefFactsList ) {
//                    writer.println( "{:db/id #db/id[:db.part/user " + key.getID() + "]" );
//                    writer.println( " :FieldSignatureRef/value " + "\""+ key.getValue() + "\""+ "}");
//                }
//                writer.close();
//            }
//            System.out.println( "FieldSignatureRef facts converted: " + fieldSignatureRefFactsList.size() );
//            
            try ( PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("../datomic_facts/SimpleNameRef.dtm", false)));) {
                for ( SimpleNameRef key : simpleNameRefFactsList ) {
                    writer.println( "{:db/id #db/id[:db.part/user " + key.getID() + "]" );
                    writer.println( " :SimpleNameRef/value " + "\"" + key.getValue() + "\"" + "}");
                }
                writer.close();
            }
            System.out.println( "SimpleNameRef facts converted: " + simpleNameRefFactsList.size() );
            
            try ( PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("../datomic_facts/ModifierRef.dtm", false)));) {
                for ( ModifierRef key : modifierRefFactsList ) {
                    writer.println( "{:db/id #db/id[:db.part/user " + key.getID() + "]" );
                    writer.println( " :ModifierRef/value " + "\"" + key.getValue() + "\"" + "}");
                }
                writer.close();
            }
            System.out.println( "ModifierRef facts converted: " + modifierRefFactsList.size() );

            
            try ( PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("../datomic_facts/FieldSignature.dtm", false)));) {
                for ( FieldSignature key : fieldSignatureFactsList ) {
                    writer.println( "{:db/id #db/id[:db.part/user " + key.getID() + "]" );
                    writer.println( " :FieldSignature/signature \"" + key.getFieldSignatureRef() +"\"]"); 
                    writer.println( " :FieldSignature/declaringClass #db/id[:db.part/user " + key.getDeclaringClass().getID() + "]");
                    writer.println( " :FieldSignature/simplename #db/id[:db.part/user " + key.getSimpleNameRef().getID() + "]");
                    writer.println( " :FieldSignature/type #db/id[:db.part/user " + key.getType().getID() + "]}");
                }
                writer.close();
            }
            System.out.println( "FieldSignature facts converted: " + fieldSignatureFactsList.size() );

//            try ( PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("../datomic_facts/FieldModifier.dtm", false)));) {
//                for ( FieldModifier key : fieldModifierFactsList ) {
//                    writer.println( "{:db/id #db/id[:db.part/user " + key.getID() + "]" );
//                    writer.println( " :FieldModifier/field #db/id[:db.part/user " + key.getField().getID() +"]"); 
//                    writer.println( " :FieldModifier/modifier #db/id[:db.part/user " + key.getModifier().getID() + "]}");                
//                }
//                writer.close();
//            }
//            System.out.println( "FieldModifier facts converted: " + fieldModifierFactsList.size() );       
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
        t = new Thread(this, "Child thread: FieldDeclarationsFactsConverter" );
        t.start();
    } 
    
    public Thread getThread() {
        return t;
    }
}
