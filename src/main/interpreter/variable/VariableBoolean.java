package main.interpreter.variable;

public class VariableBoolean extends Variable
{
    private boolean value;

    public VariableBoolean(boolean value)
    {
        super(VariableType.BOOLEAN);
        this.value = value;
    }

    @Override
    public Variable copy()
    {
        VariableBoolean copy = new VariableBoolean(value);
        copy.name = this.name;
        return copy;
    }

    @Override
    public String asString()
    {
        return Boolean.toString(value);
    }

    @Override
    public boolean asBoolean()
    {
        return value;
    }

    @Override
    public long asNumber()
    {
        return value ? 1 : 0;
    }
}
