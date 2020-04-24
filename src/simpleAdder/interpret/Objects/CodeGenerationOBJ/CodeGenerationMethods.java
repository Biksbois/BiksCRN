package simpleAdder.interpret.Objects.CodeGenerationOBJ;

import simpleAdder.interpret.TypeCheckers.BetaStackToString;
import simpleAdder.interpret.GetMethods.ViableVariable;

public class CodeGenerationMethods {
    ViableVariable vv = new ViableVariable();

    /***
     * Takes a string and adds indents to the string. Depending on the number that are
     * stored in the indent int. It will then return a string, with the specified tabs.
     * @Param indent
     * @Param OutputStr
     * @return
     */
    public String ApplyTap(int indent, String OutputStr)
    {
        for(int i = 0; i < indent; i++)
        {
            OutputStr = "    " + OutputStr;
        }
        return OutputStr;
    }
    /***
     * Used to generate the unique sample named, depending on the name of the specific sample.
     * @Param sample
     * @return
     */
    public String GetSampleName(String sample){
        return "Sample" + sample;
    }
}
