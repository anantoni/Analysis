/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package inputfactsconverter;

import datomicFacts.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author
 * anantoni
 */
public class TypeFactsConverter implements FactsConverter {
    private List<Type> typeFactsList = null;
    private List<ArrayType> arrayTypeFactsList = null;
    private List<ClassType> classTypeFactsList = null;
    private List<InterfaceType> interfaceTypeFactsList = null;
    private List<NullType> nullTypeFactsList = null;
    private List<PrimitiveType> primitiveTypeFactsList = null;
    private List<ReferenceType> referenceTypeFactsList = null;
    private FactsID id = null;
    
    
    public TypeFactsConverter(FactsID id ) {
        super();
        typeFactsList = new ArrayList<>();
        arrayTypeFactsList = new ArrayList<>();
        classTypeFactsList = new ArrayList<>();
        interfaceTypeFactsList = new ArrayList<>();
        nullTypeFactsList = new ArrayList<>();
        primitiveTypeFactsList = new ArrayList<>();
        referenceTypeFactsList = new ArrayList<>();
        this.id = id;
    }    
    
    @Override
    public void parseLogicBloxFactsFile() {
        try {
            try (BufferedReader br = new BufferedReader( new FileReader( "cache/input-facts/Type.facts" ) )) {
                String line;

                while ((line = br.readLine()) != null) {
                    line = line.trim();   
                    Type type = new Type( id.getID(), line );
                    typeFactsList.add(type);
                }
                br.close();
            }
            
            try (BufferedReader br = new BufferedReader( new FileReader( "cache/input-facts/NullType.facts" ) )) {
                String line;
                
                while ((line = br.readLine()) != null) {
                    line = line.trim(); 
                    Type type = null;
                    for ( Type type1 : typeFactsList ) {
                        if ( type1.getValue().equals(line) ) {
                            type = type1;
                            break;
                        }
                    }
                    if ( type == null ) {
                        System.out.println( "NullType type not found for: " + line ); 
                        System.exit(-1);
                    }
                    NullType nullType = new NullType(id.getID(), type);
                    nullTypeFactsList.add(nullType);
                }
                br.close();
            }
        
            try (BufferedReader br = new BufferedReader( new FileReader( "cache/input-facts/PrimitiveType.facts" ) )) {
                String line;
                
                while ((line = br.readLine()) != null) {
                    line = line.trim(); 
                    Type type = null;
                    for ( Type type1 : typeFactsList ) {
                        if ( type1.getValue().equals(line) ) {
                            type = type1;
                            break;
                        }
                    }
                    if ( type == null ) {
                        System.out.println( "NullType type not found for: " + line ); 
                        System.exit(-1);
                    }
                    PrimitiveType primitiveType = new PrimitiveType(id.getID(), type);
                    primitiveTypeFactsList.add(primitiveType);
                }
                br.close();
            }
            
            try (BufferedReader br = new BufferedReader( new FileReader( "cache/input-facts/ReferenceType.facts" ) )) {
                String line;
                
                while ((line = br.readLine()) != null) {
                    line = line.trim(); 
                    Type type = null;
                    for ( Type type1 : typeFactsList ) {
                        if ( type1.getValue().equals(line) ) {
                            type = type1;
                            break;
                        }
                    }
                    if ( type == null ) {
                        System.out.println( "Reference type not found for: " + line ); 
                        System.exit(-1);
                    }
                    ReferenceType referenceType = new ReferenceType( id.getID(), type );
                    referenceTypeFactsList.add(referenceType);
                }
                br.close();
            }
            
            try (BufferedReader br = new BufferedReader( new FileReader( "cache/input-facts/ArrayType.facts" ) )) {
                String line;

                while ((line = br.readLine()) != null) {
                    line = line.trim(); 
                    ReferenceType referenceType = null;
                    for ( ReferenceType referenceType1 : referenceTypeFactsList ) {
                        if ( referenceType1.getType().getValue().equals(line) ) {
                            referenceType = referenceType1;
                            break;
                        }
                    }
                    if ( referenceType == null ) {
                        System.out.println( "Array type not found for: " + line ); 
                        System.exit(-1);
                    }
                    ArrayType arrayType = new ArrayType(id.getID(), referenceType );
                    arrayTypeFactsList.add(arrayType);
                }
                br.close();
            }
        
            try (BufferedReader br = new BufferedReader( new FileReader( "cache/input-facts/ClassType.facts" ) )) {
                String line;

                while ((line = br.readLine()) != null) {
                    line = line.trim(); 
                    ReferenceType referenceType = null;
                    for ( ReferenceType referenceType1 : referenceTypeFactsList ) {
                        if ( referenceType1.getType().getValue().equals(line) ) {
                            referenceType = referenceType1;
                            break;
                        }
                    }
                    if ( referenceType == null ) {
                        System.out.println( "ClassType not found for: " + line ); 
                        System.exit(-1);
                    }
                    ClassType classType = new ClassType(id.getID(), referenceType );
                    classTypeFactsList.add(classType);
                }
                br.close();
            }
        
            try (BufferedReader br = new BufferedReader( new FileReader( "cache/input-facts/InterfaceType.facts" ) )) {
                String line;

                while ((line = br.readLine()) != null) {
                    line = line.trim(); 
                    ReferenceType referenceType = null;
                    for ( ReferenceType referenceType1 : referenceTypeFactsList ) {
                        if ( referenceType1.getType().getValue().equals(line) ) {
                            referenceType = referenceType1;
                            break;
                        }
                    }
                    if ( referenceType == null ) {
                        System.out.println( "Array type not found for: " + line ); 
                        System.exit(-1);
                    }
                    InterfaceType interfaceType = new InterfaceType(id.getID(), referenceType );
                    interfaceTypeFactsList.add(interfaceType);
                }
                br.close();
            }
        
            
        }
        catch( Exception ex) {
            System.out.println( ex.toString() ); 
        }
        
        
    }
    
