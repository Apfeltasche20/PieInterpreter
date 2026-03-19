package main.interpreter.action;

import main.interpreter.Interpreter;
import main.interpreter.clazz.Clazz;
import main.interpreter.function.Function;
import main.interpreter.scope.Scope;
import main.interpreter.scope.ScopeResult;
import main.interpreter.scope.ScopeType;
import main.interpreter.variable.Variable;
import main.interpreter.variable.VariableObject;
import main.util.SaveOutputStream;

import java.util.ArrayList;
import java.util.List;

public class NewClassObjectAction extends CodeAction
{
    private String className;
    private List<CodeAction> constructorArguments;

    public NewClassObjectAction(String className, List<CodeAction> constructorArguments)
    {
        this.className = className;
        this.constructorArguments = constructorArguments;
    }

    @Override
    public Variable evaluateInOtherScope(Interpreter interpreter, Scope originalScope, Scope otherScope, ScopeResult result)
    {
        Clazz clazz = otherScope.getClassByName(className);
        if(clazz == null)
        {
            System.err.println("Class " + className + " not found!");
            System.exit(-1);
        }

        Scope newObjectScope = clazz.getClassScope().copy();
        //newObjectScope.setParent(scope);

        interpreter.executeScope(newObjectScope, ScopeType.FUNCTION);
        Function constructorFunction = newObjectScope.getFunctionByName(clazz.getClassName());
        if(constructorFunction != null)
        {
            CallFunctionAction callFunctionAction = new CallFunctionAction(clazz.getClassName(), constructorArguments);
            callFunctionAction.evaluate(interpreter, newObjectScope);
        }

        return new VariableObject(newObjectScope, clazz);
    }

    @Override
    public Variable evaluate(Interpreter interpreter, Scope scope, ScopeResult result)
    {
        Clazz clazz = scope.getClassByName(className);
        if(clazz == null)
        {
            System.err.println("Class " + className + " not found!");
            System.exit(-1);
        }

        Scope newObjectScope = clazz.getClassScope().copy();
        //newObjectScope.setParent(scope);

        interpreter.executeScope(newObjectScope, ScopeType.FUNCTION);
        Function constructorFunction = newObjectScope.getFunctionByName(clazz.getClassName());
        if(constructorFunction != null)
        {
            CallFunctionAction callFunctionAction = new CallFunctionAction(clazz.getClassName(), constructorArguments);
            callFunctionAction.evaluateInOtherScope(interpreter, scope, newObjectScope);
        }

        return new VariableObject(newObjectScope, clazz);
    }

    @Override
    public CodeAction copy()
    {
        List<CodeAction> listCopy = new ArrayList<>();
        for(int i = 0;i<constructorArguments.size();i++)
            listCopy.add(constructorArguments.get(i).copy());
        return new NewClassObjectAction(className, constructorArguments);
    }

    @Override
    public void toBinary(SaveOutputStream saveOutputStream)
    {
        saveOutputStream.writeInt(10);
        saveOutputStream.saveAndWriteString(className);
        saveOutputStream.writeInt(0);
        saveOutputStream.writeInt(constructorArguments.size());
        for(int i = 0;i<constructorArguments.size();i++)
            constructorArguments.get(i).toBinary(saveOutputStream);
    }
}
