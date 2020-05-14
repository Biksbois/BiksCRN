package simpleAdder.interpret.CompilerPhases;

import com.company.node.*;
import simpleAdder.interpret.GetMethods.Get;
import simpleAdder.interpret.GetMethods.ViableVariable;
import simpleAdder.interpret.Objects.SymolTableOBJ.*;
import simpleAdder.interpret.TypeCheckers.BetaStackToString;
import simpleAdder.interpret.TypeCheckers.BiksPair;
import simpleAdder.interpret.TypeCheckers.Check;
import simpleAdder.interpret.TypeCheckers.Checker;

import java.util.HashMap;
import java.util.List;
import java.util.Stack;

public class BetaSymbolTable extends Checker {
    Check check = new Check();
    HashMap<String, SymbolTableType> st = new HashMap<String,SymbolTableType>();
    Get get = new Get();
    BetaStackToString BSTS = new BetaStackToString();
    public HashMap<String, SymbolTableType> sample = null;
    public protocolOperation tempProtocol = null;
    public Stack<protocolOperation> protocols = new Stack<>();
    public Temporary temp = null;

    TerminateProgram terminate = new TerminateProgram();

    Stack<String> loopSide;

    /***
     * Returns the current scope. Either a sample, if it is not null,
     * or the symbol table.
     * @return
     */
    public HashMap<String,SymbolTableType> CurrentScope()
    {
        if(sample != null)
        {
            return sample;
        }
        else
        {
            return st;
        }
    }

    /***
     * Adds a value to the current scope, which is either a scope or
     * the symbol table.
     * @param id
     * @param type
     * @param value
     */
    public void PutInCurrentScope(String id, String type, String value, Token token)
    {
        id = id.trim();
        if(sample != null)
        {
            if(!sample.containsKey(id))
            {
                sample.put(id,new SymbolTableType(id,type,value));
                return;
            }
        }
        else
        {
            if(!st.containsKey(id))
            {
                st.put(id,new SymbolTableType(id,type,value));
                return;
            }
        }
        terminate.VarExistsAlready(token, type, id, "PutInCurrentScope");
        //TH.terminate_program(token, "\""+id+ "\" of type " + type + " already exists.");
    }

    private void SpeciesToCurrentScope(HashMap<String, SymbolTableType> scope, String id, SymbolTableType value, Token token){
        if(!scope.containsKey(ViableVariable.SPECIE)) {
            scope.put(ViableVariable.SPECIE, new SymbolTableType(ViableVariable.SPECIE));
        }

        if (scope.containsKey(ViableVariable.SPECIE) && !scope.get(ViableVariable.SPECIE).species.containsKey(id)){
            scope.get(ViableVariable.SPECIE).species.put(id, value.value);
        }
        else {
            terminate.VarExistsAlready(token, ViableVariable.SPECIE, id, "PutInCurrentScope");
            //TH.terminate_program("Species \"" + id + "\" already exist (SpeciesToCurrentScope)");
        }
    }

    /***
     * If the object Temporary is active, a object is added to a list in Temporary
     * @param obj
     */
    public void addObjectToList(Object obj)
    {
        if(IsTempActive())
        {
            temp.ObjectToList(obj);
        }
    }

    public void reactionToReaction(BiksPair<String, String> R, Boolean isFirst, Token token){
        if (IsTempActive()){
            if (!VerifyKeyAndTypeInBoth(R.getKey(), ViableVariable.SPECIE)){
                terminate.WrongType(token, ViableVariable.SPECIE, TypeForMessage(R.getKey()), R.getKey(), "reactionToReaction");
                //TH.terminate_program("\"" + R.getKey() + "\" should be of type specie. It either does not exists or is the wrong type.");
            }else if(!isWholePositiveFloat(R.getValue())){
                terminate.ShouldBeWhole(token, ViableVariable.SPECIE,R.getKey(), R.getValue(),"reactionToReaction");
                //TH.terminate_program("The float value \"" + R.getValue() + "\" is not a whole number, which is should be");
            }
            else{
                temp.reactionToReaction(R, isFirst);
            }
        }
    }

    public Boolean isWholePositiveFloat(String fVal){
        if (fVal.contains("-")){
            return false;
        }if (fVal.contains(".")){
            String sub = fVal.split("\\.")[1];
            String zeroes = GetZeroes(sub.length());
            return sub.equals(zeroes);
        }else{
            return true;
        }
    }

    private String GetZeroes(int zeros){
        String result = "";

        for (int i = 0; i < zeros; i++){
            result += "0";
        }

        return result;
    }
    //<editor-fold desc="Get">