    @Override
    public void createDatomicFactsFile() {
        try {
            /*************************** Type.dtm **********************************************/
            try ( PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("datomic_facts/Type.dtm", false)));) {
                for ( Type type: typeFactsList ) {
                    writer.println( "{:db/id #db/id[:db.part/user " + type.getID() + "]" );
                    writer.println( " :Type/x \""+ type.getValue() +"\"}"); 
                } 
                writer.close();
            }
            System.out.println( "Type facts converted: " + typeFactsList.size() ); 
            
            /********************************* ReferenceType.dtm ***********************************/
            try ( PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("datomic_facts/ReferenceType.dtm", false)));) {
                for ( ReferenceType referenceType: referenceTypeFactsList ) {
                    writer.println( "{:db/id #db/id[:db.part/user " + referenceType.getID() + "]" );
                    writer.println( " :ReferenceType/x #db/id[:db.part/user " + referenceType.getType().getID() + "]}");  
                }
                writer.close();
            }
            System.out.println( "ReferenceType facts converted: " + referenceTypeFactsList.size() ); 
            
            /********************************* ArrayType.dtm ***********************************/
            try ( PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("datomic_facts/ArrayType.dtm", false)));) { 
                for ( ArrayType arrayType: arrayTypeFactsList ) {
                    writer.println( "{:db/id #db/id[:db.part/user " + arrayType.getID() + "]" );
                    writer.println( " :ArrayType/x #db/id[:db.part/user " + arrayType.getReferenceType().getID() + "]}");  
                }
                writer.close();
            }
            System.out.println( "ArrayType facts converted: " + arrayTypeFactsList.size() );
            
            /********************************* ClassType.dtm ***********************************/
            try ( PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("datomic_facts/ClassType.dtm", false)));) {   
                for ( ClassType classType: classTypeFactsList ) {
                    writer.println( "{:db/id #db/id[:db.part/user " + classType.getID() + "]" );
                    writer.println( " :ClassType/x #db/id[:db.part/user " + classType.getReferenceType().getID() + "]}");  
                }
                writer.close();
            }
            System.out.println( "ClassType facts converted: " + classTypeFactsList.size() );
                
            /********************************* InterfaceType.dtm ***********************************/
            try ( PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("datomic_facts/InterfaceType.dtm", false)));) {   
                for ( InterfaceType interfaceType: interfaceTypeFactsList ) {
                    writer.println( "{:db/id #db/id[:db.part/user " + interfaceType.getID() + "]" );
                    writer.println( " :InterfaceType/x #db/id[:db.part/user " + interfaceType.getReferenceType().getID() + "]}");
                } 
                writer.close();
            }
            System.out.println( "InterfaceType facts converted: " + interfaceTypeFactsList.size() );
            
            /********************************* PrimitiveType.dtm ***********************************/
            try ( PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("datomic_facts/PrimitiveType.dtm", false)));) {
                for ( PrimitiveType primitiveType: primitiveTypeFactsList ) {
                    writer.println( "{:db/id #db/id[:db.part/user " + primitiveType.getID() + "]" );
                    writer.println( " :PrimitiveType/x #db/id[:db.part/user " + primitiveType.getType().getID() + "]}");  
                }
                writer.close();
            }
            System.out.println( "PrimitiveType facts converted: " + primitiveTypeFactsList.size() );
                            
            /********************************* NullType.dtm ***********************************/
            try ( PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("datomic_facts/NullType.dtm", false)));) {
                for ( NullType nullType : nullTypeFactsList ) {
                    writer.println( "{:db/id #db/id[:db.part/user " + nullType.getID() + "]" );
                    writer.println( " :NullType/x #db/id[:db.part/user " + nullType.getType().getID() + "]}");  
                }
                writer.close();
            }
            System.out.println( "NullType facts converted: " + nullTypeFactsList.size() );
        }
        catch ( Exception ex ) {
            System.out.println( ex.toString() ); 
            System.exit(-1);
        }
    }
    
    public List<Type> getTypeFactsList() {
        return this.typeFactsList;
    }
    
    public List<ClassType> getClassTypeFactsList() {
        return this.classTypeFactsList;
    }
    
    public List<ArrayType> getArrayTypeFactsList() {
        return this.arrayTypeFactsList;
    }
}
