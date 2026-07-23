import main.analysis.StaticAnalysis;
import main.code.Code;
import main.crosscompiler.java.JavaCrossCompiler;
import main.interpreter.Interpreter;
import main.interpreter.scope.ScopeType;
import main.interpreter.variable.Variable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Main
{
    public static long loop()
    {
        long x = 1;
        long y = 1;
        long z = 1;

        long loops = 1000000;

        while(loops > 0)
        {
            z = z + x + y;
            x = x + 1;
            y = x + 2;

            loops = loops - 1;
        }

        return z;
    }

    public static void main(String[] args) throws IOException
    {
        Interpreter interpreter = new Interpreter(List.of("lib"));
        interpreter.executeFile("benchmark");
        //interpreter.execute(Files.readString(Path.of("input.pie")));

        //Interpreter interpreter = new Interpreter(List.of("lib"));
        //Code code = new Code(Files.readString(Path.of("input.pie")));

        //JavaCrossCompiler.crossCompile(code, System.out);
        //StaticAnalysis staticAnalysis = new StaticAnalysis(code);

        /*
        Interpreter interpreter = new Interpreter(List.of("lib"));
        Code code = new Code(Files.readString(Path.of("benchmark.txt")));
        code.setInitialized(true);
                //Variable resultVariable = interpreter.executeFile();
        long startTime = System.nanoTime();

        Variable resultVariable = interpreter.executeScope(code.getGlobalScope(), ScopeType.FUNCTION);

        long endTime = System.nanoTime();
        long result = resultVariable.asNumber();
        System.out.println(result);
        System.out.println((endTime - startTime) / 1000 + " micro sec");

        startTime = System.nanoTime();
        result = loop();
        endTime = System.nanoTime();
        System.out.println(result);
        System.out.println((endTime - startTime) / 1000 + " micro sec");
         */
    }
}
