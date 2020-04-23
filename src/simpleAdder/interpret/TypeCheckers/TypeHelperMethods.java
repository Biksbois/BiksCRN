package simpleAdder.interpret.TypeCheckers;

public class TypeHelperMethods{

    /***
     * Terminates program with a error message as input
     * @param message
     */
    public void terminate_program(String message) {
        System.err.println(message);
        System.exit(1);
    }
}
