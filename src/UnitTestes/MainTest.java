package UnitTestes;

import com.company.lexer.Lexer;
import com.company.node.Start;
import com.company.parser.Parser;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import simpleAdder.interpret.CompilerPhases.BetaTypeChecker;
import simpleAdder.interpret.CompilerPhases.CodeGenerator;
import simpleAdder.interpret.PythonFileGenerator;

import java.io.*;
import java.nio.file.Paths;
import java.util.Scanner;

class MainTest {

    @Test
    void main() throws IOException {
        PythonFileGenerator PY = new PythonFileGenerator("Result.txt");
        var InFile = InputFile();
        if (InFile.length > 0) {
            try {
                // Character -> Token
                Lexer lexer = new Lexer (new PushbackReader(
                        new FileReader(InFile[0]), 1024));

                Parser parser = new Parser(lexer);

                // Token -> AST
                Start ast = parser.parse() ;

                BetaTypeChecker typeChecker = new BetaTypeChecker();
                ast.apply(typeChecker);

                CodeGenerator generator = new CodeGenerator(typeChecker);
                ast.apply(generator);

                PY.WriteInputfile(generator.GetPython());
                AssertFilesEqual(Paths.get("").toAbsolutePath().toString()+"\\Result.txt",Paths.get("").toAbsolutePath().toString()+"\\src\\UnitTestes\\ExspectedResult.txt");
            }
            catch (Exception e) {
                System.out.println (e) ;
            }
        } else {
            System.err.println("usage: java simpleAdder inputFile");
            System.exit(1);
        }
    }

    private String[] InputFile()
    {
        String[] path = new String[]{"src/UnitTestes/UnitTestFileInput.sa"};

        return path;
    }

    private void AssertFilesEqual(String exspected, String result)
    {

        Assert.assertEquals(readFile(exspected),readFile(result));
        File f= new File(exspected);
        f.delete();
    }

    private String readFile(String path)
    {
        String data = "";
        try {
            File myObj = new File(path);
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
}