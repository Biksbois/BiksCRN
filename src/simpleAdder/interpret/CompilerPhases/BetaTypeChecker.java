package simpleAdder.interpret.CompilerPhases;

import com.company.analysis.DepthFirstAdapter;
import com.company.node.*;
import simpleAdder.interpret.GetMethods.Get;
import simpleAdder.interpret.GetMethods.ViableVariable;
import simpleAdder.interpret.Objects.SymolTableOBJ.parameter;
import simpleAdder.interpret.Objects.SymolTableOBJ.protocolOperation;
import simpleAdder.interpret.Objects.SymolTableOBJ.reaction;
import simpleAdder.interpret.TypeCheckers.BetaStackCalculator;
import simpleAdder.interpret.TypeCheckers.BiksPair;
import simpleAdder.interpret.TypeCheckers.OptimizedStackToString;

import java.util.*;

public class BetaTypeChecker extends DepthFirstAdapter {
    public BetaSymbolTable st;
    private final BetaStackCalculator BSC = new BetaStackCalculator();
    private final Get get = new Get();
    TerminateProgram terminate = new TerminateProgram();
    OptimizedStackToString CALC = new OptimizedStackToString();

    public BetaTypeChecker() {
        st = new BetaSymbolTable();
    }

    /***
     * Creates a float, get the expression and add to symbol table
     * @param node
     */
    public void caseAFloatNumber(AFloatNumber node)
    {
        if (get.VerifyProperties(node)){
            terminate.terminate_program("Either the expression, recursion or string was null (caseAFloatNumber)");
        }
        String id = get.Id(node);
        st.CreateInstance(ViableVariable.FLOAT, id, "caseAFloatNumber");
        node.getExpression().apply(this);
        st.NodeToCurrentScope(node.getTString());
        node.getFloats().apply(this);
    }

    /***
     * Creates a float, get the expression, add to symbol table and apply the next specie
     * @param node
     */
    public void caseAMultipleFloats(AMultipleFloats node)
    {
        if (get.VerifyProperties(node)){
            terminate.terminate_program("Either the expression, recursion or string was null (caseAMultipleFloats)");
        }
        String id = get.Id(node);
        st.CreateInstance(ViableVariable.FLOAT, id, "caseAMultipleFloats");
        node.getExpression().apply(this);
        st.NodeToCurrentScope(node.getTString());
        node.getFloats().apply(this);
    }

    /***
     * Creates a integer, get the expression and add to symbol table
     * @param node
     */
    public void caseAIntNumber(AIntNumber node)
    {
        if (get.VerifyProperties(node)){
            terminate.terminate_program("Either the expression, recursion or string was null (caseAFloatNumber)");
        }
        String id = get.Id(node);
        st.CreateInstance(ViableVariable.INT, id, "caseAFloatNumber");
        node.getExpression().apply(this);
        st.NodeToCurrentScope(node.getTString());
        node.getIntegers().apply(this);
    }

    /***
     * Creates a integer, get the expression, add to symbol table and apply the next specie
     * @param node
     */
    public void caseAMultipleIntegers(AMultipleIntegers node)
    {
        if (get.VerifyProperties(node)){
            terminate.terminate_program("Either the expression, recursion or string was null (caseAFloatNumber)");
        }
        String id = get.Id(node);
        st.CreateInstance(ViableVariable.INT, id, "caseAMultipleIntegers");
        node.getExpression().apply(this);
        st.NodeToCurrentScope(node.getTString());
        node.getIntegers().apply(this);
    }

    /***
     * Creates a specie, get the expression, add to symbol table and apply the next specie
     * @param node
     */
    public void caseAMultipleSpecie(AMultipleSpecie node)
    {
        if (get.VerifyProperties(node)){
            terminate.terminate_program("Something was null (caseAMultipleSpecie)");
        }
        String id = get.Id(node);
        st.CreateInstance(ViableVariable.SPECIE, id, "caseAMultipleSpecie");
        node.getExpression().apply(this);
        st.NodeToCurrentScope(node.getTString());
        node.getSpecie().apply(this);
    }

    /***
     * Creates a specie, gets the expressions, and adds it to the symbol table
     * @param node
     */
    public void caseASingleSpecie(ASingleSpecie node)
    {
        if (get.VerifyProperties(node)){
            terminate.terminate_program("Something was null (caseAMultipleSpecie)");
        }
        String id = get.Id(node);
        st.CreateInstance(ViableVariable.SPECIE, id, "caseAMultipleSpecie");
        node.getExpression().apply(this);
        st.NodeToCurrentScope(node.getTString());
    }

