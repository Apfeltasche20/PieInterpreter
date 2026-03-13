package main.interpreter.action;

import main.interpreter.Interpreter;
import main.interpreter.scope.Scope;
import main.interpreter.scope.ScopeEndReason;
import main.interpreter.scope.ScopeResult;
import main.interpreter.variable.Variable;
import main.interpreter.variable.VariableString;
import main.util.SaveOutputStream;

public class StringAction extends CodeAction
{
    private String string;

    public StringAction(String string)
    {
        this.string = string;
    }

    @Override
    public Variable evaluate(Interpreter interpreter, Scope scope, ScopeResult result)
    {
        result.reason = ScopeEndReason.END_OF_CODE;
        return new VariableString(string);
    }

    @Override
    public CodeAction copy()
    {
        return new StringAction(string);
    }

    @Override
    public void toBinary(SaveOutputStream saveOutputStream)
    {
        saveOutputStream.writeInt(15);
        saveOutputStream.saveAndWriteString(string);
    }
}
