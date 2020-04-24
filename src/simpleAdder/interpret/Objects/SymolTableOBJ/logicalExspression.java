package simpleAdder.interpret.Objects.SymolTableOBJ;

import java.util.Stack;

public class logicalExspression {

    public Stack<String> rhs;
    public Stack<String> lhs;

    public String logicalOperator;

    public logicalExspression(String logicalOp, Stack<String> lhs, Stack<String> rhs)
    {
        this.logicalOperator = logicalOp;
        this.lhs = rhs;
        this.rhs = lhs;
    }
}
