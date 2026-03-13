package main.interpreter.clazz;

import main.code.Compiler;
import main.interpreter.Interpreter;
import main.interpreter.action.CodeAction;
import main.interpreter.scope.Scope;
import main.interpreter.scope.ScopeEndReason;
import main.interpreter.scope.ScopeResult;
import main.interpreter.variable.Variable;
import main.util.SaveOutputStream;

public class Clazz extends CodeAction
{
    private String className;
    private Scope classScope;

    public Clazz(String className, Scope classScope)
    {
        this.className = className;
        this.classScope = classScope;
    }

    @Override
    public Variable evaluate(Interpreter interpreter, Scope scope, ScopeResult result)
    {
        result.reason = ScopeEndReason.END_OF_CODE;
        scope.addClass(this);

        return null;
    }

    public String getClassName()
    {
        return className;
    }

    public Scope getClassScope()
    {
        return classScope;
    }

    @Override
    public CodeAction copy()
    {
        return new Clazz(className, classScope.copy());
    }

    @Override
    public void toBinary(SaveOutputStream saveOutputStream)
    {
        saveOutputStream.writeInt(18);
        saveOutputStream.saveAndWriteString(className);
        Compiler.saveScope(saveOutputStream, classScope);
    }
}