    /***
     * Applies a negative number to an expression
     * @param node
     */
    public void caseANegativeParenPolarity(ANegativeParenPolarity node)
    {
        if (get.VerifyProperties(node)){
            terminate.terminate_program("Something was null (caseANegativeParenPolarity)");
        }
        node.getTLParen().apply(this);
        node.getFactor().apply(this);
        st.SymbolToStack(node);
        node.getTRParen().apply(this);
    }

    public void caseTTLParen(TTLParen node){
        st.SymbolToStack(node);
    }
    public void caseTTRParen(TTRParen node){
        st.SymbolToStack(node);
    }
    public void outAPlusOperator(APlusOperator node)
    {
        st.SymbolToStack(node);
    }
    public void outAMinusOperator(AMinusOperator node)
    {
        st.SymbolToStack(node);
    }
    public void outAFactorTerm(AFactorTerm node){}
    public void outAMultTerm(AMultTerm node)
    {
        st.SymbolToStack(node);
    }
    public void outADivideTerm(ADivideTerm node)
    {
        st.SymbolToStack(node);
    }
    public void outAOptermPower(AOptermPower node)
    {
        st.SymbolToStack(node);
    }

    /***
     * Adds a float to a stack, i a stack instance exists
     * @param node
     */
    public void caseAFloatFactor(AFloatFactor node)
    {
        if(node.getTFloat() != null && st.StackinstanceExists())
        {
            st.SymbolToStack(node.getTFloat().toString().trim(), ViableVariable.FLOAT, node.getTFloat());
        }
    }

    /***
     * Adds a variable to a stack, i a stack instance exists
     * @param node
     */
    public void caseAVariableFactor(AVariableFactor node)
    {
        if(node.getTString() != null && st.StackinstanceExists())
        {
            st.SymbolToStack(node.getTString().toString().trim(), ViableVariable.Variable, node.getTString());
        }
    }

    /***
     * Adds a integer to a stack, i a stack instance exists
     * @param node
     */
    public void caseAIntegerFactor(AIntegerFactor node)
    {
        if(node.getTInt() != null && st.StackinstanceExists())
        {
            st.SymbolToStack(node.getTInt().toString().trim(), ViableVariable.INT, node.getTInt());
        }
    }

    /***
     * Create a Rate, adds to symbol table, applies then next rate.
     * @param node
     */
    public void caseAMultipleRates(AMultipleRates node)
    {
        if(get.VerifyProperties(node))
        {
            terminate.terminate_program("Something was null (caseAMultipleRates)");
        }
        st.NodeToCurrentScope(node);
        node.getRates().apply(this);
    }

    /***
     * Creates a Rate, and adds it to the symbol table
     * @param node
     */
    public void caseASingleRates(ASingleRates node)
    {
        if(get.VerifyProperties(node))
        {
            terminate.terminate_program("Something was null (caseASingleRates)");
        }
        st.NodeToCurrentScope(node);
    }

    /***
     * This methods crreates a function object, applies the inputs and body,
     * and adds it to the symbol table.
     * @param node
     */
    public void caseALambdaFuncFunc(ALambdaFuncFunc node)
    {
        if(get.VerifyProperties(node))
        {
            terminate.terminate_program("Something was null (caseALambdaFuncFunc)");
        }
        String id = get.Id(node);
        String type = get.Type(node);
        st.CreateInstance(type, id,"caseALambdaFuncFunc");
        node.getInput().apply(this);
        node.getExpression().apply(this);
        st.NodeToCurrentScope(node.getTString());
    }

    /***
     * This creates a input object to a function, adds it to a list and applies the next input.
     * @param node
     */
    public void caseAMultiInput(AMultiInput node)
    {
        if(get.VerifyProperties(node))
        {
            terminate.terminate_program("Something was null (caseAMultiInput)");
        }
        String type = get.Type(node);
        String id = get.Id(node);
        st.addObjectToList(new parameter(id,type));
        node.getInput().apply(this);
    }

    /***
     * This creates a input object to a function, and adds it to a list.
     * @param node
     */
    public void caseASingleInput(ASingleInput node)
    {
        if(get.VerifyProperties(node))
        {
            terminate.terminate_program("Something was null (caseAMultiInput)");
        }
        String type = get.Type(node);
        String id = get.Id(node);
        st.addObjectToList(new parameter(id,type));
    }

    /***
     * This is when a function contains a void Paramers. When it is called,
     * the function is checked if it contains other parameters, since is not allowed.
     * @param node
     */
    public void caseAVoidInput(AVoidInput node)
    {
        if(get.VerifyProperties(node))
        {
            terminate.terminate_program("Something was null");
        }
        if(!st.isVoid())
        {
            terminate.terminate_program("Void should be the only parameter in a function (caseAVoidInput)");
        }
    }

