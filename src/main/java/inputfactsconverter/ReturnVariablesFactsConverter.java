/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package inputfactsconverter;

import datomicFacts.MethodSignatureRef;
import datomicFacts.ReturnVar;
import datomicFacts.Var;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author anantoni
 */
public class ReturnVariablesFactsConverter implements FactsConverter, Runnable {
    private List<Var> varFactsList = null;
    private List<MethodSignatureRef> methodSignatureRefFactsList = null;
    private List<ReturnVar> returnVarFactsList = null;
    private FactsID id = null;
    private Thread t = null;
    
    public ReturnVariablesFactsConverter(FactsID id, List<Var> varFactsList, List<MethodSignatureRef> methodSignatureRefFactsList) {
        this.varFactsList = varFactsList;
        this.methodSignatureRefFactsList = methodSignatureRefFactsList;
        this.id = id;
        this.returnVarFactsList = new ArrayList<>();
    }  
    
    @Override
    public void parseLogicBloxFactsFile() {
        try {
            try (BufferedReader br = new BufferedReader( new FileReader( "cache/input-facts/ReturnVar.facts" ) )) {
                String line;
                while ((line = br.readLine()) != null) {
                    line = line.trim();
                    String pattern = "(.*)(,\\s)(<.*>)";
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
                    Var var = null;
                    
                    for ( MethodSignatureRef method1 : methodSignatureRefFactsList ) {
                        if ( method1.getValue().equals( m.group(3) ) ) {
                            method = method1;
                            break;
                        }
                    }
                    if ( method == null ) {
                        System.out.println("ReturnVar.facts: Method signature reference not found for " + m.group(3));
                        System.exit(-1);
                    }
                    
                    for ( Var var1 : varFactsList ) {
                        if ( var1.getName().equals( m.group(1) ) ) {
                            var = var1;
                            break;
                        }
                    }
                    if ( var == null ) {
                        System.out.println("ReturnVar.facts: Variable reference not found for " + m.group(1));
                        System.exit(-1);
                    }
                    ReturnVar returnVar = new ReturnVar(id.getID(), method, var );
                    returnVarFactsList.add(returnVar);
                }
                br.close();
            }
        }
        catch( IOException ex ) {
            System.out.println( ex.toString() );
            System.exit(-1);
        }
    }

    @Override
    public void createDatomicFactsFile() {
        try {
            try ( PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("datomic_facts/ReturnVar.dtm", false)));) {
                for ( ReturnVar key : returnVarFactsList ) {
                    writer.println( "{:db/id #db/id[:db.part/user " + key.getID() + "]" );
                    writer.println( " :ReturnVar/method #db/id[:db.part/user " + key.getMethod().getID() + "]" );
                    writer.println( " :ReturnVar/var #db/id[:db.part/user " + key.getVar().getID() + "]}" );
                }
                writer.close();
            }
            System.out.println( "ReturnVar facts converted: " + returnVarFactsList.size() );
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
        t = new Thread(this, "Child thread: ReturnVariableFactsConverter" );
        t.start();
    }   
    
    public Thread getThread() {
        return t;
    }
}
