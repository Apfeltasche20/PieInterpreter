package main.util;

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
}
