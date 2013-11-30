/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package inputfactsconverter;

import datomicFacts.ActualParam;
import datomicFacts.CallGraphEdgeSourceRef;
import datomicFacts.FormalParam;
import datomicFacts.InstructionRef;
import datomicFacts.MethodInvocationRef;
import datomicFacts.MethodSignatureRef;
import datomicFacts.ThisVar;
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
public class ParameterFactsConverter extends FactsConverter {
    private ArrayList<Var> varFactsList = null;
    private ArrayList<MethodSignatureRef> methodSignatureRefFactsList = null;
    private ArrayList<MethodInvocationRef> methodInvocationRefFactsList = null;
    private ArrayList<FormalParam> formalParamFactsList = null;
    private ArrayList<ActualParam> actualParamFactsList = null;
    private ArrayList<ThisVar> thisVarFactsList = null;
    private ArrayList<InstructionRef> instructionRefFactsList = null;
    private ArrayList<CallGraphEdgeSourceRef> callGraphEdgeSourceRefFactsList = null;
    private FactsID id = null;
    
    public ParameterFactsConverter(FactsID id, ArrayList<Var> varFactsList, ArrayList<CallGraphEdgeSourceRef> callGraphEdgeSourceRefFactsList, ArrayList<MethodSignatureRef> methodSignatureRefFactsList) {
        this.varFactsList = varFactsList;
        this.methodSignatureRefFactsList = methodSignatureRefFactsList;
        methodInvocationRefFactsList = new ArrayList<>();
        formalParamFactsList = new ArrayList<>();
        actualParamFactsList = new ArrayList<>();
        thisVarFactsList = new ArrayList<>();
        this.callGraphEdgeSourceRefFactsList = callGraphEdgeSourceRefFactsList;
        this.id = id;
    }

    @Override
    public void parseLogicBloxFactsFile() {
        try {
                    
            try (BufferedReader br = new BufferedReader( new FileReader( "../cache/input-facts/FormalParam.facts" ) )) {
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
                        System.out.println( "FormalParam.facts: Could not find match - " + line );
                        System.exit(-1);
                    }
                    MethodSignatureRef method = null;
                    int paramIndexRef = -1;
                    Var var = null;

                    for ( MethodSignatureRef methodSignatureRef1 : methodSignatureRefFactsList ) {
                        if ( methodSignatureRef1.getValue().equals( m.group(3) ) ) {
                            method = methodSignatureRef1;
                            break;
                        }
                    }
                    if ( method == null ) { 
                        System.out.println( "FormalParam.facts: Method Signature Ref not found for: " + m.group(3) );
                        System.exit(-1);
                    }

                    for ( Var var1 : varFactsList ) {
                        if ( var1.getName().equals( m.group(5) ) ) {
                            var = var1;
                            break;
                        }
                    }
                    if ( var == null ) { 
                        System.out.println( "FormalParam.facts: Variable Ref not found for: " + m.group(5) );
                        System.exit(-1);
                    }

                    try {
                        paramIndexRef = Integer.parseInt( m.group(1) );
                    }
                    catch (Exception ex) {
                        System.out.println( m.group(1) + " not a valid parameter index" );
                        System.exit(-1);
                    }
                    if ( paramIndexRef < 0 ) {
                        System.out.println("Invalid index number");
                        System.exit(-1);
                    }
                    FormalParam formalParam = new FormalParam( id.getID(), paramIndexRef, method, var );
                    formalParamFactsList.add( formalParam );

                }
                br.close();  
            }
            
            try (BufferedReader br = new BufferedReader( new FileReader( "../cache/input-facts/MethodInvocationRef.facts" ) )) {
                String line;

                while ((line = br.readLine()) != null) {
                    line = line.trim();
                    boolean callGraphEdgeSourceRefFound = false;
                    for ( CallGraphEdgeSourceRef callGraphEdgeSourceRef : callGraphEdgeSourceRefFactsList ) {
                        if ( callGraphEdgeSourceRef.getInstructionRef().getInstruction().equals(line) ) {
                            MethodInvocationRef methodInvocationRef = new MethodInvocationRef(id.getID(), callGraphEdgeSourceRef);
                            methodInvocationRefFactsList.add(methodInvocationRef);
                            callGraphEdgeSourceRefFound = true;
                            break;
                        }
                    }
                    if ( callGraphEdgeSourceRefFound == false ) {
                        System.out.println("MethodInvocationRef.facts: CallGraphEdgeSourceRef reference not found for MethodInvocation " + line ); 
                        System.exit(-1);
                    }
                }
                br.close();
            }
            
            try (BufferedReader br = new BufferedReader( new FileReader( "../cache/input-facts/ActualParam.facts" ) )) {
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
                            System.out.println( "ActualParam.facts: Could not find match - " + line );
                            System.exit(-1);
                        }
                        MethodInvocationRef invocation = null;
                        int paramIndexRef = -1;
                        Var var = null;

