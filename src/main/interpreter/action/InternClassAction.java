package main.interpreter.action;

import main.interpreter.Interpreter;
import main.interpreter.clazz.Clazz;
import main.interpreter.scope.Scope;
import main.interpreter.scope.ScopeEndReason;
import main.interpreter.scope.ScopeResult;
import main.interpreter.variable.Variable;
import main.util.SaveOutputStream;

import java.util.ArrayList;
import java.util.List;

public class InternClassAction extends Clazz
{
    private List<InternFunctionAction> internFunctions;

    public InternClassAction(String className)
    {
        super(className, null);
        this.internFunctions = new ArrayList<>();
    }

    public void addInternFunction(InternFunctionAction internFunctionAction)
    {
        internFunctions.add(internFunctionAction);
    }

    public InternFunctionAction getInternFunction(String name)
    {
        for(int i = 0;i<internFunctions.size();i++)
            if(internFunctions.get(i).getName().equals(name))
                return internFunctions.get(i);
        return null;
    }

    @Override
    public Variable evaluate(Interpreter interpreter, Scope scope, ScopeResult result)
    {
        result.reason = ScopeEndReason.END_OF_CODE;
        scope.addClass(this);

        return null;
    }

    @Override
    public CodeAction copy()
    {
        return new InternClassAction(getClassName());
    }

    @Override
    public void toBinary(SaveOutputStream saveOutputStream)
    {

    }
}
