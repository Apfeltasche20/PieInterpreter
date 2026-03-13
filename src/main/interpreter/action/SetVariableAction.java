package main.interpreter.action;

import main.interpreter.Interpreter;
import main.interpreter.scope.Scope;
import main.interpreter.scope.ScopeEndReason;
import main.interpreter.scope.ScopeResult;
import main.interpreter.variable.Variable;
import main.util.SaveOutputStream;

public class SetVariableAction extends CodeAction
{
    public String variableName;
    public CodeAction action;

    public SetVariableAction(String variableName, CodeAction action)
    {
        this.variableName = variableName;
        this.action = action;
    }

    @Override
    public Variable evaluate(Interpreter interpreter, Scope scope, ScopeResult result)
    {
        result.reason = ScopeEndReason.END_OF_CODE;

        Variable newValue = action.evaluate(interpreter, scope).copy();
        Variable variableForNewValue = scope.getVariableByName(variableName);
        if(variableForNewValue == null)
        {
            System.err.println("Variable " + variableName + " not found!");
            System.exit(-1);
        }
        newValue.setName(variableForNewValue.getName());
        scope.replaceVariable(newValue);

        return newValue;
        //return action.evaluate(interpreter, scope);
    }

    @Override
    public CodeAction copy()
    {
        return new SetVariableAction(variableName, action.copy());
    }

    @Override
    public void toBinary(SaveOutputStream saveOutputStream)
    {
        saveOutputStream.writeInt(14);
        saveOutputStream.saveAndWriteString(variableName);
        action.toBinary(saveOutputStream);
    }
}
