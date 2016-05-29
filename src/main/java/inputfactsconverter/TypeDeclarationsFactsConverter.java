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
 * @author
 * anantoni
 */
public class TypeDeclarationsFactsConverter implements FactsConverter, Runnable {
    List<Type> typeFactsList = null;
    List<ClassType> classTypeFactsList = null;
    List<DirectSuperClass> directSuperClassFactsList = null;
    List<DirectSuperInterface> directSuperInterfaceFactsList = null;
    List<ApplicationClass> applicationClassFactsList = null;
    List<MainClass> mainClassFactsList = null;
    FactsID id = null;
    Thread t = null;
    
    public TypeDeclarationsFactsConverter( FactsID id, List<Type> typeFactsList, List<ClassType> classTypeFactsList ) {
        directSuperClassFactsList = new ArrayList<>();
        directSuperInterfaceFactsList = new ArrayList<>();
        applicationClassFactsList = new ArrayList<>();
        mainClassFactsList = new ArrayList<>();
        this.typeFactsList = typeFactsList;
        this.classTypeFactsList = classTypeFactsList;
        this.id = id;
    }
    
    @Override
    public void parseLogicBloxFactsFile() { 
        
        try {
            try (BufferedReader br = new BufferedReader( new FileReader( "../cache/input-facts/DirectSuperclass.facts" ) )) {
                String line;
                while ((line = br.readLine()) != null) {
                    line = line.trim();
                    String pattern = "(.*?)(,\\s)(.*+)";
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
                    Type refClass = null;
                    Type superClass = null;
                    
                    for ( Type type : typeFactsList ) {
                        if ( type.getValue().equals( m.group(1) ) ) {
                            refClass = type;
                            break;
                        }
                    }
                    for ( Type type : typeFactsList ) {
                        if ( type.getValue().equals( m.group(3) ) ) {
                            superClass = type;
                            break;
                        }
                    }
                    
                    DirectSuperClass directSuperClass = new DirectSuperClass( id.getID(), refClass, superClass );
                    directSuperClassFactsList.add( directSuperClass );
                }
                br.close();
            }
            
            try (BufferedReader br = new BufferedReader( new FileReader( "../cache/input-facts/DirectSuperinterface.facts" ) )) {
                String line;
                while ((line = br.readLine()) != null) {
                    line = line.trim();
                    String pattern = "(.*?)(,\\s)(.*+)";
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
                    Type refClass = null;
                    Type superInterface = null;
                    
                    for ( Type type : typeFactsList ) {
                        if ( type.getValue().equals( m.group(1) ) ) {
                            refClass = type;
                            break;
                        }
                    }
                    for ( Type type : typeFactsList ) {
                        if ( type.getValue().equals( m.group(3) ) ) {
                            superInterface = type;
                            break;
                        }
                    }
                    
                    DirectSuperInterface directSuperInterface = new DirectSuperInterface( id.getID(), refClass, superInterface );
                    directSuperInterfaceFactsList.add( directSuperInterface );
                }
                br.close();
            }
            
            try (BufferedReader br = new BufferedReader( new FileReader( "../cache/input-facts/ApplicationClass.facts" ) )) {
                String line;
                while ((line = br.readLine()) != null) {
                    line = line.trim();
                    Type refClass = null;
                    
                    for ( Type type : typeFactsList ) {
                        if ( type.getValue().equals( line ) ) {
                            refClass = type;
                            break;
                        }
                    }
                    
                    ApplicationClass applicationClass = new ApplicationClass( id.getID(), refClass );
                    applicationClassFactsList.add(applicationClass);
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
            try ( PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("../datomic_facts/DirectSuperclass.dtm", false))); ) {
                for ( DirectSuperClass key : directSuperClassFactsList ) {
                    writer.println( "{:db/id #db/id[:db.part/user " + key.getID() + "]" );
                    writer.println( " :DirectSuperclass/ref #db/id[:db.part/user " + key.getRefClassType().getID() +"]"); 
                    writer.println( " :DirectSuperclass/super #db/id[:db.part/user " + key.getSuperClassType().getID() + "]}");
                }
                writer.close();
            }
            System.out.println( "DirectSuperclass facts converted: " + directSuperClassFactsList.size() );
            try ( PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("../datomic_facts/DirectSuperinterface.dtm", false))); ) {
                for ( DirectSuperInterface key: directSuperInterfaceFactsList ) {
                    writer.println( "{:db/id #db/id[:db.part/user " + key.getID() + "]" );
                    writer.println( " :DirectSuperinterface/class #db/id[:db.part/user " + key.getClassType().getID() + "]"); 
                    writer.println( " :DirectSuperinterface/interface #db/id[:db.part/user " + key.getInterfaceType().getID() + "]}");
                }
                writer.close();
            }
            System.out.println( "DirectSuperinterface facts converted: " + directSuperInterfaceFactsList.size() );
            try ( PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("../datomic_facts/ApplicationClass.dtm", false))); ) {
                for ( ApplicationClass key : applicationClassFactsList ) {
                    writer.println( "{:db/id #db/id[:db.part/user " + key.getID() + "]" );
                    writer.println( " :ApplicationClass/ref #db/id[:db.part/user " + key.getType().getID() + "]}");
                }
                writer.close();
            }
            System.out.println( "ApplicationClass facts converted: " + applicationClassFactsList.size() );
        }
        catch ( Exception ex ) {
            ex.printStackTrace();
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
        t = new Thread(this, "Child thread: TypeDeclarationsFactsConverter" );
        t.start();
    } 
     
    public Thread getThread() {
        return t;
    } 
}
