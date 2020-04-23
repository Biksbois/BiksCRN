package UnitTestes;

import com.company.lexer.Lexer;
import com.company.node.Start;
import com.company.parser.Parser;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import simpleAdder.interpret.BetaTypeChecker;
import simpleAdder.interpret.CodeGenerator;
import simpleAdder.interpret.IntermediateCodeGeneration;
import simpleAdder.interpret.PythonFileGenerator;

import java.io.*;
import java.nio.file.Paths;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

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
                AssertFilesEqual("D:\\Java_programs\\P4\\BiksCRN\\Result.txt","D:\\Java_programs\\P4\\BiksCRN\\src\\UnitTestes\\ExspectedResult.txt");
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
        String[] path = new String[]{"UnitTestFileInput.sa"};

        return path;
    }

    private void AssertFilesEqual(String exspected, String result)
    {

        Assert.assertEquals(readFile(exspected),readFile(result));
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