    /***
     * Checks if a function is a void function (without parameters)
     * @return
     */
    public Boolean isVoid()
    {
        if(!IsFuncActive())
        {
            TH.terminate_program("Temp instance was null");
        }
        return temp.Func.parameters.size() == 0;
    }

    /***
     * Gets a value form the input key in the symbol current scope.
     * @param key
     * @return
     */
    public String GetValue(String key)
    {
        if(sample != null && sample.containsKey(key)) {
            return sample.get(key).value;
        }else if(sample != null && sample.containsKey(ViableVariable.SPECIE) && sample.get(ViableVariable.SPECIE).species.containsKey(key)){
            return sample.get(ViableVariable.SPECIE).species.get(key);
        } else if(st.containsKey(key)) {
            return st.get(key).value;
        }else if(st.containsKey(ViableVariable.SPECIE) && st.get(ViableVariable.SPECIE).species.containsKey(key)){
            return st.get(ViableVariable.SPECIE).species.get(key);
        } else {
            TH.terminate_program("Key "+ key +" is not initialized (GetValue)");
            return "";
        }
    }

    /***
     * Gets a function from the input key in the current scope
     * @param key
     * @return
     */
    public function GetFunction(String key){
        if(sample != null && sample.containsKey(key))
        {
            return sample.get(key).func;
        }else if(st.containsKey(key))
        {
            return st.get(key).func;
        }
        else
        {
            TH.terminate_program("Key "+ key +" is not initialized (GetFunction)");
            return null;
        }
    }

    /***
     * Gets type from an input key from the current scope
     * @param key
     * @return
     */
    public String GetType(String key)
    {
        key = key.trim();
        if(sample != null)
        {
            if (sample.containsKey(key)){
                return sample.get(key).type;
            }else if(sample.containsKey(ViableVariable.SPECIE) && sample.get(ViableVariable.SPECIE).species.containsKey(key)){
                return ViableVariable.SPECIE;
            }
        }

        if(st.containsKey(key)){
            return st.get(key).type;
        }else if(st.containsKey(ViableVariable.SPECIE) && st.get(ViableVariable.SPECIE).species.containsKey(key)){
            return ViableVariable.SPECIE;
        }
        else if(IsFuncActive())
        {
            if(vv.isReservedWord(key))
            {
                return key;
            }
            for (parameter p : temp.Func.parameters) {
                if(p.name.equals(key))
                {
                    return p.type;
                }
            }
            TH.terminate_program("Parameter " + key + " does not exist in function");
        }
        TH.terminate_program("Key " + key + " not found in symbol table, could not exist or illegal reference made inside local scope");
        return "";
    }

    public String TypeForMessage(String key){
        if(sample != null)
        {
            if (sample.containsKey(key)){
                return sample.get(key).type;
            }else if(sample.containsKey(ViableVariable.SPECIE) && sample.get(ViableVariable.SPECIE).species.containsKey(key)){
                return ViableVariable.SPECIE;
            }
        }

        if(st.containsKey(key)){
            return st.get(key).type;
        }else if(st.containsKey(ViableVariable.SPECIE) && st.get(ViableVariable.SPECIE).species.containsKey(key)){
            return ViableVariable.SPECIE;
        }
        return "";
    }

    public String ValueForMessage(String key){
        if(sample != null)
        {
            if (sample.containsKey(key) && sample.get(key).value != null){
                return sample.get(key).value;
            }else if(sample.containsKey(ViableVariable.SPECIE) && sample.get(ViableVariable.SPECIE).species.containsKey(key)){
                return sample.get(ViableVariable.SPECIE).species.get(key);
            }
        }

        if(st.containsKey(key) && st.get(key).value != null){
            return st.get(key).value;
        }else if(st.containsKey(ViableVariable.SPECIE) && st.get(ViableVariable.SPECIE).species.containsKey(key)){
            return st.get(ViableVariable.SPECIE).species.get(key);
        }
        return "";
    }

    /***
     * Is used when a function is called, and the input parameters it is called with is mathched with
     * the actual parameters.
     * @param funcName
     * @param method
     */
    public void VerifyParameters(String funcName, String method){
        if (!AreParametersActive()){
            TH.terminate_program("Either temp, reaction or the parameter list is null (" + method + ")");
        }
        temp.VerifyAndSetRate(GetFunction(funcName), method);
    }

    private Boolean AreParametersActive(){
        return IsTempActive() && temp.Reac != null && temp.Para != null;
    }
    //</editor-fold>

    //<editor-fold desc="Add">

    //</editor-fold>

    //<editor-fold desc="Verify">

