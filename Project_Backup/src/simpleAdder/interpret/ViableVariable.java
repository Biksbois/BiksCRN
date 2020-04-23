package simpleAdder.interpret;

public class ViableVariable {
    public static final String INT = "int";
    public static final String FLOAT = "float";
    public static final String RATE = "rate";
    public static final String Variable = "variable";
    public static final String SAMPLE = "sample";
    public static final String SPECIE = "specie";
    public static final String CRN = "crn";
    public static final String FUNC = "func";
    public static final String REMMOL = "RemMol";
    public static final String ADDMOL = "AddMol";
    public static final String MIX = "Mix";
    public static final String EQUILIBRATE = "Equilibrate";
    public static final String SPLIT = "Split";
    public static final String DISPOSE = "Dispose";
    public static final String STACK = "stack";
    public static final String PARAMETER = "parameter";
    public static final String SCOPE = "scope";
    public static final String REACTION = "reaction";
    public static final String TITRATION = "titration";
    public static final String TITRATIONLIST = "titrationlist";
    public static final String LOGICALEXPR = "logicalexpression";

    public static final int MAX_FLOATING_POINT = 6;
    public static final int MAX_FLOAT = 39;
    public static final String MAX_F = "340282346638528860000000000000000000000";
    public static final String MAX_INT = "2147483647";

    public static final String mult = "*";
    public static final String div = "/";
    public static final String power = "**";
    public static final String plus = "+";
    public static final String minus = "-";

    public static final String CYCLE = "cycle";
    public static final String VOID = "void";

    public static final String[] RESERVED_WORDS = {CYCLE, VOID};

    /***
     * From an input string, it checks if it is a reserved word.
     * @param str
     * @return
     */
    public boolean isReservedWord(String str)
    {
        for (String _str: RESERVED_WORDS) {
            if (_str.equals(str))
            {
                return true;
            }
        }
        return false;
    }

}
