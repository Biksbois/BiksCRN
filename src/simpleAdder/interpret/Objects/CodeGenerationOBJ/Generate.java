package simpleAdder.interpret.Objects.CodeGenerationOBJ;

import simpleAdder.interpret.Objects.SymolTableOBJ.protocolOperation;
import simpleAdder.interpret.Objects.SymolTableOBJ.SymbolTableType;

import java.util.HashMap;
import java.util.Stack;

public class Generate {

    Sample sample = new Sample();
    Protocol protocol = new Protocol();

    /*
     * This class function as a collection of the generation method.
    */

    public String Sample(HashMap<String, SymbolTableType> global, Stack<protocolOperation> stack, String key)
    {
        return sample.GenerateSample(global, stack, key);
    }

    public String Protocol(HashMap<String, SymbolTableType> global, Stack<protocolOperation> stack){
        return protocol.Generate(global, stack);
    }

}
