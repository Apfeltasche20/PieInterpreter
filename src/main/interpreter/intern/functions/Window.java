package main.interpreter.intern.functions;

import main.interpreter.intern.ClassStorage;
import main.interpreter.variable.Variable;
import main.interpreter.variable.VariableBoolean;
import main.interpreter.variable.VariableNumber;
import main.interpreter.variable.VariableString;

import javax.swing.*;
import java.lang.System;
import java.util.List;

public class Window
{
    public static Variable windowCreate(List<Variable> args)
    {
        JFrame window = new JFrame();
        long classID = ClassStorage.storeObject(window);
        return new VariableNumber(classID);
    }

    public static Variable windowSetVisible(List<Variable> args)
    {
        if(args.size() < 2)
        {
            System.err.println("readFile: Not enough arguments given");
            System.err.println("Usage: windowSetVisible(long windowID, boolean visible)");
            return new Variable();
        }

        Variable windowIdVariable = args.get(0);
        if(!(windowIdVariable instanceof VariableNumber))
        {
            System.err.println("windowSetVisible: First Argument not a Long");
            System.err.println("Usage: windowSetVisible(long windowID, boolean visible)");
            return new Variable();
        }

        Variable windowVisiableVariable = args.get(1);
        if(!(windowVisiableVariable instanceof VariableBoolean))
        {
            System.err.println("windowSetVisible: Second Argument not a Boolean");
            System.err.println("Usage: windowSetVisible(long windowID, boolean visible)");
            return new Variable();
        }

        Object referencedObject = ClassStorage.getStoredObject(windowIdVariable.asNumber());
        if(!(referencedObject instanceof JFrame))
        {
            System.err.println("windowSetVisible: Reference was not a JFrame");
            System.err.println("Usage: windowSetVisible(long windowID, boolean visible)");
            return new Variable();
        }

        ((JFrame) referencedObject).setVisible(windowVisiableVariable.asBoolean());
        return new Variable();
    }
}
