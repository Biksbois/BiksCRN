package simpleAdder.interpret.Objects.SymolTableOBJ;

import simpleAdder.interpret.TypeCheckers.BiksPair;
import simpleAdder.interpret.TypeCheckers.PossibleToAdd;

import java.util.Stack;

public class PrimitiveType {
    private final String ID;
    public String type;
    private final Stack<String> stack = new Stack<>();
    private String result;

    private final Boolean CanBeNegative;
    private final Boolean CanHaveDecimals;

    private final PossibleToAdd ps;

    public PrimitiveType(String ID, String type, Boolean AddInt, Boolean AddFloat, Boolean AddSpecie, Boolean AddRate, Boolean CanBeNegative, Boolean CanHaveDecimals){
        this.ID = ID;
        this.type = type;

        this.CanBeNegative = CanBeNegative;
        this.CanHaveDecimals = CanHaveDecimals;

        ps = new PossibleToAdd(AddInt, AddFloat, AddSpecie, AddRate);
    }

    public void SymolToStack(String symbol){
        stack.push(symbol);
    }

    public void SetResult(String result){
        this.result = CanHaveDecimals ? result : result.split("\\.")[0];
    }

    public String GetErrormessage(){
        return type + " " +  ID + " is either null or negative, which is not allowed.";
    }

    public boolean AddToStack(String value, String type){
        if (ps.CanBeAdded(type)){
            stack.push(value);
            return true;
        }
        return false;
    }

    public Boolean ResultIsValid(){
        return IsValid();
    }

    private Boolean IsValid(){
        return !result.contains("-") || CanBeNegative;
    }

    public Stack<String> GetStack(){
        return stack;
    }

    public BiksPair<String, SymbolTableType> GetIntance(){
        return new BiksPair<>(ID, new SymbolTableType(ID, type, result));
    }
}
