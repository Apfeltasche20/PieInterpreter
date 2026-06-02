import main.interpreter.Interpreter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class Main
{
    public static void main(String[] args) throws IOException
    {
        Interpreter interpreter = new Interpreter(new ArrayList<>());
        interpreter.execute(Files.readString(Path.of("input.pie")));
    }
}
