package main.interpreter.action;

import main.interpreter.Interpreter;
import main.interpreter.scope.Scope;
import main.interpreter.scope.ScopeResult;
import main.interpreter.variable.Variable;
import main.util.SaveOutputStream;

public class InternFunctionAction extends CodeAction
{
    private String functionName;

    public InternFunctionAction(String functionName)
    {
        this.functionName = functionName;
    }

    public String getName()
    {
        return functionName;
    }

    @Override
    public Variable evaluate(Interpreter interpreter, Scope scope, ScopeResult result)
    {
        return null;
    }

    @Override
    public CodeAction copy()
    {
        return new InternFunctionAction(functionName);
    }

    @Override
    public void toBinary(SaveOutputStream saveOutputStream)
    {

    }
}
