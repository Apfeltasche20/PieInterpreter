package main.interpreter.action;

import main.interpreter.Interpreter;
import main.interpreter.scope.Scope;
import main.interpreter.scope.ScopeEndReason;
import main.interpreter.scope.ScopeResult;
import main.interpreter.variable.Variable;
import main.interpreter.variable.VariableArray;
import main.interpreter.variable.VariableBoolean;
import main.interpreter.variable.VariableNumber;
import main.util.SaveOutputStream;

public class ArrayAction extends CodeAction
{
    private CodeAction length;

    public ArrayAction(CodeAction length)
    {
        this.length = length;
    }

    @Override
    public Variable evaluate(Interpreter interpreter, Scope scope, ScopeResult result)
    {
        result.reason = ScopeEndReason.END_OF_CODE;

        Variable arrayLength = length.evaluate(interpreter, scope);

        if(!(arrayLength instanceof VariableNumber))
        {
            System.err.println("Array Length can only be a Number!");
            return new VariableArray(0);
        }

        return new VariableArray((int) arrayLength.asNumber());
    }

    @Override
    public CodeAction copy()
    {
        return new ArrayAction(length);
    }

    @Override
    public void toBinary(SaveOutputStream saveOutputStream)
    {
        saveOutputStream.writeInt(19);
        saveOutputStream.writeInt(0);
        length.toBinary(saveOutputStream);
    }
}
