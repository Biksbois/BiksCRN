package simpleAdder.interpret.TypeCheckers;

import java.util.HashMap;
import java.util.Stack;

public class BetaStackToString extends Checker {


    /***
     * This recursive method, will take a stack and get the rate depending on the order of the content of
     * the stack. If the method encounters a end parantese, it implies that the method contains both a left
     * and a righthand rate, and therefore calls the method a second time to retrieve the righthand value.
     * The method knows that it is done retrieving the rates, when it reaches a start parantese or if the
     * stack is empty.
     * @Param stack
     * @return
     */

    public String StackToString(Stack<String> stack){
        if (stack.size() == 1){
            return stack.pop();
        }else{
            return Run(stack);
        }
    }

    public String Run(Stack<String> stack){
        String results = "";
        String rhs = "";
        String lhs = "";
        String sym = "";
        Stack<String> numbers = new Stack<>();
        while (!stack.isEmpty()){
            if (stack.peek().equals(")")){
                lhs = stack.pop();
                rhs = Run(stack);
                results += rhs + lhs;
            }else if(stack.peek().equals("(")){
                return numbers.isEmpty() ?  stack.pop() + results : stack.pop() + numbers.pop() + results;
            }else if(IsMultDivide(stack.peek())){
                sym = stack.pop();
                rhs = GetNext(stack);
                lhs = GetNext(stack);
                results += lhs + sym + rhs;
            }else if(IsPlusMinus(stack.peek())){
                sym = stack.pop();
                lhs = GetNext(stack);
                if (!numbers.isEmpty()){
                    rhs = numbers.pop();
                    results = lhs + sym + rhs + results;
                }else if(results.equals("") || sym.equals("-") && stack.peek().equals("(")){
                    results = sym + lhs;
                }else{
                    results = lhs + sym + results;
                }
            }else{
                if (stack.peek().equals(vv.CYCLE)){
                    stack.pop();
                    numbers.push("(i*self.h)");
                }
                else{
                    numbers.push(stack.pop());
                }
            }
        }
        while (!numbers.isEmpty()){
            stack.push(numbers.pop());
        }
        return results;
    }

    /***
     * This recursive method, is used to get the specific rate of the reaction, it continues until the
     * stack is either empty, or if the encounters a end parantese. It checks if the rate is already
     * made, or if it includes divide, multiplication or if the number is powered. Otherwise it will continue
     * and see if it encounters a end parantese, which would emply, that a the would have to run the
     * StackToRate method.
     * @Param stack
     * @return
     */
    public String GetNext(Stack<String> stack, HashMap<String, HashMap<String, String>>... species){
        String next = "";
        String sym = "";
        while (!stack.isEmpty()){
            if (IsMultDivide(stack.peek())){
                sym = stack.pop();
                next = sym + GetNext(stack, species) + next;
            }else if(stack.peek().equals(")")){
                next += stack.pop();
                next = Run(stack) + next;
            }else if(next.length() > 0 && next.charAt(0) == '('){
                return next;
            }
            else if(stack.peek().equals("(")){
                return next + stack.pop();
            }
            else{
                if (stack.peek().equals(vv.CYCLE)){
                    stack.pop();
                    return "(i*self.h)" + next;
                }
                else{
                    return stack.pop() + next;
                }
            }
        }
        return next;
    }
    
    /***
     * Checks if the string is a minus or a plus.
     * @Param s
     * @return
     */
    private boolean IsPlusMinus(String s){
        return s.equals(vv.plus) || s.equals(vv.minus) ? true: false;
    }

    /***
     * Checks if the string is a multiplication, divide or powered.
     * @Param s
     * @return
     */
    private boolean IsMultDivide(String s){
        return s.equals(vv.mult) || s.equals(vv.div) || s.equals(vv.power) ? true: false;
    }

    public boolean IsOperator(String str){
        return IsMultDivide(str) || IsPlusMinus(str) || str.equals("(") || str.equals(")");
    }
}
