package simpleAdder.interpret.Objects.CodeGenerationOBJ;

import simpleAdder.interpret.GetMethods.ViableVariable;
import simpleAdder.interpret.Objects.SymolTableOBJ.titration;
import simpleAdder.interpret.Objects.SymolTableOBJ.protocolOperation;
import simpleAdder.interpret.Objects.SymolTableOBJ.SymbolTableType;

import java.util.*;
/**
 Class for generating the python code for the sample
 */
public class Sample extends CodeGenerationMethods {

    int Level = 0; //Since the target code is python indents are important for the code to work, the integer level keeps track of how many indents shold be applied.
    String PrettySample =""; //This is the string that will contain the generated code.
    Dict dict = new Dict();
    Euler euler = new Euler();
    Titration titration = new Titration();

    public String GenerateSample(HashMap<String, SymbolTableType> global, Stack<protocolOperation> stack, String sample)
    {
        HashMap<String,SymbolTableType> local = global.get(sample).scope;
        ResetGlobalValues(); //Reset global value in the code, since the same instance is used more than once.
        PrettySample += GenerateSampleCode(global,local,sample); //generates the python code for sample.
        return PrettySample;
    }

    private String GenerateSampleCode(HashMap<String, SymbolTableType> global, HashMap<String, SymbolTableType> local, String sample)
    {
        String PrettyResult = "";
        PrettyResult += ApplyTab(Level,GenerateContruct(sample))+"\n";
        Level++;
        PrettyResult += dict.GenerateDictionary(global,local, Level);
        PrettyResult += ApplyTab(Level,GenerateDictEnd()) + "\n";
        PrettyResult += GenerateApplyTitration() + "\n";
        PrettyResult += GenerateLocalVariables() + "\n\n";
        PrettyResult += GenerateGlobalTitVariables(local);
        PrettyResult += euler.Generate(local,Level,"");
        PrettyResult += titration.Generate(CheckNullTitra(local, ViableVariable.ADDMOL), CheckNullTitra(local, ViableVariable.REMMOL), Level) + "\n";
        PrettyResult += GenerateAnimation(sample) + "\n";
        Level--;
        return PrettyResult;
    }

    private String GenerateGlobalTitVariables(HashMap<String, SymbolTableType> local)
    {
        String PrettyResult = "";
        if(local.containsKey(ViableVariable.ADDMOL))
        {
            PrettyResult += GenerateTitName(ViableVariable.ADDMOL, local.get(ViableVariable.ADDMOL).titrations) + "\n";
        }
        if(local.containsKey(ViableVariable.REMMOL))
        {
            PrettyResult += GenerateTitName(ViableVariable.REMMOL, local.get(ViableVariable.REMMOL).titrations) + "\n";
        }
        return PrettyResult;

    }

    private String GenerateTitName(String type, List<titration> titrations)
    {
        String PrettyResult = "";
        for (int i = 0; i < titrations.size();i++)
        {
            PrettyResult += ApplyTab(Level,type+i+"=0\n");
        }
        return PrettyResult;
    }

    private String GenerateApplyTitration()
    {
        return  "    def AccTitration(self, act, time):\n" +
                "        time += self.h\n" +
                "        if(act <= time):\n" +
                "            result = math.floor(time/act)\n" +
                "            time = time - act * result\n" +
                "            return result, time\n" +
                "        else:\n" +
                "            return 0, time";
    }

    /**
     Resets the global values in the class*/
    private void ResetGlobalValues()
    {
        PrettySample = "";
        Level = 0;
    }
    /**
     Generates the code used to animate the graph*/
    private String GenerateAnimation(String s)
    {
        return  "    @staticmethod\n" +
                "    def Animate(i) :\n" +
                "        plt.cla()\n" +
                "        for i in range(" + GetSampleName(s) + ".bitesize):\n" +
                "            index = next(" + GetSampleName(s) + ".index)\n" +
                "            if len(" + GetSampleName(s) + ".stepList) == 0:\n" +
                "                " + GetSampleName(s) + ".stepList.append(index*" + GetSampleName(s) + ".h)\n" +
                "            else:\n" +
                "                " + GetSampleName(s) + ".stepList.append(index*" + GetSampleName(s) + ".h)\n" +
                "                " + GetSampleName(s) + ".Euler(" + GetSampleName(s) + ", index)\n" +
                "                " + GetSampleName(s) + ".ApplyTitration(" + GetSampleName(s) + ")\n" +
                "                if(index >= " + GetSampleName(s) + ".steps):\n" +
                "                    " + GetSampleName(s) + ".stepList.pop()\n" +
                "                    break\n" +
                "\n" +
                "        DrawGraph(" + GetSampleName(s) + ".sample, " + GetSampleName(s) + ".stepList, \"" + s + "\", index, " + GetSampleName(s) + ".steps)";
        /*
        return  "    @staticmethod\n" +
                "    def Animate(i) :\n" +
                "        plt.cla()\n" +
                "\n" +
                "        index = next(" + GetSampleName(s) + ".index)\n" +
                "        \n" +
                "        if(index < " + GetSampleName(s) + ".steps):\n" +
                "            " + GetSampleName(s) + ".stepList.append(index*" + GetSampleName(s) + ".h)\n" +
                "\n" +
                "        DrawGraph(" + GetSampleName(s) + ".sample, " + GetSampleName(s) + ".stepList, \"A\", index, " + GetSampleName(s) + ".steps)\n" +
                "\n" +
                "        if(index+1 < " + GetSampleName(s) + ".steps):\n" +
                "            " + GetSampleName(s) + ".Euler(" + GetSampleName(s) + ", index+1)\n" +
                "            " + GetSampleName(s) + ".ApplyTitration(" + GetSampleName(s) + ", index+1)\n";

         */
    }

    /**
     * checks if a specefic Titration is null
     */
    private List<titration> CheckNullTitra(HashMap<String, SymbolTableType> T, String key)
    {
        return T.get(key) == null ? null: T.get(key).titrations;
    }

    /**
     Generates the sample class heading*/
    private String GenerateContruct(String sampleName)
    {
        return "class Sample"+sampleName+"():";
    }

    /**Generates the local variable */
    private String GenerateLocalVariables(){
        return  "\n    stepList = []\n" +
                "    index = count()\n" +
                "    steps = 100\n" +
                "    h = 0.0025\n" +
                "    bitesize = 1\n";
    }

    /**
     Generates the ending charaters for a dictionary notation*/
    private String GenerateDictEnd()
    {
        return "}\n";
    }
}
