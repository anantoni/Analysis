/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package inputfactsconverter;

import datomicFacts.MethodSignatureRef;
import datomicFacts.Type;
import datomicFacts.VarDeclaringMethod;
import datomicFacts.VarRef;
import datomicFacts.VarType;
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
public class VariableDeclarationsFactsConverter extends FactsConverter implements Runnable {
    ArrayList<Type> typeFactsList = null;
    ArrayList<VarRef> varRefFactsList = null;
    ArrayList<MethodSignatureRef> methodSignatureRefFactsList = null;
    ArrayList<VarType> varTypeFactsList = null;
    ArrayList<VarDeclaringMethod> varDeclaringMethodFactsList = null;
    FactsID id = null;
    private Thread t = null;
    
    public VariableDeclarationsFactsConverter(FactsID id, ArrayList<Type> typeFactsList ) {
        this.typeFactsList = typeFactsList;
        this.id = id;
        varRefFactsList = new ArrayList<>();
        varTypeFactsList = new ArrayList<>();
        methodSignatureRefFactsList = new ArrayList<>();
        varDeclaringMethodFactsList = new ArrayList<>();
    }
    
    @Override
    void parseLogicBloxFactsFile() { 
        
        try {
            try (BufferedReader br = new BufferedReader( new FileReader( "../cache/input-facts/MethodSignatureRef.facts" ) ) ) {
                String line;
                while ((line = br.readLine()) != null) {
                    line = line.trim();
                    MethodSignatureRef methodSignatureRef = new MethodSignatureRef( id.getID(), line );
                    methodSignatureRefFactsList.add( methodSignatureRef );
                    
                }
                br.close();
            }
            
            try (BufferedReader br = new BufferedReader( new FileReader( "../cache/input-facts/Var-Type.facts" ) ) ) {
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
                        System.out.println( "Type reference not found for: " + m.group(3) );
                        System.exit(-1);
                    }
                    
                    VarRef ref = new VarRef( id.getID(), m.group(1) );
                    varRefFactsList.add(ref);
                    VarType varType = new VarType( id.getID(), ref, type );
                    varTypeFactsList.add( varType );
                }
                br.close();
            }
            
            try (BufferedReader br = new BufferedReader( new FileReader( "../cache/input-facts/Var-DeclaringMethod.facts" ) ) ) {
                String line;
                while ((line = br.readLine()) != null) {
                    line = line.trim();
                    String pattern = "(.*)(,\\s)(<.*>)|";
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
                        System.out.println( line );
                        System.exit(-1);
                    }
                    
                    MethodSignatureRef method = null;
                    VarRef ref = null;
                    
                    for ( MethodSignatureRef methodSignatureRef : methodSignatureRefFactsList ) {
                        if ( methodSignatureRef.getValue().equals( m.group(3) ) ) {
                            method = methodSignatureRef;
                            break;
                        }
                    }
                    
                    if ( method == null ) {
                        System.out.println( "Declaring method reference not found : " + m.group(3) );
                        System.exit(-1);
                    }
                    
                    for ( VarRef varRef : varRefFactsList ) {
                        if ( varRef.getValue().equals( m.group(1) ) ) {
                            ref = varRef;
                            break;
                        }
                    }
                    if ( ref == null ) { 
                        System.out.println( "Variable reference not found for: " + m.group(1) );
                        System.exit(-1);
                    }
                    
                    VarDeclaringMethod varDeclaringMethod= new VarDeclaringMethod( id.getID(), ref, method );
                    varDeclaringMethodFactsList.add( varDeclaringMethod );
                    
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
            try ( PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("../datomic_facts/VarRef.dtm", false)));) {
                for ( VarRef key : varRefFactsList ) {
                    writer.println( "{:db/id #db/id[:db.part/user " + key.getID() + "]" );
                    writer.println( " :VarRef/value " + "\""+ key.getValue() + "\""+ "}");
                }
                writer.close();
            }
            System.out.println( "VarRef facts converted: " + varRefFactsList.size() );
            
            try ( PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("../datomic_facts/MethodSignatureRef.dtm", false)));) {
                for ( MethodSignatureRef key : methodSignatureRefFactsList) {
                    writer.println( "{:db/id #db/id[:db.part/user " + key.getID() + "]" );
                    writer.println( " :MethodSignatureRef/value " + "\"" + key.getValue() + "\"" + "}");
                }
                writer.close();
            }
            System.out.println( "MethodSignatureRef facts converted: " + methodSignatureRefFactsList.size() );

            
            try ( PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("../datomic_facts/Var-Type.dtm", false)));) {
                for ( VarType key : varTypeFactsList ) {
                    writer.println( "{:db/id #db/id[:db.part/user " + key.getID() + "]" );
                    writer.println( " :Var-Type/ref #db/id[:db.part/user " + key.getVarRef().getID() +"]"); 
                    writer.println( " :Var-Type/type #db/id[:db.part/user " + key.getType().getID() + "]}");
                }
                writer.close();
            }
            System.out.println( "Var-Type facts converted: " + varTypeFactsList.size() );

            try ( PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("../datomic_facts/Var-DeclaringMethod.dtm", false)));) {
                for ( VarDeclaringMethod key : varDeclaringMethodFactsList ) {
                    writer.println( "{:db/id #db/id[:db.part/user " + key.getID() + "]" );
                    writer.println( " :Var-DeclaringMethod/ref #db/id[:db.part/user " + key.getMethodSignatureRef().getID() +"]"); 
                    writer.println( " :Var-DeclaringMethod/method #db/id[:db.part/user " + key.getMethodSignatureRef().getID() + "]}");                
                }
                writer.close();
            }
            System.out.println( "Var-DeclaringMethod facts converted: " + varDeclaringMethodFactsList.size() );       
        }        
        catch ( Exception ex ) {
            System.out.println( ex.toString() ); 
            System.exit(-1);
        }
    }
    
    public ArrayList<MethodSignatureRef> getMethodSignatureRefFactsList() {
        return this.methodSignatureRefFactsList;
    }
    
    public ArrayList<VarRef> getVarRefFactsList() {
        return this.varRefFactsList;
    }

    
    @Override
    public void run() {
        this.parseLogicBloxFactsFile();
        this.createDatomicFactsFile();
    }
    
    public void startThread() {
        t = new Thread(this, "Child thread: VariableDeclarationsFactsConverter" );
        t.start();
    } 
    
    public Thread getThread() {
        return t;
    }
    
}
