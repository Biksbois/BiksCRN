package simpleAdder.interpret.ObjectGeneration;

import javafx.util.Pair;
import simpleAdder.interpret.Objects.STObjects.reaction;
import simpleAdder.interpret.SymbolTableType;
import simpleAdder.interpret.ViableVariable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class HelpMethods {
    ViableVariable vv = new ViableVariable();
    PrinterMethods PM = new PrinterMethods();
    Dict dict = new Dict();

    /***
     * Takes a input string (InputStr), and adds the amounts of tabs to the string
     * that the int (indent) specifies. It does so by first adding the indents to the
     * OutputStr, and then adding it to the input string.
     * @Param indent
     * @Param OutputStr
     * @Param InputStr
     * @return
     */
    public String ApplyTap(int indent, String OutputStr, String InputStr)
    {
        for(int i = 0; i < indent; i++)
        {
            OutputStr += "\t";
        }
        return OutputStr + InputStr;
    }

    /***
     * Takes a string and adds indents to the string. Depending on the number that are
     * stored in the indent int. It will then return a string, with the specified tabs.
     * @Param indent
     * @Param OutputStr
     * @return
     */
    public String ApplyTap(int indent, String OutputStr)
    {
        for(int i = 0; i < indent; i++)
        {
            OutputStr = "    " + OutputStr;
        }
        return OutputStr;
    }

    /***
     * Used to generate the unique sample named, depending on the name of the specific sample.
     * @Param sample
     * @return
     */
    public String GetSampleName(String sample){
        return "Sample" + sample;
    }

    /***
     * This method, checks wether the CRN exist in the scope, if it does not exist it will return a 
     * empty string, otherwise it will initiate the generation of the head of the Euler function.
     * @Param scope
     * @Param level
     * @Param str
     * @return
     */
    public String HelpGenerateEuler(HashMap<String, SymbolTableType> scope, int level, String str)
    {
        if (scope.get(vv.CRN) == null){
            return "";
        }
        else
        {
            return GenerateEuler(scope.get(vv.CRN).crn, level, str);
        }
    }

    /***
     * Generates the head of the Euler function.
     * @Param str
     * @return
     */
    private String GenerateEulerDcl(String str){

        return "def Euler"+str+"(self, i) :\n";
    }

    /***
     * Checks if the reaction is directed to the left side, if it is, it will set the rate of the left side
     * direction. If the right direction rate is null, it will also add that rate.
     * @Param reac
     * @return
     */
    public void SetRate(reaction reac){
        if (reac.rateLhs == null && reac.lhs != null){
            reac.rateLhs = StackToRate(reac.lhs);
        }
        if (reac.rateRhs == null){
            reac.rateRhs = StackToRate(reac.rhs);
        }
    }

    /***
     * This recursive method, will take a stack and get the rate depending on the order of the content of 
     * the stack. If the method encounters a end parantese, it implies that the method contains both a left 
     * and a righthand rate, and therefore calls the method a second time to retrieve the righthand value.
     * The method knows that it is done retrieving the rates, when it reaches a start parantese or if the 
     * stack is empty.
     * @Param stack
     * @return
     */
    private String StackToRate(Stack<String> stack){
        String results = "";
        String rhs = "";
        String lhs = "";
        String sym = "";
        Stack<String> numbers = new Stack<>();
        while (!stack.isEmpty()){
            if (stack.peek().equals(")")){
                lhs = stack.pop();
                rhs = StackToRate(stack);
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
                    results += lhs + sym + rhs;
                }else if(results.equals("") || sym.equals("-") && stack.peek().equals("(")){
                    results = sym + lhs;
                }else{
                    results = lhs + sym + results;
                }
            }else{
                if (stack.peek().equals(vv.CYCLE)){
                    stack.pop();
                    numbers.push("i");
                }
                else {
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
    public String GetNext(Stack<String> stack){
        String next = "";
        String sym = "";
        while (!stack.isEmpty()){
            if (IsMultDivide(stack.peek())){
                sym = stack.pop();
                next = sym + GetNext(stack) + next;
                //next += sym + GetNext(stack);
            }else if(stack.peek().equals(")")){
                next = StackToRate(stack) + next;
                //next += StackToRate(stack);
            }else if(next.length() > 0 && next.charAt(0) == '('){
                return next;
            }
            else{
                if (stack.peek().equals(vv.CYCLE)){
                    stack.pop();
                    return "i " + next;
                }
                else {
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
    public boolean IsPlusMinus(String s){
        return s.equals(vv.plus) || s.equals(vv.minus) ? true: false;
    }

    /***
     * Checks if the string is a multiplication, divide or powered.
     * @Param s
     * @return
     */
    public boolean IsMultDivide(String s){
        return s.equals(vv.mult) || s.equals(vv.div) || s.equals(vv.power) ? true: false;
    }

    /***
     * Handles the generation of the deriviation of the Euler method, so that it is possible the be run 
     * in Python. It takes the reactions and orders them correctly, and use the level, to control the 
     * amount of tabs. It will finally return the Python code for the Euler method.
     * @Param reacs
     * @Param level
     * @Param str
     * @return
     */
    private String GenerateEuler(List<reaction> reacs, int Level, String str){
        String PrettyResult = PM.ApplyTap(Level, GenerateEulerDcl(str));
        HashMap<String, String> species = new HashMap<>();

        int rNum = 0;
        Level++;
        for (reaction r : reacs){

            SetRate(r);
            rNum = NumberOfReactions(r, rNum,species);
            PrettyResult += PM.ApplyTap(Level, r.lhsDerivedEq.getKey()+"="+r.lhsDerivedEq.getValue()+"\n");
            if (!r.isOneway){
                PrettyResult += PM.ApplyTap(Level, r.rhsDerivedEq.getKey()+"="+r.rhsDerivedEq.getValue()+"\n");
            }
        }
        Level--;
        PrettyResult += "\n";
        Level++;
        for (Map.Entry<String, String> s : species.entrySet()){
            PrettyResult += PM.ApplyTap(Level, "self.sample[\"" + s.getKey() + "\"].append((" + GenerateReactionRef(s.getKey(),reacs)+ ")*self.h+self.sample.get(\"" + s.getKey() + "\")[-1])\n") ;
        }
        Level--;

        return PrettyResult;
    }

    /***
     * Will return the total amount of reactions in the euler method. It takes the reactions and add them 
     * to the rNum, which is the amount of reactions that are already in the Euler method.
     * @Param reac
     * @Param rNum
     * @Param Species
     * @return
     */
    public int NumberOfReactions(reaction reac,int rNum, HashMap<String,String> Species)
    {
        String name = "r"+ ++rNum;
        String Value = reac.rateRhs;
        for (Pair<String, String> p : reac.lhsPair) {
            if (!Species.containsKey(p.getKey())){
                Species.put(p.getKey(),"");
            }
            Value += "*self.sample.get(\""+p.getKey()+"\")[-1]";
        }
        reac.lhsDerivedEq = new Pair<>(name,Value);

        if(!reac.isOneway)
        {
            name = "r"+ ++rNum;
            Value = reac.rateLhs;
            for (Pair<String, String> p : reac.rhsPair) {
                if (!Species.containsKey(p.getKey())){
                    Species.put(p.getKey(),"");
                }
                Value += "*self.sample.get(\""+p.getKey()+"\")[-1]";
            }
            reac.rhsDerivedEq = new Pair<>(name,Value);
        }
        return rNum;
    }

    /***
     * Generates the derived equations for each of the reactions. It will finally return the string, that 
     * will be used in the GenerateEuler method.
     * @Param specie
     * @Param reacList
     * @return
     */
    public String GenerateReactionRef(String specie, List<reaction> reacList) {
        String PrettyResult = "";
        for (reaction r : reacList)
        {
            PrettyResult += DerivedForOne(r.lhsPair, r.lhsDerivedEq.getKey(), specie, "-");
            PrettyResult += DerivedForOne(r.rhsPair, r.lhsDerivedEq.getKey(), specie, "+");
            if(!r.isOneway)
            {
                PrettyResult += DerivedForOne(r.lhsPair, r.rhsDerivedEq.getKey(), specie, "+");
                PrettyResult += DerivedForOne(r.rhsPair, r.rhsDerivedEq.getKey(), specie, "-");
            }

        }
        if ('+' == PrettyResult.charAt(0)){
            PrettyResult = PrettyResult.substring(1);
        }
        return PrettyResult;
    }

    /***
     * Takes a symbol and adds it to the derived equation. It will do that for each side of the reaction
     * and then finally return it to the GenerateReactionRef method.
     * @Param pairs
     * @Param derivedEq
     * @Param specie
     * @Param symbol
     * @return
     */
    private String DerivedForOne(List<Pair<String, String>> pairs, String derivedEq, String specie, String symbol){
        String PrettyResult = "";
        for (Pair<String, String> p: pairs) {
            if(specie.equals(p.getKey()))
            {
                PrettyResult += symbol+derivedEq;

            }
        }
        return PrettyResult;
    }

}
