package simpleAdder.interpret.TypeCheckers;

import simpleAdder.interpret.ViableVariable;

public class Check {
    public StackChecker StackType = new StackChecker();
    public ViableVariable vv = new ViableVariable();
    public ListChecker ListType = new ListChecker();
    public FuncChecker FuncType = new FuncChecker();
    public ValueChecker ValueType = new ValueChecker();
    public TitrationChecker TitType = new TitrationChecker();
}
