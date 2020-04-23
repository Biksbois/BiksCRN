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
}
