package simpleAdder.interpret.CompilerPhases;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.*;

public class IntermediateCodeGeneration {

    String rawPath;
    String Codename;
    String OutputName = "BiksIntermediate.sa";

    public IntermediateCodeGeneration(String path,String codename)
    {
        Codename = codename;
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
            File myObj = new File(rawPath+Codename);
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
            FileWriter fw = new FileWriter(rawPath + OutputName);
            fw.write(code);
            fw.close();
        }catch (IOException e)
        {
            System.err.println("File path did not exist: "+ rawPath+OutputName);
            System.exit(1);
        }


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
        WriteInputfile(code);
    }
}
