package main.interpreter.intern.classes;

import main.interpreter.variable.Variable;
import main.interpreter.variable.VariableString;

import java.nio.charset.StandardCharsets;

@ExposedClass(internModule = "test", internName = "String")
public class InternString
{
    private String value;

    public InternString(String value)
    {
        this.value = value;
    }

    @ExposedFunction
    public void append(String string)
    {
        value += string;
    }

    @ExposedFunction(name = "toString")
    public String getString()
    {
        return value;
    }

    @ExposedFunction
    public byte[] getBytes()
    {
        return value.getBytes(StandardCharsets.UTF_8);
    }
}
