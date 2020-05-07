package simpleAdder.interpret.TypeCheckers;

import simpleAdder.interpret.GetMethods.ViableVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class OptimizedStackToString {
    ViableVariable vv = new ViableVariable();

    public String Calculate(Stack<String> stack){
        List<String> lResult = new ArrayList<>();
        String temp = "";
        float fResult = 0;
        Stack<String> numbers = new Stack<>();
        Stack<String> operators = new Stack<>();
        while (!stack.isEmpty()){
            if (IsPlusMinus(stack.peek())){
                temp = ApplyPlusMinus(operators, numbers, stack);
                if (temp.contains("i")){
                    lResult.add(temp);
                }else{
                    fResult += Float.parseFloat(temp);
                }
                temp = "";
            }else if(IsMultDivide(stack.peek())){
                operators.push(stack.pop());
            }else if(stack.peek().equals(vv.power)){
                numbers.push(ApplyPower(stack));
            }else if(stack.peek().equals(")")){
                stack.pop();
                numbers.push(Calculate(stack));
            }else if(stack.peek().equals("(")){
                stack.pop();
                break;
            }else if(stack.peek().equals(vv.CYCLE)){
                numbers.push("i");
                stack.pop();
            }
            else{
                numbers.push(stack.pop());
            }
        }

        if (!operators.isEmpty()){
            temp = ApplyPlusMinus(operators, numbers, stack);
            if (temp.contains("i")){
                lResult.add(temp);
            }else {
                fResult += Float.parseFloat(temp);
            }
            temp = "";
        }else if (!numbers.isEmpty()){
            if (numbers.peek().contains("i")){
                lResult.add(numbers.pop());
                temp = "";
            }else {
                fResult += Float.parseFloat(numbers.pop());
            }
        }

        if (fResult == 0){
            return ListToString(lResult).substring(1);
        }else {
            return fResult + ListToString(lResult);
        }
    }

    private String ListToString(List<String> lResult) {
        if(lResult.size() == 0){
            return "";
        }

        String result = "";
        for (int i = 0; i < lResult.size(); i++){
            for (int j = i+1; j < lResult.size(); j++){
                if (OutWeight(lResult.get(i), lResult.get(j))){
                    lResult.remove(j);
                    lResult.remove(i);
                    if (i > 0){
                        i--;
                        j--;
                    }
                }
            }
        }
        for (String s : lResult){
            result += "+" + s;
        }
        return result;
    }

    private boolean OutWeight(String s1, String s2) {
        if (IsNegative(s1) != IsNegative(s2) && AreEqual(s1, s2)){
            return true;
        }
        return false;
    }

    private boolean AreEqual(String s1, String s2) {
        if (IsNegative(s1)) {
            s1 = ParseNegativeString(s1);
        }else {
            s2 = ParseNegativeString(s2);
        }
        return s1.equals(s2) ? true : false;
    }

    private String ParseNegativeString(String s1) {
        return s1.substring(1, s1.length()-6);
    }

    private boolean IsNegative(String s) {
        if (s.length() > 7 && '(' == s.charAt(0) && ")*(-1)".equals(s.substring(s.length()-6))){
            return true;
        }
        return false;
    }

    private String ApplyPower(Stack<String> expr){
        expr.pop();

        String rhs = GetNext(expr);
        String lhs = GetNext(expr);

        if (rhs.contains("i") || lhs.contains("i")){
            return lhs + "**" + "(" + rhs + ")";
        }else {
            return String.valueOf(Math.pow(Float.valueOf(lhs), Float.valueOf(rhs)));
        }
    }

    private String GetNext(Stack<String> expr){
        if (expr.peek().equals(")")){
            expr.pop();
            return Calculate(expr);
        }else if(expr.peek().equals(vv.CYCLE)){
            expr.pop();
            return "i";
        }
        else{
            return expr.pop();
        }
    }

    private String ApplyPlusMinus(Stack<String> operators, Stack<String> numbers, Stack<String> expr){
        String lhs = "";
        String symLHS = expr.isEmpty() ? vv.plus : IsPlusMinus(expr.peek()) ? expr.pop() : vv.plus;

        if (numbers.size() == 0){
            if (expr.peek().equals(vv.CYCLE)){
                lhs = "i";
                expr.pop();
            }else {
                lhs = expr.pop();
            }
        }else if (operators.size() == 0){
            lhs = numbers.pop();
        }else {
            operators = ReverseStack(operators);
            lhs = ApplyMultDivide(numbers, operators);
        }

        return ApplySymbol(symLHS, lhs);
    }

    private String ApplyMultDivide(Stack<String> numbers, Stack<String> operators){
        float result = 0;
        Boolean CycleMet = false;

        if (numbers.peek().equals("i")){
            return StackToString(numbers, operators);
        }else{
            result = Float.valueOf(numbers.pop());
        }

        while (!numbers.isEmpty() && !CycleMet){
            if (operators.peek().equals(vv.mult)){
                if (numbers.peek().contains("i")){
                    CycleMet = true;
                }else{
                    result *= Float.valueOf(numbers.pop());
                    operators.pop();
                }
            }else if(operators.peek().equals(vv.div)){
                if (numbers.peek().equals("i")){
                    CycleMet = true;
                }else {
                    result /= Float.valueOf(numbers.pop());
                    operators.pop();
                }
            }
        }

        if (CycleMet){
            return result + operators.pop() + StackToString(numbers, operators);
        }else {
            return String.valueOf(result);
        }
    }

    private String StackToString(Stack<String> numbers, Stack<String> operators) {
        String result = "";

        while (!numbers.isEmpty()){
            result += numbers.pop() + (operators.isEmpty() ? "" : operators.pop());
        }

        return result;
    }

    private String ApplySymbol(String sym, String value){
        if (value.contains("i")){
            return sym.equals(vv.minus) ? "(" + value + ")*(-1)" : value;
        }
        return sym.equals(vv.minus) ? "-" + value : value;
    }

    public boolean IsMultDivide(String s){
        return s.equals(vv.mult) || s.equals(vv.div) ? true: false;
    }

    public boolean IsPlusMinus(String s){
        return s.equals(vv.plus) || s.equals(vv.minus) ? true: false;
    }

    public <E> Stack<E> ReverseStack(Stack<E> entries) {
        Stack<E> temp = new Stack<E>();
        while (!entries.empty()) {
            temp.push(entries.pop());
        }
        return temp;
    }
}
