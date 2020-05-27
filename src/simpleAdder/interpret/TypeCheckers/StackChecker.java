package simpleAdder.interpret.TypeCheckers;

import java.math.BigDecimal;
import java.util.Stack;

public class StackChecker extends Checker {
    OptimizedStackToString CALC = new OptimizedStackToString();

    /***
     * Calculates the value of a expression as a stack
     * @param Stack
     * @return
     */
    public String Calculate(Stack<String> Stack){ //TODO obselete
        Stack = copystack(Stack);
        return String.valueOf(CALC.Calculate(Stack));
    }

    private Stack<String> copystack(Stack<String> stack) {
        Stack<String> returnStack = new Stack<>();
        while (!stack.isEmpty())
        {
            if(stack.peek().contains("-") && !(stack.peek().charAt(0) == '-'))
            {
                BigDecimal d = new BigDecimal(stack.pop());

                returnStack.push(d.toPlainString());
            }
            else
                {
                    returnStack.push(stack.pop());
                }
        }
        return ReverseStack(returnStack);
    }

    public <E> Stack<E> ReverseStack(Stack<E> entries) {
        Stack<E> temp = new Stack<E>();
        while (!entries.empty()) {
            temp.push(entries.pop());
        }
        return temp;
    }



}
