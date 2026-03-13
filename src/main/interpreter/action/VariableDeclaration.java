package main.interpreter.action;

import main.interpreter.Interpreter;
import main.interpreter.scope.Scope;
import main.interpreter.scope.ScopeEndReason;
import main.interpreter.scope.ScopeResult;
import main.interpreter.variable.Variable;
import main.util.SaveOutputStream;

public class VariableDeclaration extends CodeAction
{
    protected String name;
    protected CodeAction value;

    public VariableDeclaration(String name)
    {
        this.name = name;
        this.value = null;
    }

    public void setValue(CodeAction value)
    {
        this.value = value;
    }

    public String getName()
    {
        return name;
    }

    public CodeAction getValue()
    {
        return value;
    }

    @Override
    public Variable evaluate(Interpreter interpreter, Scope scope, ScopeResult result)
    {
        result.reason = ScopeEndReason.END_OF_CODE;
        Variable variable;
        if(value != null)
        {
            variable = value.evaluate(interpreter, scope);
        }
        else
        {
            variable = new Variable();
        }
        variable.setName(name);
        scope.addVariable(variable);
        return variable;
    }

    @Override
    public CodeAction copy()
    {
        VariableDeclaration copy = new VariableDeclaration(name);
        if(value != null)
            copy.value = value.copy();
        return copy;
    }

    @Override
    public void toBinary(SaveOutputStream saveOutputStream)
    {
        saveOutputStream.writeInt(16);
        saveOutputStream.writeInt(value != null ? 1 : 0);
        if(value != null)
            value.toBinary(saveOutputStream);
    }
}
