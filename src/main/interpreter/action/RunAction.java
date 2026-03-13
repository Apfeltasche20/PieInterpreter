package main.interpreter.action;

import main.interpreter.Interpreter;
import main.interpreter.scope.Scope;
import main.interpreter.scope.ScopeEndReason;
import main.interpreter.scope.ScopeResult;
import main.interpreter.variable.Variable;
import main.util.SaveOutputStream;

public class RunAction extends CodeAction
{
    public CodeAction codeAction;

    public RunAction(CodeAction codeAction)
    {
        this.codeAction = codeAction;
    }

    @Override
    public Variable evaluate(Interpreter interpreter, Scope scope, ScopeResult result)
    {
        result.reason = ScopeEndReason.END_OF_CODE;
        return null;
    }

    @Override
    public CodeAction copy()
    {
        return new RunAction(codeAction.copy());
    }

    @Override
    public void toBinary(SaveOutputStream saveOutputStream)
    {
        saveOutputStream.writeInt(13);
        saveOutputStream.writeInt(0);
        codeAction.toBinary(saveOutputStream);
    }
}
