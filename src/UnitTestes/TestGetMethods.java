package UnitTestes;

import java.util.Stack;

public class TestGetMethods {
    public Stack<String> InitializedStack(){
        Stack<String> expr = new Stack<>();

        expr.push("1");
        expr.push("+");
        expr.push("2");
        expr.push("9");
        expr.push("8");
        expr.push("2");
        expr.push("2");
        expr.push("2");
        expr.push("**");
        expr.push("*");
        expr.push("/");
        expr.push("*");
        expr.push("/");
        expr.push("-");
        expr.push("(");
        expr.push("2");
        expr.push("+");
        expr.push("2");
        expr.push(")");
        expr.push("+");
        expr.push("2");
        expr.push("2");
        expr.push("**");
        expr.push("+");
        expr.push("2");
        expr.push("(");
        expr.push("3");
        expr.push("-");
        expr.push("1");
        expr.push(")");
        expr.push("**");
        expr.push("+");
        expr.push("(");
        expr.push("1");
        expr.push("-");
        expr.push(")");

        return expr;
    }

    private Stack<String> StringToStack(String input){
        String[] expr = input.split(" ");
        Stack<String> stack = new Stack<>();
        for (int i = 0; i < expr.length; i++){
            stack.push(expr[i]);
        }
        return stack;
    }

    public Stack<String> GetPlusMinusStack(boolean ContainCycle){
        String extra = "";
        if (ContainCycle){
            extra = "cycle";
        }else{
            extra = "3";
        }
        String expr = "1 + 2 - " + extra + " - 4 + 5 + ( - 5 )";
        return StringToStack(expr);
    }

    public Stack<String> GetMultDivide(boolean ContainCycle){
        String extra = "";
        if (ContainCycle){
            extra = "cycle";
        }else{
            extra = "3";
        }
        String expr = "2 32 ( 1 - ) 9 * * / + 23 " + extra + " 0 * * - ( 2 - ) ( 2 - ) * + " + extra +" " + extra + " **";
        return StringToStack(expr);
    }

    public Stack<String> GetPower(boolean ContainCycle){
        String extra = "";
        if (ContainCycle){
            extra = "cycle";
        }else{
            extra = "3";
        }
        String expr = "1 2 ** - " + extra + " ( 4 + 5 ) **";
        return StringToStack(expr);
    }

    public Stack<String> GetOutWeigh(){
        String expr = "2 2 ** 3 12 cycle 13 / * / * - 1 cycle 13 / * + 42";
        return StringToStack(expr);
    }
}
