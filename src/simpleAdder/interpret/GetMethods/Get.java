package simpleAdder.interpret.GetMethods;

import com.company.node.*;

public class Get {
    ErrorMessage error = new ErrorMessage();

    public String ErrorMessage(PFactor node, String message,  String method){
        return error.Get(node, message, method);
    }

    ViableVariable vv = new ViableVariable();

    public String LogicalOperator(ASingleLogicalExpr node){
        return node.getLogicalOperator().toString().trim();
    }

    public String RHS(ASingleLogicalExpr node){
        return node.getExpression().toString().trim();
    }

    public String LHS(ASingleLogicalExpr node){
        return node.getDouble().toString().trim();
    }

    public String BooleanOperator(AMultipleLogicalExprs node){
        return node.getBoolOperator().toString().trim();
    }

    public String Key(PFactor node){
        if (node instanceof AFloatFactor){
            return ((AFloatFactor) node).getTFloat().toString().trim();
        }else if(node instanceof  AVariableFactor){
            return ((AVariableFactor) node).getTString().toString().trim();
        }
        return ((AIntegerFactor) node).getTInt().toString().trim();
    }

    /***
     * Methods related to reactions and function in the language
     */
    //<editor-fold desc="Reactions and functions">
    public Node Factor(AOneWayArrows node){
        return node.getReactionRateI();
    }

    public Node Factor(ATwoWayArrows node){
        return node.getReactionRateI();
    }

    public String Rate(AUnitReactionRateI node){
        return node.getFactor().toString().trim();
    }


    public String FunctionName(AFCallFuncCall node){
        return node.getTString().toString().trim();
    }
    //</editor-fold>

    /**
     * Methods that takes a input in the form of a string and returns a boolean from it.
     */
    //<editor-fold desc="Checks">
    public Boolean IsValidType(String type){
        return type.equals(ViableVariable.INT) || type.equals(ViableVariable.FLOAT) || type.equals(ViableVariable.RATE);
    }
    public Boolean IsNegative(String value){
        return value.contains("-");
    }
    //</editor-fold>
    /**
     * The sample method returns the string of the sample which in most instaces function as a key to it position in the symbol table.
     */
    //<editor-fold desc="Sample">

    public String Specie(ASingleTitrations node){
        return node.getTString().toString().trim();
    }

    public String Sample(ASingleEquili node){
        return node.getTString().toString().trim();
    }

    public String Sample(ADisposeProtocolbody node){
        return node.getTString().toString().trim();
    }

    public String Sample(ASplitSampleref node){
        return node.getTString().toString().trim();
    }

    public String Value(PFactor node){
        return node.toString().trim();
    }


    public String Sample(ASingleProtocolparam node){
        return node.getTString().toString().trim();
    }

    public String Sample(AMultiProtocolparam node){
        return node.getTString().toString().trim();
    }

    public String Sample(ASingleProtoexstend node){
        return node.getTString().toString().trim();
    }

    public String Sample(AMultiProtoexstend node){
        return node.getTString().toString().trim();
    }

    //</editor-fold>

    /**
     * The id and type method are used to retrieve a string version of name or the type.
     */
    //<editor-fold desc="Id and Type">
    public String Id(AFloatNumber node){
        return node.getTString().toString().trim();
    }

    public String Id(AMultipleFloats node){
        return node.getTString().toString().trim();
    }

    public String Id(AIntNumber node){
        return node.getTString().toString().trim();
    }

    public String Id(AMultipleIntegers node){
        return node.getTString().toString().trim();
    }

    public String Value(AMultipleRates node){
        return node.getTFloat().toString().trim();
    }

    public String Id(AMultipleRates node){
        return node.getTString().toString().trim();
    }

    public String Type(AMultipleRates node){
        return ViableVariable.RATE;
    }

    public String Id(ASampleProtocolbody node){
        return node.getTString().toString().trim();
    }

    public String Id(AMultipleSpecie node){
        return node.getTString().toString().trim();
    }

    public String Id(ASingleSpecie node){
        return node.getTString().toString().trim();
    }

    public String Id(ALambdaFuncFunc node){return node.getTString().toString().trim(); }

    public String Type(ALambdaFuncFunc node){return ViableVariable.FUNC; }

    public String Id(AMultiInput node)
    {
        return node.getTString().toString().trim();
    }

    public String Type(AMultiInput node)
    {
        return node.getType().toString().trim();
    }

    public String Id(ASingleInput node)
    {
        return node.getTString().toString().trim();
    }

    public String Type(ASingleInput node)
    {
        return node.getType().toString().trim();
    }

    public String Id(ASampleinitSample node)
    {
        return node.getTString().toString().trim();
    }

    public String Type(ASampleinitSample node)
    {
        return ViableVariable.SAMPLE;
    }
    //</editor-fold>

