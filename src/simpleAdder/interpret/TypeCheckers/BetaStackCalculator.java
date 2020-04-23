package simpleAdder.interpret.TypeCheckers;

import simpleAdder.interpret.ViableVariable;

import java.util.Stack;

public class BetaStackCalculator {
    ViableVariable vv = new ViableVariable();

    public float Calculate(Stack<String> stack){
        float result = 0;
        Stack<Float> numbers = new Stack<>();
        Stack<String> operators = new Stack<>();
        while (!stack.isEmpty()){
            if (IsPlusMinus(stack.peek())){
                result += ApplyPlusMinus(operators, numbers, stack);
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
            }
            else{
                numbers.push(Float.parseFloat(stack.pop()));
            }
        }

        if (!operators.isEmpty()){
            result += ApplyPlusMinus(operators, numbers, stack);
        }else if (!numbers.isEmpty()){
            result += numbers.pop();
        }

        return result;
    }

    private float ApplyPower(Stack<String> expr){
        expr.pop();

        float rhs = GetNext(expr);
        float lhs =GetNext(expr);

        return (float) Math.pow(lhs, rhs);
    }

    private float GetNext(Stack<String> expr){
        if (expr.peek().equals(")")){
            expr.pop();
            return Calculate(expr);
        }else{
            return Float.parseFloat(expr.pop());
        }
    }

    private float ApplyPlusMinus(Stack<String> operators, Stack<Float> numbers, Stack<String> expr){
        float lhs;
        String symLHS = expr.isEmpty() ? vv.plus : expr.pop();

        if (numbers.size() == 0){
            lhs = Float.parseFloat(expr.pop());
        }else if (operators.size() == 0){
            lhs = numbers.pop();
        }else {
            operators = ReverseStack(operators);
            lhs = ApplyMultDivide(numbers, operators);
        }

        return ApplySymbol(symLHS, lhs);
    }

    private float ApplyMultDivide(Stack<Float> numbers, Stack<String> operators){
        float result = numbers.pop();
        while (!numbers.isEmpty()){
            if (operators.peek().equals(vv.mult)){
                result *= numbers.pop();
            }else if(operators.peek().equals(vv.div)){
                result /= numbers.pop();
            }
            operators.pop();
        }
        return result;
    }

    private float ApplySymbol(String sym, float value){
        return sym.equals(vv.minus) ? -value : value;
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
