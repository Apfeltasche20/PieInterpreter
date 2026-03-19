package main.util;

import main.interpreter.variable.Variable;
import main.interpreter.variable.VariableArray;
import main.interpreter.variable.VariableNumber;

public class Util
{
    public static boolean isNumber(String string)
    {
        try
        {
            long number = Long.parseLong(string);
            return true;
        } catch (NumberFormatException e)
        {
            return false;
        }
    }

    public static boolean isString(String string)
    {
        return string.startsWith("\"") && string.endsWith("\"");
    }

    public static boolean isMathSymbol(String string)
    {
        return switch (string)
        {
            case "+", "-", "*", "/" -> true;
            default -> false;
        };
    }

    public static VariableArray toArray(byte[] inputArray)
    {
        VariableArray variableArray = new VariableArray(inputArray.length);
        Variable[] array = variableArray.getValues();
        for(int i = 0;i<inputArray.length;i++)
            array[i] = new VariableNumber(((long)inputArray[i]) & 0xFF);
        return variableArray;
    }
}
