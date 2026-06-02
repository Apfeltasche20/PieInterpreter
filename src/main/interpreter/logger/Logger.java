package main.interpreter.logger;

public class Logger
{
    private static boolean debugLogging = false;

    public static void setDebugLogging(boolean debugLogging)
    {
        Logger.debugLogging = debugLogging;
    }

    public static boolean isDebugLogging()
    {
        return debugLogging;
    }

    public static void debugLog(String string)
    {
        if(debugLogging)
            System.out.println(string);
    }
}
