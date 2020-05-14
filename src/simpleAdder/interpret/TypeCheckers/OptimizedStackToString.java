package simpleAdder.interpret.TypeCheckers;

import simpleAdder.interpret.CompilerPhases.TerminateProgram;
import simpleAdder.interpret.GetMethods.ViableVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class OptimizedStackToString {
    ViableVariable vv = new ViableVariable();
    TerminateProgram TP = new TerminateProgram();

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
            }else if(stack.peek().equals(ViableVariable.power)){
                numbers.push(ApplyPower(stack));
            }else if(stack.peek().equals(")")){
                stack.pop();
                numbers.push(Calculate(stack));
            }else if(stack.peek().equals("(")){
                stack.pop();
                break;
            }else if(stack.peek().equals(ViableVariable.CYCLE)){
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
            String result = ListToString(lResult);
            if (result.equals("0")){
                return result;
            }else if(result.equals("")){
                return "0";
            }else{
                return result.substring(1);
            }
        }else {
            return fResult + ListToString(lResult);
        }
    }

    private String ListToString(List<String> lResult) {
        if(lResult.size() == 0){
            return "0";
        }

        String result = "";
        for (int i = 0; i < lResult.size(); i++){
            for (int j = i+1; j < lResult.size(); j++){
                if (OutWeight(lResult.get(i), lResult.get(j))){
                    lResult.remove(j);
                    lResult.remove(i);
                    if (i == 0){
                        i--;
                        j--;
                        break;
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
        return IsNegative(s1) != IsNegative(s2) && AreEqual(s1, s2);
    }

    private boolean AreEqual(String s1, String s2) {
        if (IsNegative(s1)) {
            s1 = ParseNegativeString(s1);
        }else {
            s2 = ParseNegativeString(s2);
        }
        return s1.equals(s2);
    }

    private String ParseNegativeString(String s1) {
        return s1.substring(1, s1.length()-6);
    }

    private boolean IsNegative(String s) {
        return s.length() > 7 && '(' == s.charAt(0) && ")*(-1)".equals(s.substring(s.length() - 6));
    }

    private String ApplyPower(Stack<String> expr){
        expr.pop();

        String rhs = GetNext(expr);
        String lhs = GetNext(expr);

        if (IsZero(lhs)){
            return "1";
        }else if(IsZero(rhs)){
            return "0";
        }else if(IsOne(lhs)){
            return "1";
        }else if(IsOne(rhs)){
            return lhs;
        }else if (rhs.contains("i") || lhs.contains("i")){
            return lhs + "**" + "(" + rhs + ")";
        }else {
            return String.valueOf(Math.pow(Float.valueOf(lhs), Float.valueOf(rhs)));
        }
    }

    private boolean IsOne(String val) {
        return !val.contains("i") && Float.parseFloat(val) == 1;
    }

    private String GetNext(Stack<String> expr){
        if (expr.peek().equals(")")){
            expr.pop();
            return Calculate(expr);
        }else if(expr.peek().equals(ViableVariable.CYCLE)){
            expr.pop();
            return "i";
        }
        else{
            return expr.pop();
        }
    }

    private String ApplyPlusMinus(Stack<String> operators, Stack<String> numbers, Stack<String> expr){
        String lhs = "";
        String symLHS = expr.isEmpty() ? ViableVariable.plus : IsPlusMinus(expr.peek()) ? expr.pop() : ViableVariable.plus;

        if (numbers.size() == 0){
            if (expr.peek().equals(ViableVariable.CYCLE)){
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
        String sRes = "";
        Boolean ZeroMet = false;
        Boolean UseResult = true;

        if (numbers.peek().contains("1") && operators.peek().equals(ViableVariable.mult)){
            operators.pop();
            numbers.pop();
        }
        if(numbers.peek().contains("i") ){
            sRes = AssignToResult(numbers, operators, result, false) + (operators.isEmpty() ? "" : operators.pop());
        }
        if (numbers.isEmpty()){
            return sRes;
        }else{
            result = Float.valueOf(numbers.pop());
            ZeroMet = result == 0;
        }

        while (!numbers.isEmpty() && !ZeroMet){
            if (IsZero(numbers.peek())){
                if(operators.peek().equals(ViableVariable.div)){
                    TP.terminate_program("Dividing by zero is not allowed.");
                }
                ZeroMet = true;
                EmptyStacks(numbers, operators);
            }else if(numbers.peek().contains("i")){
                sRes += AssignToResult(numbers, operators, result, true) + (operators.isEmpty() ? "" : operators.pop());
                if (!numbers.isEmpty()){
                    result = Float.valueOf(numbers.pop());
                    ZeroMet = result == 0;
                }else{
                    UseResult = false;
                }
            }
            else if (operators.peek().equals(ViableVariable.mult)){
                result *= Float.valueOf(numbers.pop());
                operators.pop();

            }else if(operators.peek().equals(ViableVariable.div)){
                result /= Float.valueOf(numbers.pop());
                operators.pop();
            }
        }

        if (ZeroMet){
            return "0";
        }else if (!UseResult){
            return sRes;
        }else {
            return sRes + (operators.isEmpty() ? "" : operators.pop()) + result;
        }
    }

    private void RemoveOneAndZero(Stack<String> operators, Stack<String> numbers) {
        while (!numbers.isEmpty()){
            /*
            if(IsZero(numbers.peek())){
                if (operators.peek().equals(vv.div)){
                    TP.terminate_program("Dividing by zero is not allowed.");
                }
                numbers.pop();
                PopOperator(operators);
            }else
             */
            if(IsOne(numbers.peek())){
                numbers.pop();
                PopOperator(operators);
            }else{
                break;
            }
        }
    }

    private String AssignToResult(Stack<String> numbers, Stack<String> operators, float result, boolean UseResult) {
        String sRes = "";
        if (!UseResult){
            sRes = numbers.pop();
        }
        else if (result == 0){
            sRes = numbers.pop();
            operators.pop();
        }else{
            sRes = result + operators.pop() + numbers.pop();
        }
        while (!numbers.isEmpty()){
            RemoveOneAndZero(operators, numbers);
            if (!numbers.isEmpty() && numbers.peek().contains("i")){
                sRes += operators.pop() + numbers.pop();
            }else{
                break;
            }
        }
        return sRes;
    }

    private void PopOperator(Stack<String> operators) {
        if (!operators.isEmpty()){
            operators.pop();
        }
    }

    private void EmptyStacks(Stack<String> numbers, Stack<String> operators) {
        operators.clear();
        numbers.clear();
    }

    private String StackToString(Stack<String> numbers, Stack<String> operators) {
        String result = "";
        Boolean ZeroMet = false;

        while (!numbers.isEmpty() && !ZeroMet){
            if (IsZero(numbers.peek())){
                if (DivideByZero(operators, result)){
                    TP.terminate_program("Dividing by zero is not allowed.");
                }
                EmptyStacks(numbers, operators);
                ZeroMet = true;
            }else{
                result += numbers.pop() + (operators.isEmpty() ? "" : operators.pop());
            }
        }

        if (ZeroMet){
            return "0";
        }else{
            return result;
        }
    }

    private boolean IsZero(String peek) {
        return !peek.contains("i") && Float.parseFloat(peek) == 0;
    }

    private boolean DivideByZero(Stack<String> operators, String result) {
        if (!operators.isEmpty() && operators.peek().equals(ViableVariable.div)){
            return true;
        }else return result != "" && ViableVariable.div.equals(result.substring(result.length() - 1));
    }


    private String ApplySymbol(String sym, String value){
        if (value.contains("i")){
            return sym.equals(ViableVariable.minus) ? "(" + value + ")*(-1)" : value;
        }
        return sym.equals(ViableVariable.minus) ? "-" + value : value;
    }

    public boolean IsMultDivide(String s){
        return s.equals(vv.mult) || s.equals(vv.div);
    }

    public boolean IsPlusMinus(String s){
        return s.equals(vv.plus) || s.equals(vv.minus);
    }

    public <E> Stack<E> ReverseStack(Stack<E> entries) {
        Stack<E> temp = new Stack<E>();
        while (!entries.empty()) {
            temp.push(entries.pop());
        }
        return temp;
    }
}
