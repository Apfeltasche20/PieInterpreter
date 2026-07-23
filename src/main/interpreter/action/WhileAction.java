package main.interpreter.action;

import main.code.Compiler;
import main.interpreter.Interpreter;
import main.interpreter.scope.Scope;
import main.interpreter.scope.ScopeResult;
import main.interpreter.scope.ScopeType;
import main.interpreter.variable.Variable;
import main.util.SaveOutputStream;

public class WhileAction extends CodeAction
{
    public CodeAction condition;
    public Scope scope;

    public WhileAction(CodeAction codeAction)
    {
        this.condition = codeAction;
        this.scope = new Scope();
    }

    @Override
    public Variable evaluate(Interpreter interpreter, Scope scope, ScopeResult result)
    {
        this.scope.setParent(scope);

        Variable variable = condition.evaluate(interpreter, scope);
        boolean conditionValue = variable.asBoolean();
        ScopeResult innerResult = new ScopeResult();
        Variable returnVariable = null;
        while(conditionValue)
        {
            returnVariable = interpreter.executeScope(this.scope, ScopeType.CONDITION, innerResult);

            variable = condition.evaluate(interpreter, scope);
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
        System.out.println("WARNING While action to Binary not implemented!");
        //saveOutputStream.writeInt(6);
        //saveOutputStream.writeInt(0);
        //condition.toBinary(saveOutputStream);
        //elseAction.toBinary(saveOutputStream);
        //Compiler.saveScope(saveOutputStream, scope);
    }
}