    /***
     * Verifies a key with a given type exists in the current scope
     * @param key
     * @param type
     * @return
     */
    public boolean VerifyKeyAndTypeInScope(String key, String type){
        if (sample == null){
            return  false;
        } else if (type.equals(ViableVariable.SPECIE) && sample.containsKey(ViableVariable.SPECIE) && sample.get(ViableVariable.SPECIE).species.containsKey(key)) {
            return true;
        }else return sample.containsKey(key) && sample.get(key).type.equals(type);
    }

    /***
     * Verifies a key with a given type exists in the symbol table
     * @param key
     * @param type
     * @return
     */
    public Boolean VerifyKeyAndTypeInST(String key, String type){
        if (type.equals(ViableVariable.SPECIE) && st.containsKey(ViableVariable.SPECIE) && st.get(ViableVariable.SPECIE).species.containsKey(key)) {
            return true;
        } else return st.containsKey(key) && st.get(key).type.equals(type);
    }

    /***
     * Verifies a key with a given type exists in the either the current scope or the symbol table.
     * @param key
     * @param type
     * @return
     */
    public Boolean VerifyKeyAndTypeInBoth(String key, String type){
        if (VerifyKeyAndTypeInST(key, type)){
            return true;
        }else return VerifyKeyAndTypeInScope(key, type);
    }

    //</editor-fold>

    //<editor-fold desc="NodeToCurrentScope">

    /***
     * Adds the active object from temp to the current scope
     */
    public void NodeToCurrentScope(Token token){
        BiksPair<String, SymbolTableType> pair = temp.GetInstanece();
        if (pair.getValue().type.equals(ViableVariable.SPECIE)){
            SpeciesToCurrentScope(CurrentScope(), pair.getKey(), pair.getValue(), token);
        }
        else{
            PairToCurrentScope(pair);
        }
        temp = null;
    }

    /***
     * Adds the active object from temp to the current scope
     */
    public void NodeToCurrentScope(ADclTitration node){
        BiksPair<String, SymbolTableType> pair = temp.GetInstanece(node.getTitrationdcl().toString().trim());
        PairToCurrentScope(pair);
    }

    /***
     * Adds the active object from temp to the current scope
     */
    private void PairToCurrentScope(BiksPair<String, SymbolTableType> pair){
        if (CurrentScope().containsKey(pair.getKey())){
            TH.terminate_program("There already exists a variable of with a similar name as \"" + pair.getValue().type + " " + pair.getKey() + "\".");
        }
        CurrentScope().put(pair.getKey(), pair.getValue());
    }

    /***
     * Adds a rate node to the current scope
     * @param node
     */
    public void NodeToCurrentScope(AMultipleRates node){
        String id = get.Id(node);
        String type = get.Type(node);
        String value = get.Value(node);
        PutInCurrentScope(id,type,value, node.getTFloat());
    }

    /***
     * Adds a rate node to the current scope
     * @param node
     */
    public void NodeToCurrentScope(ASingleRates node){
        String id = node.getTString().toString().trim();
        String type = ViableVariable.RATE;
        String value = node.getTFloat().toString();
        PutInCurrentScope(id,type,value, node.getTFloat());
    }

    /***
     * Adds a sample to the current scope
     * @param node
     */
    public void SampleToSymboltable(ASampleinitSample node){
        String id = get.Id(node);
        String type = get.Type(node);
        st.put(id, new SymbolTableType(id, type, sample));
        sample = null;
    }
    //</editor-fold>

    //<editor-fold desc="SymbolToStack">

    public void SymbolToStack(TTLParen node){
        SymbolToStack("(");
    }

    public void SymbolToStack(TTRParen node){
        SymbolToStack(")");
    }

    public void SymbolToStack(APlusOperator node){
        SymbolToStack("+");
    }

    public void SymbolToStack(AMinusOperator node){
        SymbolToStack("-");
    }

    public void SymbolToStack(ANegativeParenPolarity node){
        SymbolToStack("-");
    }

    public void SymbolToStack(AMultTerm node){
        SymbolToStack("*");
    }

    public void SymbolToStack(ADivideTerm node){
        SymbolToStack("/");
    }

    public void SymbolToStack(AOptermPower node){
        SymbolToStack("**");
    }
    //</editor-fold>

    //<editor-fold desc="Tests">

    /***
     * Creates an instance of a type in temporary
     * @param type
     * @param method
     */
    public void CreateInstance(String type, String method){
        temp = new Temporary(type, method);
    }

    /***
     * Creates an instance of a type in temporary
     * @param type
     * @param method
     */
    public void CreateInstance(String type, String id, String method){
        temp = new Temporary(type, id, method);
    }

    /***
     * Creates an instance of a type in temporary
     * @param method
     */
    public void CreateInstance(String method){
        sample = new HashMap<>();
    }

