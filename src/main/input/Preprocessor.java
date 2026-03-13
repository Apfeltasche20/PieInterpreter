package main.input;

public class Preprocessor
{
    public static String preprocess(String string)
    {
        string = string.replace("\t", " ").replace("\r", " ").replace("\n", " ");
        while(string.contains("  "))
            string = string.replace("  ", " ");
        return string;
    }
}