    /***
     * Creates an object of a sample, and applies all the attributes.
     * @param node
     */
    public void caseASampleinitSample(ASampleinitSample node)
    {
        if(get.VerifyProperties(node))
        {
            terminate.terminate_program("Something was null (caseASampleinitSample)");
        }
        st.CreateInstance("caseASampleinitSample");
        node.getSamplefunc().apply(this);
        st.SampleToSymboltable(node);

    }

    /***
     * This applies all the reactions, and adds them the list of reactions. At the end
     * the reaction is verified and added to the sample.
     * @param node
     */
    public void caseABlockCrnfunc(ABlockCrnfunc node)
    {
        if(get.VerifyProperties(node))
        {
            terminate.terminate_program("Something was null (caseABlockCrnfunc)");
        }
        st.CreateInstance(ViableVariable.CRN,"caseABlockCrnfunc");

        List<PReaction> copy = new ArrayList<PReaction>(node.getReaction());
        for(PReaction e : copy)
        {
            e.apply(this);
            st.temp.ObjectToList();
        }
        st.NodeToCurrentScope(node.getTLTurborg());
    }

    /***
     * Creates the reaction object. At this point we don't know if it is a double or single, but
     * the reactant is set, and the reaction is applied.
     * @param node
     */
    public void caseADoubleReaction(ADoubleReaction node)
    {
        if(get.VerifyProperties(node) && st.temp != null)
        {
            terminate.terminate_program("Something was null (caseADoubleReaction)");
        }

        st.temp.CreateObjecte("caseADoubleReaction");
        node.getReactant().apply(this);
        node.getArrows().apply(this);
    }

    /***
     * This method handles reactoins in the syntax:
     * INT MULT SPECIE PLUS REACTANT
     * Get name and value for the reaction and add as a
     * pair to the reaction
     * @param node
     */
    public void caseAMultipleReactant(AMultipleReactant node)
    {
        String specie = node.getTString().toString().trim();
        String value = FactorToValues(node.getFactor());
        st.reactionToReaction(new BiksPair<>(specie,value), true, node.getTString());
        node.getReactant().apply(this);
    }

    /***
     * This method handles reactoins in the syntax:
     * SPECIE PLUS REACTANT
     * Get name and set value to "1" for the reaction and add as a
     * pair to the reaction
     * @param node
     */
    public void caseAPlusReactant(APlusReactant node)
    {
        String specie = node.getTString().toString().trim();
        String value = "1";
        st.reactionToReaction(new BiksPair<>(specie,value), true, node.getTString());
        node.getReactant().apply(this);
    }

    /***
     * This method handles reactoins in the syntax:
     * SPECIE
     * Get name and set value to "1" for the reaction and add as a
     * pair to the reaction
     * @param node
     */
    public void caseAStringReactant(AStringReactant node)
    {
        String specie = node.getTString().toString().trim();
        String value = "1";
        st.reactionToReaction(new BiksPair<>(specie,value), false, node.getTString());
    }

    /***
     * This method handles reactoins in the syntax:
     * INT MULT SPECIE
     * Get name and value for the reaction and add as a
     * pair to the reaction
     * @param node
     */
    public void caseASingleReactant(ASingleReactant node)
    {
        String specie = node.getTString().toString().trim();
        String value = FactorToValues(node.getFactor());
        st.reactionToReaction(new BiksPair<>(specie,value), false, node.getTString());
    }

    /***
     * This method translates a factor to its actual value, and returns as a string. Checks if it is a
     * variable, in this case it gets its value and type from symbol table, or a
     * integer or float.
     * @param node
     * @return
     */
    public String FactorToValues(PFactor node){
        if(node instanceof AVariableFactor)
        {
            String key = ((AVariableFactor) node).getTString().toString().trim();
            if(st.VerifyKeyAndTypeInBoth(key, ViableVariable.INT) || st.VerifyKeyAndTypeInBoth(key, ViableVariable.FLOAT)) {
                return st.GetValue(key);
            }else {
                terminate.WrongType(((AVariableFactor) node).getTString(), st.TypeForMessage(key), "integer or float", key, "FactorToValues");
            }
        }
        else if(node instanceof AFloatFactor)
        {
            return ((AFloatFactor) node).getTFloat().toString().trim();
        }else if(node instanceof AIntegerFactor)
        {
            return ((AIntegerFactor) node).getTInt().toString().trim();
        }else
        {
            terminate.terminate_program("This should never happen wtf did you do you fucking swine (FactorToValues)");
        }
        return null;
    }
    /***
     * This adds atributes to a reaction, in the case of a single reaction. A boolean is set,
     * the product is set, and the rate is applied.
     * @param node
     */
    public void caseAOneWayArrows(AOneWayArrows node)
    {
        if(get.VerifyProperties(node))
        {
            terminate.terminate_program("Something was null (caseAOneWayArrows)");
        }
        node.getReactant().apply(this);
        st.SetBoolean(true, "caseAOneWayArrows");

        Node rNode = get.Factor(node);

        if(rNode instanceof AUnitReactionRateI)
        {
            String rate = get.Rate((AUnitReactionRateI) rNode);
            st.AddStringToField(rate,"caseAOneWayArrows");
        }
        else
        {
            node.getReactionRateI().apply(this);
        }
    }

