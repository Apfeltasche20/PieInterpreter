package main.launcher;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class LaunchOptions
{
    private List<String> includeDirs;
    private String mainFile;
    private boolean debugPrint;

    public LaunchOptions(String[] args)
    {
        includeDirs = new ArrayList<>();
        try
        {
            includeDirs.add(new File(Launcher.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getPath() + "/lib");
        } catch (URISyntaxException e)
        {
            System.err.println("Could not add default Search Path to Include Directories!");
        }

        parseArgs(args);
    }

    private void parseArgs(String[] args)
    {
        for(int i = 0;i<args.length;i++)
        {
            String nextArgument = args[i];
            if(nextArgument.startsWith("-"))
            {
                String argumentType = nextArgument.substring(1);
                switch (argumentType)
                {
                    case "I" -> {
                        includeDirs.add(args[++i]);
                    }
                    case "debugPrint" -> {
                        debugPrint = true;
                    }
                    default -> {
                        System.out.println("Unknown Option " + nextArgument);
                    }
                }
            }
            else
            {
                if(mainFile != null)
                    System.out.println("WARNING: Two Main Files given. Ignoring the Second one.");
                else
                {
                    mainFile = nextArgument;
                }
            }
        }
    }

    public void validate()
    {
        if(mainFile == null)
        {
            System.err.println("No Main File given!");
            System.exit(1);
        }
    }

    public List<String> getIncludeDirs()
    {
        return includeDirs;
    }

    public String getMainFile()
    {
        return mainFile;
    }

    public boolean isDebugPrint()
    {
        return debugPrint;
    }
}
