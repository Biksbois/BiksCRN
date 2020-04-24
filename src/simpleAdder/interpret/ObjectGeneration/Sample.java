package simpleAdder.interpret.ObjectGeneration;

import com.company.node.PSample;
import com.sun.source.tree.Scope;
import javafx.util.Pair;
import simpleAdder.interpret.Objects.STObjects.reaction;
import simpleAdder.interpret.Objects.STObjects.titration;
import simpleAdder.interpret.Objects.protocolOperation;
import simpleAdder.interpret.SymbolTableType;
import simpleAdder.interpret.TypeCheckers.StackChecker;
import simpleAdder.interpret.TypeCheckers.TitrationChecker;
import simpleAdder.interpret.ViableVariable;

import javax.sql.RowSetReader;
import java.util.*;
/**
 Class for generating the python code for the sample
 */
public class Sample extends HelpMethods{

    int Level = 0; //Since the target code is python indents are important for the code to work, the integer level keeps track of how many indents shold be applied.
    String PrettySample =""; //This is the string that willl contain the generated code.

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
        PrettyResult += PM.ApplyTap(Level,GenerateContruct(sample))+"\n";
        Level++;
        PrettyResult += dict.GenerateDictionary(global,local, Level);
        PrettyResult += PM.ApplyTap(Level,GenerateDictEnd());
        PrettyResult += GenerateApplyTitration();
        PrettyResult += GenerateLocalVariables() + "\n";
        PrettyResult += GenerateGlobalTitVariables(local);
        PrettyResult += HelpGenerateEuler(local,Level,"") + "\n";
        PrettyResult += GenerateTits(CheckNullTit(local,vv.ADDMOL), CheckNullTit(local,vv.REMMOL)) + "\n";
        PrettyResult += GenerateAnimation(sample, TitrationExists(CheckNullTit(local,vv.ADDMOL), CheckNullTit(local,vv.REMMOL))) + "\n";
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
            PrettyResult += PM.ApplyTap(Level,type+i+"=0\n");
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
    private List<titration> CheckNullTit(HashMap<String, SymbolTableType> T, String key)
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

    /**
     Generates the python code for the titration mecanic*/
    private String GenerateTits(List<titration> AddTits, List<titration> RemTits)
    {
        if (AddTits == null && RemTits == null){
            return "";
        }
        String PrettyResult = PM.ApplyTap(Level,"def ApplyTitration(self,i):\n");
        PrettyResult += GenerateAddTit(AddTits);
        PrettyResult += GenerateRemTit(RemTits);
        return PrettyResult;
    }

    /**
     Generates the code for the addition titrations*/
    private String GenerateAddTit(List<titration> AddTits)
    {
        int i = 0;
        String PrettyResult = "";
        if (AddTits != null){
            Level++;

            for (titration tit : AddTits) {
                if (tit.LogicalExpr != null && tit.LogicalExpr.size() > 0){
                    PrettyResult += ConstructConditionalStatement(tit);
                    Level++;
                }
                PrettyResult += PM.ApplyTap(Level,"Result, self."+vv.ADDMOL+ i +" = self.TitAccumilationA(self, "+tit.Timestep+", self."+vv.ADDMOL+ i++ +")\n");
                PrettyResult += PM.ApplyTap(Level,"self.sample[\""+tit.Species+"\"][-1] = self.sample.get(\""+tit.Species+"\")[-1]+Result*1\n");
                Level -= tit.LogicalExpr != null ? 1 : 0;
                /*  a, self.AddMol0 = self.TitAccumilationA(self, 0.01, self.AddMol0)
                    self.sample["U"][-1] = self.sample.get("U")[-1]+a*10*/
            }
            Level--;
        }
        return PrettyResult;
    }

    /**
     generates the code for the remove titrations*/
    private  String GenerateRemTit(List<titration> RemTits)
    {
        String PrettyResult = "";
        int i = 0;
        if (RemTits != null){
            Level++;
            for (titration tit : RemTits) {
                if (tit.LogicalExpr != null){
                    PrettyResult += ConstructConditionalStatement(tit);
                    Level++;
                }
                PrettyResult += PM.ApplyTap(Level,"if self.sample.get(\""+tit.Species+"\")[-1]-1 <= 0:\n");
                Level++;
                PrettyResult += PM.ApplyTap(Level,"self.sample.get(\""+tit.Species+"\")[-1] = 0\n");
                Level--;
                PrettyResult += PM.ApplyTap(Level,"else:\n");
                Level++;
                //PrettyResult += PM.ApplyTap(Level,"self.sample[\""+tit.Species+"\"][-1] = self.sample.get(\""+tit.Species+"\")[-1]-self.TitAccumilationA(self, "+tit.Timestep+", self."+vv.REMMOL+ i++ +")*1\n");
                PrettyResult += PM.ApplyTap(Level,"Result, self."+vv.REMMOL+ i +" = self.TitAccumilationA(self, "+tit.Timestep+", self."+vv.REMMOL+ i++ +")\n");
                PrettyResult += PM.ApplyTap(Level,"self.sample[\""+tit.Species+"\"][-1] = self.sample.get(\""+tit.Species+"\")[-1]-Result*1\n");

                Level -= tit.LogicalExpr != null ? 1 : 0;
                Level--;
            }
            Level--;
        }
        return PrettyResult;
    }

    /**
     Construct the conditional statement for the titrations*/
    private String ConstructConditionalStatement(titration tit)
    {
        String PrettyResult = "";
        PrettyResult += PM.ApplyTap(Level,"if ");
        PrettyResult += GenerateLogicalExpression(tit);
        PrettyResult += ":\n";
        return PrettyResult;
    }

    /**
     Generates a logical exsprression*/
    private String GenerateLogicalExpression(titration tit)
    {
        String PrettyResult = "";
        if(tit.LogicalExpr != null)
        {
            for (int i = 0; i < tit.LogicalExpr.size();i++)
            {
                if(i == 0)
                {
                    PrettyResult += "";
                }
                PrettyResult +=CheckValue(tit.LogicalExpr.get(i).lhsExrp) + tit.LogicalExpr.get(i).logicalOperator +CheckValue(tit.LogicalExpr.get(i).rhsExrp);
                if(tit.booleanOperator != null && tit.booleanOperator.size() > i)
                {
                    PrettyResult += TransBool(tit.booleanOperator.get(i));
                }
            }
        }
        return PrettyResult;
    }

    /**Checks if a String value is variable name*/
    private String CheckValue(String str)
    {
        return Character.isDigit(str.charAt(0)) ? str : "self.sample.get(\""+str+"\")[-1]";
    }

    /**
     * Trnaslates the logical operator thier python equavalent*/
    private String TransBool(String bool)
    {
        switch (bool){
            case "&&": return " and ";
            case "||": return  " or ";
            default: return "";
        }

    }
}
