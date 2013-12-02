/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package inputfactsconverter;

import datomicFacts.MethodSignatureRef;
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
 * @author
 * anantoni
 */
public class VariableDeclarationsFactsConverter extends FactsConverter implements Runnable {
    ArrayList<Type> typeFactsList = null;
    ArrayList<Var> varFactsList = null;
    ArrayList<MethodSignatureRef> methodSignatureRefFactsList = null;
    FactsID id = null;
    private Thread t = null;
    
    public VariableDeclarationsFactsConverter(FactsID id, ArrayList<Type> typeFactsList ) {
        this.typeFactsList = typeFactsList;
        this.id = id;
        varFactsList = new ArrayList<>();
        methodSignatureRefFactsList = new ArrayList<>();
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
                    
                    Var var = new Var( id.getID(), m.group(1), type );
                    varFactsList.add(var);
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
                    boolean varFound = false;
                    for ( Var var : varFactsList ) 
                        if ( var.getName().equals(m.group(1) ) ) {
                            var.setDeclaringMethod(method);
                            varFound = true;
                        }
                    if ( varFound == false ) {
                        System.out.println("Var-DeclaringMethod.facts: Var not found for: " + m.group(1) );
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
            try ( PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("../datomic_facts/Var.dtm", false)));) {
                for ( Var key : varFactsList ) {
                    writer.println( "{:db/id #db/id[:db.part/user " + key.getID() + "]" );
                    writer.println( " :Var/name " + "\""+ key.getName() + "\"");
                    writer.println( " :Var/type #db/id[:db.part/user " + key.getType().getID() + "]");
                    writer.println( " :Var/method #db/id[:db.part/user " + key.getDeclaringMethod().getID() + "]}");

                }
                writer.close();
            }
            System.out.println( "Var facts converted: " + varFactsList.size() );
            
            try ( PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("../datomic_facts/MethodSignatureRef.dtm", false)));) {
                for ( MethodSignatureRef key : methodSignatureRefFactsList) {
                    writer.println( "{:db/id #db/id[:db.part/user " + key.getID() + "]" );
                    writer.println( " :MethodSignatureRef/value " + "\"" + key.getValue() + "\"" + "}");
                }
                writer.close();
            }
            System.out.println( "MethodSignatureRef facts converted: " + methodSignatureRefFactsList.size() );
    
        }        
        catch ( Exception ex ) {
            System.out.println( ex.toString() ); 
            System.exit(-1);
        }
    }
    
    public ArrayList<MethodSignatureRef> getMethodSignatureRefFactsList() {
        return this.methodSignatureRefFactsList;
    }
    
    public ArrayList<Var> getvarFactsList() {
        return this.varFactsList;
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
