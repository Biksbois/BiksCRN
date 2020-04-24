package simpleAdder.interpret.TypeCheckers;


import simpleAdder.interpret.GetMethods.ViableVariable;

public class Checker {
    public TypeHelperMethods TH = new TypeHelperMethods();
    public ViableVariable vv = new ViableVariable();

    /***
     * Checks if a given input string contains a letter.
     * Used to see if it is a variable
     * @param str
     * @return
     */
    public boolean CheckStringLetter(String str)
    {
        char[] charArr = str.toCharArray();
        for (char _char:charArr) {
            if(Character.isLetter(_char))
            {
                return true;
            }
        }
        return false;
    }
}
