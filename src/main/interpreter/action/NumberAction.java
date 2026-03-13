package main.interpreter.action;

import main.interpreter.Interpreter;
import main.interpreter.scope.Scope;
import main.interpreter.scope.ScopeEndReason;
import main.interpreter.scope.ScopeResult;
import main.interpreter.variable.Variable;
import main.interpreter.variable.VariableNumber;
import main.util.SaveOutputStream;

public class NumberAction extends CodeAction
{
    private long number;

    public NumberAction(String number)
    {
        this.number = Long.parseLong(number);
    }

    public NumberAction(long number)
    {
        this.number = number;
    }

    @Override
    public Variable evaluate(Interpreter interpreter, Scope scope, ScopeResult result)
    {
        result.reason = ScopeEndReason.END_OF_CODE;
        return new VariableNumber(number);
    }

    @Override
    public CodeAction copy()
    {
        return new NumberAction(number);
    }

    @Override
    public void toBinary(SaveOutputStream saveOutputStream)
    {
        saveOutputStream.writeInt(11);
        saveOutputStream.writeInt(0);
        saveOutputStream.writeLong(number);
    }
}
