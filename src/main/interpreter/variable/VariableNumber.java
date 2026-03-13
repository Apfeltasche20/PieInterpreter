package main.interpreter.variable;

public class VariableNumber extends Variable
{
    private long value;

    public VariableNumber(long value)
    {
        super(VariableType.NUMBER);
        this.value = value;
    }

    @Override
    public Variable copy()
    {
        VariableNumber copy = new VariableNumber(value);
        copy.name = this.name;
        return copy;
    }

    @Override
    public String asString()
    {
        return String.valueOf(value);
    }

    @Override
    public boolean asBoolean()
    {
        return value != 0;
    }

    @Override
    public long asNumber()
    {
        return value;
    }
}