                        for ( MethodInvocationRef methodInvocationRef : methodInvocationRefFactsList ) {
                            if ( methodInvocationRef.getCallGraphEdgeSourceRef().getInstructionRef().getInstruction().equals( m.group(3) ) ) {
                                invocation = methodInvocationRef;
                                break;
                            }
                        }
                        if ( invocation == null ) { 
                            System.out.println( "ActualParam.facts: Method Invocation Ref not found for: " + m.group(3) );
                            System.exit(-1);
                        }

                        for ( Var var1 : varFactsList ) {
                            if ( var1.getName().equals( m.group(5) ) ) {
                                var = var1;
                                break;
                            }
                        }
                        if ( var == null ) { 
                            System.out.println( "ActualParam.facts: Variable Ref not found for: " + m.group(5) );
                            System.exit(-1);
                        }
                        
                        try {
                            paramIndexRef = Integer.parseInt( m.group(1) );
                        }
                        catch (Exception ex) {
                            System.out.println( m.group(1) + " not a valid parameter index" );
                            System.exit(-1);
                        }
                        if ( paramIndexRef < 0 ) {
                            System.out.println("Invalid index number");
                            System.exit(-1);
                        }
                        ActualParam actualParam = new ActualParam( id.getID(), paramIndexRef, invocation, var );
                        actualParamFactsList.add( actualParam );
                        
                    }
                    br.close();  
            }
            
            try (BufferedReader br = new BufferedReader( new FileReader( "../cache/input-facts/ThisVar.facts" ) )) {
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
                            System.out.println( "ThisVar: Could not find match - " + line );
                            System.exit(-1);
                        }
                        MethodSignatureRef method = null;
                        Var var = null;

                        for ( MethodSignatureRef methodSignatureRef : methodSignatureRefFactsList ) {
                            if ( methodSignatureRef.getValue().equals( m.group(1) ) ) {
                                method = methodSignatureRef;
                                break;
                            }
                        }
                        if ( method == null ) { 
                            System.out.println( "ThisVar.facts: Method signature not found for: " + m.group(1) );
                            System.exit(-1);
                        }

                        for ( Var var1 : varFactsList ) {
                            if ( var1.getName().equals( m.group(3) ) ) {
                                var = var1;
                                break;
                            }
                        }
                        if ( var == null ) { 
                            System.out.println( "ThisVar.facts: Variable reference not not found for: " + m.group(3) );
                            System.exit(-1);
                        }

                        ThisVar thisVar = new ThisVar( id.getID(), method, var );
                        thisVarFactsList.add(thisVar);
                        
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
            try ( PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("../datomic_facts/MethodInvocationRef.dtm", false)));) {
                for ( MethodInvocationRef key : methodInvocationRefFactsList ) {
                    writer.println( "{:db/id #db/id[:db.part/user " + key.getID() + "]" );
                    writer.println( " :MethodInvocationRef/x #db/id[:db.part/user " + key.getCallGraphEdgeSourceRef().getID() + "]}" );
                    
                }
                writer.close();
            }
            System.out.println( "MethodInvocationRef facts converted: " + methodInvocationRefFactsList.size() );
            
            try ( PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("../datomic_facts/ActualParam.dtm", false)));) {
                for ( ActualParam key : actualParamFactsList ) {
                    writer.println( "{:db/id #db/id[:db.part/user " + key.getID() + "]" );
                    writer.println( " :ActualParam/index " + key.getIndex() );
                    writer.println( " :ActualParam/invocation #db/id[:db.part/user " + key.getInvocation().getID() + "]" );
                    writer.println( " :ActualParam/var #db/id[:db.part/user " + key.getVar().getID() + "]}" );
                    
                }
                writer.close();
            }
            System.out.println( "ActualParam facts converted: " + actualParamFactsList.size() );
            
            try ( PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("../datomic_facts/FormalParam.dtm", false)));) {
                for ( FormalParam key : formalParamFactsList ) {
                    writer.println( "{:db/id #db/id[:db.part/user " + key.getID() + "]" );
                    writer.println( " :FormalParam/index " + key.getIndex() );
                    writer.println( " :FormalParam/method #db/id[:db.part/user " + key.getMethod().getID() + "]" );
                    writer.println( " :FormalParam/var #db/id[:db.part/user " + key.getVar().getID() + "]}" );
                    
                }
                writer.close();
            }
            System.out.println( "FormalParam facts converted: " + formalParamFactsList.size() );
            
            try ( PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("../datomic_facts/ThisVar.dtm", false)));) {
                for ( ThisVar key : thisVarFactsList ) {
                    writer.println( "{:db/id #db/id[:db.part/user " + key.getID() + "]" );
                    writer.println( " :ThisVar/method #db/id[:db.part/user " + key.getMethod().getID() + "]" );
                    writer.println( " :ThisVar/var #db/id[:db.part/user " + key.getVar().getID() + "]}" );
                    
                }
                writer.close();
            }
            System.out.println( "ThisVar facts converted: " + thisVarFactsList.size() );
        }        
        catch ( Exception ex ) {
            System.out.println( ex.toString() ); 
            System.exit(-1);
        }
    }
    
    public ArrayList<MethodInvocationRef> getMethodInvocationRefFactsList() {
        return this.methodInvocationRefFactsList;
    }
    
}
