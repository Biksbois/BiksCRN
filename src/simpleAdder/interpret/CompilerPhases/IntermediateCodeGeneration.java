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
    String VariableOrNumeral = "(\\d+|(\\w(\\d|\\w)*))";
    String Variable = "(\\w(\\d|\\w)*)";
    String instant = "(I|i)(N|n)(S|s)(T|t)(A|a)(N|n)(T|t)";

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
                data += myReader.nextLine()+"\r\n";
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
            FileWriter fw = new FileWriter(Paths.get("").toAbsolutePath().toString() +"\\"+ OutputName);
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
                if(CheckIfTime(equili))
                {
                    str = str.replace(equili,RemoveBloat(equili)+ " each 1 bitesize "+GenerateUniqeName(str)+";");
                }
                else
                    {
                        str = str.replace(equili,RemoveBloat(equili)+ " each 1 bitesize "+ value.trim() +";");
                    }

            }
        }

        return str;
    }

    private String GenerateUniqeName(String str) {
        String BaseName = "instant";
        int id = 0;
        while (str.contains(BaseName))
        {
            BaseName = BaseName+String.valueOf(id);
        }
        return BaseName;
    }

    public Boolean CheckIfTime(String str)
    {
        return GetMatches(str,"\\s(t|T)\\s").length == 1 ? true:false;
    }

    public String RemoveBloat(String str)
    {
        String trimmedResult = GetMatches(str,"Equilibrate.*;")[0];
        trimmedResult = trimmedResult.replaceAll(instant+";","");

        if(trimmedResult.contains("each"))
        {
            trimmedResult = trimmedResult.replaceAll("each\\s"+VariableOrNumeral,"");
        }

        if(trimmedResult.contains("bitesize"))
        {
            trimmedResult = trimmedResult.replaceAll("bitesize\\s"+VariableOrNumeral,"");
        }
        return trimmedResult;
    }

    public boolean CheckForInstant(String str)
    {
        return Pattern.compile(instant+"\\s*;").matcher(str).find();
    }

    public String LogicalExsprresionFix(String str)
    {
        String[] matches = GetMatches(str,"while\\s+\\(.*\\)");
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
        String[] matches = GetMatches(str,"for\\s+"+VariableOrNumeral);
        if(matches.length != 0)
        {
            String FixStr = matches[0]+" c";
            str = str.replace(matches[0],FixStr);
        }

        return str;
    }

    public boolean checkForTypeNotation(String str)
    {
        String[] matches = GetMatches(str,"for\\s+"+VariableOrNumeral+"\\s+(t|c|T|C)(\\s+by\\s+|\\s+each\\s+|\\s+bitesize\\s+)?");

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
        String[] matches = GetMatches(str,"CRN\\s*\\{(.|\\s)*\\}");
        for (var s: matches) {
            str = str.replace(s,ReplaceNotation(s));
        }
        return str;
    }

    public String ReplaceNotation(String str)
    {
        String[] matches = GetMatches(str,"(\\+|\\,|\\>|\\s)?\\d+(\\w(\\d|\\w)*)(\\+|\\,|\\<|\\-)");
        for (var blip: matches) {

            str = str.replaceAll(GetOriginalSpecies(blip),GetNumeral(blip)+"*"+GetName(blip));
        }
        return str;
    }

    public String GetOriginalSpecies(String str)
    {
        return GetMatches(str,"\\d"+Variable)[0];
    }

    public String GetNumeral(String str)
    {
        if(str.contains("Equilibrate"))
        {
            return GetMatches(GetMatches(str,"for\\s+"+VariableOrNumeral+"\\s+")[0],"\\s+"+VariableOrNumeral)[0];
        }
        else
            {
                return GetMatches(str,VariableOrNumeral)[0];
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
