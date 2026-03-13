package main.interpreter.function;

import main.code.Compiler;
import main.interpreter.Interpreter;
import main.interpreter.action.CodeAction;
import main.interpreter.action.VariableDeclaration;
import main.interpreter.scope.Scope;
import main.interpreter.scope.ScopeEndReason;
import main.interpreter.scope.ScopeResult;
import main.interpreter.variable.Variable;
import main.interpreter.variable.VariableFunction;
import main.util.SaveOutputStream;

import java.util.List;

public class Function extends CodeAction
{
    private String name;
    private List<VariableDeclaration> arguments;
    private Scope functionScope;

    public Function(String name, List<VariableDeclaration> arguments)
    {
        this.name = name;
        this.arguments = arguments;
        this.functionScope = new Scope();
    }

    public List<VariableDeclaration> getArguments()
    {
        return arguments;
    }

    public Scope getFunctionScope()
    {
        return functionScope;
    }

    public String getName()
    {
        return name;
    }

    @Override
    public Function copy()
    {
        Function copy = new Function(name, arguments);
        copy.functionScope = this.functionScope.copy();
        return copy;
    }

    @Override
    public void toBinary(SaveOutputStream saveOutputStream)
    {
        saveOutputStream.writeInt(17);
        saveOutputStream.saveAndWriteString(name);
        saveOutputStream.writeInt(0);
        saveOutputStream.writeInt(arguments.size());
        for(int i = 0;i<arguments.size();i++)
            arguments.get(i).toBinary(saveOutputStream);
        Compiler.saveScope(saveOutputStream, functionScope);
    }

    @Override
    public Variable evaluate(Interpreter interpreter, Scope scope, ScopeResult result)
    {
        result.reason = ScopeEndReason.END_OF_CODE;
        scope.addFunction(this);
        return new VariableFunction(this);
    }
}
