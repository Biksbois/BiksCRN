package simpleAdder.interpret.Objects.CodeGenerationOBJ;

import simpleAdder.interpret.GetMethods.ViableVariable;
import simpleAdder.interpret.Objects.SymolTableOBJ.titration;
import simpleAdder.interpret.TypeCheckers.Checker;

import java.util.List;
import java.util.Stack;

public class Titration extends CodeGenerationMethods{
    int Level = 0;
    Checker check = new Checker();

    /**
     Generates the python code for the titration mecanic*/
    public String Generate(List<titration> AddMol, List<titration> RemMol, int level)
    {
        Level = level;
        String PrettyResult = "";

        if (TitratoinNullOrZero(AddMol, RemMol)){
            return GenerateEmptyTitration("");
        }
        PrettyResult += ApplyTab(Level,"def ApplyTitration(self):\n");

        PrettyResult += GenerateAddMol(AddMol);
        PrettyResult += GenerateRemMol(RemMol);

        return PrettyResult + "\n";
    }

    /**
     Generates the python code for the titration mecanic*/
    public String Generate(List<titration> AddMol, List<titration> RemMol, int level, String name)
    {
        Level = level;
        String PrettyResult = "";

        if (TitratoinNullOrZero(AddMol, RemMol)){
            return GenerateEmptyTitration(name);
        }
        PrettyResult += ApplyTab(Level,"def ApplyTitration"+name+"(self,i):\n");
        PrettyResult += GenerateAddMol(AddMol);
        PrettyResult += GenerateRemMol(RemMol);
        return PrettyResult + "\n";
    }

    private Boolean TitratoinNullOrZero(List<titration> AddMol, List<titration> RemMol){
        if (AddMol == null && RemMol == null){
            return true;
        }
        else{
            return (AddMol == null || AddMol != null && AddMol.size() == 0) && (RemMol == null || RemMol != null && RemMol.size() == 0);
        }
    }

    private String GenerateEmptyTitration(String name){
        String PrettyResult = "";
        PrettyResult += ApplyTab(Level,"def ApplyTitration" + name + "(self):\n");
        Level++;
        PrettyResult += ApplyTab(Level, "pass\n");
        Level--;
        return PrettyResult;
    }

    /**
     Generates the code for the addition titrations*/
    private String GenerateAddMol(List<titration> AddTits)
    {
        int i = 0;
        String PrettyResult = "";
        if (AddTits != null){
            Level++;

            for (titration titra : AddTits) {
                if (titra.LogicalExpr != null && titra.LogicalExpr.size() > 0){
                    PrettyResult += ConstructConditionalStatement(titra);
                    Level++;
                }
                PrettyResult += ApplyTab(Level,"Result, self."+ ViableVariable.ADDMOL + i +" = self.AccTitration(self, "+titra.Timestep+", self."+ ViableVariable.ADDMOL + i++ +")\n");
                PrettyResult += ApplyTab(Level,"self.sample[\""+titra.Species+"\"][-1] = self.sample.get(\""+titra.Species+"\")[-1]+Result*1\n");
                Level -= titra.LogicalExpr != null ? 1 : 0;
            }
            Level--;
        }
        return PrettyResult;
    }

    /**
     generates the code for the remove titrations*/
    private  String GenerateRemMol(List<titration> RemTits)
    {
        String PrettyResult = "";
        int i = 0;
        if (RemTits != null){
            Level++;
            for (titration titra : RemTits) {
                if (titra.LogicalExpr != null){
                    PrettyResult += ConstructConditionalStatement(titra);
                    Level++;
                }
                PrettyResult += ApplyTab(Level,"Result, self."+ ViableVariable.REMMOL + i +" = self.AccTitration(self, "+titra.Timestep+", self."+ ViableVariable.REMMOL + i++ +")\n");
                PrettyResult += ApplyTab(Level,"if Result > 0 and self.sample.get(\""+titra.Species+"\")[-1]-1 <= 0:\n");
                Level++;
                PrettyResult += ApplyTab(Level,"self.sample.get(\""+titra.Species+"\")[-1] = 0\n");
                Level--;
                PrettyResult += ApplyTab(Level,"elif Result > 0:\n");
                Level++;
                PrettyResult += ApplyTab(Level,"self.sample[\""+titra.Species+"\"][-1] = self.sample.get(\""+titra.Species+"\")[-1]-Result*1\n");
                Level -= titra.LogicalExpr != null ? 1 : 0;
                Level--;
            }
            Level--;
        }
        return PrettyResult;
    }

    /**
     Construct the conditional statement for the titrations*/
    private String ConstructConditionalStatement(titration titra)
    {
        String PrettyResult = "";
        PrettyResult += ApplyTab(Level,"if ");
        PrettyResult += GenerateLogicalExpression(titra);
        PrettyResult += ":\n";
        return PrettyResult;
    }

    /**
     Generates a logical exsprression*/
    private String GenerateLogicalExpression(titration titra)
    {
        String PrettyResult = "";
        if(titra.LogicalExpr != null)
        {
            for (int i = 0; i < titra.LogicalExpr.size();i++)
            {
                if(i == 0)
                {
                    PrettyResult += "";
                }

                String lhs = StackToPython((Stack<String>) titra.LogicalExpr.get(i).lhs.clone());
                String rhs = StackToPython((Stack<String>) titra.LogicalExpr.get(i).rhs.clone());

                PrettyResult += lhs + titra.LogicalExpr.get(i).logicalOperator + rhs;

                if(titra.booleanOperator != null && titra.booleanOperator.size() > i)
                {
                    PrettyResult += TransBool(titra.booleanOperator.get(i));
                }
            }
        }
        return PrettyResult;
    }

    public String StackToPython(Stack<String> stack){
        Stack<String> result = new Stack<>();
        while (!stack.isEmpty()){
            result.push(CheckValue(stack.pop()));
        }
        return CALC.Calculate(check.ReverseStack(result));
    }

    /**Checks if a String value is variable name*/
    private String CheckValue(String str)
    {
        return Character.isDigit(str.charAt(0)) || check.IsOperator(str) ? str : "self.sample.get(\""+str+"\")[-1]";
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
