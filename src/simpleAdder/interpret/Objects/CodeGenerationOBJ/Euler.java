package simpleAdder.interpret.Objects.CodeGenerationOBJ;

import javafx.util.Pair;
import simpleAdder.interpret.Objects.SymolTableOBJ.reaction;
import simpleAdder.interpret.Objects.SymolTableOBJ.SymbolTableType;
import simpleAdder.interpret.TypeCheckers.BetaStackToString;
import simpleAdder.interpret.GetMethods.ViableVariable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Euler extends CodeGenerationMethods {
    ViableVariable vv = new ViableVariable();
    BetaStackToString BSTS = new BetaStackToString();

    /***
     * This method, checks wether the CRN exist in the scope, if it does not exist it will return a
     * empty string, otherwise it will initiate the generation of the head of the Euler function.
     * @Param scope
     * @Param level
     * @Param str
     * @return
     */
    public String Generate(HashMap<String, SymbolTableType> scope, int level, String str)
    {
        if (scope.get(vv.CRN) == null){
            return "\n";
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
    private void SetRate(reaction reac){
        if (reac.rateLhs == null && reac.lhs != null){
            reac.rateLhs = BSTS.StackToString(reac.lhs);
        }
        if (reac.rateRhs == null){
            reac.rateRhs = BSTS.StackToString(reac.rhs);
        }
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
        String PrettyResult = ApplyTap(Level, GenerateEulerDcl(str));
        HashMap<String, String> species = new HashMap<>();

        int rNum = 0;
        Level++;
        PrettyResult += ApplyTap(Level,"if(i < self.steps):\n");
        Level++;
        for (reaction r : reacs){

            SetRate(r);
            rNum = NumberOfReactions(r, rNum,species);
            PrettyResult += ApplyTap(Level, r.lhsDerivedEq.getKey()+"="+r.lhsDerivedEq.getValue()+"\n");
            if (!r.isOneway){
                PrettyResult += ApplyTap(Level, r.rhsDerivedEq.getKey()+"="+r.rhsDerivedEq.getValue()+"\n");
            }
        }
        Level--;
        PrettyResult += "\n";
        Level++;
        for (Map.Entry<String, String> s : species.entrySet()){
            PrettyResult += ApplyTap(Level, "self.sample[\"" + s.getKey() + "\"].append((" + GenerateReactionRef(s.getKey(),reacs)+ ")*self.h+self.sample.get(\"" + s.getKey() + "\")[-1])\n") ;
        }
        Level--;
        Level--;

        return PrettyResult + "\n";
    }

    /***
     * Will return the total amount of reactions in the euler method. It takes the reactions and add them
     * to the rNum, which is the amount of reactions that are already in the Euler method.
     * @Param reac
     * @Param rNum
     * @Param Species
     * @return
     */
    private int NumberOfReactions(reaction reac,int rNum, HashMap<String,String> Species)
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
    private String GenerateReactionRef(String specie, List<reaction> reacList) {
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