    /***
     * This adds atributes to a reaction, in the case of a double reaction. A boolean is set,
     * the product is set, and the rates are applied.
     * @param node
     */
    public void caseATwoWayArrows(ATwoWayArrows node)
    {
        if (get.VerifyProperties(node)){
            terminate.terminate_program("Something was null (caseATwoWayArrows)");
        }

        node.getReactant().apply(this);
        st.SetBoolean(false, "caseATwoWayArrows");

        Node rNode = get.Factor(node);

        if (rNode instanceof AUnitReactionRateI){
            String rate = get.Rate((AUnitReactionRateI) rNode);
            st.AddStringToField(rate, "caseATwoWayArrows");
        }
        else
        {
            node.getReactionRateI().apply(this);
        }

        node.getReactionRateIi().apply(this);
    }

    /***
     * This adds atributes to a reaction object. The rate can either be a Rate or a function
     * either is checked and applied.
     * @param node
     */
    public void caseAReactionRateReactionRateIi(AReactionRateReactionRateIi node)
    {
        if (get.VerifyProperties(node)){
            terminate.terminate_program("something was null (caseAReactionRateReactionRateIi)");
        }
        Node rNode = node.getReactionRateI();

        if (rNode instanceof AUnitReactionRateI){
            String rate = get.Rate((AUnitReactionRateI) rNode);
            st.AddStringToField(rate, "caseAReactionRateReactionRateIi");
        }else {
            node.getReactionRateI().apply(this);
        }
    }

    /***
     * This method is used when a function is called, and parameters are applied.
     * It gets the input parameters and verifies them.
     * @param node
     */
    public void caseAFCallFuncCall(AFCallFuncCall node)
    {
        if (get.VerifyProperties(node)){
            terminate.terminate_program("Something was null (caseAFCallFuncCall)");
        }
        String funcName = get.FunctionName(node);
        if (!st.VerifyKeyAndTypeInBoth(funcName, ViableVariable.FUNC)){
            terminate.VarDontExist(node.getTString(), funcName, "Function", "caseAFCallFuncCall");
            //terminate.terminate_program("Function \"" + funcName + "\" is not defined (caseAFCallFuncCall)");
        }
        st.temp.CreateObjecte("", "caseAFCallFuncCall");
        node.getFuncParen().apply(this);
        st.VerifyParameters(funcName, "caseAFCallFuncCall");
    }

    /***
     * This method initializes the list of titrations, applies the getTitrations, which fills
     * thelist up, and adds the list to the symbol table. Can either be RemMol or AddMol
     * @param node
     */
    public void caseADclTitration(ADclTitration node)
    {
        if(get.VerifyProperties(node))
        {
            terminate.terminate_program("Something was null (caseADclTitration)");
        }
        st.CreateInstance(ViableVariable.TITRATIONLIST,"caseADclTitration");
        node.getTitrations().apply(this);
        st.NodeToCurrentScope(node);
    }

    /***
     * Creates a titration object and adds to the list of tittrations.
     * @param node
     */
    public void caseASingleTitrations(ASingleTitrations node)
    {
        if(get.VerifyProperties(node))
        {
            terminate.terminate_program("Something was null (caseASingleTitrations)");
        }
        NodeToFactor(node,"caseASingleTitrations");
        node.getTitrationEnd().apply(this);
        st.temp.OptimizeConditionsAndAdd();
        //st.temp.ObjectToList();
    }

