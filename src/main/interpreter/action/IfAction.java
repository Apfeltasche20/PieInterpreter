package main.interpreter.action;

import main.code.Compiler;
import main.interpreter.Interpreter;
import main.interpreter.scope.ScopeResult;
import main.interpreter.scope.ScopeType;
import main.interpreter.scope.Scope;
import main.interpreter.variable.Variable;
import main.util.SaveOutputStream;

public class IfAction extends CodeAction
{
    public CodeAction condition;
    public ElseAction elseAction;
    public Scope scope;

    public IfAction(CodeAction codeAction)
    {
        this.condition = codeAction;
        this.scope = new Scope();
    }

    @Override
    public Variable evaluate(Interpreter interpreter, Scope scope, ScopeResult result)
    {
        Variable variable = condition.evaluate(interpreter, scope);
        boolean conditionValue = variable.asBoolean();
        ScopeResult innerResult = new ScopeResult();
        Variable returnVariable = null;
        if(conditionValue)
        {
            returnVariable = interpreter.executeScope(this.scope, ScopeType.CONDITION, innerResult);
        }
        else if(elseAction != null)
        {
            returnVariable = elseAction.evaluate(interpreter, scope, innerResult);
        }
        result.reason = innerResult.reason;
        return returnVariable;
    }

    @Override
    public CodeAction copy()
    {
        IfAction copy = new IfAction(condition.copy());
        if(elseAction != null)
            copy.elseAction = (ElseAction) this.elseAction.copy();
        copy.scope = scope.copy();
        return copy;
    }

    @Override
    public void toBinary(SaveOutputStream saveOutputStream)
    {
        saveOutputStream.writeInt(6);
        saveOutputStream.writeInt(0);
        condition.toBinary(saveOutputStream);
        elseAction.toBinary(saveOutputStream);
        Compiler.saveScope(saveOutputStream, scope);
    }
}
