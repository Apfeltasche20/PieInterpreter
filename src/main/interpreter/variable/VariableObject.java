package main.interpreter.variable;

import main.interpreter.clazz.Clazz;
import main.interpreter.scope.Scope;

public class VariableObject extends Variable
{
    private Scope object;
    private Clazz clazz;

    public VariableObject(String name, Scope object, Clazz clazz)
    {
        super(VariableType.OBJECT);
        this.name = name;
        this.object = object;
        this.clazz = clazz;
    }

    public Object getRawValue()
    {
        return this;
    }

    public Class<?> getTypeClass()
    {
        return VariableObject.class;
    }

    public VariableObject(Scope object, Clazz clazz)
    {
        super(VariableType.OBJECT);
        this.object = object;
        this.clazz = clazz;
    }

    @Override
    public Variable copy()
    {
        VariableObject copy = new VariableObject(object, clazz);
        copy.name = this.name;
        return copy;
    }

    public Scope getObject()
    {
        return object;
    }

    public Clazz getClazz()
    {
        return clazz;
    }

    @Override
    public String asString()
    {
        return "Object of Class " + clazz.getClassName();
    }
}