    /***
     * Cast a ASingleTitrations node to a PFacor node and call method CreatTitNodeObject
     * @param node
     * @param method
     */
    public void NodeToFactor(ASingleTitrations node, String method)
    {
        if (!st.VerifyKeyAndTypeInBoth(get.Specie(node), ViableVariable.SPECIE)){
            terminate.terminate_program("It should be of the species type");
        }

        if (!st.VerifySpecie(get.Specie(node))){
            terminate.terminate_program("Specie " + get.Specie(node) + " cannot be used in titration since it is not included in any reactions.");
        }

        CreateTitNodeObject(node.getFactor(), get.Specie(node), method, node.getTString());
    }



    /***
     * Cast a AMultipleTitration node to a PFacor node and call method CreatTitNodeObject
     * @param node
     * @param method
     */
    public void NodeToFactor(AMultipleTitrations node, String method)
    {
        String specie = node.getTString().toString().trim();
        if (!st.VerifyKeyAndTypeInBoth(specie, ViableVariable.SPECIE)){
            terminate.terminate_program("It should be of the species type");
        }
        CreateTitNodeObject(node.getFactor(), specie, method, node.getTString());
    }

    /***
     * This is called using the NodeToFactor which casts a Titratoin node to PFactor which is used by both
     * multiple and single titration. This method creates the titration object.
     * @param node
     * @param species
     * @param method
     */
    public void CreateTitNodeObject(PFactor node, String species, String method, Token token)
    {
        String value ="";
        if(node instanceof AIntegerFactor)
        {
            value = ((AIntegerFactor) node).getTInt().toString().trim();
        }else if(node instanceof AFloatFactor)
        {
            value = get.Key(node);
        }else{
            String key = get.Key(node);
            String type = st.GetType(key, token);
            value = st.GetValue(key);
            if(!get.IsValidType(type))
            {
                terminate.terminate_program("Invalid type assignment ("+method+")");
            }
        }
        if(get.IsNegative(value) || Float.parseFloat(value) == 0)
        {
            terminate.terminate_program("A rate cannot be negative or zero ("+method+")");
        }
        st.temp.CreateObjecte(species,method,value);
    }

    /***
     * Creates a titration objct with species and value, and applies then en which coud be a loop
     * or noting, adds to list, resets, and applies the next titration. Recursivly.
     * @param node
     */
    public void caseAMultipleTitrations(AMultipleTitrations node)
    {
        if(get.VerifyProperties(node))
        {
            terminate.terminate_program("Something was null (caseAMultipleTitrations)");
        }
        NodeToFactor(node,"caseAMultipleTitrations");
        node.getTitrationContinue().apply(this);
        //st.temp.ObjectToList();
        st.temp.OptimizeConditionsAndAdd();
        node.getTitrations().apply(this);
    }

    /***
     * Creaets a object on a titration with the logical expressions, which are
     * applied.
     * @param node
     */
    public void caseAWhileLoop(AWhileLoop node)
    {
        if(get.VerifyProperties(node))
        {
            terminate.terminate_program(node.getTLParen(), "Something was null (caseAWhileLoop)");
        }
        st.temp.CreateObjecte("caseAWhileLoop");
        node.getLogicalExprs().apply(this);
    }

    /***
     * This node is called from logical_expr. It creates an object of a logical operator, and adds
     * the atribues from the node to it, two expressions and a logical operator in between, and it
     * is added to a list, and the object is set to null after again.
     * @param node
     */
    public void caseASingleLogicalExpr(ASingleLogicalExpr node)
    {
        if(get.VerifyProperties(node))
        {
            terminate.terminate_program("Something was null (caseASingleLogicalExpr)");
        }
        st.loopSide = new Stack<>();
        node.getExpression().apply(this);
        Stack<String> lhs = (Stack<String>) st.loopSide.clone();
        st.loopSide = new Stack<>();

        node.getDouble().apply(this);
        Stack<String> rhs = (Stack<String>) st.loopSide.clone();
        st.loopSide = null;

        st.temp.CreateObjecte(rhs,lhs,get.LogicalOperator(node),"caseASingleLogicalExpr");
        st.temp.ObjectToList();
    }

    /***
     * This is a recursive version of the method above, where besides creating the oject, it also applies the next
     * logical expressions.
     * @param node
     */
    public void caseAMultipleLogicalExprs(AMultipleLogicalExprs node)
    {
        if(get.VerifyProperties(node))
        {
            terminate.terminate_program("Something was null (caseAMultipleLogicalExprs)");
        }
        node.getLogicalExpr().apply(this);
        st.AddStringToField(get.BooleanOperator(node),"caseAMultipleLogicalExprs");
        node.getLogicalExprs().apply(this);
    }

