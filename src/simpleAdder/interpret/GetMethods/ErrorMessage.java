package simpleAdder.interpret.GetMethods;

import com.company.node.*;
/**
 * A class contanin some error message contructs.
 */
public class ErrorMessage {
    /**
     * @param node is PFactor type, this method generates an error message for it depending on the underlieyng type.
     * */
    public String Get(PFactor node, String message , String method){
        if (node instanceof AIntegerFactor){
            return IntError(((AIntegerFactor) node).getTInt()) + message + MethodName(method);
        }
        else if(node instanceof AFloatFactor){
            return FloatError(((AFloatFactor) node).getTFloat()) + message + MethodName(method);
        }
        else{
            return VariableError(((AVariableFactor) node).getTString()) + message + MethodName(method);
        }
    }

    public String Get(Token token, String message){
        return ErrorFormat(token.getLine()) + message;
    }

    public String Get(int line, String message){
        return ErrorFormat(line) + message;
    }

    /**
     * @param method is the name of the method the method is being called from.
     * The functionality of this method is simply to convert the method name to a form that is easier to read inside and error message.
     */
    private String MethodName(String method){
        return " (" + method + ")";
    }

    /**
     * @param node is of type TTInt and is mainly used to distinguese the method.
     * The method calls ErrorFormat which generates the final message
     */
    private String IntError(TTInt node){
        return ErrorFormat(node.getLine(), node.getPos(), node.getText());
    }

    /**
     * @param node is of type TTFloat and is mainly used to distinguese the method.
     * The method calls ErrorFormat which generates the final message
     */
    private String FloatError(TTFloat node){
        return ErrorFormat(node.getLine(), node.getPos(), node.getText());
    }

    /**
     * @param node is of type TTString and is mainly used to distinguese the method.
     * The method calls ErrorFormat which generates the final message
     */
    private String VariableError(TTString node){
        return ErrorFormat(node.getLine(), node.getPos(), node.getText());
    }

    /**
     * This method simply uses the input to create a string
     * @param line is an int and should represent the line of the error.
     */
    private String ErrorFormat(int line){
        return "Line: " + line + " Description: ";
    }

    private String ErrorFormat(int line, int pos, String text) {
        return "Line: " + line + " Position: " + pos + " Value: " + text + " Message: ";
    }
}
