package simpleAdder.interpret.ObjectGeneration;

public class PrinterMethods {
    
    /***
     * Takes a input string (InputStr), and adds the amounts of tabs to the string
     * that the int (indent) specifies. It does so by first adding the indents to the
     * OutputStr, and then adding it to the input string.
     * @Param indent
     * @Param OutputStr
     * @Param InputStr
     * @return
     */
    public String ApplyTap(int indent, String OutputStr, String InputStr)
    {
        for(int i = 0; i < indent; i++)
        {
            OutputStr += "\t";
        }
        return OutputStr + InputStr;
    }

    /***
     * Takes a string and adds indents to the string. Depending on the number that are
     * stored in the indent int. It will then return a string, with the specified tabs.
     * @Param indent
     * @Param OutputStr
     * @return
     */
    public String ApplyTap(int indent, String OutputStr)
    {
        for(int i = 0; i < indent; i++)
        {
            OutputStr = "    " + OutputStr;
        }
        return OutputStr;
    }


}
