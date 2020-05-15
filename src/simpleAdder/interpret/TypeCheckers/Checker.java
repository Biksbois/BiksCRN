package simpleAdder.interpret.TypeCheckers;


import simpleAdder.interpret.CompilerPhases.TerminateProgram;
import simpleAdder.interpret.GetMethods.ViableVariable;

import java.util.Stack;

public class Checker {
    public TerminateProgram TH = new TerminateProgram();
    public ViableVariable vv = new ViableVariable();

    /***
     * Checks if a given input string contains a letter.
     * Used to see if it is a variable
     * @param str
     * @return
     */
    public boolean CheckStringLetter(String str)
    {
        char[] charArr = str.toCharArray();
        for (char _char:charArr) {
            if(Character.isLetter(_char))
            {
                return true;
            }
        }
        return false;
    }

    private boolean IsPlusMinus(String s){
        return s.equals(vv.plus) || s.equals(vv.minus);
    }

    /***
     * Checks if the string is a multiplication, divide or powered.
     * @Param s
     * @return
     */
    private boolean IsMultDivide(String s){
        return s.equals(vv.mult) || s.equals(vv.div) || s.equals(vv.power);
    }

    public boolean IsOperator(String str){
        return IsMultDivide(str) || IsPlusMinus(str) || str.equals("(") || str.equals(")");
    }

    public <E> Stack<E> ReverseStack(Stack<E> entries) {
        Stack<E> temp = new Stack<E>();
        while (!entries.empty()) {
            temp.push(entries.pop());
        }
        return temp;
    }
}