    /***
     * This is one possible protocol body, where either Mix or Split is called. At this point we do not know which one it is
     * so we apply the node, where the correct instance is created and check by instance of which one it is, and apply this one.
     * @param node
     */
    public void caseASampleProtocolbody(ASampleProtocolbody node)
    {
        if(node.getTString() == null || node.getSampleref() == null)
        {
            terminate.terminate_program("Something was null (caseASampleProtocolbody)");
        }
        if(!st.VerifyKeyAndTypeInST(get.Id(node), ViableVariable.SAMPLE))
        {
            terminate.terminate_program("It is only possible to mix objects of type sample (caseASampleProtocolbody)");
        }
        node.getSampleref().apply(this);
        if (node.getSampleref() instanceof AMixSampleref)
        {
            st.tempProtocol.mix.ResultingSample = get.Id(node);
            st.protocols.push(st.tempProtocol);
            st.tempProtocol = null;
        }
        else if(node.getSampleref() instanceof ASplitSampleref)
        {
            st.AddStringToField(get.Id(node), "caseASampleProtocolbody");

            BiksPair<Boolean, String> pair = st.tempProtocol.split.IsDistributionvalueValid();
            if (!pair.getKey()){
                terminate.terminate_program(pair.getValue() + " (caseASplitSampleref)");
            }

            st.protocols.push(st.tempProtocol);
            st.tempProtocol = null;
        }
        else{
            terminate.terminate_program("No case was hit (caseASampleProtocolbody)");
        }
    }

    /***
     * THis is the Dispose protocol. This method does not apply any node attributes, since it can get them all.
     * First we get the sample we wish to dispose, and verify it is a sample. After we get the parameters, this
     * is done by checking if the parameters is instance of a ANonzeroDisposePara, since this is the only we we
     * can get parameters. If so, we get the value from the parameters and add to symbol table. If no parameters
     * we add to symbol table without parameters.
     * @param node
     */
    public void caseADisposeProtocolbody(ADisposeProtocolbody node)
    {
        if (get.VerifyProperties(node)){
            terminate.terminate_program("Something was null (caseADisposeProtocolbody)");
        }

        Node input = node.getDisposePara();
        if(!st.VerifyKeyAndTypeInST(get.Sample(node), ViableVariable.SAMPLE))
        {
            terminate.terminate_program("It is only possible to dispose a sample (caseADisposeProtocolbody)");
        }

        if (input instanceof ANonzeroDisposePara){
            input = ((ANonzeroDisposePara) input).getFactor();
            if (input instanceof AVariableFactor){
                String key = ((AVariableFactor) input).getTString().toString().trim();
                //String type = st.GetType(((AVariableFactor) input).getTString().toString().trim());
                if(st.VerifyKeyAndTypeInST(key, ViableVariable.FLOAT) || st.VerifyKeyAndTypeInST(key, ViableVariable.INT))
                {
                    String value = st.GetValue(((AVariableFactor) input).getTString().toString().trim());
                    if (ValueExceedsLimits(value)){
                        terminate.terminate_program("When disposing, the value should be between 0 and 1. \""  +key + "\" with value " + value + " exceeds this limit.");
                    }
                    st.tempProtocol = new protocolOperation(ViableVariable.DISPOSE, get.Sample(node), value);
                }
                else
                {
                    terminate.terminate_program("It is only possible to dispose using float and int types or void (caseADisposeProtocolbody)");
                }
            }
            else
            {
                String value = input.toString().trim();

                if (ValueExceedsLimits(value)){
                    terminate.terminate_program("When disposing, the value should be between 0 and 1. the value of " + value + " exceeds this limit.");
                }

                st.tempProtocol = new protocolOperation(ViableVariable.DISPOSE, get.Sample(node), value);
            }

        }
        else
        {
            st.tempProtocol = new protocolOperation(ViableVariable.DISPOSE, get.Sample(node), "1");
        }
        st.protocols.push(st.tempProtocol);
        st.tempProtocol = null;
    }

    private boolean ValueExceedsLimits(String value){
        return Float.parseFloat(value) > 1 || Float.parseFloat(value) < 0;
    }

    /***
     * This is the protocol Mix, and the object is created here. Besides that, the first sample we wish
     * to mix is added. At last the rest of the samples we wish to mix are applied.
     * @param node
     */
    public void caseAMixSampleref(AMixSampleref node)
    {
        if(get.VerifyProperties(node))
        {
            terminate.terminate_program("Unexspected null in mix function (caseAMixSampleref)");
        }
        if(st.tempProtocol != null)
        {
            terminate.terminate_program("Should be null (caseAMixSampleref)");
        }
        st.tempProtocol = new protocolOperation(ViableVariable.MIX);
        st.tempProtocol.addToMix(node.getTString().toString().trim(),"caseAMixSampleref"); //TODO AddStringAttribute
        node.getProtocolparam().apply(this);
    }

