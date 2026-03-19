package main.interpreter.intern.functions.ui;

import main.interpreter.Interpreter;
import main.interpreter.intern.ClassStorage;
import main.interpreter.variable.*;

import java.awt.image.BufferedImage;
import java.lang.System;
import java.util.List;

public class UI
{
    public static Variable windowCreate(Interpreter interpreter, List<Variable> args)
    {
        InternWindow window = new InternWindow(interpreter);
        long classID = ClassStorage.storeObject(window);
        return new VariableNumber(classID);
    }

    public static Variable windowSetVisible(Interpreter interpreter, List<Variable> args)
    {
        if(args.size() < 2)
        {
            System.err.println("windowSetVisible: Not enough arguments given");
            System.err.println("Usage: windowSetVisible(long windowID, boolean visible)");
            return new Variable();
        }

        Variable windowIdVariable = args.get(0);
        /*
        if(!(windowIdVariable instanceof VariableNumber))
        {
            System.err.println("windowSetVisible: First Argument not a Long");
            System.err.println("Usage: windowSetVisible(long windowID, boolean visible)");
            return new Variable();
        }
         */

        Variable windowVisiableVariable = args.get(1);
        /*
        if(!(windowVisiableVariable instanceof VariableBoolean))
        {
            System.err.println("windowSetVisible: Second Argument not a Boolean");
            System.err.println("Usage: windowSetVisible(long windowID, boolean visible)");
            return new Variable();
        }
         */

        Object referencedObject = ClassStorage.getStoredObject(windowIdVariable.asNumber());
        if(!(referencedObject instanceof InternWindow))
        {
            System.err.println("windowSetVisible: Reference was not a Window");
            System.err.println("Usage: windowSetVisible(long windowID, boolean visible)");
            return new Variable();
        }

        ((InternWindow) referencedObject).setVisible(windowVisiableVariable.asBoolean());
        return new Variable();
    }

    public static Variable windowSetSize(Interpreter interpreter, List<Variable> args)
    {
        if(args.size() < 3)
        {
            System.err.println("windowSetSize: Not enough arguments given");
            System.err.println("Usage: windowSetSize(long windowID, int width, int height)");
            return new Variable();
        }

        Variable windowIdVariable = args.get(0);
        /*
        if(!(windowIdVariable instanceof VariableNumber))
        {
            System.err.println("windowSetSize: windowID not a Long");
            System.err.println("Usage: windowSetSize(long windowID, int width, int height)");
            return new Variable();
        }
         */

        Variable windowWidthVariable = args.get(1);
        /*
        if(!(windowWidthVariable instanceof VariableNumber))
        {
            System.err.println("windowSetSize: width not a Number");
            System.err.println("Usage: windowSetSize(long windowID, int width, int height)");
            return new Variable();
        }
         */

        Variable windowHeightVariable = args.get(2);
        /*
        if(!(windowHeightVariable instanceof VariableNumber))
        {
            System.err.println("windowSetSize: height not a Number");
            System.err.println("Usage: windowSetSize(long windowID, int width, int height)");
            return new Variable();
        }
         */

        Object referencedObject = ClassStorage.getStoredObject(windowIdVariable.asNumber());
        if(!(referencedObject instanceof InternWindow))
        {
            System.err.println("windowSetSize: Reference was not a Window");
            System.err.println("Usage: windowSetSize(long windowID, int width, int height)");
            return new Variable();
        }

        ((InternWindow) referencedObject).setSize((int) windowWidthVariable.asNumber(), (int) windowHeightVariable.asNumber());
        return new Variable();
    }

    public static Variable windowSetName(Interpreter interpreter, List<Variable> args)
    {
        if(args.isEmpty())
        {
            System.err.println("windowSetName: Not enough arguments given");
            System.err.println("Usage: windowSetName(long windowID, string name)");
            return new Variable();
        }


        Variable windowIdVariable = args.get(0);
        /*
        if(!(windowIdVariable instanceof VariableNumber))
        {
            System.err.println("windowSetName: windowID not a Number");
            System.err.println("Usage: windowSetName(long windowID, string name)");
            return new Variable();
        }
         */

        Variable windowNameVariable = args.get(1);
        /*
        if(!(windowNameVariable instanceof VariableString))
        {
            System.err.println("windowSetName: name not a Boolean");
            System.err.println("Usage: windowSetName(long windowID, string name)");
            return new Variable();
        }
         */

        Object referencedObject = ClassStorage.getStoredObject(windowIdVariable.asNumber());
        if(!(referencedObject instanceof InternWindow))
        {
            System.err.println("windowSetName: Reference was not a Window");
            System.err.println("Usage: windowSetName(long windowID, string name)");
            return new Variable();
        }

        ((InternWindow) referencedObject).setTitle(windowNameVariable.asString());
        return new Variable();
    }

    public static Variable windowSetDrawHandler(Interpreter interpreter, List<Variable> args)
    {
        if(args.size() < 2)
        {
            System.err.println("windowSetDrawHandler: Not enough arguments given");
            System.err.println("Usage: windowSetDrawHandler(long windowID, function drawHandler)");
            return new Variable();
        }

        Variable windowIdVariable = args.get(0);
        Variable windowDrawHandlerVariable = args.get(1);
        if(!(windowDrawHandlerVariable instanceof VariableFunction))
        {
            System.err.println("windowSetDrawHandler: drawHandler was not a Function");
            System.err.println("Usage: windowSetDrawHandler(long windowID, function drawHandler)");
            return new Variable();
        }

        Object referencedObject = ClassStorage.getStoredObject(windowIdVariable.asNumber());
        if(!(referencedObject instanceof InternWindow))
        {
            System.err.println("windowSetDrawHandler: Reference was not a InternWindow");
            System.err.println("Usage: windowSetDrawHandler(long windowID, function drawHandler)");
            return new Variable();
        }

        ((InternWindow) referencedObject).addDrawHandler(windowDrawHandlerVariable.asFunction());
        return new Variable();
    }

