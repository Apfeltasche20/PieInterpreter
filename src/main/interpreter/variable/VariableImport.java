package main.interpreter.variable;

import main.interpreter.scope.Scope;

public class VariableImport extends Variable
{ ;
    private Scope scope;

    public VariableImport(String fileName, Scope scope)
    {
        super(VariableType.IMPORT);
        this.name = fileName;
        this.scope = scope;
    }

    public Scope getScope()
    {
        return scope;
    }

    @Override
    public Variable copy()
    {
        return new VariableImport(name, scope);
    }

    @Override
    public String asString()
    {
        return "File " + name + " import";
    }
}
