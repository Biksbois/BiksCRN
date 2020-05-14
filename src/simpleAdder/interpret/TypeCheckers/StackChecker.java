package simpleAdder.interpret.TypeCheckers;

import java.util.Stack;

public class StackChecker extends Checker {
    OptimizedStackToString CALC = new OptimizedStackToString();

    /***
     * Calculates the value of a expression as a stack
     * @param Stack
     * @return
     */
    public String Calculate(Stack<String> Stack){ //TODO obselete
        return String.valueOf(CALC.Calculate(Stack));
    }

    public <E> Stack<E> ReverseStack(Stack<E> entries) {
        Stack<E> temp = new Stack<E>();
        while (!entries.empty()) {
            temp.push(entries.pop());
        }
        return temp;
    }



}
