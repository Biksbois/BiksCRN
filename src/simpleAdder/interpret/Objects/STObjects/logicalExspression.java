package simpleAdder.interpret.Objects.STObjects;

public class logicalExspression {

    public String rhsExrp;
    public String lhsExrp;

    public String logicalOperator;

    public logicalExspression(String logicalOp, String Rhs, String Lhs)
    {
        this.logicalOperator = logicalOp;
        this.lhsExrp = Lhs;
        this.rhsExrp = Rhs;
    }
}
