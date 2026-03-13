package main.interpreter.action;

import main.interpreter.Interpreter;
import main.interpreter.scope.Scope;
import main.interpreter.scope.ScopeEndReason;
import main.interpreter.scope.ScopeResult;
import main.interpreter.variable.Variable;
import main.interpreter.variable.VariableArray;
import main.interpreter.variable.VariableImport;
import main.interpreter.variable.VariableObject;
import main.util.SaveOutputStream;

public class ExecuteOnOtherScopeAction extends CodeAction
{
    private String variableName;
    private CodeAction action;

    public ExecuteOnOtherScopeAction(String variableName)
    {
        this.variableName = variableName;
        //this.action = action;
    }

    public void setAction(CodeAction action)
    {
        this.action = action;
    }

    @Override
    public Variable evaluate(Interpreter interpreter, Scope scope, ScopeResult result)
    {
        result.reason = ScopeEndReason.END_OF_CODE;
        Variable variableToExecuteOn = scope.getVariableByName(variableName);
        if(variableToExecuteOn == null)
        {
            System.err.println("Variable " + variableName + " not found!");
            System.exit(-1);
        }
        else
        {
            if(variableToExecuteOn instanceof VariableObject)
            {
                Scope objectScope = ((VariableObject) variableToExecuteOn).getObject();
                return action.evaluateInOtherScope(interpreter, scope, objectScope);
            }
            else if(variableToExecuteOn instanceof VariableImport)
            {
                Scope fileScope = ((VariableImport) variableToExecuteOn).getScope();
                return action.evaluateInOtherScope(interpreter, scope, fileScope);
            }
            else
            {
                if(action instanceof CallFunctionAction)
                {
                    return variableToExecuteOn.executeFunctionOnVariable(interpreter, scope, (CallFunctionAction) action);
                }
                else
                {
                    System.err.println("Cannot use " + action.getClass().getName() + " on a variable");
                    System.exit(-1);
                }
            }
        }
        return null;
    }

    @Override
    public CodeAction copy()
    {
        ExecuteOnOtherScopeAction copy = new ExecuteOnOtherScopeAction(variableName);
        copy.action = this.action;
        return copy;
    }

    @Override
    public void toBinary(SaveOutputStream saveOutputStream)
    {
        saveOutputStream.writeInt(4);
        saveOutputStream.saveAndWriteString(variableName);
        action.toBinary(saveOutputStream);
    }
}