    void SymbolToStack(String value){
        if (loopSide != null){
            loopSide.push(value);
        }else if (IsTempActive()){
            temp.AddToStack(value);
        }
    }

    /***
     * Adds a symbol to either a stack in temp or tempProtocol
     * @param value
     * @param type
     */
    public void SymbolToStack(String  value, String type){
        if (type.equals(ViableVariable.Variable)){
            type = GetType(value);
            if (type.equals(ViableVariable.SPECIE) && loopSide != null){
                loopSide.push(value);
                return;
            }
            if(!IsFuncActive())
            {
                value = GetValue(value);
            }
        }
        if(loopSide != null){
            loopSide.push(value);
        }else if (IsTempActive()){
            temp.AddToStack(value, type);
        }else if(tempProtocol != null){
            tempProtocol.ValueToSplit(value, type);
        }
    }
    //</editor-fold>

    private Boolean IsFuncActive(){
        return IsTempActive() && temp.Func != null;
    }

    public boolean StackinstanceExists(){
        return (IsTempActive() && temp.StackinstanceExists()) || tempProtocol != null || loopSide != null;
    }

    public boolean IsTempActive(){
        return temp != null;
    }

    public void SetBoolean(Boolean bool, String method){
        if (IsTempActive()){
            temp.SetBoolean(bool, method);
        }else {
            TH.terminate_program("Temp is null (" + method + ")");
        }
    }

    /***
     * Adds a input string to either an object in temp or tempProtocol
     * @param str
     * @param method
     */
    public void AddStringToField(String str, String method){
        if (temp != null){
            temp.AddStringField(str, method);
        }else if(tempProtocol != null){
            tempProtocol.AddStringToField(str, method);
        }else {
            TH.terminate_program("No instance was hit (AddStringToFild) (" + method + ")");
        }
    }

    private String CheckValue(String str)
    {
        String value = "";
        String type = "";
        if(Character.isDigit(str.charAt(0)))
        {
            return str;
        }
        else
        {
            if (st.containsKey(str)){
                type = GetType(str);
                value = GetValue(str);
            }else if(st.containsKey(ViableVariable.SPECIE) && st.get(ViableVariable.SPECIE).species.containsKey(str)){
                if (VerifySpecie(str)){
                    return str;
                }else{
                    TH.terminate_program("The specie " + str + " cannot be used in titration, because it is no a part of any reaction");
                }
            }
            else if (sample != null){
                if (sample.containsKey(str)){
                    type = GetType(str);
                    value = GetValue(str);
                }else if(sample.containsKey(ViableVariable.SPECIE) && sample.get(ViableVariable.SPECIE).species.containsKey(str)){
                    if (VerifySpecie(str)){
                        return str;
                    }else{
                        TH.terminate_program("The specie " + str + " cannot be used in titration, because it is no a part of any reaction");
                    }
                }
            }
            else {
                TH.terminate_program("Value " + str + " is not initialized");
            }

            if (value != null && type.equals(ViableVariable.INT) || type.equals(ViableVariable.FLOAT)){
                return value;
            }else{
                TH.terminate_program("You cannot compare type " + type + " in titration");
                return "";
            }
        }
    }

    /***
     * Verifies if a specie exists in eiter a sample or the symbol table
     * and also exists in a reaction
     * @param specie
     * @return
     */
    public boolean VerifySpecie(String specie){
        if(sample != null && sample.containsKey(ViableVariable.CRN)){
            if (st.containsKey(ViableVariable.SPECIE) && UsedInCRN(specie, sample.get(ViableVariable.CRN).crn)){
                return true;
            }
            return sample.containsKey(ViableVariable.SPECIE) && UsedInCRN(specie, sample.get(ViableVariable.CRN).crn);
        }
        return false;
    }

    /***
     * Checks if a specie is used in a list of reactions.
     * @param specie
     * @param reactions
     * @return
     */
    private boolean UsedInCRN(String specie, List<reaction> reactions){
        for (reaction r: reactions) {
            if (CheckOnePairlist(r.lhsPair, specie) || CheckOnePairlist(r.rhsPair, specie)){
                return true;
            }
        }
        return false;
    }

    /***
     * Is called by UsedInCRN to check one reactoin, which is a list of pairs, to see
     * if a given specie is used in it.
     * @param pairs
     * @param specie
     * @return
     */
    private boolean CheckOnePairlist(List<BiksPair<String, String>> pairs, String specie){
        for (BiksPair<String, String> p: pairs) {
            if (specie.equals(p.getKey())){
                return true;
            }
        }
        return false;
    }



}