    /***
     * This is the protocol "Split" whre the instance is created, and the t_string, which is the node
     * we split, is vefified is a sample added as an atribute. Following the next samples to split into
     * are applied and the species it is split into, and the percents each piece gets.
     * @param node
     */
    public void caseASplitSampleref(ASplitSampleref node)
    {
        if(get.VerifyProperties(node) && st.tempProtocol != null)
        {
            terminate.terminate_program("Something was null (caseASplitSampleref)");
        }
        st.tempProtocol = new protocolOperation(ViableVariable.SPLIT);
        if(!st.VerifyKeyAndTypeInST(get.Sample(node), ViableVariable.SAMPLE))
        {
            terminate.terminate_program("It is only possible to split the object type sample (caseASplitSampleref)");
        }
        st.AddStringToField(get.Sample(node), "caseASplitSampleref");
        node.getProtoexstend().apply(this);
        node.getFuncParameters().apply(this);
    }

    /***
     * This is called by mix and is the samples we wish to mix together. It gets a t_string
     * verfies it is a sample in the symbol talbe, and adds to mix
     * @param node
     */
    public void caseASingleProtocolparam(ASingleProtocolparam node)
    {
        if(get.VerifyProperties(node))
        {
            terminate.terminate_program("null (caseASingleProtocolparam)");
        }
        if(!st.VerifyKeyAndTypeInST(get.Sample(node), ViableVariable.SAMPLE))
        {
            terminate.terminate_program("It is only possible to mix objects of type sample (caseAMultiProtocolparam)");
        }
        st.tempProtocol.addToMix(get.Sample(node),"caseASingleProtocolparam"); //TODO AddStringAttribute
    }

    /***
     * This is the recursive version of the method above, where besdes getting the
     * t_string and verifing it is a sample, also applies the rest
     * @param node
     */
    public void caseAMultiProtocolparam(AMultiProtocolparam node)
    {
        if (get.VerifyProperties(node)){
            terminate.terminate_program("Something was null, while it really not be (caseAMultiProtocolparam)");
        }
        if(!st.VerifyKeyAndTypeInST(get.Sample(node), ViableVariable.SAMPLE))
        {
            terminate.terminate_program("It is only possible to mix objects of type sample (caseAMultiProtocolparam)");
        }
        st.tempProtocol.addToMix(get.Sample(node),"caseAMultiProtocolparam");
        node.getProtocolparam().apply(this);
    }

    /***
     * This is called by "split", and it gets a t_string from the node, verify it is a sample in the
     * symbol table, and adds it the the active version of split.
     * @param node
     */
    public void caseASingleProtoexstend(ASingleProtoexstend node)
    {
        if(get.VerifyProperties(node))
        {
            terminate.terminate_program("Something was null (caseASingleProtoexstend)");
        }
        String sample = get.Sample(node);
        if(!st.VerifyKeyAndTypeInST(sample, ViableVariable.SAMPLE))
        {
            terminate.terminate_program("It is only possible to split samples in split (caseASingleProtoexstend)");
        }
        st.tempProtocol.addToSplit(ViableVariable.SAMPLE,sample,"caseASingleProtoexstend"); //TODO convert to AddStringAttribute
    }

    /***
     * This is the recursive version of the method above, where besides getting the
     * t_string, vefirying it is a sample in the symbol table, also applies the rest.
     * @param node
     */
    public void caseAMultiProtoexstend(AMultiProtoexstend node)
    {
        if(get.VerifyProperties(node))
        {
            terminate.terminate_program("Something was null (caseAMultiProtoexstend)");
        }
        String Sample = get.Sample(node);
        if(!st.VerifyKeyAndTypeInST(Sample, ViableVariable.SAMPLE))
        {
            terminate.terminate_program("It is not possible use other than sample in the split function (caseAMultiProtoexstend)");
        }
        st.tempProtocol.addToSplit(ViableVariable.SAMPLE,Sample,"caseAMultiProtoexstend");
        node.getProtoexstend().apply(this);
    }

    public void caseATimeOption(ATimeOption node){
    }
    public void caseACycleOption(ACycleOption node){

    }

