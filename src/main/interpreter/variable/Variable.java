package main.interpreter.variable;

import main.interpreter.Interpreter;
import main.interpreter.action.CallFunctionAction;
import main.interpreter.scope.Scope;

public class Variable
{
    protected VariableType currentType;
    protected String name;

    public Variable(VariableType currentType)
    {
        this.currentType = currentType;
    }

    public Variable()
    {
        this(VariableType.NONE);
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public VariableType getCurrentType()
    {
        return currentType;
    }

    public Variable executeFunctionOnVariable(Interpreter interpreter, Scope scope, CallFunctionAction callFunctionAction)
    {
        System.err.println("Function " + callFunctionAction.functionName + " not supported on " + getClass().getName());
        return new Variable();
    }

    public Variable copy()
    {
        Variable copy = new Variable(currentType);
        copy.name = this.name;
        return copy;
    }

    public String asString()
    {
        return "null";
    }

    public boolean asBoolean()
    {
        return false;
    }

    public long asNumber()
    {
        return 0;
    }
}
