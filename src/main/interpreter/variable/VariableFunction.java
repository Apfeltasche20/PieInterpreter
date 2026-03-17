package main.interpreter.variable;

import main.interpreter.function.Function;

public class VariableFunction extends Variable
{
    private Function value;

    public VariableFunction(Function value)
    {
        super(VariableType.FUNCTION);
        this.value = value;
    }

    @Override
    public Variable copy()
    {
        VariableFunction copy = new VariableFunction(value);
        copy.name = this.name;
        return copy;
    }

    @Override
    public String asString()
    {
        return "Function " + value.getName();
    }

    @Override
    public Function asFunction()
    {
        return value;
    }
}
