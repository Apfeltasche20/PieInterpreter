package main.interpreter.intern.functions;

import main.interpreter.Interpreter;
import main.interpreter.intern.ClassStorage;
import main.interpreter.variable.Variable;
import main.interpreter.variable.VariableArray;
import main.interpreter.variable.VariableNumber;
import main.interpreter.variable.VariableString;
import main.util.Util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
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
        /*
        if(!(filePathVariable instanceof VariableString))
        {
            System.err.println("readFile: First Argument not a String");
            System.err.println("Usage: readFile(String filePath)");
            return new Variable();
        }
         */

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

    public static Variable readFileRaw(Interpreter interpreter, List<Variable> args)
    {
        if(args.isEmpty())
        {
            System.err.println("readFileRaw: No arguments given");
            System.err.println("Usage: readFileRaw(String filePath)");
            return new Variable();
        }

        Variable filePathVariable = args.getFirst();

        String filePath = filePathVariable.asString();
        try
        {
            byte[] fileContent = java.nio.file.Files.readAllBytes(Path.of(filePath));
            return Util.toArray(fileContent);
        } catch (IOException e)
        {
            System.err.println("readFileRaw: Error Opening File " + filePath);
            System.err.println("Usage: readFileRaw(String filePath)");
            return new Variable();
        }
    }

    public static Variable readImage(Interpreter interpreter, List<Variable> args)
    {
        if(args.isEmpty())
        {
            System.err.println("readImage: No arguments given");
            System.err.println("Usage: readImage(String filePath)");
            return new Variable();
        }

        Variable filePathVariable = args.getFirst();

        String filePath = filePathVariable.asString();
        try
        {
            BufferedImage bufferedImage = ImageIO.read(new java.io.File(filePath));
            return new VariableNumber(ClassStorage.storeObject(bufferedImage));
        } catch (IOException e)
        {
            System.err.println("readImage: Error Opening File " + filePath);
            System.err.println("Usage: readImage(String filePath)");
            return new Variable();
        }
    }
}
