/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package inputfactsconverter;
import datomicFacts.CallGraphEdgeSourceRef;
import datomicFacts.InstructionIndex;
import datomicFacts.InstructionRef;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
/**
 *
 * @author anantoni
 */
public class InstructionsFactsConverter extends FactsConverter {
    private ArrayList<InstructionRef> instructionRefFactsList = null;
    private ArrayList<CallGraphEdgeSourceRef> callGraphEdgeSourceRefFactsList = null;
    private ArrayList<InstructionIndex> instructionIndexFactsList = null;
    private FactsID id = null;
    
    public InstructionsFactsConverter(FactsID id) {
        instructionRefFactsList = new ArrayList<>();
        instructionIndexFactsList = new ArrayList<>();
        callGraphEdgeSourceRefFactsList = new ArrayList<>();
        this.id = id;
    }
    @Override
    void parseLogicBloxFactsFile() {         
        try {
            try (BufferedReader br = new BufferedReader( new FileReader( "../cache/input-facts/InstructionRef.facts" ) )) {
                String line;
                while ((line = br.readLine()) != null ) {
                    line = line.trim();
                    InstructionRef instructionRef = new InstructionRef( id.getID(), line ); 
                    instructionRefFactsList.add( instructionRef );                    
                }
                br.close();
            }
            
            try (BufferedReader br = new BufferedReader( new FileReader( "../cache/input-facts/CallGraphEdgeSourceRef.facts" ) )) {
                String line;
                while ((line = br.readLine()) != null ) {
                    line = line.trim();
                    InstructionRef instructionRef = null;
                    for ( InstructionRef instructionRef1 : instructionRefFactsList ) {
                        if ( line.equals( instructionRef1.getInstruction() ) ) {
                            instructionRef = instructionRef1;
                            break;
                        }
                    }
                    if ( instructionRef == null ) {
                        System.out.println("Instruction Reference not found for CallGraphEdgeSourceRef: " + line );
                        System.exit(-1);
                    }
                    CallGraphEdgeSourceRef callGraphEdgeSourceRef = new CallGraphEdgeSourceRef(id.getID(), instructionRef);
                    callGraphEdgeSourceRefFactsList.add(callGraphEdgeSourceRef);                    
                }
                br.close();
            }
            
            try (BufferedReader br = new BufferedReader( new FileReader( "../cache/input-facts/Instruction-Index.facts" ) )) {
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
                    
                    InstructionRef instructionRef = null;
                    for ( InstructionRef instructionRef1 : instructionRefFactsList ) {
                        if ( instructionRef1.getInstruction().equals(m.group(1))) {
                            instructionRef = instructionRef1;
                            break;
                        }
                    }
                    
                    if ( instructionRef == null ) {
                        System.out.println( "Instruction reference not found for Instruction-Index: " + m.group(1) );
                        System.exit(-1);
                    }
                    InstructionIndex instructionIndex = new InstructionIndex( id.getID(), instructionRef, Integer.parseInt( m.group(3) ) ); 
                    instructionIndexFactsList.add(instructionIndex);           //decrement id and map the InstructionIndex object to it
                }
                br.close();

            }
            
        }
        catch( IOException | NumberFormatException ex) {
            System.out.println( ex.toString() );
            System.exit(-1);
            
        }
        
    }
    
    /**
     *
     */
    @Override
    public void createDatomicFactsFile() {
        try {
            try ( PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("../datomic_facts/InstructionRef.dtm", false))); ) {
                for ( InstructionRef key: instructionRefFactsList ) {
                    writer.println( "{:db/id #db/id[:db.part/user " + key.getID() + "]" );
                    writer.println( " :InstructionRef/x \"" + key.getInstruction().replace( "\\", "\\\\").replace( "\"", "\\\"") + "\" }"); 
                }
                writer.close();
            }
            System.out.println( "InstructionRef facts converted: " + instructionRefFactsList.size() );
            
            try ( PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("../datomic_facts/CallGraphEdgeSourceRef.dtm", false))); ) {
                for ( CallGraphEdgeSourceRef key :callGraphEdgeSourceRefFactsList ) {
                    writer.println( "{:db/id #db/id[:db.part/user " + key.getID() + "]" );
                    writer.println( " :CallGraphEdgeSourceRef/x #db/id[:db.part/user " + key.getInstructionRef().getID() + "]}"); 
                }
                writer.close();
            }
            System.out.println( "CallGraphEdgeSourceRef facts converted: " + callGraphEdgeSourceRefFactsList.size() );
            
            try ( PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("../datomic_facts/Instruction-Index.dtm", false))); ) {    
                for ( InstructionIndex key: instructionIndexFactsList ) {
                    writer.println( "{:db/id #db/id[:db.part/user " + key.getID() + "]" );
                    writer.println( " :Instruction-Index/instruction #db/id[:db.part/user " + key.getInstruction().getID() + "]");
                    writer.println( " :Instruction-Index/index " + key.getIndex() + "}" );
                }
                writer.close();
            }
            System.out.println( "Instruction-Index facts converted: " + instructionIndexFactsList.size() );
            
        }
        catch ( Exception ex ) {
            System.out.println( ex.toString() ); 
            System.exit(-1);
        }
    }
    
    public ArrayList<InstructionRef> getInstructionRefFactsList() {
        return this.instructionRefFactsList;
    }

    public ArrayList<CallGraphEdgeSourceRef> getCallGraphEdgeSourceRefFactsList() {
        return this.callGraphEdgeSourceRefFactsList;
    }
    
}