    /***
     * Creates a object of protocolObject by creating a equilibrate object from the ASingleEquili node
     * No attributes are applied, but we get the t_string and t_factor attributes.
     * @param node
     */
    public void outASingleEquili(ASingleEquili node)
    {
        Node eNode = node.getExtendequili();
        String bitesize = "1";
        String stepSize = "0.0025";
        String interval = "100";
        String cycles = "";

        if(eNode instanceof AStepExtendequili)
        {
            stepSize = FactorToValues(((AStepExtendequili) eNode).getFactor());
            if (stepSize.contains("-")){
                terminate.ShouldBePositive(node.getTString(), stepSize, ((AStepExtendequili) eNode).getFactor().toString().trim(), "outASingleEquili");
            }
        }

        String sample = get.Sample(node);
        if(st.VerifyKeyAndTypeInBoth(sample, ViableVariable.SAMPLE))
        {
            cycles = GetPositiveWholeFactor(node.getFactor(), node.getTString());

            Node unit = node.getOption();
            if(unit instanceof ATimeOption){
                cycles = String.valueOf(Math.floor( Float.parseFloat(cycles) / Float.parseFloat(stepSize)));
            }
            CheckFunctionValues((int) Float.parseFloat(cycles), sample, node.getTString());
            Node step = node.getExtendequili();
            if (step instanceof ASemiExtendequili){
                Node Interval = ((ASemiExtendequili) step).getTimestep();
                if (Interval instanceof AWithTimestep){
                    interval = GetTimestep((AWithTimestep) Interval);
                    bitesize = GetBiteSize(((AWithTimestep) Interval).getBitesize());

                }else if(Interval instanceof ANoTimestep){
                    bitesize = GetBiteSize(((ANoTimestep) Interval).getBitesize());
                }
            }else if (step instanceof AStepExtendequili){
                Node Interval = ((AStepExtendequili) step).getTimestep();
                if (Interval instanceof AWithTimestep){
                    interval = GetTimestep((AWithTimestep) Interval);
                    bitesize = GetBiteSize(((AWithTimestep) Interval).getBitesize());
                }else if(Interval instanceof ANoTimestep){
                    bitesize = GetBiteSize(((ANoTimestep) Interval).getBitesize());
                }
                stepSize = FactorToValues(((AStepExtendequili) step).getFactor());
            }
            st.protocols.push(new protocolOperation(ViableVariable.EQUILIBRATE,sample, cycles, stepSize, interval, bitesize));
        }
        else
        {
            terminate.WrongType(node.getTString(), st.TypeForMessage(sample), ViableVariable.SAMPLE, sample, "outASingleEquili");
        }
    }

    private String GetBiteSize(PBitesize node) {
        if (node instanceof AWithBitesize){
            return GetPositiveWholeFactor(((AWithBitesize) node).getFactor(), ((AWithBitesize) node).getTBitesize());
        }else{
            return "1";
        }
    }

    private String GetTimestep(AWithTimestep node){
        return GetPositiveWholeFactor(node.getFactor(), node.getTEach());
    }

    private String GetPositiveWholeFactor(PFactor factor, Token token){
        String value = FactorToValues(factor);
        if (!st.isWholePositiveFloat(value)){
            terminate.ShouldBeWholeAndPositive(token, value, "GetPositiveWholeFactor");
        }
        return value;
    }

    public void CheckFunctionValues (int endValue, String sample, Token token) {
        if (st.st.get(sample).scope.containsKey(ViableVariable.CRN)) {
            for (reaction reac : st.st.get(sample).scope.get(ViableVariable.CRN).crn) {
                CheckStacksOneValues(reac.lhs, reac.rhs, 0, sample, token);
                CheckStacksOneValues(reac.lhs, reac.rhs, 1, sample, token);
                CheckStacksOneValues(reac.lhs, reac.rhs, endValue, sample, token);
            }
        }
    }

    public void CheckStacksOneValues(Stack<String> rhs, Stack<String> lhs, int limit, String sample, Token token){
        if (!CalcOneStack(lhs, limit) || !CalcOneStack(rhs, limit)) {
            terminate.FunctionBecomesNegative(token, sample, limit, "outASingleEquili");
        }
    }

    public boolean CalcOneStack(Stack<String> stack, int end)
    {
        Stack<String> temp;
        if(stack != null)
        {
            temp = ReplaceStack((Stack<String>) stack.clone(), end);
            float f = Float.parseFloat(CALC.Calculate(temp));
            return !(f < 0);
        }

        return true;
    }

    public Stack<String> ReplaceStack(Stack<String> stack, int value)
    {
        Stack<String> result = new Stack<>();
        while (!stack.empty())
        {
            if(stack.peek().equals(ViableVariable.CYCLE))
            {
                stack.pop();
                result.push(Integer.toString(value));
            }
            else
            {
                result.push(stack.pop());
            }
        }
        return BSC.ReverseStack(result);
    }
}
