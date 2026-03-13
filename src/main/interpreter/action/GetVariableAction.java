package main.interpreter.action;

import main.interpreter.Interpreter;
import main.interpreter.scope.Scope;
import main.interpreter.scope.ScopeEndReason;
import main.interpreter.scope.ScopeResult;
import main.interpreter.variable.Variable;
import main.util.SaveOutputStream;

public class GetVariableAction extends CodeAction
{
    public String variableName;

    public GetVariableAction(String variableName)
    {
        this.variableName = variableName;
    }

    @Override
    public Variable evaluate(Interpreter interpreter, Scope scope, ScopeResult result)
    {
        result.reason = ScopeEndReason.END_OF_CODE;
        Variable variable = scope.getVariableByName(variableName);
        if(variable == null)
        {
            System.err.println("Variable " + variableName + " not found!");
            System.exit(-1);
        }
        return variable;
    }

    @Override
    public CodeAction copy()
    {
        return new GetVariableAction(variableName);
    }

    @Override
    public void toBinary(SaveOutputStream saveOutputStream)
    {
        saveOutputStream.writeInt(5);
        saveOutputStream.saveAndWriteString(variableName);
    }
}
