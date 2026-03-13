package main.interpreter.variable;

import main.interpreter.Interpreter;
import main.interpreter.action.CallFunctionAction;
import main.interpreter.action.CodeAction;
import main.interpreter.scope.Scope;

import java.util.List;

public class VariableArray extends Variable
{
    private int length;
    private Variable[] values;

    public VariableArray(int length)
    {
        super(VariableType.ARRAY);
        this.length = length;
        this.values = new Variable[length];
        for(int i = 0;i<values.length;i++)
            values[i] = new Variable();
    }

    @Override
    public Variable executeFunctionOnVariable(Interpreter interpreter, Scope scope, CallFunctionAction callFunctionAction)
    {
        switch (callFunctionAction.functionName)
        {
            case "set" -> {
                List<CodeAction> arguments = callFunctionAction.arguments;
                if(arguments.size() < 2)
                {
                    System.err.println("<array>.set(): not enough Arguments given");
                    System.err.println("Usage: <array>.set(index, value)");
                    return new Variable();
                }

                Variable index = arguments.getFirst().evaluate(interpreter, scope);
                if(!(index instanceof VariableNumber))
                {
                    System.err.println("<array>.set(): First Argument is not a Number");
                    System.err.println("Usage: <array>.set(index, value)");
                    return new Variable();
                }

                int arrayIndex = Math.toIntExact(index.asNumber());
                if((arrayIndex < 0) || (arrayIndex > length))
                {
                    System.err.println("<array>.set(): Index out of bounds. " + arrayIndex + " for size " + length);
                    System.err.println("Usage: <array>.set(index, value)");
                    return new Variable();
                }

                Variable newValue = arguments.get(1).evaluate(interpreter, scope);
                values[arrayIndex] = newValue;
                return newValue;
            }
            case "get" -> {
                List<CodeAction> arguments = callFunctionAction.arguments;
                if(arguments.isEmpty())
                {
                    System.err.println("<array>.get(): not enough Arguments given");
                    System.err.println("Usage: <array>.get(index)");
                    return new Variable();
                }

                Variable index = arguments.getFirst().evaluate(interpreter, scope);
                if(!(index instanceof VariableNumber))
                {
                    System.err.println("<array>.set(): First Argument is not a Number");
                    System.err.println("Usage: <array>.get(index)");
                    return new Variable();
                }

                int arrayIndex = Math.toIntExact(index.asNumber());
                if((arrayIndex < 0) || (arrayIndex > length))
                {
                    System.err.println("<array>.get(): Index out of bounds. " + arrayIndex + " for size " + length);
                    System.err.println("Usage: <array>.get(index)");
                    return new Variable();
                }

                return values[arrayIndex];
            }
            default -> {
                System.err.println("Function " + callFunctionAction.functionName + " not supported on Array!");
                return new Variable();
            }
        }
    }

    @Override
    public Variable copy()
    {
        VariableArray copy = new VariableArray(length);
        for(int i = 0;i<length;i++)
            copy.values[i] = values[i].copy();
        return copy;
    }

    @Override
    public String asString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for(int i = 0;i<length;i++)
        {
            builder.append(values[i].asString());
            if(i < (length - 1))
                builder.append(", ");
        }
        builder.append("]");
        return builder.toString();
    }
}
