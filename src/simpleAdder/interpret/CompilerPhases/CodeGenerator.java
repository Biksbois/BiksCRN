package simpleAdder.interpret.CompilerPhases;

import com.company.analysis.DepthFirstAdapter;
import com.company.node.*;
import simpleAdder.interpret.GetMethods.Get;
import simpleAdder.interpret.GetMethods.PreGeneratedPython;
import simpleAdder.interpret.Objects.CodeGenerationOBJ.Generate;
import simpleAdder.interpret.Objects.SymolTableOBJ.protocolOperation;
import simpleAdder.interpret.Objects.SymolTableOBJ.SymbolTableType;

import java.io.IOException;
import java.util.HashMap;
import java.util.Stack;

public class CodeGenerator extends DepthFirstAdapter {

    Stack<protocolOperation> protocols;
    HashMap<String, SymbolTableType> symbolTable;
    Generate generate = new Generate();
    Get get = new Get();
    String prettyPrint = "";

    /***
     * This method is called when the code generation is done, and the code is written into a python file.
     * It returns all the generated code.
     * @return
     */
    public String GetPython(){
        return prettyPrint;
    }

    public CodeGenerator(BetaTypeChecker TC) throws IOException {
        protocols = TC.st.protocols;
        symbolTable = TC.st.st;
        prettyPrint += generate.PremadePython();
    }

    /***
     * Each time a sample node is met while traversing the tree, it is generated using the symbol table
     * and the key
     * @param node
     */
    public void outASampleinitSample(ASampleinitSample node)
    {
        prettyPrint += generate.Sample(symbolTable, protocols, get.Id(node));
    }

    /***
     * Once all samples are generated, the protocol node are generated. This is done using the
     * stack of protocols created in the type checker
     * @param node
     */
    public void outADclProtocol(ADclProtocol node)
    {
        prettyPrint += generate.Protocol(symbolTable, protocols);
    }
}
