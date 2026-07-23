package main.interpreter.action;

import main.code.Compiler;
import main.interpreter.Interpreter;
import main.interpreter.scope.Scope;
import main.interpreter.scope.ScopeResult;
import main.interpreter.scope.ScopeType;
import main.interpreter.variable.Variable;
import main.util.SaveOutputStream;

public class ForAction extends CodeAction
{
    public CodeAction loopStart;
    public CodeAction condition;
    public CodeAction loopIteration;
    public Scope scope;

    public ForAction(CodeAction loopStart, CodeAction condition, CodeAction loopIteration)
    {
        this.loopStart = loopStart;
        this.condition = condition;
        this.loopIteration = loopIteration;
        this.scope = new Scope();
    }

    @Override
    public Variable evaluate(Interpreter interpreter, Scope scope, ScopeResult result)
    {
        this.scope.setParent(scope);

        loopStart.evaluate(interpreter, this.scope);

        Variable variable = condition.evaluate(interpreter, this.scope);
        boolean conditionValue = variable.asBoolean();
        ScopeResult innerResult = new ScopeResult();
        Variable returnVariable = null;
        while(conditionValue)
        {
            returnVariable = interpreter.executeScope(this.scope, ScopeType.CONDITION, innerResult);

            loopIteration.evaluate(interpreter, this.scope);

            variable = condition.evaluate(interpreter, this.scope);
            conditionValue = variable.asBoolean();
        }
        result.reason = innerResult.reason;
        return returnVariable;
    }

    @Override
    public CodeAction copy()
    {
        WhileAction copy = new WhileAction(condition.copy());
        copy.scope = scope.copy();
        return copy;
    }

    @Override
    public void toBinary(SaveOutputStream saveOutputStream)
    {
        saveOutputStream.writeInt(20);
        saveOutputStream.writeInt(0);
        loopStart.toBinary(saveOutputStream);
        condition.toBinary(saveOutputStream);
        loopIteration.toBinary(saveOutputStream);
        Compiler.saveScope(saveOutputStream, scope);
    }
}
