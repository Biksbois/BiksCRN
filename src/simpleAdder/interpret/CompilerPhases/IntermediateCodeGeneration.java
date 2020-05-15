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
            FileWriter fw = new FileWriter(Paths.get("").toAbsolutePath().toString() + OutputName);
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
        return Paths.get("").toAbsolutePath().toString()+OutputName;
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
        WriteInputfile(code);
    }

    public String FixEquilibrateSyntax(String str)
    {
        String[] matches = Pattern.compile("Protocol\\s*\\{(\\s|\\w|\\d|\\*|\\+|;|:|,|.)*\\}")
                .matcher(str)
                .results()
                .map(MatchResult::group)
                .toArray(String[]::new);
        str = str.replace(matches[0],FixEquilibrate(matches[0]));


        return str;
    }

    public String FixEquilibrate(String str)
    {
        String[] matches = Pattern.compile("Equilibrate\\s*(.)*;")
                .matcher(str)
                .results()
                .map(MatchResult::group)
                .toArray(String[]::new);

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
        String[] matches = Pattern.compile("\\sby\\s")
                .matcher(str)
                .results()
                .map(MatchResult::group)
                .toArray(String[]::new);
        String FixStr = " c"+matches[0];
        str = str.replace(matches[0],FixStr);
        return str;
    }

    public boolean checkForTypeNotation(String str)
    {
        String[] matches = Pattern.compile("(t|c|T|C)\\sby\\s")
                .matcher(str)
                .results()
                .map(MatchResult::group)
                .toArray(String[]::new);

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
        String[] matches = Pattern.compile("CRN\\s*\\{(\\s|\\w|\\d|->|<->|,|\\*|\\+|;|:)*\\}")
                .matcher(str)
                .results()
                .map(MatchResult::group)
                .toArray(String[]::new);
        for (var s: matches) {
            String Notation = ReplaceNotation(s);
            str = str.replace(s,Notation);
        }
        return str;
    }

    public String ReplaceNotation(String str)
    {
        String[] matches = Pattern.compile("\\d+([a-z]|[A-Z])+(([a-z]|[A-Z])|\\d)*")
                .matcher(str)
                .results()
                .map(MatchResult::group)
                .toArray(String[]::new);
        for (var blip: matches) {
            String test = GetNumeral(blip)+"*"+GetName(blip);
            str = str.replaceAll(blip,test);
        }
        return str;
    }

    public String GetNumeral(String str)
    {
            return Pattern.compile("\\d+")
                    .matcher(str)
                    .results()
                    .map(MatchResult::group)
                    .toArray(String[]::new)[0];
    }

    public String GetName(String str)
    {
            return Pattern.compile("([a-z]|[A-Z])+(([a-z]|[A-Z])|\\d)*")
                .matcher(str)
                .results()
                .map(MatchResult::group)
                .toArray(String[]::new)[0];
    }
}
