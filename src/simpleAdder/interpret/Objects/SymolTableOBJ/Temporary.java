package simpleAdder.interpret.Objects.SymolTableOBJ;

import javafx.util.Pair;
import simpleAdder.interpret.TypeCheckers.Check;
import simpleAdder.interpret.TypeCheckers.TypeHelperMethods;
import simpleAdder.interpret.GetMethods.ViableVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Temporary {
    TypeHelperMethods TH = new TypeHelperMethods();
    ViableVariable vv = new ViableVariable();
    Check check = new Check();

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
                Prim = check.ValueType.NewPrimitiveType(Prim, vv.INT, id, method, true, true, false, false, true, false);
                break;
            case ViableVariable.SPECIE:
                Prim = check.ValueType.NewPrimitiveType(Prim, vv.SPECIE, id, method, true, true, true, false, false, false);
                break;
            case ViableVariable.FLOAT:
                Prim = check.ValueType.NewPrimitiveType(Prim, vv.FLOAT, id, method, true, true, false, false, true, true);
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

    public void reactionToReaction(Pair<String, String> R, Boolean isFirst){
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
        Pair<Boolean, Stack<String>> pair = check.FuncType.ParametersToStack(Para, func);
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
    public Pair<String, SymbolTableType> GetInstanece(){
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
            return new Pair<>(vv.CRN,new SymbolTableType(vv.CRN,vv.CRN,crn));
        }

        TH.terminate_program("No possible outcome was met (GetInstanece)");
        return new Pair<String, SymbolTableType>("", new SymbolTableType("","",""));
    }

    /***
     * Returns the active instance
     * @return
     */
    public  Pair<String, SymbolTableType> GetInstanece(String key)
    {
        if(TitList != null && Tit == null)
        {
            return new Pair<>(key,new SymbolTableType(key,TitList,vv.TITRATIONLIST));
        }
        else
        {
            TH.terminate_program("No possible outcome was met (GetInstanece)");
            return new Pair<String, SymbolTableType>("", new SymbolTableType("","",""));
        }
    }

    /***
     * Chekcs if it is possible to add to a stack in one of the instances in this class
     * @return
     */
    public boolean StackinstanceExists(){
        if (Prim != null || Func != null || Reac != null || LogExpr != null){
            return true;
        }
        return false;
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
}
