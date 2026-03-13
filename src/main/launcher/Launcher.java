package main.launcher;

import main.interpreter.Interpreter;
import main.interpreter.logger.Logger;

public class Launcher
{
    public static void main(String[] args)
    {
        LaunchOptions launchOptions = new LaunchOptions(args);
        launchOptions.validate();

        Logger.setDebugLogging(launchOptions.isDebugPrint());

        Interpreter interpreter = new Interpreter(launchOptions.getIncludeDirs());
        interpreter.executeFile(launchOptions.getMainFile());
    }
}
