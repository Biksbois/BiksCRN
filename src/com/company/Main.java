package com.company;

/* Create an AST, then invoke our interpreter. */
import simpleAdder.interpret.CompilerPhases.BetaTypeChecker;
import com.company.parser.* ;
import com.company.lexer.*;
import com.company.node.*;
import simpleAdder.interpret.CompilerPhases.CodeGenerator;
import simpleAdder.interpret.CompilerPhases.GenerateAST;
import simpleAdder.interpret.CompilerPhases.IntermediateCodeGeneration;
import simpleAdder.interpret.PythonFileGenerator;

import java.io.* ;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        //System.out.println("test");
        PythonFileGenerator PY = new PythonFileGenerator("python.py");
        String[] filepath = new String[1];

        if(args.length == 0 )
        {
                Scanner myObj = new Scanner(System.in);  // Create a Scanner object
                System.out.println("Input file not provided");
                System.out.println("Enter filepath:");
                filepath[0] = myObj.nextLine();
        }else
        {
            File arg = new File(args[0]);
            if(!arg.exists())
            {
                Scanner myObj = new Scanner(System.in);  // Create a Scanner object
                System.out.println("Input file not provided");
                System.out.println("Enter filepath:");
                filepath[0] = myObj.nextLine();
            }else
                {
                    filepath[0] = args[0];
                }
        }


        String path = Paths.get("").toAbsolutePath().toString() + "\\";
        String inputFile = "BiksCRN.sa";
        IntermediateCodeGeneration intermidiatCodeGeneration = new IntermediateCodeGeneration(path,inputFile);
        intermidiatCodeGeneration.Generate();

        if (filepath.length > 0) {
            try {
                // Character -> Token
                Lexer lexer = new Lexer (new PushbackReader(
                        new FileReader(filepath[0]), 1024));

                Parser parser = new Parser(lexer);

                // Token -> AST
                Start ast = parser.parse() ;

                //GenerateAST AST = new GenerateAST();
                //ast.apply(AST);
                //System.out.println(AST.GetAST());

                BetaTypeChecker typeChecker = new BetaTypeChecker();
                ast.apply(typeChecker);

                CodeGenerator generator = new CodeGenerator(typeChecker);
                ast.apply(generator);

                PY.WriteInputfile(generator.GetPython());
            }
            catch (Exception e) {
                System.out.println (e) ;
            }
        } else {
            System.err.println("usage: java simpleAdder inputFile");
            System.exit(1);
        }
    }
}
