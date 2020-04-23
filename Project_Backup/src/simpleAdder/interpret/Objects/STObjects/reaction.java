package simpleAdder.interpret.Objects.STObjects;

import javafx.util.Pair;
import simpleAdder.interpret.TypeCheckers.Check;
import simpleAdder.interpret.TypeCheckers.PossibleToAdd;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class reaction extends Check {
    public String rateRhs = null;
    public String rateLhs = null;
    public Stack<String> lhs = null;
    public Stack<String> rhs = null;
    public boolean isOneway = false;
    public List<Pair<String,String>> rhsPair = null;
    public List<Pair<String,String>> lhsPair = null;
    public Pair<String,String> lhsDerivedEq = null;
    public Pair<String,String> rhsDerivedEq = null;


    public boolean isRhsSet()
    {
        return rhs != null || rateRhs != null;
    }
    public boolean isLhsSet()
    {
        return (lhs != null || rateLhs != null) && !isOneway;
    }

    public reaction()
    {

    }

    /**
     * Sets, the rate for the correct side of the reaction
     * @param rate is a string, but in reality represents a float
     * @return returns true if the rate has succesfully been added to the reaction.
     */
    public Boolean SetRate(String rate){
        if (!isRhsSet()){
            rateRhs = rate;
            return true;
        }else if (!isLhsSet()){
            rateLhs = rate;
            return true;
        }else {
            return false;
        }
    }
    // TODO: 04/04/2020 SetRate meget det samme som AddStringField, eventuel kun brug den ene
    /**
     * Sets, the rate for the correct side of the reaction
     * @param stack is a stack, the stack is a function or exspression.
     * @return returns true if the rate has succesfully been added to the reaction.
     */
    public Boolean SetRate(Stack<String> stack){
        if (!isRhsSet()){
            rhs = (Stack<String>) stack.clone();
            return true;
        }else if (!isLhsSet()){
            lhs = (Stack<String>) stack.clone();
            return true;
        }else {
            return false;
        }
    }

    /**
     * Sets, the rate for the correct side of the reaction
     * @param str is a string, but in reality represents a float or integer
     * @return returns true if the rate has succesfully been added to the reaction.
     */
    public boolean AddStringField(String str, String method)
    {
        if (this.rateRhs == null)
        {
            this.rateRhs = str;
            return true;
        }else if(this.rateLhs == null && !this.isOneway)
        {
            this.rateLhs = str;
            return true;
        }else
        {
             return false;
        }
    }

    public Boolean ReactionToReactionList(Pair<String, String> R, Boolean isFirst)
    {
        if(lhsPair == null && !isFirst){
            lhsPair = new ArrayList<>();
            rhsPair = new ArrayList<>();
            lhsPair.add(R);
            return true;
        }else if(lhsPair == null){
            lhsPair = new ArrayList<>();
            lhsPair.add(R);
            return true;
        }
        else if(!isFirst && rhsPair == null && lhsPair != null){
            lhsPair.add(R);
            rhsPair = new ArrayList<>();
            return true;
        }else if (lhsPair != null && rhsPair != null){
            rhsPair.add(R);
            return true;
        }else if (lhsPair != null && rhsPair == null){
            lhsPair.add(R);
            return true;
        }else{
            return false;
        }
    }
}
