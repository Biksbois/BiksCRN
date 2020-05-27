package simpleAdder.interpret.Objects.SymolTableOBJ;

import com.company.node.Switch;
import simpleAdder.interpret.CompilerPhases.TerminateProgram;
import simpleAdder.interpret.TypeCheckers.BiksPair;
import simpleAdder.interpret.TypeCheckers.Check;
import simpleAdder.interpret.GetMethods.ViableVariable;
import simpleAdder.interpret.TypeCheckers.Checker;
import simpleAdder.interpret.TypeCheckers.OptimizedStackToString;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Temporary {
    TerminateProgram TH = new TerminateProgram();
    Check check = new Check();
    Checker checker = new Checker();
    OptimizedStackToString CALC = new OptimizedStackToString();

    public List<reaction> crn = null;
    public List<parameter> Para = null;
    public List<titration> TitList = null;

    public reaction Reac = null;
    public function Func = null;
    public titration Tit = null;
    public logicalExspression LogExpr = null;
    public PrimitiveType Prim = null;

    public Temporary(String type, String method){
        switch (type){
            case ViableVariable.CRN:
                crn = check.ListType.CheckList(crn, method);
                break;
            case ViableVariable.TITRATIONLIST:
                TitList = check.TitType.CheckTitrations(TitList, method);
                break;
            default: TH.terminate_program(type + " cannot be initialized this way (Temporary)");
        }
    }

    public Temporary(String type, String id, String method){
        switch (type){
            case ViableVariable.INT:
                Prim = check.ValueType.NewPrimitiveType(Prim, ViableVariable.INT, id, method, true, true, false, false, true, false);
                break;
            case ViableVariable.SPECIE:
                Prim = check.ValueType.NewPrimitiveType(Prim, ViableVariable.SPECIE, id, method, true, true, true, false, false, false);
                break;
            case ViableVariable.FLOAT:
                Prim = check.ValueType.NewPrimitiveType(Prim, ViableVariable.FLOAT, id, method, true, true, false, false, true, true);
                break;
            case ViableVariable.FUNC:
                Func = check.FuncType.CheckFunction(Func, id, method);
                break;
            default: TH.terminate_program(type + " cannot be initialized this way (Temporary)");
        }
    }

    /***
     * Adds a vvalue to an istance if the type allows it
     * @param value
     * @param type
     */
    public void AddToStack(String value, String type){
        if (Func != null){
            Func.AddToStack(value, type);
        } else if(Reac != null && Para != null){
            Para.add(new parameter(value, type));
        }else if (Reac != null){

        }else if(LogExpr != null){

        }else if(Prim != null){
            if (!Prim.AddToStack(value, type)){
                TH.terminate_program("Type " + type + " cannot be added to a " + Prim.type);
            }
        }else{
            TH.terminate_program("All instances are null (AddToStack)");
        }
    }

    /***
     * Adds a value to a stack
     * @param value
     */
    public void AddToStack(String value){
        if (Func != null){
            Func.SymolToStack(value);
        }else if(Prim != null){
            Prim.SymolToStack(value);
        }
    }

    /***
     * Adds a object to a list, if the instnace allows it
     * @param obj
     */
    public void ObjectToList(Object obj)
    {
        if (Func != null){
            if (!Func.ParameterToParameterlist(obj)){
                TH.terminate_program("Object added to parameterlist was not a parameter (ObjectToList)");
            }
        }
        else{
            TH.terminate_program("All instances are null (ObjectToList)");
        }
    }

    public void reactionToReaction(BiksPair<String, String> R, Boolean isFirst){
        if (Reac == null){
            TH.terminate_program("All instances was null (reactionToReaction)");
        }else if (!Reac.ReactionToReactionList(R, isFirst)) {
            TH.terminate_program("Reaction " + R.getKey() + " " + R.getValue() + " could not be added to a reaciton (reactionToReaction)");
        }
    }

    /***
     * Add a local object to a list, and sets object to null
     */
    public void ObjectToList()
    {
        if (Reac != null){
            crn.add(Reac);
            Reac = null;
            Para = null;
        }else if(TitList != null && Tit != null && LogExpr == null)
        {
            TitList.add(Tit);
            Tit = null;
        }else if(TitList != null && Tit != null && LogExpr != null)
        {
            if(Tit.LogicalExpr == null)
            {
                Tit.LogicalExpr = new ArrayList<>();
            }
            Tit.LogicalExpr.add(LogExpr);
            LogExpr = null;
        }
        else{
            TH.terminate_program("All instances are null (ObjectToList)");
        }
    }

    /***
     * Sets a rate on a reaciton, either as a stack or string depending on
     * if the function called can be calcualted.
     * @param func
     * @param method
     */
    public void VerifyAndSetRate(function func, String method){
        check.FuncType.VerifyParameters(func, Para);
        BiksPair<Boolean, Stack<String>> pair = check.FuncType.ParametersToStack(Para, func);
        if (pair.getKey()){
            Reac.SetRate(check.StackType.Calculate(pair.getValue()));
        }else if(!pair.getKey() && pair.getValue() != null){
            Reac.SetRate(pair.getValue());
        }else{
            TH.terminate_program("Function \"" + func.id + "\" is not valid (" + method + ")");
        }

    }

    /***
     * Returns the active instance.
     * @return
     */
    public BiksPair<String, SymbolTableType> GetInstanece(){
        if (Prim != null) {
            Prim.SetResult(check.StackType.Calculate(Prim.GetStack()));
            if (Prim.ResultIsValid()){
                return Prim.GetIntance();
            }else{
                TH.terminate_program(Prim.GetErrormessage());
            }
        }
        else if(Func != null)
        {
            return Func.GetInstanece();
        }else if(crn != null)
        {
            return new BiksPair<>(ViableVariable.CRN,new SymbolTableType(ViableVariable.CRN, ViableVariable.CRN,crn));
        }

        TH.terminate_program("No possible outcome was met (GetInstanece)");
        return new BiksPair<>("", new SymbolTableType("","",""));
    }

    /***
     * Returns the active instance
     * @return
     */
    public  BiksPair<String, SymbolTableType> GetInstanece(String key)
    {
        if(TitList != null && Tit == null)
        {
            return new BiksPair<>(key,new SymbolTableType(key,TitList, ViableVariable.TITRATIONLIST));
        }
        else
        {
            TH.terminate_program("No possible outcome was met (GetInstanece)");
            return new BiksPair<>("", new SymbolTableType("","",""));
        }
    }

    /***
     * Chekcs if it is possible to add to a stack in one of the instances in this class
     * @return
     */
    public boolean StackinstanceExists(){
        return Prim != null || Func != null || Reac != null || LogExpr != null;
    }

    /***
     * Creates an object, depending on which lists are active
     * @param method
     */
    public void CreateObjecte(String method)
    {
        if(TitList != null && Tit != null && Tit.LogicalExpr == null)
        {
            Tit.LogicalExpr = new ArrayList<>();
        }else if(TitList != null && Tit != null && Tit.booleanOperator == null)
        {
            Tit.booleanOperator = new ArrayList<>();
        }else if(crn != null && Reac == null){
            Reac = new reaction();
        }
    }

    /***
     * Creates and object depending on which lists are active, taking one string input to the constructor.
     * @param str
     * @param method
     */
    public void CreateObjecte(String str,String method)
    {
        if(crn != null && Reac == null)
        {
            Reac = new reaction();
        }else if (crn != null && Reac != null){
            Para = new ArrayList<>(); //TODO move this to CreateObject without parameters
        }
        else
        {
             TH.terminate_program("Attempting to overwrite reaction, before it is commited to the crn is illigal ("+method+")");
        }
    }

    /***
     * Creates an object depending on which lists are active, using two strings to the constructor
     * @param timeStep
     * @param method
     * @param species
     */
    public void CreateObjecte(String timeStep,String method, String species)
    {
        if(TitList != null && Tit == null)
        {
            Tit = new titration(species,timeStep);
        }
        else
        {
            TH.terminate_program("Attempting to overwrite reaction, before it is commited to the crn is illigal ("+method+")");
        }
    }

    /***
     * Creates an object depending on which lists are active, using three strings to the constructor
     * @param Rhs
     * @param method
     * @param Lhs
     */

    public void CreateObjecte(Stack<String> Rhs, Stack<String> Lhs, String logicalOp,String method)
    {
        if(TitList != null && Tit != null && LogExpr == null) {
            LogExpr = new logicalExspression(logicalOp, Rhs, Lhs);
        }
        else
        {
            TH.terminate_program("Attempting to overwrite reaction, before it is commited to the crn is illigal ("+method+")");
        }
    }

    /***
     * Add a string to a object, depending on which objects are active
     * @param str
     * @param method
     */
    public void AddStringField(String str,String method)
    {
        if(crn != null && Reac != null)
        {
            if(!Reac.SetRate(str))
            {
                TH.terminate_program("String could not be added to field (" +method +")");
            }
        }else if(Tit != null)
        {
            if(Tit.booleanOperator == null)
            {
                Tit.booleanOperator = new ArrayList<>();
            }
            Tit.booleanOperator.add(str);
        }
        else
        {
            TH.terminate_program("Attempting to overwrite reaction, before it is commited to the crn is illigal ("+method+")");
        }
    }

    public void SetBoolean(Boolean bool, String method){
        if (crn != null && Reac != null){
            Reac.isOneway = bool;
        }else{
            TH.terminate_program("Reac is null (" + method + ")");
        }
    }

    public void OptimizeConditionsAndAdd() {
        if (Tit.LogicalExpr != null){
            char[] logExpr = GetLogExpr(Tit.LogicalExpr, Tit.booleanOperator);
            String loopBody = GetLoopBody(logExpr, Tit.booleanOperator);
            if (LoopDependentOnSpecie(loopBody)){
                Tit = null;
            }else{
                TerminateExpressions(loopBody);
            }
        }

        if(Tit != null) {
            ObjectToList();
        }
    }

    private void TerminateExpressions(String loopBody) {
        for (int i = loopBody.length()-1; i >= 0; i-=2){
            if (loopBody.charAt(i) != 'S'){
                RemoveLogicalExpr(i);
                RemoveBooleanOperator(i);
            }
        }
    }

    private void RemoveLogicalExpr(int i) {
        int index = i/2;
        if (Tit.LogicalExpr != null){
            Tit.LogicalExpr.remove(index);
        }
    }

    private void RemoveBooleanOperator(int i) {
        if (i==0){
            if (Tit.booleanOperator != null && Tit.booleanOperator.size() > 0){
                Tit.booleanOperator.remove(0);
            }
        }else{
            int index = (i-2)/2;
            if (Tit.booleanOperator != null && i != 0){
                Tit.booleanOperator.remove(index);
            }
        }
    }


    private boolean LoopDependentOnSpecie(String loopBody){
        String[] arr = loopBody.split("&");
        String result = "";
        for (String s: arr){
            if (s.contains("1")){
                result += "1";
            }else if(!s.contains("1") && s.contains("S")){
                result += "S";
            }else{
                result += "0";
            }
        }
        return result.contains("0") ? true : false;
    }

    private boolean FindBooleanValue(boolean b, String loopBody) {
        loopBody = loopBody.replaceAll("S", b == true ? "1" : "0");
        String[] split = loopBody.split("&");
        for (String s:split){
            if (!s.contains("1")){
                return false;
            }
        }
        return true;
    }

    private String GetLoopBody(char[] logExpr, List<String> booleanOperator) {
        String result = "";
        for (int i = 0; i < logExpr.length; i++){
            if (BooleanOperatorExists(i, booleanOperator)){
                result += Character.toString(logExpr[i]) + ReplaceBool(booleanOperator.get(i));
            }else{
                result += Character.toString(logExpr[i]);
            }
        }
        return result;
    }

    private char ReplaceBool(String s) {
        return s.equals("&&") ? '&' : '|';
    }

    private boolean BooleanOperatorExists(int i, List<String> booleanOperator) {
        return booleanOperator == null ? false : i < booleanOperator.size() ? true : false;
    }

    private char[] GetLogExpr(List<logicalExspression> logicalExpr, List<String> booleanOperator) {
        String result = "";
        for (int i = 0; i < logicalExpr.size(); i++){
            if (ContainsSpecie(logicalExpr.get(i))){
                result += 'S';
            }else{
                if (AlwaysTrue(logicalExpr.get(i))){
                    result += '1';
                }else{
                    result +='0';
                }
            }
        }
        return result.toCharArray();
    }

    private boolean AlwaysTrue(logicalExspression log) {
        String rhs = CALC.Calculate((Stack<String>) log.rhs.clone());
        String lhs = CALC.Calculate((Stack<String>) log.lhs.clone());

        float value = Float.valueOf(rhs) - Float.valueOf(lhs);

        switch (log.logicalOperator){
            case "<": return value > 0 ? true : false;
            case ">": return value > 0 ? false : true;
            case "<=": return value >= 0 ? true : false;
            case ">=": return value <= 0 ? true : false;
            case "==": return value == 0 ? true : false;
            case "!=": return value == 0 ? false : true;
            default:
                System.out.println("you are not supposed to be here");
                return true;
        }
    }

    private boolean ContainsSpecie(logicalExspression logicalExspression) {
        return StackContainsSpecie((Stack<String>)logicalExspression.lhs.clone()) || StackContainsSpecie((Stack<String>)logicalExspression.rhs.clone()) ? true : false;
    }

    private boolean StackContainsSpecie(Stack<String> clone) {
        while (!clone.isEmpty()){
            if (IsString(clone.pop())){
                return true;
            }
        }
        return false;
    }

    private boolean IsString(String pop) {
        return !Character.isDigit(pop.charAt(0)) && !checker.IsOperator(pop);
    }
}
