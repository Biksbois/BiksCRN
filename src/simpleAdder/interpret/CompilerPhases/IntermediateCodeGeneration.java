package simpleAdder.interpret.CompilerPhases;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public class IntermediateCodeGeneration {

    String rawPath;
    String OutputName = "BiksIntermediate.sa";

    public IntermediateCodeGeneration(String path)
    {
        rawPath = path;
    }

    /***
     * This method loads a file and reads it data into a string, which is returned.
     * @return
     */
    private String LoadFile()
    {
        String data = "";
        try {
            File myObj = new File(rawPath);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                data += myReader.nextLine();
            }
            myReader.close();
        } catch(FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return data;
    }

    /***
     * This method writes an input string into a file using the path from the constructor.
     * @param code
     * @throws IOException
     */
    private void WriteInputfile(String code) throws IOException {
        try{
            //var test = Paths.get("").toAbsolutePath().toString() +"\\"+ OutputName;
            FileWriter fw = new FileWriter(Paths.get("").toAbsolutePath().toString() +"\\"+ OutputName);
            //C:\Users\jeppe\Documents\GitHub\BiksCRNBiksIntermediate.sa
            fw.write(code);
            fw.close();
        }catch (IOException e)
        {
            System.err.println("File path did not exist: "+ Paths.get("").toAbsolutePath().toString()+OutputName);
            System.exit(1);
        }


    }

    public String GetPath()
    {
        return Paths.get("").toAbsolutePath().toString()+"\\"+OutputName;
    }

    /***
     * This method translate code to the intermediate code. This is done in three steps,
     * first the file is loaded, then characters are replaced to fit the SableCC syntax,
     * and at last it is written into a new file.
     * @throws IOException
     */
    public void Generate() throws IOException {
        String code = LoadFile();
        code = code.replaceAll("\\{","\\{\n");
        code = code.replaceAll("}","}\n");
        code = code.replaceAll("\t","");
        code = code.replaceAll(";",";\r\n");
        code = code.replaceAll("    ","");
        code = code.replaceAll("while\\(", "while (");
        code = code.replaceAll("Mix\\(", "Mix (");
        code = code.replaceAll("Dispose\\(", "Dispose (");
        code = code.replaceAll("Split\\(", "Split (");
        code = ReplaceCRNSpecies(code);
        code = FixEquilibrateSyntax(code);
        code = LogicalExsprresionFix(code);
        code = RewriteEquilibrate(code);
        WriteInputfile(code);
    }

    public String RewriteEquilibrate(String str)
    {
        String[] matches = GetMatches(str, "Equilibrate.*;");
        for (String equili: matches) {
            if(CheckForInstant(equili))
            {
                String value = GetNumeral(equili);
                str = str.replace(equili,RemoveBloat(equili)+ " each 1 bitesize "+ value.trim() +";");
            }
        }

        return str;
    }
    public String RemoveBloat(String str)
    {
        return GetMatches(str,"Equilibrate.*(by(\\d+.\\d+)|((t|T)|(c|C)))")[0]
                .replaceAll("\\d+\\s+(I|i)(N|n)(S|s)(T|t)(A|a)(N|n)(T|t)",
                        GetNumeral(GetMatches(str,"\\d+\\s+(I|i)(N|n)(S|s)(T|t)(A|a)(N|n)(T|t)")[0]));
    }

    public boolean CheckForInstant(String str)
    {
        return Pattern.compile("\\d+\\s+(I|i)(N|n)(S|s)(T|t)(A|a)(N|n)(T|t)").matcher(str).find();
    }

    public String LogicalExsprresionFix(String str)
    {
        String[] matches = GetMatches(str,"while\\s\\(.*\\)");
        for (String _while: matches) {

            str = str.replace(_while,"while "+IdentifyLogicalExp(_while));
        }
        return str;
    }

    public String IdentifyLogicalExp(String str)
    {
        return GetMatches(str,"\\(.*\\)")[0].replaceAll("\\s","");
    }

    public String FixEquilibrateSyntax(String str)
    {
        String[] matches = GetMatches(str,"Protocol\\s*\\{(\\s|\\w|\\d|\\*|\\+|;|:|,|.)*\\}");
        str = str.replace(matches[0],FixEquilibrate(matches[0]));


        return str;
    }

    public String FixEquilibrate(String str)
    {
        String[] matches = GetMatches(str,"Equilibrate\\s*(.)*;");

        for (String s: matches) {
            String Notation = RepaireEquilibrate(s);
            str = str.replace(s,Notation);
        }
        return str;
    }

    public String RepaireEquilibrate(String str)
    {
        if(checkForTypeNotation(str))
        {
            return str;
        }else
            {
                return FixToDefault(str);
            }

    }

    public String FixToDefault(String str)
    {
        String[] matches = GetMatches(str,"(\\sby\\s|\\seach\\s|\\sbitesize\\s)");
        String FixStr = " c"+matches[0];
        str = str.replace(matches[0],FixStr);
        return str;
    }

    public boolean checkForTypeNotation(String str)
    {
        String[] matches = GetMatches(str,"for\\s.+\\s(t|c|T|C)(\\sby\\s|\\seach\\s|\\sbitesize\\s)");

        if(matches.length == 0)
        {
            return false;
        }else
            {
                return true;
            }
    }
    public String ReplaceCRNSpecies(String str)
    {
        String[] matches = GetMatches(str,"CRN\\s*\\{(\\s|\\w|\\d|->|<->|,|\\*|\\+|;|:)*\\}");
        for (var s: matches) {
            str = str.replace(s,ReplaceNotation(s));
        }
        return str;
    }

    public String ReplaceNotation(String str)
    {
        String[] matches = GetMatches(str,"\\d+([a-z]|[A-Z])+(([a-z]|[A-Z])|\\d)*");
        for (var blip: matches) {
            str = str.replaceAll(blip,GetNumeral(blip)+"*"+GetName(blip));
        }
        return str;
    }

    public String GetNumeral(String str)
    {
        if(str.contains("Equilibrate"))
        {
            return GetMatches(str,"\\s\\d+\\s")[0];
        }
        else
            {
                return GetMatches(str,"\\d+")[0];
            }
    }

    public String GetName(String str)
    {
            return GetMatches(str,"([a-z]|[A-Z])+(([a-z]|[A-Z])|\\d)*")[0];
    }

    private String[] GetMatches(String original, String regex)
    {
        return Pattern.compile(regex)
                .matcher(original)
                .results()
                .map(MatchResult::group)
                .toArray(String[]::new);
    }
}
