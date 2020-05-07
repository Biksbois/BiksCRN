package simpleAdder.interpret.CompilerPhases;

import com.company.analysis.DepthFirstAdapter;
import com.company.node.*;
import java.util.Stack;

public class GenerateAST extends DepthFirstAdapter {
    String AST = "";
    Stack<String> TemptAST = new Stack<>();

    public String GetAST(){
        return "\\begin{figure}\n" +
                "    \\Tree " + AST + "\n" +
                "    \\caption{Sometimes my genius is almost frightening}\n" +
                "    \\label{fig:AST}\n" +
                "\\end{figure}";
    }

    public void outAInitializedclInit(AInitializedclInit node)
    {
        AddNode(node.getTInitializedcl());
    }

    public void outAIntegerFactor(AIntegerFactor node){
        String s = TemptAST.pop();
        TemptAST.push(GetNode("Int", s));
    }

    public void outAVariableFactor(AVariableFactor node){
        AddNode("var");
    }

    public void outAOperatorExpression(AOperatorExpression node){
        String rhs = TemptAST.pop();
        String lhs = TemptAST.pop();

        if (node.getOperator().toString().trim().equals("+")){
            TemptAST.push(GetNode("Plus", lhs, rhs));
        }else{
            TemptAST.push(GetNode("Minus", lhs, rhs));
        }
    }

    public void outAIntNumber(AIntNumber node){
        AddNode("Assign", TemptAST);
    }

    public void caseTTInt(TTInt node){
        String t = " " + node.getText();
        TemptAST.push(t);
    }

    public void caseTTString(TTString node){
        AST = " " + node.getText().trim() + AST;
    }

    private void AddNode(Node node){
        String nString = node.toString().trim();
        AST = " [." + nString + AST + " ]";
    }

    private void AddNode(String name){
        AST = " [." + name + AST + " ]";
    }

    private void AddNode(String name, Stack<String> st){
        AST = " [." + name + AST + st.pop() + " ]";
    }

    private String GetNode(String name, String lhs, String rhs){
        return " [." + name + lhs + rhs + " ]";
    }

    private String GetNode(String name, String value){
        return " [." + name + " " + value + " ]";
    }
}