    public static Variable windowCenterOnScreen(Interpreter interpreter, List<Variable> args)
    {
        if(args.isEmpty())
        {
            System.err.println("windowCenterOnScreen: Not enough arguments given");
            System.err.println("Usage: windowCenterOnScreen(long windowID)");
            return new Variable();
        }

        Variable windowIdVariable = args.getFirst();
        Object referencedObject = ClassStorage.getStoredObject(windowIdVariable.asNumber());
        if(!(referencedObject instanceof InternWindow))
        {
            System.err.println("windowSetDrawHandler: Reference was not a Window");
            System.err.println("Usage: windowCenterOnScreen(long windowID)");
            return new Variable();
        }

        ((InternWindow) referencedObject).centerOnScreen();
        return new Variable();
    }

    public static Variable canvasDrawText(Interpreter interpreter, List<Variable> args)
    {
        if(args.size() < 4)
        {
            System.err.println("canvasDrawText: Not enough arguments given");
            System.err.println("Usage: canvasDrawText(long canvasId, string text, int x, int y)");
            return new Variable();
        }

        Variable windowIdVariable = args.get(0);
        Variable windowTextVariable = args.get(1);
        Variable windowXVariable = args.get(2);
        Variable windowYVariable = args.get(3);

        Object referencedObject = ClassStorage.getStoredObject(windowIdVariable.asNumber());
        if(!(referencedObject instanceof InternWindow.WindowClass))
        {
            System.err.println("canvasDrawText: Reference was not a Canvas");
            System.err.println("Usage: canvasDrawText(long canvasId, string text, int x, int y)");
            return new Variable();
        }

        ((InternWindow.WindowClass) referencedObject).drawText(windowTextVariable.asString(), (int) windowXVariable.asNumber(), (int) windowYVariable.asNumber());
        return new Variable();
    }

    public static Variable canvasDrawBytes(Interpreter interpreter, List<Variable> args)
    {
        if(args.size() < 4)
        {
            System.err.println("canvasDrawBytes: Not enough arguments given");
            System.err.println("Usage: canvasDrawBytes(long canvasId, byte[] image, int x, int y)");
            return new Variable();
        }

        Variable windowIdVariable = args.get(0);
        Variable windowTextVariable = args.get(1);
        Variable windowXVariable = args.get(2);
        Variable windowYVariable = args.get(3);

        Object referencedObject = ClassStorage.getStoredObject(windowIdVariable.asNumber());
        if(!(referencedObject instanceof InternWindow.WindowClass))
        {
            System.err.println("canvasDrawBytes: Reference was not a Canvas");
            System.err.println("Usage: canvasDrawBytes(long canvasId, byte[] image, int x, int y)");
            return new Variable();
        }

        ((InternWindow.WindowClass) referencedObject).drawImageBytes(windowTextVariable.asByteArray(), (int) windowXVariable.asNumber(), (int) windowYVariable.asNumber());
        return new Variable();
    }

    public static Variable canvasDrawImage(Interpreter interpreter, List<Variable> args)
    {
        if(args.size() < 6)
        {
            System.err.println("canvasDrawImage: Not enough arguments given");
            System.err.println("Usage: canvasDrawImage(long canvasId, image image, int x, int y, int width, int height)");
            return new Variable();
        }

        Variable windowIdVariable = args.get(0);
        Variable windowImageVariable = args.get(1);
        Variable windowXVariable = args.get(2);
        Variable windowYVariable = args.get(3);
        Variable windowWidthVariable = args.get(4);
        Variable windowHeightVariable = args.get(5);

        Object referencedObject = ClassStorage.getStoredObject(windowIdVariable.asNumber());
        if(!(referencedObject instanceof InternWindow.WindowClass))
        {
            System.err.println("canvasDrawImage: Reference was not a Canvas");
            System.err.println("Usage: canvasDrawImage(long canvasId, image image, int x, int y, int width, int height)");
            return new Variable();
        }

        if(!(windowImageVariable instanceof VariableObject))
        {
            System.err.println("canvasDrawImage: image was not a image");
            System.err.println("Usage: canvasDrawImage(long canvasId, image image, int x, int y, int width, int height)");
            return new Variable();
        }

        Variable imageVariable = ((VariableObject)windowImageVariable).getObject().getVariableByName("imageId");
        if(imageVariable == null)
        {
            System.err.println("canvasDrawImage: image was not a image");
            System.err.println("Usage: canvasDrawImage(long canvasId, image image, int x, int y, int width, int height)");
            return new Variable();
        }

        Object imageObject = ClassStorage.getStoredObject(imageVariable.asNumber());
        if(!(imageObject instanceof BufferedImage))
        {
            System.err.println("canvasDrawImage: Reference was not a Image");
            System.err.println("Usage: canvasDrawImage(long canvasId, image image, int x, int y, int width, int height)");
            return new Variable();
        }

        ((InternWindow.WindowClass) referencedObject).drawImage((BufferedImage) imageObject, (int) windowXVariable.asNumber(), (int) windowYVariable.asNumber(), (int) windowWidthVariable.asNumber(), (int) windowHeightVariable.asNumber());
        return new Variable();
    }

}
