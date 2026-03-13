package main.interpreter.action;

import main.interpreter.Interpreter;
import main.interpreter.scope.Scope;
import main.interpreter.scope.ScopeEndReason;
import main.interpreter.scope.ScopeResult;
import main.interpreter.variable.Variable;
import main.util.SaveOutputStream;

import java.util.ArrayList;
import java.util.List;

public class InternAction extends CodeAction
{
    public CodeAction internAction;

    public InternAction(CodeAction internAction)
    {
        this.internAction = internAction;
    }

    @Override
    public Variable evaluate(Interpreter interpreter, Scope scope, ScopeResult result)
    {
        result.reason = ScopeEndReason.END_OF_CODE;
        if(internAction instanceof CallFunctionAction)
        {
            String functionName = ((CallFunctionAction) internAction).functionName;
            List<CodeAction> args = ((CallFunctionAction) internAction).arguments;
            List<Variable> variables = new ArrayList<>();
            for (int i = 0; i < args.size(); i++)
                variables.add(args.get(i).evaluate(interpreter, scope));
            return interpreter.executeInternFunction(functionName, variables);
        }
        else
        {
            System.err.println("Only Function can be called intern!");
            return new Variable();
        }
    }

    @Override
    public CodeAction copy()
    {
        return new InternAction(internAction.copy());
    }

    @Override
    public void toBinary(SaveOutputStream saveOutputStream)
    {
        saveOutputStream.writeInt(8);
        saveOutputStream.writeInt(0);
        internAction.toBinary(saveOutputStream);
    }
}
