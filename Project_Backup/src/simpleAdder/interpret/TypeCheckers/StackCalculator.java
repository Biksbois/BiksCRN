package simpleAdder.interpret.TypeCheckers;

import javafx.util.Pair;
import simpleAdder.interpret.ViableVariable;
import java.util.Stack;

public class StackCalculator extends Checker {

    /***
     * Calculates an input stack
     * @param stack
     * @return
     */
    public String Run(Stack<String> stack){
        if (stack == null){
            TH.terminate_program("The stack is null, handle this (StackCalculator.Run)");
        }
        Stack<String> calc = ReverseStack(stack);
        String result = GetResult(calc);
        //System.out.println(result + " is the result");
        return result.equals("-0.0") ? "0.0" : result;
    }

    /***
     * Pops from the input stack untill it is empty. Each time it is popped, it is either a number
     * or a symbol.
     * When plus or minus: righthand and lefthandisde are combined
     * When divide or mult: it is added the the active stac, either left or right hand side
     * When power: the active stack is popped twice and calcualted
     * When parentheses: the GetResult is called recursively until the end parentehse
     * When number: it is added to the active stack
     * @param calc
     * @return
     */
    private String GetResult(Stack<String> calc){
        Stack<Float> RHS = new Stack<>();
        Stack<Float> LHS = new Stack<>();
        String lastOperator = "";
        Boolean UseLeft = true;
        Pair<Boolean, String> pair;
        while(!calc.isEmpty()){
            if (IsSymbol(calc.peek().trim())){
                pair = ApplySymbol(calc.pop().trim(), LHS, RHS, UseLeft, lastOperator);
                UseLeft = pair.getKey();
                lastOperator = pair.getValue();
            }else if (calc.peek().trim().equals("(")){
                calc.pop();
                Float temp = Float.parseFloat(GetResult(calc));
                if (calc.isEmpty() || calc.peek().trim().equals(vv.power)){
                    LHS.push(temp);
                }else if (LHS.size() == 0){
                    LHS.push(temp);
                }else{
                    RHS.push(temp);
                }
            }else if(calc.peek().trim().equals(")")){
                calc.pop();
                return FindResult(LHS, RHS, UseLeft, lastOperator);
            }else{
                ApplyNumber(calc.pop().trim(), LHS, RHS, UseLeft);
            }
        }
        return FindResult(LHS, RHS, UseLeft, lastOperator);
    }

    /***
     * When the input stack is empty the lhs and rhs stacks are handled using the lastOperator or just the left stack.
     * @param LHS
     * @param RHS
     * @param UseLeft
     * @param lastOperator
     * @return
     */
    private String FindResult( Stack<Float> LHS, Stack<Float> RHS, Boolean UseLeft, String lastOperator){
        if (UseLeft){
            Float f = LHS.pop();
            return f.toString();
        }
        else{
            return lastOperator.equals(vv.plus) ? PlusResult(LHS, RHS) : MinusResult(LHS, RHS);
        }
    }

    /***
     * If the last operator was plus, add the two remaining values in the two stacks
     * @param LHS
     * @param RHS
     * @return
     */
    private String PlusResult(Stack<Float> LHS, Stack<Float> RHS){
        try {
            Float r = RHS.isEmpty() ? LHS.pop() : RHS.pop();
            Float l = LHS.isEmpty() ? RHS.pop() : LHS.pop();
            Float result = l + r;
            //System.out.println(l + " + " + r + " = " + result + " - PlusResult");
            return result.toString();
        }catch (Exception E){
            TH.terminate_program("Stacks was null (PlusResult)");
        }
        return "";
    }

    /***
     * If the last operator was minus, add the two remaining values in the two stacks
     * @param LHS
     * @param RHS
     * @return
     */
    private String MinusResult(Stack<Float> LHS, Stack<Float> RHS){
        Float result = null;
        if (RHS.isEmpty()){
            result = LHS.pop() * (-1);
        }else{
            try {
                result = LHS.pop() - RHS.pop();
            }catch (Exception e){
                TH.terminate_program("Stack was null (MinusResult)");
            }
        }
        return result.toString();
    }

    /***
     * Apply a number to the active stack
     * @param number
     * @param LHS
     * @param RHS
     * @param UseLeft
     */
    private void ApplyNumber(String number, Stack<Float> LHS, Stack<Float> RHS, Boolean UseLeft){
        if (UseLeft){
            LHS.push(Float.parseFloat(number));
        }
        else{
            RHS.push(Float.parseFloat(number));
        }
    }

