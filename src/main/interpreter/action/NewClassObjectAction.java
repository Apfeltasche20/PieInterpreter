package main.interpreter.action;

import main.interpreter.Interpreter;
import main.interpreter.clazz.Clazz;
import main.interpreter.function.Function;
import main.interpreter.intern.classes.ExposedFunction;
import main.interpreter.scope.Scope;
import main.interpreter.scope.ScopeResult;
import main.interpreter.scope.ScopeType;
import main.interpreter.variable.Variable;
import main.interpreter.variable.VariableJavaObject;
import main.interpreter.variable.VariableObject;
import main.util.SaveOutputStream;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
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

        if(clazz instanceof InternClassAction)
        {
            Class<?> internClass = interpreter.getInternClass(clazz.getClassName());
            if(internClass == null)
            {
                System.err.println("Intern Class " + clazz.getClassName() + " not found!");
                return null;
            }

            Variable[] variables = new Variable[constructorArguments.size()];
            Class<?>[] types = new Class[constructorArguments.size()];
            Object[] args = new Object[constructorArguments.size()];
            for(int i = 0;i<args.length;i++)
            {
                variables[i] = constructorArguments.get(i).evaluate(interpreter, scope);
                args[i] = variables[i].getRawValue();
                types[i] = variables[i].getTypeClass();
            }

            try
            {
                Constructor<?> constructor = internClass.getConstructor(types);
                if(!constructor.isAnnotationPresent(ExposedFunction.class))
                {
                    System.err.println("Constructor on Class " + clazz.getClassName() + " with arguments " + Arrays.toString(types) + " is not accessible!");
                    return new Variable();
                }
                Object javaObject = constructor.newInstance(args);
                return new VariableJavaObject((InternClassAction) clazz, javaObject);
            } catch (NoSuchMethodException e)
            {
                System.err.println("Could not find Constructor with arguments " + Arrays.toString(types) + " on Intern Class " + clazz.getClassName() + "!");
                return new Variable();
            } catch (InvocationTargetException e)
            {
                System.err.println("Error invoking Constructor with arguments " + Arrays.toString(types) + " on Intern Class " + clazz.getClassName() + "!");
                e.printStackTrace();
                return new Variable();
            } catch (InstantiationException e)
            {
                System.err.println("Error calling Constructor with arguments " + Arrays.toString(types) + " on Intern Class " + clazz.getClassName() + "!");
                return new Variable();
            } catch (IllegalAccessException e)
            {
                System.err.println("Error accessing Constructor with arguments " + Arrays.toString(types) + " on Intern Class " + clazz.getClassName() + "!");
                return new Variable();
            }
        }
        else
        {
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

    public String getClassName()
    {
        return className;
    }

    public List<CodeAction> getConstructorArguments()
    {
        return constructorArguments;
    }
}
