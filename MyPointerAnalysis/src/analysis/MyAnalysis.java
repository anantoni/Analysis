/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package analysis;

import datomic.Connection;
import datomic.Peer;
import datomic.Util;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;

/**
 *
 * @author anantoni
 */
public class MyAnalysis {
    Connection conn = null;
    
    public MyAnalysis( Connection conn ) {
        this.conn = conn;
    }
    
    public void runAnalysis() {
         long startTime = System.nanoTime();
        
        /******************** Reachable ***************************/
        Collection results = Peer.q("[:find ?method :where" +
                         "[?mainMethodDeclaration :MainMethodDeclaration/method ?method]]",
                         conn.db());
        
        for ( Object result : results) {
           Object tempid = Peer.tempid(":db.part/user");
           List tx = Util.list(Util.map(":db/id", tempid, 
                                        ":Reachable/method", ((List) result).get(0)));
           try { Object txResult = conn.transact(tx).get(); }
           catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
                ex.toString();
                System.exit(-1);
           }
        }
        Collection reachable_added_to_database = results;
        
        results = Peer.q("[:find ?method :where" +
                         "[?implicitReachable :ImplicitReachable/sig ?method]]",
                         conn.db());
                
        for ( Object result : results) {
           Object tempid = Peer.tempid(":db.part/user");
           List tx = Util.list(Util.map(":db/id", tempid, 
                                        ":Reachable/method", ((List) result).get(0)));
           try { Object txResult = conn.transact(tx).get(); }
           catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
                ex.toString();
                System.exit(-1);
           }
        }
        reachable_added_to_database.addAll(results);
        results = Peer.q("[:find ?clinit :where" +
                         "[?initializedClass :InitializedClass/classOrInterface ?class]" +
                         "[?classInitializer :ClassInitializer/type ?class]" +
                         "[?classInitializer :ClassInitializer/method ?clinit]]",
                         conn.db());

        for ( Object result : results) {
            List tx = Util.list(Util.map(":db/id", Peer.tempid(":db.part/user"), 
                                        ":Reachable/method", ((List) result).get(0)));

            try { Object txResult = conn.transact(tx).get(); }
            catch (InterruptedException | ExecutionException ex) {
                ex.toString();
                System.exit(-1);
            }
        }
        reachable_added_to_database.addAll(results);
        System.out.println("Starting reachable functions: " + reachable_added_to_database.size());
        reachFixPoint(reachable_added_to_database, startTime);        

    }
    
    public void reachFixPoint( Collection reachable_added_to_database, long startTime ) {
        Collection var_points_to_added_to_database = null;
        Collection call_graph_edge_added_to_database = null;
        Collection instance_field_points_to_added_to_database = null;
        Collection static_field_points_to_added_to_database = null;
        Collection array_index_points_to_added_to_database = null;
        Collection assign_added_to_database = null;
        Collection results = null;
        
        int counter = 0;
        
        while (true) {
            boolean fix_point_reached = true;

            /************** Variable points to Heap object**********************/
            results = Peer.q("[:find ?heap ?var :where"+
                             "[?reachable :Reachable/method ?inmethod]" +
                             "[?assignHeapNormalAllocation :AssignNormalHeapAllocation/inmethod ?inmethod]" +
                             "[?assignHeapNormalAllocation :AssignNormalHeapAllocation/heap ?heap]" +
                             "[?assignHeapNormalAllocation :AssignNormalHeapAllocation/var ?var]]",
                             conn.db() );
            if ( counter == 0 )
                var_points_to_added_to_database = results;
            else {
                results.removeAll(var_points_to_added_to_database);
                var_points_to_added_to_database.addAll(results);
            }
            
            if ( !(results.isEmpty()) )
                fix_point_reached = false;
            for ( Object result : results ) {
               List tx = Util.list(Util.map(":db/id", Peer.tempid(":db.part/user"), 
                                            ":VarPointsTo/heap", ((List) result).get(0),
                                            ":VarPointsTo/var", ((List) result).get(1)));
               try { Object txResult = conn.transact(tx).get(); }
               catch (InterruptedException | ExecutionException ex) {
                    ex.printStackTrace();
                    ex.toString();
                    System.exit(-1);
               }
            }
            System.out.println("VarPointsTo added from AssignNormalHeapAllocation: " + results.size() );
            results = Peer.q("[:find ?heap ?var :where"+
                             "[?reachable :Reachable/method ?inmethod]" +
                             "[?assignAuxiliaryHeapAllocation :AssignAuxiliaryHeapAllocation/inmethod ?inmethod]" +
                             "[?assignAuxiliaryHeapAllocation :AssignAuxiliaryHeapAllocation/heap ?heap]" +
                             "[?assignAuxiliaryHeapAllocation :AssignAuxiliaryHeapAllocation/var ?var]]",
                             conn.db() );
            
            results.removeAll(var_points_to_added_to_database);
            var_points_to_added_to_database.addAll(results);
            
            if ( !(results.isEmpty()) )
                fix_point_reached = false;
            for ( Object result : results) {
               List tx = Util.list(Util.map(":db/id", Peer.tempid(":db.part/user"), 
                                            ":VarPointsTo/heap", ((List) result).get(0),
                                            ":VarPointsTo/var", ((List) result).get(1)));
               try {
                    Object txResult = conn.transact(tx).get();
               }
               catch (InterruptedException | ExecutionException ex) {
                    ex.printStackTrace();
                    ex.toString();
                    System.exit(-1);
               }
            }
            System.out.println( "VarPointsTo added from AssignAuxialiaryHeapAllocation: " + results.size() );

            results = Peer.q("[:find ?heap ?var :where"+
                             "[?reachable :Reachable/method ?inmethod]" +
                             "[?assignContextInsensitiveHeapAllocation :AssignContextInsensitiveHeapAllocation/inmethod ?inmethod]" +
                             "[?assignContextInsensitiveHeapAllocation :AssignContextInsensitiveHeapAllocation/heap ?heap]" +
                             "[?assignContextInsensitiveHeapAllocation :AssignContextInsensitiveHeapAllocation/var ?var]]",
                             conn.db() );
            
            results.removeAll(var_points_to_added_to_database);
            var_points_to_added_to_database.addAll(results);
            
            if ( !(results.isEmpty()) )
                fix_point_reached = false;
            for ( Object result : results) {
               List tx = Util.list(Util.map(":db/id", Peer.tempid(":db.part/user"), 
                                            ":VarPointsTo/heap", ((List) result).get(0),
                                            ":VarPointsTo/var", ((List) result).get(1)));
               try { Object txResult = conn.transact(tx).get(); }
               catch (InterruptedException | ExecutionException ex) {
                    ex.printStackTrace();
                    ex.toString();
                    System.exit(-1);
               }
            }
            System.out.println( "VarPointsTo from AssignContextInsensitiveHeapAllocation: " + results.size());
            
            
            
            results = Peer.q("[:find ?heap ?to :where"+
                            "[?reachable :Reachable/method ?inmethod]" +
                            "[?loadInstanceField :LoadInstanceField/inmethod ?inmethod]" +
                            "[?loadInstanceField :LoadInstanceField/sig ?fieldsig]" +
                            "[?loadInstanceField :LoadInstanceField/base ?base]" +
                            "[?loadInstanceField :LoadInstanceField/to ?to]" + 
                            "[?basePointsTo :VarPointsTo/var ?base]" +
                            "[?basePointsTo :VarPointsTo/heap ?heapbase]" +
                            "[?instanceFieldPointsTo :InstanceFieldPointsTo/heapbase ?heapbase]"+
                            "[?instanceFieldPointsTo :InstanceFieldPointsTo/fieldsig ?fieldsig]"+
                            "[?instanceFieldPointsTo :InstanceFieldPointsTo/heap ?heap]]",
                            conn.db() );
            results.removeAll(var_points_to_added_to_database);
            var_points_to_added_to_database.addAll(results);
            if ( !(results.isEmpty()) )
                fix_point_reached = false;
            for ( Object result : results) {
               List tx = Util.list(Util.map(":db/id", Peer.tempid(":db.part/user"), 
                                            ":VarPointsTo/heap", ((List) result).get(0),
                                            ":VarPointsTo/var", ((List) result).get(1)));
               try { Object txResult = conn.transact(tx).get(); }
               catch (InterruptedException | ExecutionException ex) {
                    ex.printStackTrace();
                    ex.toString();
                    System.exit(-1);
               }
            }
            System.out.println("VarPointsTo from load instance field where instance field points to: " + results.size() ); 
            
            results = Peer.q("[:find ?heap ?to :where"+
                            "[?reachable :Reachable/method ?inmethod]" +
                            "[?loadStaticField :LoadStaticField/inmethod ?inmethod]" +
                            "[?loadStaticField :LoadStaticField/sig ?fieldsig]" +
                            "[?staticFieldPointsTo :StaticFieldPointsTo/fieldsig ?fieldsig]"+
                            "[?loadStaticField :LoadStaticField/to ?to]" +
                            "[?staticFieldPointsTo :StaticFieldPointsTo/heap ?heap]]",
                            conn.db() );
            
            results.removeAll(var_points_to_added_to_database);
            var_points_to_added_to_database.addAll(results);
            
            if ( !(results.isEmpty()) )
                fix_point_reached = false;
            for ( Object result : results) {
               List tx = Util.list(Util.map(":db/id", Peer.tempid(":db.part/user"), 
                                            ":VarPointsTo/heap", ((List) result).get(0),
                                            ":VarPointsTo/var", ((List) result).get(1)));
               try { Object txResult = conn.transact(tx).get(); }
               catch (InterruptedException | ExecutionException ex) {
                    ex.printStackTrace();
                    ex.toString();
                    System.exit(-1);
               }
            }
            System.out.println("VarPointsTo from load static field where static fields points to: " + results.size());
            
           
            results = Peer.q("[:find ?heap ?var :where" +
                             "[?reachable :Reachable/method ?inmethod]" +
                             "[?assignLocal :AssignLocal/inmethod ?inmethod]" +
                             "[?assignLocal :AssignLocal/to ?var]" +
                             "[?assignLocal :AssignLocal/from ?from]" +
                             "[?fromPointsTo :VarPointsTo/var ?from]" +
                             "[?fromPointsTo :VarPointsTo/heap ?heap]]",                                              
                             conn.db() );
            
            results.removeAll(var_points_to_added_to_database);
            var_points_to_added_to_database.addAll(results);
            
            if ( !(results.isEmpty()) )
                fix_point_reached = false;
            for ( Object result : results) {
               List tx = Util.list(Util.map(":db/id", Peer.tempid(":db.part/user"), 
                                            ":VarPointsTo/heap", ((List) result).get(0),
                                            ":VarPointsTo/var", ((List) result).get(1)));
               
               try { Object txResult = conn.transact(tx).get(); }
               catch (InterruptedException | ExecutionException ex) {
                    ex.printStackTrace();
                    ex.toString();
                    System.exit(-1);
               }
            }
            System.out.println("VarPointsTo from AssignLocal: " + results.size());
             /************** Assignments *******************************/
            results = Peer.q("[:find ?type ?from ?to :where"+
                            "[?reachable :Reachable/method ?inmethod]"+
                            "[?assignCast :AssignCast/inmethod ?inmethod]" +
                            "[?assignCast :AssignCast/type ?type]" +
                            "[?assignCast :AssignCast/from ?from]" +
                            "[?assignCast :AssignCast/to ?to]]" ,                                                                         
                            conn.db() );
            
            if ( counter == 0 )
                assign_added_to_database = results;
            else {
                results.removeAll(assign_added_to_database);
                assign_added_to_database.addAll(results);
            }
            if ( !(results.isEmpty()) )
                fix_point_reached = false;
            for ( Object result : results) {
               Object tempid = Peer.tempid(":db.part/user");
               List tx = Util.list(Util.map(":db/id", tempid, 
                                            ":Assign/type", ((List) result).get(0),
                                            ":Assign/from", ((List) result).get(1),
                                            ":Assign/to", ((List) result).get(2)));
               try { Object txResult = conn.transact(tx).get(); }
               catch (InterruptedException | ExecutionException ex) {
                    ex.printStackTrace();
                    ex.toString();
                    System.exit(-1);
               }
            }
            System.out.println("Assign from AssignCast: " + results.size());
            
            /************** InterProcedural Assigns *****************************/
            results = Peer.q("[:find ?type ?actual ?formal :where"+
                            "[?callGraphEdge :CallGraphEdge/tomethod ?method]" +
                            "[?callGraphEdge :CallGraphEdge/invocation ?invocation]"+
                            "[?formalParam :FormalParam/method ?method]" +
                            "[?formalParam :FormalParam/index ?index]" +
                            "[?formalParam :FormalParam/var ?formal]" +
                            "[?actualParam :ActualParam/invocation ?invocation]" +
                            "[?actualParam :ActualParam/index ?index]" +
                            "[?actualParam :ActualParam/var ?actual]" +
                            "[?formal :Var/type ?type]]",
                            conn.db() );
            
            results.removeAll(assign_added_to_database);
            assign_added_to_database.addAll(results);
            if ( !(results.isEmpty()) )
                fix_point_reached = false;
            for ( Object result : results) {
               List tx = Util.list(Util.map(":db/id", Peer.tempid(":db.part/user"), 
                                            ":Assign/type", ((List) result).get(0),
                                            ":Assign/from", ((List) result).get(1),
                                            ":Assign/to", ((List) result).get(2)));
               try { Object txResult = conn.transact(tx).get(); }
               catch (InterruptedException | ExecutionException ex) {
                    ex.printStackTrace();
                    ex.toString();
                    System.exit(-1);
               }
            }
            System.out.println("Assign from Parameters: " + results.size());

            
            results = Peer.q("[:find ?type ?return ?local :where"+
                            "[?callGraphEdge :CallGraphEdge/invocation ?invocation]"+
                            "[?assignReturnValue :AssignReturnValue/invocation ?invocation]" +
                            "[?callGraphEdge :CallGraphEdge/tomethod ?method]"+
                            "[?returnVar :ReturnVar/method ?method]" + 
                            "[?returnVar :ReturnVar/var ?return]" +
                            "[?assignReturnValue :AssignReturnValue/to ?local]" +
                            "[?local :Var/type ?type]]",                            
                            conn.db() );
            
            results.removeAll(assign_added_to_database);
            assign_added_to_database.addAll(results);
            if ( !(results.isEmpty()) )
                fix_point_reached = false;
            for ( Object result : results) {
               List tx = Util.list(Util.map(":db/id", Peer.tempid(":db.part/user"), 
                                            ":Assign/type", ((List) result).get(0),
                                            ":Assign/from", ((List) result).get(1),
                                            ":Assign/to", ((List) result).get(2)));
               try { Object txResult = conn.transact(tx).get(); }
               catch (InterruptedException | ExecutionException ex) {
                    ex.printStackTrace();
                    ex.toString();
                    System.exit(-1);
               }
            }
            System.out.println("Assign from Return Value: " + results.size());
            
            results = Peer.q("[:find ?heap ?to :where"+
                             "[?assign :Assign/to ?to]"+
                             "[?assign :Assign/from ?from]"+
                             "[?assign :Assign/type ?type]"+
                             "[?fromPointsTo :VarPointsTo/var ?from]"+
                             "[?fromPointsTo :VarPointsTo/heap ?heap]"+
                             "[?heapAllocationType :HeapAllocation-Type/heap ?heap]"+
                             "[?heapAllocationType :HeapAllocation-Type/type ?heaptype]"+
                             "[?assignCompatible :AssignCompatible/source ?heaptype]"+
                             "[?assignCompatible :AssignCompatible/target ?type]]",
                             conn.db() );
            
            results.removeAll(var_points_to_added_to_database);
            var_points_to_added_to_database.addAll(results);
            
            if ( !(results.isEmpty()) )
                fix_point_reached = false;
            
            for ( Object result : results) {
               List tx = Util.list(Util.map(":db/id", Peer.tempid(":db.part/user"), 
                                            ":VarPointsTo/heap", ((List) result).get(0),
                                            ":VarPointsTo/var", ((List) result).get(1)));
               try { Object txResult = conn.transact(tx).get(); }
               catch (InterruptedException | ExecutionException ex) {
                    ex.printStackTrace();
                    ex.toString();
                    System.exit(-1);
               }
            }
            System.out.println( "VarPointsTo from Assign: " + results.size());
            
            /********** Fields *****************/
            results = Peer.q("[:find ?fieldsig ?heap :where" +
                     "[?reachable :Reachable/method ?inmethod]" +
                     "[?storeStaticField :StoreStaticField/inmethod ?inmethod]" +                    
                     "[?storeStaticField :StoreStaticField/signature ?fieldsig]" +
                     "[?storeStaticField :StoreStaticField/from ?from]" +
                     "[?varPointsTo :VarPointsTo/var ?from]" +
                     "[?varPointsTo :VarPointsTo/heap ?heap]]" ,
                     conn.db());
            
            if ( counter == 0 )
                static_field_points_to_added_to_database = results;
            else {
                results.removeAll(static_field_points_to_added_to_database);
                static_field_points_to_added_to_database.addAll(results);
            }
            if ( !(results.isEmpty()) )
                fix_point_reached = false; 

            for ( Object result : results) {
               List tx = Util.list(Util.map(":db/id", Peer.tempid(":db.part/user"), 
                                            ":StaticFieldPointsTo/fieldsig", ((List) result).get(0),
                                            ":StaticFieldPointsTo/heap", ((List) result).get(1)));
               try { Object txResult = conn.transact(tx).get(); }
               catch (InterruptedException | ExecutionException ex) {
                    ex.printStackTrace();
                    ex.toString();
                    System.exit(-1);
               }
            }
            System.out.println("StaticFieldPointsTo: " + results.size());
            
            results = Peer.q("[:find ?heapbase ?fieldsig ?heap :where" +
                            "[?reachable :Reachable/method ?inmethod]" +
                            "[?storeInstanceField :StoreInstanceField/inmethod ?inmethod]" +
                            "[?storeInstanceField :StoreInstanceField/base ?base]" +
                            "[?storeInstanceField :StoreInstanceField/from ?from]" +
                            "[?storeInstanceField :StoreInstanceField/signature ?fieldsig]" +
                            "[?basePointsTo :VarPointsTo/var ?base]" +   
                            "[?fromPointsTo :VarPointsTo/var ?from]" +
                            "[?basePointsTo :VarPointsTo/heap ?heapbase]" +
                            "[?fromPointsTo :VarPointsTo/heap ?heap]]",
                            conn.db());

            if ( counter == 0 )
                instance_field_points_to_added_to_database = results;
            else {
                results.removeAll(instance_field_points_to_added_to_database);
                instance_field_points_to_added_to_database.addAll(results);
            }
            if ( !(results.isEmpty()) )
                fix_point_reached = false;
            for ( Object result : results) {
               List tx = Util.list(Util.map(":db/id", Peer.tempid(":db.part/user"),
                                            ":InstanceFieldPointsTo/heapbase", ((List) result).get(0),
                                            ":InstanceFieldPointsTo/fieldsig", ((List) result).get(1),
                                            ":InstanceFieldPointsTo/heap", ((List) result).get(2)));
               try { Object txResult = conn.transact(tx).get(); }
               catch (InterruptedException | ExecutionException ex) {
                    ex.printStackTrace();
                    ex.toString();
                    System.exit(-1);
               }
            }
            System.out.println("InstanceFieldPointsTo: " + results.size()); 
            
            /*************** Arrays ********************/
            results = Peer.q("[:find ?heapbase ?heap :where" +
                             "[?reachable :Reachable/method ?inmethod]" +
                             "[?storeArrayIndex :StoreArrayIndex/inmethod ?inmethod]" +
                             "[?storeArrayIndex :StoreArrayIndex/from ?from]" +
                             "[?storeArrayIndex :StoreArrayIndex/base ?base]" +
                             "[?basePointsTo :VarPointsTo/var ?base]" +
                             "[?basePointsTo :VarPointsTo/heap ?heapbase]" +
                             "[?fromPointsTo :VarPointsTo/var ?from]" +
                             "[?fromPointsTo :VarPointsTo/heap ?heap]"+
                             "[?heapAllocationType :HeapAllocation-Type/heap ?heap]" +
                             "[?heapAllocationType :HeapAllocation-Type/type ?heaptype]" +
                             "[?baseHeapAllocationType :HeapAllocation-Type/heap ?heapbase]" +
                             "[?baseHeapAllocationType :HeapAllocation-Type/type ?heapbasetype]" +
                             "[?componentType :ComponentType/arrayType ?heapbasetype]" +
                             "[?componentType :ComponentType/componentType ?componenttype]" +
                             "[?assignCompatible :AssignCompatible/target ?componenttype]" +
                             "[?assignCompatible :AssignCompatible/source ?heaptype]]",                     
                             conn.db());

            if ( counter == 0 )
                array_index_points_to_added_to_database = results;
            else {
                results.removeAll(array_index_points_to_added_to_database);
                array_index_points_to_added_to_database.addAll(results);
            }
            if ( !(results.isEmpty()) )
                fix_point_reached = false;
            for ( Object result : results) {
               List tx = Util.list(Util.map(":db/id", Peer.tempid(":db.part/user"),
                                            ":ArrayIndexPointsTo/baseheap", ((List) result).get(0),
                                            ":ArrayIndexPointsTo/heap", ((List) result).get(1)));
               try { Object txResult = conn.transact(tx).get(); }
               catch (InterruptedException | ExecutionException ex) {
                    ex.printStackTrace();
                    ex.toString();
                    System.exit(-1);
               }
            }
            
            System.out.println( "ArrayIndexPointsTo added: " + results.size()); 
            
            results = Peer.q("[:find ?heap ?to :where" +
                             "[?reachable Reachable/method ?inmethod]" +
                             "[?loadArrayIndex :LoadArrayIndex/inmethod ?inmethod]" +                    
                             "[?loadArrayIndex :LoadArrayIndex/to ?to]" +
                             "[?loadArrayIndex :LoadArrayIndex/base ?base]" +
                             "[?varPointsTo :VarPointsTo/var ?base]" +   
                             "[?varPointsTo :VarPointsTo/heap ?heapbase]" +
                             "[?arrayIndexPointsTo :ArrayIndexPointsTo/baseheap ?heapbase]" +
                             "[?arrayIndexPointsTo :ArrayIndexPointsTo/heap ?heap]"+   
                             "[?to :Var/type ?type]" +   
                             "[?heapAllocationType :HeapAllocation-Type/heap ?heapbase]" +
                             "[?heapAllocationType :HeapAllocation-Type/type ?heapbasetype]" +
                             "[?componentType :ComponentType/arrayType ?heapbasetype]" +
                             "[?componentType :ComponentType/componentType ?basecomponenttype]" +
                             "[?assignCompatible :AssignCompatible/source ?basecomponenttype]" +
                             "[?assignCompatible :AssignCompatible/target ?type]]",                     
                             conn.db());
            
            results.removeAll(var_points_to_added_to_database);
            var_points_to_added_to_database.addAll(results);
            if ( !(results.isEmpty()) )
                fix_point_reached = false;
            for ( Object result : results) {
               List tx = Util.list(Util.map(":db/id", Peer.tempid(":db.part/user"), 
                                            ":VarPointsTo/heap", ((List) result).get(0),
                                            ":VarPointsTo/var", ((List) result).get(1)));
               try { Object txResult = conn.transact(tx).get();}
               catch (InterruptedException | ExecutionException ex) {
                    ex.printStackTrace();
                    ex.toString();
                    System.exit(-1);
               }
            }
            System.out.println("VarPointsTo from LoadArrayIndex added: " + results.size());
            
            /**** Reachable Methods ****/
            results = Peer.q("[:find ?heap ?this :where" +
                     "[?reachable :Reachable/method ?inmethod]" +
                     "[?virtualMethodInvocation :VirtualMethodInvocation/inmethod ?inmethod]" +   
                     "[?virtualMethodInvocation :VirtualMethodInvocation/invocation ?invocation]" +
                     "[?virtualMethodInvocation :VirtualMethodInvocation/signature ?signature]" +
                     "[?method :Method/signature ?signature]" +
                     "[?method :Method/simplename ?simplename]" +
                     "[?method :Method/descriptor ?descriptor]" +
                     "[?virtualMethodInvocation :VirtualMethodInvocation/base ?base]" +
                     "[?varPointsTo :VarPointsTo/var ?base]" +
                     "[?varPointsTo :VarPointsTo/heap ?heap]" +
                     "[?heapAllocationType :HeapAllocation-Type/heap ?heap]" +
                     "[?heapAllocationType :HeapAllocation-Type/type ?type]" +
                     "[?methodLookup :MethodLookup/simplename ?simplename]" +
                     "[?methodLookup :MethodLookup/descriptor ?descriptor]" +   
                     "[?methodLookup :MethodLookup/type ?type]" +                         
                     "[?methodLookup :MethodLookup/method ?tomethod]" +
                     "[?thisVar :ThisVar/method ?tomethod]" +
                     "[?thisVar :ThisVar/var ?this]]" ,
                     conn.db());
            
            results.removeAll(var_points_to_added_to_database);
            var_points_to_added_to_database.addAll(results);
            
            if ( !(results.isEmpty()) )
                fix_point_reached = false;
            
            for ( Object result : results) {
               List tx = Util.list(Util.map(":db/id", Peer.tempid(":db.part/user"), 
                                            ":VarPointsTo/heap", ((List) result).get(0),
                                            ":VarPointsTo/var", ((List) result).get(1)));
               try { Object txResult = conn.transact(tx).get(); }
               catch (InterruptedException | ExecutionException ex) {
                    ex.printStackTrace();
                    ex.toString();
                    System.exit(-1);
               }
            }
            
            System.out.println( "VarPointsTo from virtual: " + results.size() );
            
            results = Peer.q("[:find ?invocation ?tomethod :where" +
                     "[?reachable :Reachable/method ?inmethod]" +
                     "[?virtualMethodInvocation :VirtualMethodInvocation/inmethod ?inmethod]" +   
                     "[?virtualMethodInvocation :VirtualMethodInvocation/invocation ?invocation]" +
                     "[?virtualMethodInvocation :VirtualMethodInvocation/signature ?signature]" +
                     "[?method :Method/signature ?signature]" +
                     "[?method :Method/simplename ?simplename]" +
                     "[?method :Method/descriptor ?descriptor]" +
                     "[?virtualMethodInvocation :VirtualMethodInvocation/base ?base]" +
                     "[?varPointsTo :VarPointsTo/var ?base]" +
                     "[?varPointsTo :VarPointsTo/heap ?heap]" +
                     "[?heapAllocationType :HeapAllocation-Type/heap ?heap]" +
                     "[?heapAllocationType :HeapAllocation-Type/type ?type]" +
                     "[?methodLookup :MethodLookup/simplename ?simplename]" +
                     "[?methodLookup :MethodLookup/descriptor ?descriptor]" +
                     "[?methodLookup :MethodLookup/type ?type]" +
                     "[?methodLookup :MethodLookup/method ?tomethod]" +
                     "[?thisVar :ThisVar/method ?tomethod]]",
                     conn.db());
            
            if ( counter == 0 ) 
                call_graph_edge_added_to_database = results;
            else {
                results.removeAll(call_graph_edge_added_to_database);
                call_graph_edge_added_to_database.addAll(results);
            }
                
            if ( !(results.isEmpty()) )
                fix_point_reached = false;
            
            for ( Object result : results) {
               List tx = Util.list(Util.map(":db/id", Peer.tempid(":db.part/user"), 
                                            ":CallGraphEdge/invocation", ((List) result).get(0),
                                            ":CallGraphEdge/tomethod", ((List) result).get(1)));
               try { Object txResult = conn.transact(tx).get(); }
               catch (InterruptedException | ExecutionException ex) {
                    ex.printStackTrace();
                    ex.toString();
                    System.exit(-1);
               }
            }
            System.out.println( "CallGraphEdge from virtual: " + results.size() );
            
            results = Peer.q("[:find ?heap ?this :where" +
                     "[?reachable :Reachable/method ?inmethod]" +
                     "[?specialMethodInvocation :SpecialMethodInvocation/inmethod ?inmethod]" +
                     "[?specialMethodInvocation :SpecialMethodInvocation/invocation ?invocation]" +
                     "[?specialMethodInvocation :SpecialMethodInvocation/signature ?signature]" +    
                     "[?method :Method/signature ?signature]" +
                     "[?specialMethodInvocation :SpecialMethodInvocation/base ?base]" + 
                     "[?varPointsTo :VarPointsTo/var ?base]" +
                     "[?varPointsTo :VarPointsTo/heap ?heap]" +
                     "[?method :Method/declaration ?tomethod]"+
                     "[?thisVar :ThisVar/method ?tomethod]" +
                     "[?thisVar :ThisVar/var ?this]]" ,
                     conn.db());
            
            results.removeAll(var_points_to_added_to_database);
            var_points_to_added_to_database.addAll(results);
            
            if ( !(results.isEmpty()) )
                fix_point_reached = false;
            
            for ( Object result : results) {
               List tx = Util.list(Util.map(":db/id", Peer.tempid(":db.part/user"), 
                                            ":VarPointsTo/heap", ((List) result).get(0),
                                            ":VarPointsTo/var", ((List) result).get(1)));
               try { Object txResult = conn.transact(tx).get(); }
               catch (InterruptedException | ExecutionException ex) {
                    ex.printStackTrace();
                    ex.toString();
                    System.exit(-1);
               }
            }
            System.out.println( "VarPointsTo from special: " + results.size() );

            results = Peer.q("[:find ?invocation ?tomethod :where" +
                     "[?reachable :Reachable/method ?inmethod]" +
                     "[?specialMethodInvocation :SpecialMethodInvocation/inmethod ?inmethod]" +
                     "[?specialMethodInvocation :SpecialMethodInvocation/invocation ?invocation]" +
                     "[?specialMethodInvocation :SpecialMethodInvocation/signature ?signature]" +
                     "[?method :Method/signature ?signature]" +
                     "[?method :Method/declaration ?tomethod]" +
                     "[?specialMethodInvocation :SpecialMethodInvocation/base ?base]" +
                     "[?varPointsTo :VarPointsTo/var ?base]" +
                     "[?thisVar :ThisVar/method ?tomethod]]" ,
                     conn.db());
            
            results.removeAll(call_graph_edge_added_to_database);
            call_graph_edge_added_to_database.addAll(results);
            
            if ( !(results.isEmpty()) )
                fix_point_reached = false;

            for ( Object result : results) {
               List tx = Util.list(Util.map(":db/id", Peer.tempid(":db.part/user"), 
                                            ":CallGraphEdge/invocation", ((List) result).get(0),
                                            ":CallGraphEdge/tomethod", ((List) result).get(1)));
               try { Object txResult = conn.transact(tx).get(); }
               catch (InterruptedException | ExecutionException ex) {
                    ex.printStackTrace();
                    ex.toString();
                    System.exit(-1);
               }
            }
            
            System.out.println( "CallGraphEdge from special: " + results.size() );
            results = Peer.q("[:find ?invocation ?tomethod :where" +
                     "[?reachable :Reachable/method ?inmethod]" +
                     "[?staticMethodInvocation :StaticMethodInvocation/inmethod ?inmethod]" +
                     "[?staticMethodInvocation :StaticMethodInvocation/invocation ?invocation]" +
                     "[?staticMethodInvocation :StaticMethodInvocation/signature ?signature]" +   
                     "[?method :Method/signature ?signature]" +
                     "[?method :Method/declaration ?tomethod]]",
                     conn.db());
            
            results.removeAll(call_graph_edge_added_to_database);
            call_graph_edge_added_to_database.addAll(results);
            
            if ( !(results.isEmpty()) )
                fix_point_reached = false;
            
            for ( Object result : results) {
               List tx = Util.list(Util.map(":db/id", Peer.tempid(":db.part/user"), 
                                            ":CallGraphEdge/invocation", ((List) result).get(0),
                                            ":CallGraphEdge/tomethod", ((List) result).get(1)));
               try { Object txResult = conn.transact(tx).get(); }
               catch (InterruptedException | ExecutionException ex) {
                    ex.printStackTrace();
                    ex.toString();
                    System.exit(-1);
               }
            }

            results = Peer.q("[:find ?tomethod :where" +
                     "[?callGraphEdge :CallGraphEdge/invocation ?invocation]" +
                     "[?callGraphEdge :CallGraphEdge/tomethod ?tomethod]]",
                     conn.db());
            
            results.removeAll(reachable_added_to_database);
            reachable_added_to_database.addAll(results);
            if ( !(results.isEmpty()) )
                fix_point_reached = false;
            
            for ( Object result : results) {
                List tx = Util.list(Util.map(":db/id", Peer.tempid(":db.part/user"), ":Reachable/method", ((List) result).get(0)));

                try { Object txResult = conn.transact(tx).get(); }
                catch (InterruptedException | ExecutionException ex) {
                    ex.printStackTrace();
                    ex.toString();
                    System.exit(-1);
                }
            }
            System.out.println("Static reachable added: " + results.size());
            if ( fix_point_reached == true) {
                long stopTime = System.nanoTime();
                long elapsedTime = stopTime - startTime;
                System.out.println( "Analysis time: " + (double)(elapsedTime/(double)1000000000));
                break;
            }
            counter++;
            System.out.println("Loop number: " + (counter-1));
            
        }
       
    }
    
    
    
}
