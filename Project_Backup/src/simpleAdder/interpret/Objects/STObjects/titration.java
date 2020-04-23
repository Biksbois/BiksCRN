package simpleAdder.interpret.Objects.STObjects;

import java.util.List;

public class titration {

    public String Species = null;
    public String Timestep = null;

    public List<logicalExspression> LogicalExpr = null;
    public List<String> booleanOperator = null;

    public titration()
    {

    }

    public titration(String Ts, String spec)
    {
        this.Species = spec;
        this.Timestep = Ts;
    }


}
