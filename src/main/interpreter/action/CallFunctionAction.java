package main.interpreter.action;

import main.code.Compiler;
import main.interpreter.Interpreter;
import main.interpreter.scope.ScopeEndReason;
import main.interpreter.scope.ScopeResult;
import main.interpreter.scope.ScopeType;
import main.interpreter.function.Function;
import main.interpreter.scope.Scope;
import main.interpreter.variable.Variable;
import main.util.SaveOutputStream;

import java.util.ArrayList;
import java.util.List;

public class CallFunctionAction extends CodeAction
{
    public String functionName;
    public List<CodeAction> arguments;

    public CallFunctionAction(String functionName, List<CodeAction> arguments)
    {
        this.functionName = functionName;
        this.arguments = arguments;
    }

    @Override
    public Variable evaluateInOtherScope(Interpreter interpreter, Scope originalScope, Scope otherScope, ScopeResult result)
    {
        result.reason = ScopeEndReason.END_OF_CODE;
        Function functionToCall = otherScope.getFunctionByName(functionName);
        if(functionToCall == null)
        {
            System.err.println("Function " + functionName + " not found!");
            return new Variable();
        }

        Scope functionScope = functionToCall.getFunctionScope();
        List<VariableDeclaration> variableDeclarations = functionToCall.getArguments();
        List<Variable> argVariables = new ArrayList<>();

        for(int i = 0;i<variableDeclarations.size();i++)
            argVariables.add(variableDeclarations.get(i).evaluate(interpreter, functionScope));
        for(int i = 0;i<arguments.size();i++)
        {
            if(i >= argVariables.size())
                break;

            Variable oldVariable = argVariables.get(i);
            Variable newVariable = arguments.get(i).evaluate(interpreter, originalScope).copy();
            newVariable.setName(oldVariable.getName());
            functionScope.addVariable(newVariable);
        }

        return interpreter.executeScope(functionScope, ScopeType.FUNCTION);
    }

    @Override
    public Variable evaluate(Interpreter interpreter, Scope scope, ScopeResult result)
    {
        result.reason = ScopeEndReason.END_OF_CODE;
        Function functionToCall = scope.getFunctionByName(functionName);
        if(functionToCall == null)
        {
            System.err.println("Function " + functionName + " not found!");
            return new Variable();
        }

        Scope functionScope = functionToCall.getFunctionScope();
        List<VariableDeclaration> variableDeclarations = functionToCall.getArguments();
        List<Variable> argVariables = new ArrayList<>();

        for(int i = 0;i<variableDeclarations.size();i++)
            argVariables.add(variableDeclarations.get(i).evaluate(interpreter, functionScope));
        for(int i = 0;i<arguments.size();i++)
        {
            if(i >= argVariables.size())
                break;

            Variable oldVariable = argVariables.get(i);
            Variable newVariable = arguments.get(i).evaluate(interpreter, scope).copy();
            newVariable.setName(oldVariable.getName());
            functionScope.addVariable(newVariable);
        }

        return interpreter.executeScope(functionScope, ScopeType.FUNCTION);
    }

    @Override
    public CodeAction copy()
    {
        List<CodeAction> listCopy = new ArrayList<>();
        for(int i = 0;i<arguments.size();i++)
            listCopy.add(arguments.get(i).copy());
        return new CallFunctionAction(functionName, listCopy);
    }

    @Override
    public void toBinary(SaveOutputStream saveOutputStream)
    {
        saveOutputStream.writeInt(2);
        saveOutputStream.saveAndWriteString(functionName);
        saveOutputStream.writeInt(0);
        saveOutputStream.writeInt(arguments.size());
        for(int i = 0;i<arguments.size();i++)
            arguments.get(i).toBinary(saveOutputStream);
    }
}
