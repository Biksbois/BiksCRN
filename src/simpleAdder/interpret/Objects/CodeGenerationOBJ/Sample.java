package simpleAdder.interpret.Objects.CodeGenerationOBJ;

import simpleAdder.interpret.Objects.SymolTableOBJ.titration;
import simpleAdder.interpret.Objects.SymolTableOBJ.protocolOperation;
import simpleAdder.interpret.Objects.SymolTableOBJ.SymbolTableType;

import java.util.*;
/**
 Class for generating the python code for the sample
 */
public class Sample extends CodeGenerationMethods {

    int Level = 0; //Since the target code is python indents are important for the code to work, the integer level keeps track of how many indents shold be applied.
    String PrettySample =""; //This is the string that willl contain the generated code.
    Dict dict = new Dict();
    Euler euler = new Euler();
    Titration titration = new Titration();

    public String GenerateSample(HashMap<String, SymbolTableType> global, Stack<protocolOperation> stack, String sample)
    {
        HashMap<String,SymbolTableType> local = global.get(sample).scope;
        if (EmptySample(local)){
            return "";
        }
        ResetGlobalValues(); //Resests global value in the code, since the same instance is used more than once.
        PrettySample += GenerateSampleCode(global,local,sample); //generates the python code for sample.
        return PrettySample;
    }

    private String GenerateSampleCode(HashMap<String, SymbolTableType> global, HashMap<String, SymbolTableType> local, String sample)
    {
        String PrettyResult = "";
        PrettyResult += ApplyTap(Level,GenerateContruct(sample))+"\n";
        Level++;
        PrettyResult += dict.GenerateDictionary(global,local, Level);
        PrettyResult += ApplyTap(Level,GenerateDictEnd());
        PrettyResult += GenerateApplyTitration();
        PrettyResult += GenerateLocalVariables() + "\n";
        PrettyResult += GenerateGlobalTitVariables(local);
        PrettyResult += euler.Generate(local,Level,"");
        PrettyResult += titration.Generate(CheckNullTitra(local,vv.ADDMOL), CheckNullTitra(local,vv.REMMOL), Level);
        PrettyResult += GenerateAnimation(sample, TitrationExists(CheckNullTitra(local,vv.ADDMOL), CheckNullTitra(local,vv.REMMOL))) + "\n";
        Level--;
        return PrettyResult;
    }

    private String GenerateGlobalTitVariables(HashMap<String, SymbolTableType> local)
    {
        String PrettyResult = "";
        if(local.containsKey(vv.ADDMOL))
        {
            PrettyResult += GenerateTitName(vv.ADDMOL, local.get(vv.ADDMOL).titrations);
        }
        if(local.containsKey(vv.REMMOL))
        {
            PrettyResult += GenerateTitName(vv.REMMOL, local.get(vv.REMMOL).titrations);
        }
        return PrettyResult;

    }

    private String GenerateTitName(String type, List<titration> titrations)
    {
        String PrettyResult = "";
        for (int i = 0; i < titrations.size();i++)
        {
            PrettyResult += ApplyTap(Level,type+i+"=0\n");
        }
        return PrettyResult;
    }

    private String GenerateApplyTitration()
    {
        return  "    def TitAccumilationA(self, act, titra):\n" +
                "        titra += self.h\n" +
                "        if(act <= titra):\n" +
                "            result = math.floor(titra/act)\n" +
                "            titra = titra - act * result\n" +
                "            return result, titra\n" +
                "        else:\n" +
                "            return 0, titra";
    }

    /**
     Resets the global values in the class*/
    private void ResetGlobalValues()
    {
        PrettySample = "";
        Level = 0;
    }

    /**
     Checks if titaration exsists on the sample*/
    private boolean TitrationExists(List<titration> add, List<titration> rem){
        return (add == null && rem == null) ? false: true;
    }

    /**Checks if the sample is empty*/
    private boolean EmptySample(HashMap<String, SymbolTableType> sample){
        return sample.isEmpty() ? true:false;
    }

    /**
     Generates the code used to animate the graph*/
    private String GenerateAnimation(String s, boolean CallTitrationMethod)
    {
        return  "    @staticmethod\n" +
                "    def Animate(i) :\n" +
                "        plt.cla()\n" +
                "\n" +
                "        " + GetSampleName(s) + ".stepList.append(next(Sample"+s+".index)*"+GetSampleName(s)+".h)\n" +
                "\n" +
                "        for key, value in Sample"+s+".sample.items():\n" +
                "            plt.plot(Sample"+s+".stepList, value, label=key)\n" +
                "            plt.legend()\n" +
                "\n" +
                "        Sample"+s+".Euler(Sample"+s+", i)\n" +
                (CallTitrationMethod ? "        Sample"+s+".ApplyTitration(Sample"+s+", i+1)" : "") + "\n\n";

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
                "    h = 0.0025";
    }

    /**
     Generates the ending charaters for a dictionary notation*/
    private String GenerateDictEnd()
    {
        return "}\n";
    }
}
