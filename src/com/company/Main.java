package com.company;

/* Create an AST, then invoke our interpreter. */
import com.company.lexer.Lexer;
import com.company.node.Start;
import com.company.parser.Parser;
import simpleAdder.interpret.CompilerPhases.BetaTypeChecker;
import simpleAdder.interpret.CompilerPhases.CodeGenerator;
import simpleAdder.interpret.CompilerPhases.IntermediateCodeGeneration;
import simpleAdder.interpret.GetMethods.FilePath;
import simpleAdder.interpret.PythonFileGenerator;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PushbackReader;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        PythonFileGenerator PY = new PythonFileGenerator("python.py");
        FilePath filePath = new FilePath();

        String path = filePath.get(args);
        IntermediateCodeGeneration intermidiatCodeGeneration = new IntermediateCodeGeneration(path);
        intermidiatCodeGeneration.Generate();
        path = intermidiatCodeGeneration.GetPath();
        if (path.length() > 0) {
            try {
                // Character -> Token
                Lexer lexer = new Lexer (new PushbackReader(
                        new FileReader(path), 1024));

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
                System.out.println("Build was succesful");
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
