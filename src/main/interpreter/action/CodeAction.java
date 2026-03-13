package main.interpreter.action;

import main.code.Compiler;
import main.interpreter.Interpreter;
import main.interpreter.scope.Scope;
import main.interpreter.scope.ScopeResult;
import main.interpreter.variable.Variable;
import main.util.SaveOutputStream;

public abstract class CodeAction
{
    public Variable evaluate(Interpreter interpreter, Scope scope)
    {
        ScopeResult result = new ScopeResult();
        return evaluate(interpreter, scope, result);
    }

    public Variable evaluateInOtherScope(Interpreter interpreter, Scope originalScope, Scope otherScope, ScopeResult result)
    {
        return evaluate(interpreter, otherScope, result);
    }

    public Variable evaluateInOtherScope(Interpreter interpreter, Scope originalScope, Scope otherScope)
    {
        ScopeResult result = new ScopeResult();
        return evaluateInOtherScope(interpreter, originalScope, otherScope, result);
    }

    public abstract Variable evaluate(Interpreter interpreter, Scope scope, ScopeResult result);
    public abstract CodeAction copy();
    public abstract void toBinary(SaveOutputStream saveOutputStream);
}
