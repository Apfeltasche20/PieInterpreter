package main.interpreter.intern.functions;

import main.interpreter.Interpreter;
import main.interpreter.variable.Variable;
import main.interpreter.variable.VariableString;

import java.io.IOException;
import java.lang.System;
import java.nio.file.Path;
import java.util.List;

public class File
{
    public static Variable readFile(Interpreter interpreter, List<Variable> args)
    {
        if(args.isEmpty())
        {
            System.err.println("readFile: No arguments given");
            System.err.println("Usage: readFile(String filePath)");
            return new Variable();
        }

        Variable filePathVariable = args.getFirst();
        if(!(filePathVariable instanceof VariableString))
        {
            System.err.println("readFile: First Argument not a String");
            System.err.println("Usage: readFile(String filePath)");
            return new Variable();
        }

        String filePath = filePathVariable.asString();
        try
        {
            String fileContent = java.nio.file.Files.readString(Path.of(filePath));
            return new VariableString(fileContent);
        } catch (IOException e)
        {
            System.err.println("readFile: Error Opening File " + filePath);
            System.err.println("Usage: readFile(String filePath)");
            return new Variable();
        }
    }
}
