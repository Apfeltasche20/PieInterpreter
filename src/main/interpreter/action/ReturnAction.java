package main.interpreter.action;

import main.interpreter.Interpreter;
import main.interpreter.scope.Scope;
import main.interpreter.scope.ScopeEndReason;
import main.interpreter.scope.ScopeResult;
import main.interpreter.variable.Variable;
import main.util.SaveOutputStream;

public class ReturnAction extends CodeAction
{
    public CodeAction returnValue;

    public ReturnAction(CodeAction returnValue)
    {
        this.returnValue = returnValue;
    }

    @Override
    public Variable evaluate(Interpreter interpreter, Scope scope, ScopeResult result)
    {
        result.reason = ScopeEndReason.RETURN;
        return returnValue.evaluate(interpreter, scope);
    }

    @Override
    public CodeAction copy()
    {
        return new ReturnAction(returnValue.copy());
    }

    @Override
    public void toBinary(SaveOutputStream saveOutputStream)
    {
        saveOutputStream.writeInt(12);
        saveOutputStream.writeInt(0);
        returnValue.toBinary(saveOutputStream);
    }
}
