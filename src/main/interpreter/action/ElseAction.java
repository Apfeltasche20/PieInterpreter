package main.interpreter.action;

import main.code.Compiler;
import main.interpreter.Interpreter;
import main.interpreter.scope.ScopeResult;
import main.interpreter.scope.ScopeType;
import main.interpreter.scope.Scope;
import main.interpreter.variable.Variable;
import main.util.SaveOutputStream;

public class ElseAction extends CodeAction
{
    public Scope scope;

    public ElseAction()
    {
        this.scope = new Scope();
    }

    @Override
    public Variable evaluate(Interpreter interpreter, Scope scope, ScopeResult result)
    {
        ScopeResult innerResult = new ScopeResult();
        Variable returnVariable = interpreter.executeScope(this.scope, ScopeType.CONDITION, innerResult);
        result.reason = innerResult.reason;

        return returnVariable;
    }

    @Override
    public CodeAction copy()
    {
        ElseAction elseAction = new ElseAction();
        elseAction.scope = this.scope.copy();
        return elseAction;
    }

    @Override
    public void toBinary(SaveOutputStream saveOutputStream)
    {
        saveOutputStream.writeInt(3);
        saveOutputStream.writeInt(0);
        Compiler.saveScope(saveOutputStream, scope);
    }
}
