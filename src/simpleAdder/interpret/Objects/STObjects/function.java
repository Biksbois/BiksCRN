package simpleAdder.interpret.Objects.STObjects;
import javafx.util.Pair;
import simpleAdder.interpret.SymbolTableType;
import simpleAdder.interpret.TypeCheckers.Checker;
import simpleAdder.interpret.TypeCheckers.PossibleToAdd;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class function extends Checker {
    public List<parameter> parameters = new ArrayList<>();
    public String id;
    public Stack<String> body = new Stack<>();
    public Boolean isCalcable = true;

    public function(String id, Stack<String> body, List<parameter> parameters)
    {
        this.id = id;
        this.body = body;
        this.parameters = parameters;
    }

    public function(String id)
    {
        this.id = id;
    }

    public function()
    {

    }

    /**
     * Creates a pair of string and symboltable type, the symboltabletype will contain the current instance of function.
     */
    public Pair<String, SymbolTableType> GetInstanece(){
        return new Pair<>(this.id, new SymbolTableType(this.id, vv.FUNC, this));
    }

    /**
     * Pushes a value to the stack
     * @param value of type string, pushed to stack
     * @param type of type string, not used
     */
    public void AddToStack(String value, String type){
            body.push(value);
            if(vv.isReservedWord(value))
            {
                isCalcable = false;
            }
    }
    // TODO: 04/04/2020 symbol to stack and AddToStack very similiar 
    /**
     * Method putting parameters into the parameters list.
     * @param para Object, which will be casted to a parameter
     * @return returns a boolean true if succesful false if not
     */
    public Boolean ParameterToParameterlist(Object para){
        try {
            parameters.add((parameter) para);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    /**
     * Takes a string ans pushes it to the symbol stack
     * @param symbol of type String, should be math symbols as the stack is used as stack calculator.
     */
    public void SymolToStack(String symbol){
        body.push(symbol);
    }


}

