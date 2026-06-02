package main.interpreter.variable;

public class VariableString extends Variable
{
    private String value;

    public VariableString(String value)
    {
        super(VariableType.STRING);
        this.value = value;
    }

    public Object getRawValue()
    {
        return value;
    }

    public Class<?> getTypeClass()
    {
        return String.class;
    }

    @Override
    public Variable copy()
    {
        VariableString copy = new VariableString(value);
        copy.name = this.name;
        return copy;
    }

    @Override
    public String asString()
    {
        return value;
    }

    @Override
    public boolean asBoolean()
    {
        return !value.isEmpty();
    }
}
