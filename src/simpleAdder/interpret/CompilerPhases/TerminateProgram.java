package simpleAdder.interpret.CompilerPhases;

import com.company.node.Token;
import simpleAdder.interpret.GetMethods.ErrorMessage;

public class TerminateProgram {
    ErrorMessage EM = new ErrorMessage();

    /***
     * Terminates program with a error message as input
     * @param message
     */
    public void terminate_program(String message) {
        System.err.println(message);
        System.exit(1);
    }

    public void terminate_program(Token token, String message) {
        System.err.println(EM.Get(token, message));
        System.exit(1);
    }

    public void VarExistsAlready(Token token, String type, String value){
        String message = "\"" + value + "\"" + " of type " + type + " is already defined ";
        System.err.println(EM.Get(token, message));
        System.exit(1);
    }

    public void VarExistsAlready(Token token, String type, String value, String method){
        String message = "\"" + value + "\"" + " of type " + type + " is already defined (" + method + ")";
        System.err.println(EM.Get(token, message));
        System.exit(1);
    }

    public void VarDontExist(Token token, String id, String method){
        String message = "Variable \"" + id + "\"" + " is never initialized (" + method + ")";
        System.err.println(EM.Get(token, message));
        System.exit(1);
    }

    public void VarDontExist(Token token, String id, String type, String method){
        String message = type + " \"" + id + "\"" + " is never initialized (" + method + ")";
        System.err.println(EM.Get(token, message));
        System.exit(1);
    }

    public void WrongType(Token token, String typeIs, String typeShouldBe, String id, String method){
        if (typeIs.equals("")){
            VarDontExist(token, id, method);
        }
        String message = "\"" + id + "\" of type " + typeIs + " has the wrong type. It should be replaced by a variable of type " + typeShouldBe + " (" + method + ")";
        System.err.println(EM.Get(token, message));
        System.exit(1);
    }

    public void ShouldBeWhole(Token token, String type, String id, String value, String method){
        if (type.equals("")){
            VarDontExist(token, id, method);
        }
        String message = "\"" + id + "\" of type " + type + " should be a whole number. The current value is "+ value + ". (" + method + ")";
        System.err.println(EM.Get(token, message));
        System.exit(1);
    }

    public void ShouldBeWhole(Token token, String value, String method){
        String message = "The value " + value + " should be a whole number in its current context. (" + method + ")";
        System.err.println(EM.Get(token, message));
        System.exit(1);
    }

    public void ShouldBeWholeAndPositive(Token token, String value, String method){
        String message = "The value " + value + " should be a whole and positive value in its current context. (" + method + ")";
        System.err.println(EM.Get(token, message));
        System.exit(1);
    }

    public void ShouldBePositive(Token token, String value, String varName, String method){
        String message = "The variable \"" + varName + "\" is assigned the value " + value + ", but it should be positive. (" + method + ")";
        System.err.println(EM.Get(token, message));
        System.exit(1);
    }
}