    /***
     * Apply a symbol to either the left or rigtht stac, depending on the symbol and what the active stack is.
     * @param symbol
     * @param LHS
     * @param RHS
     * @param UseLeft
     * @param lastOperator
     * @return
     */
    private Pair<Boolean, String> ApplySymbol(String symbol, Stack<Float> LHS, Stack<Float> RHS, Boolean UseLeft, String lastOperator){
        float result;

        if (vv.mult.equals(symbol)){
            if (UseLeft && LHS.size() > 1) {
                LHS = MultiStack(LHS);
            } else if(UseLeft){
                //System.out.print(LHS.peek() + " * " + RHS.peek() + " = ");
                Float ff = LHS.pop() * RHS.pop();
                LHS.push(ff);
                //System.out.println(ff + " - LHS * RHS");
            }
            else{
                RHS = MultiStack(RHS);
            }
        }else if (vv.div.equals(symbol)){
            if (UseLeft && LHS.size() > 1) {
                LHS = DivideStack(LHS);
            }else if(UseLeft){
                //System.out.print(LHS.peek() + " / " + RHS.peek() + " = ");
                Float aa = LHS.pop() / RHS.pop();
                LHS.push(aa);
                //System.out.println(aa + " - LHS / RHS");
            }
            else{
                RHS = DivideStack(RHS);
            }
        }else if (vv.power.equals(symbol)){
            if (UseLeft){
                LHS = PowerStack(LHS);
            }else{
                RHS = PowerStack(RHS);
            }
        }else{
            if (UseLeft){
                UseLeft = !UseLeft;
                lastOperator = symbol.equals(vv.plus) ? vv.plus : vv.minus;
            }
            else {
                if (lastOperator.equals(vv.plus)){
                    //System.out.print(LHS.peek() + " + " + RHS.peek() + " = ");
                    result = LHS.pop() + RHS.pop();
                    LHS.push(result);
                    //System.out.println(result + " - LHS + RHS");
                }
                else{
                    if (RHS.isEmpty()){

                    }
                    else{
                        //System.out.print(LHS.peek() + " - " + RHS.peek() + " = ");
                        result = LHS.pop() - RHS.pop();
                        LHS.push(result);
                        //System.out.println(result + " - LHS - RHS");
                    }
                }
                lastOperator = symbol.equals(vv.plus) ? vv.plus : vv.minus;
            }
        }
        return new Pair<>(UseLeft, lastOperator);
    }

    /***
     * Apply the power operation to a stack
     * @param stack
     * @return
     */
    private Stack<Float> PowerStack(Stack<Float> stack){
        Float L = stack.pop();
        Float R = stack.pop();
        Float result = (float) Math.pow(R, L);
        stack.push(result);
        //System.out.println(R + " ^ " + L + " = " + result + " - PowerStack");
        return stack;
    }

    /***
     * Apply multiply to a stack
     * @param stack
     * @return
     */
    public Stack<Float> MultiStack(Stack<Float> stack){
        Float L = stack.pop();
        Float R = stack.pop();
        Float result = L * R;
        stack.push(result);
        //System.out.println(R + " * " + L + " = " + result + " - MultiStack");
        return stack;
    }

    /***
     * Apply divide to a stack
     * @param stack
     * @return
     */
    public Stack<Float> DivideStack(Stack<Float> stack){
        Float R = stack.pop();
        Float L = stack.pop();
        Float result = L / R;
        stack.push(result);
        //System.out.println(L + " / " + R + " = " + result + " - DivideStack");
        return stack;
    }

    /***
     * Chekc if a string parameter is a symbol
     * @param s
     * @return
     */
    private Boolean IsSymbol(String s){
        return CheckSym(s, vv.mult) || CheckSym(s, vv.div) || CheckSym(s, vv.plus) || CheckSym(s, vv.minus) ||CheckSym(s, vv.power);
    }

    private Boolean CheckSym(String string, String sym){
        return sym.equals(string);
    }

    private <E> Stack<E> ReverseStack(Stack<E> entries) {
        Stack<E> temp = new Stack<E>();
        while (!entries.empty()) {
            temp.push(entries.pop());
        }
        return temp;
    }
}