    /**
     * The VerifyProperty methods are used to check for different null values, these are unique for each node.
     * So the range of checks are different depending on the input.
     */
    //<editor-fold desc="VerifyProperties">
    public Boolean VerifyProperties(ADisposeProtocolbody node){
        return node.getTString() == null || node.getDisposePara() == null;
    }

    public Boolean VerifyProperties(AMixSampleref node){
        return node.getProtocolparam() == null || node.getTString() == null;
    }

    public Boolean VerifyProperties(ASplitSampleref node){
        return node.getFuncParameters() == null || node.getProtoexstend() == null || node.getTString() == null;
    }

    public Boolean VerifyProperties(AMultiProtocolparam node){
        return node.getTString() == null || node.getProtocolparam() == null;
    }

    public Boolean VerifyProperties(ASingleProtocolparam node){
        return node.getTString() == null;
    }

    public Boolean VerifyProperties(AMultiProtoexstend node){
        return node.getProtoexstend() == null || node.getTString() == null;
    }

    public Boolean VerifyProperties(ASingleProtoexstend node){
        return node.getTString() == null;
    }

    public Boolean VerifyProperties(AFloatNumber node){
        return node.getExpression() == null || node.getFloats() == null || node.getTString() == null || node.getTString() == null;
    }

    public Boolean VerifyProperties(AIntNumber node){
        return node.getExpression() == null || node.getIntegers() == null || node.getTString() == null || node.getTString() == null;
    }

    public Boolean VerifyProperties(AMultipleFloats node){
        return node.getExpression() == null || node.getFloats() == null || node.getTString() == null;
    }

    public Boolean VerifyProperties(AMultipleRates node)
    {
        return node.getTString() == null || node.getTFloat() == null || node.getRates() == null;
    }

    public Boolean VerifyProperties(AMultipleIntegers node){
        return node.getExpression() == null || node.getIntegers() == null || node.getTString() == null || node.getTString() == null;
    }

    public Boolean VerifyProperties(ANegativeParenPolarity node){
        return node.getTMinus() == null || node.getFactor() == null || node.getTLParen() == null || node.getTRParen() == null;
    }

    public Boolean VerifyProperties(ASingleSpecie node){
        return node.getTString() == null || node.getExpression() == null;
    }

    public Boolean VerifyProperties(AMultipleSpecie node){
        return node.getTString() == null || node.getExpression() == null || node.getSpecie() == null;
    }

    public Boolean VerifyProperties(ASingleRates node)
    {
        return node.getTString() == null || node.getTFloat() == null;
    }

    public Boolean VerifyProperties(ALambdaFuncFunc node)
    {
        return node.getTString() == null || node.getInput() == null || node.getExpression() == null;
    }

    public Boolean VerifyProperties(AMultiInput node)
    {
        return node.getTString() == null || node.getInput() == null || node.getType() == null;
    }

    public Boolean VerifyProperties(AVoidInput node)
    {
        return node.getTVoiddcl() == null;
    }

    public Boolean VerifyProperties(ASingleInput node)
    {
        return node.getTString() == null || node.getType() == null;
    }

    public Boolean VerifyProperties(ASampleinitSample node)
    {
        return node.getTString() == null || node.getSamplefunc() == null;
    }

    public Boolean VerifyProperties(ABlockCrnfunc node)
    {
        return node.getReaction() == null;
    }

    public Boolean VerifyProperties(AOneWayArrows node)
    {
        return node.getReactant() == null || node.getReactionRateI() == null;
    }

    public Boolean VerifyProperties(ATwoWayArrows node)
    {
        return node.getReactant() == null || node.getReactionRateI() == null || node.getReactionRateIi() == null;
    }

    public Boolean VerifyProperties(ADoubleReaction node)
    {
        return node.getReactant() == null || node.getArrows() == null;
    }

    public Boolean VerifyProperties(AFCallFuncCall node)
    {
        return node.getTString() == null || node.getFuncParen() == null;
    }

    public Boolean VerifyProperties(AReactionRateReactionRateIi node){
        return node.getReactionRateI() == null;
    }

    public Boolean VerifyProperties(AWhileLoop node){
        return node.getLogicalExprs() == null;
    }

    public Boolean VerifyProperties(AMultipleLogicalExprs node){
        return node.getLogicalExpr() == null || node.getLogicalExprs() == null || node.getBoolOperator() == null;
    }

    public Boolean VerifyProperties(ASingleLogicalExpr node){
        return node.getExpression() == null || node.getDouble() == null || node.getLogicalOperator() == null;
    }

    public Boolean VerifyProperties(ADclTitration node){
        return node.getTitrations() == null;
    }

    public Boolean VerifyProperties(ASingleTitrations node){
        return node.getTString() == null || node.getFactor() == null || node.getTitrationEnd() == null;
    }

    public Boolean VerifyProperties(AMultipleTitrations node){
        return node.getTitrationContinue() == null || node.getTString() == null || node.getFactor() == null || node.getTitrations() == null;
    }
    //</editor-fold>

}
