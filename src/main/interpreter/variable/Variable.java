package main.interpreter.variable;

import main.interpreter.Interpreter;
import main.interpreter.action.CallFunctionAction;
import main.interpreter.function.Function;
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

    public Class<?> getTypeClass()
    {
        return null;
    }

    public Object getRawValue()
    {
        return null;
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

    public Function asFunction()
    {
        return null;
    }

    public Variable[] asArray()
    {
        return new Variable[0];
    }

    public byte[] asByteArray()
    {
        return new byte[0];
    }

    public static Variable wrapJavaValueIntoVariable(Interpreter interpreter, Object value)
    {
        switch (value)
        {
            case Boolean b ->
            {
                return new VariableBoolean(b);
            }
            case Long l ->
            {
                return new VariableNumber(l);
            }
            case Integer i ->
            {
                return new VariableNumber(i);
            }
            case String string ->
            {
                return new VariableString(string);
            }
            default ->
            {
                VariableJavaObject variableJavaObject = interpreter.createInternObjectFromJavaObject(value);
                if (variableJavaObject != null)
                    return variableJavaObject;

                System.err.println("Could not wrap Java Value of Class " + value.getClass().getName());
                return null;
            }
        }
    }
}
