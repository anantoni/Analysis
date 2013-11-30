/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package inputfactsconverter;

/**
 *
 * @author anantoni
 */
public class InputFactsConverter {

    /**
     * @param args the command line arguments
     */
    public InputFactsConverter() {
        System.out.println("Reached constructor"); 
        //Integer id = -1000;
        FactsID id = new FactsID( -1000 );
        
        
        /************************** NOT RUNNABLE *************************/
        /************* Instructions facts converter *********************/
        InstructionsFactsConverter ifc = new InstructionsFactsConverter(id);
        ifc.parseLogicBloxFactsFile();
        ifc.createDatomicFactsFile();
        id.printID();
        
        /********************* Type facts converter *********************/
        TypeFactsConverter tfc = new TypeFactsConverter(id);
        tfc.parseLogicBloxFactsFile();
        tfc.createDatomicFactsFile();
        id.printID();
        
        /***************** HeapAllocation facts converter *********************/
        HeapAllocationFactsConverter hatfc = new HeapAllocationFactsConverter(id, tfc.getTypeFactsList(), ifc.getCallGraphEdgeSourceRefFactsList() );
        hatfc.startThread();
        
         /************** Field Declarations facts converter *************************/
        FieldDeclarationsFactsConverter fdfc = new FieldDeclarationsFactsConverter(id, tfc.getTypeFactsList());
        fdfc.startThread();
       
         /*************** VariableDeclarations facts converter ****************************/
        VariableDeclarationsFactsConverter vdfc = new VariableDeclarationsFactsConverter(id, tfc.getTypeFactsList());
        vdfc.startThread();        
        
        try {
            fdfc.getThread().join();
            vdfc.getThread().join();
            hatfc.getThread().join();
            
        }
        catch ( InterruptedException ex ) {
            ex.printStackTrace();
            System.out.println(ex.toString());
            System.exit(-1);
        }
        
        ParameterFactsConverter pfc = new ParameterFactsConverter(id, vdfc.getvarFactsList(), ifc.getCallGraphEdgeSourceRefFactsList(), vdfc.getMethodSignatureRefFactsList() );
        pfc.parseLogicBloxFactsFile();
        pfc.createDatomicFactsFile();
        id.printID();
        
        /***************************** NOT RUNNABLE *********************************/
        
        
        
        /******************** TypeDeclarations facts converter *******************/
        TypeDeclarationsFactsConverter tdfc = new TypeDeclarationsFactsConverter(id, tfc.getTypeFactsList(), tfc.getClassTypeFactsList());
        tdfc.startThread();
       
        /************** Method Declarations and Signatures facts converter **********************/
        MethodDeclarationsAndSignaturesFactsConverter mdasfc = new MethodDeclarationsAndSignaturesFactsConverter(id,tfc.getTypeFactsList(),fdfc.getSimpleNameRefFactsList(), vdfc.getMethodSignatureRefFactsList(), fdfc.getModifierRefFactsList());
        mdasfc.startThread();
        
        ReturnVariablesFactsConverter rvfc = new ReturnVariablesFactsConverter(id, vdfc.getvarFactsList(), vdfc.getMethodSignatureRefFactsList());
        rvfc.startThread();
        
        SpecialMethodInvocationsFactsConverter smifc = new SpecialMethodInvocationsFactsConverter(id, vdfc.getvarFactsList(), vdfc.getMethodSignatureRefFactsList(), pfc.getMethodInvocationRefFactsList());
        smifc.startThread();
        
        StaticMethodInvocationsFactsConverter staticmifc = new StaticMethodInvocationsFactsConverter(id, vdfc.getMethodSignatureRefFactsList(), pfc.getMethodInvocationRefFactsList());
        staticmifc.startThread();
        
        VirtualMethodInvocationsFactsConverter vmifc = new VirtualMethodInvocationsFactsConverter(id, vdfc.getvarFactsList(), vdfc.getMethodSignatureRefFactsList(), pfc.getMethodInvocationRefFactsList() );
        vmifc.startThread();
        
        AssignmentsFactsConverter afc = new AssignmentsFactsConverter(id, hatfc.getHeapAllocationRefFactsList(), vdfc.getvarFactsList(), pfc.getMethodInvocationRefFactsList(), vdfc.getMethodSignatureRefFactsList(), tfc.getTypeFactsList() );
        afc.startThread();
        
        FieldsFactsConverter ffc = new FieldsFactsConverter( id, vdfc.getvarFactsList(), vdfc.getMethodSignatureRefFactsList(), fdfc.getFieldSignatureRefFactsList() );
        ffc.startThread();
        
        ArrayFactsConverter arrayfc = new ArrayFactsConverter(id, vdfc.getMethodSignatureRefFactsList(), vdfc.getvarFactsList(), tfc.getTypeFactsList(), tfc.getArrayTypeFactsList());
        arrayfc.startThread();
        
        
        try {
            tdfc.getThread().join();
            mdasfc.getThread().join();
            rvfc.getThread().join();
            smifc.getThread().join();
            staticmifc.getThread().join();
            vmifc.getThread().join();
            afc.getThread().join();
            ffc.getThread().join();
            arrayfc.getThread().join();
        }
        catch ( InterruptedException ex ) {
            ex.printStackTrace();
            System.out.println(ex.toString());
            System.exit(-1);
        }
        LibraryFactsConverter lfc = new LibraryFactsConverter(id, tfc.getTypeFactsList(), vdfc.getMethodSignatureRefFactsList(), mdasfc.getMethodDescriptorRefFactsList() , fdfc.getSimpleNameRefFactsList(), hatfc.getHeapAllocationRefFactsList(), vdfc.getvarFactsList() );
        lfc.parseLogicBloxFactsFile();
        lfc.createDatomicFactsFile();
        
    }
}
