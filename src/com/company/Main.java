package com.company;

/* Create an AST, then invoke our interpreter. */
import simpleAdder.interpret.CompilerPhases.BetaTypeChecker;
import com.company.parser.* ;
import com.company.lexer.*;
import com.company.node.*;
import simpleAdder.interpret.CompilerPhases.CodeGenerator;
import simpleAdder.interpret.CompilerPhases.IntermediateCodeGeneration;
import simpleAdder.interpret.PythonFileGenerator;

import java.io.* ;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) throws IOException {
        PythonFileGenerator PY = new PythonFileGenerator("python.py");

        String path = Paths.get("").toAbsolutePath().toString() + "\\";
        String inputFile = "BiksCRN.sa";

        IntermediateCodeGeneration intermidiatCodeGeneration = new IntermediateCodeGeneration(path,inputFile);
        intermidiatCodeGeneration.Generate();

        if (args.length > 0) {
            try {
                // Character -> Token
                Lexer lexer = new Lexer (new PushbackReader(
                        new FileReader(args[0]), 1024));

                Parser parser = new Parser(lexer);

                // Token -> AST
                Start ast = parser.parse() ;

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
