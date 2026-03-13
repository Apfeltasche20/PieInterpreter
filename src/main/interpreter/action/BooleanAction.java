package main.interpreter.action;

import main.interpreter.Interpreter;
import main.interpreter.scope.Scope;
import main.interpreter.scope.ScopeEndReason;
import main.interpreter.scope.ScopeResult;
import main.interpreter.variable.Variable;
import main.interpreter.variable.VariableBoolean;
import main.util.SaveOutputStream;

public class BooleanAction extends CodeAction
{
    public boolean value;

    public BooleanAction(boolean value)
    {
        this.value = value;
    }

    @Override
    public Variable evaluate(Interpreter interpreter, Scope scope, ScopeResult result)
    {
        result.reason = ScopeEndReason.END_OF_CODE;
        return new VariableBoolean(value);
    }

    @Override
    public CodeAction copy()
    {
        return new BooleanAction(value);
    }

    @Override
    public void toBinary(SaveOutputStream saveOutputStream)
    {
        saveOutputStream.writeInt(1);
        saveOutputStream.writeInt(value ? 1 : 0);
    }
}
