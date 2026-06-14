package main.interpreter.variable;

import main.interpreter.Interpreter;
import main.interpreter.action.CallFunctionAction;
import main.interpreter.action.InternClassAction;
import main.interpreter.action.InternFunctionAction;
import main.interpreter.intern.classes.ExposedFunction;
import main.interpreter.scope.Scope;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class VariableJavaObject extends Variable
{
    private InternClassAction internClass;
    private Object object;

    public VariableJavaObject(InternClassAction internClass, Object object)
    {
        super(VariableType.JAVA_OBJECT);
        this.internClass = internClass;
        this.object = object;
    }

    public Object getRawValue()
    {
        return object;
    }

    @Override
    public Class<?> getTypeClass()
    {
        return object.getClass();
    }

    @Override
    public Variable executeFunctionOnVariable(Interpreter interpreter, Scope scope, CallFunctionAction callFunctionAction)
    {
        InternFunctionAction functionToCall = internClass.getInternFunction(callFunctionAction.functionName);
        if(functionToCall == null)
        {
            System.err.println("Function "+callFunctionAction.functionName+" not defined on Intern Class "+internClass.getClassName()+"!");
            return new Variable();
        }

        Method[] methods = object.getClass().getMethods();
        Method methodToCall = null;
        for(int i = 0;i<methods.length;i++)
        {
            Method current = methods[i];
            ExposedFunction annotation = current.getAnnotation(ExposedFunction.class);
            if(annotation != null)
            {
                if(annotation.name().isEmpty())
                {
                    if (current.getName().equals(callFunctionAction.functionName))
                    {
                        methodToCall = current;
                        break;
                    }
                }
                else
                {
                    if (annotation.name().equals(callFunctionAction.functionName))
                    {
                        methodToCall = current;
                        break;
                    }
                }
            }
        }

        if(methodToCall == null)
        {
            System.err.println("Function "+callFunctionAction.functionName+" not found on Intern Class "+internClass.getClassName()+"!");
            return new Variable();
        }

        Class<?>[] shouldParameterClasses = methodToCall.getParameterTypes();
        Variable[] variables = new Variable[callFunctionAction.arguments.size()];
        Object[] args = new Object[callFunctionAction.arguments.size()];
        Class<?>[] isParameterClasses = new Class[args.length];

        for(int i = 0;i<args.length;i++)
        {
            variables[i] = callFunctionAction.arguments.get(i).evaluate(interpreter, scope);
            args[i] = variables[i].getRawValue();
            isParameterClasses[i] = variables[i].getTypeClass();
        }

        boolean signatureIsCorrect = shouldParameterClasses.length == isParameterClasses.length;
        if(signatureIsCorrect)
        {
            for(int i = 0;i<shouldParameterClasses.length;i++)
            {
                if (!shouldParameterClasses[i].isAssignableFrom(isParameterClasses[i]))
                {
                    signatureIsCorrect = false;
                    break;
                }
            }
        }
        if(!signatureIsCorrect)
        {
            System.err.println("Function "+callFunctionAction.functionName+" on Internal Class "+internClass.getClassName()+" not called with right Arguments");
            System.err.println("Needed: " + Arrays.toString(shouldParameterClasses));
            System.err.println("Got: " + Arrays.toString(isParameterClasses));
            return new Variable();
        }

        try
        {
            Object returnValue = methodToCall.invoke(object, args);
            if(returnValue != null)
                return Variable.wrapJavaValueIntoVariable(interpreter, returnValue);
            else
                return new Variable();
        } catch (IllegalAccessException e)
        {
            System.err.println("Error accessing Function "+callFunctionAction.functionName+" with arguments on Intern Class " + internClass.getClassName() + "!");
            return new Variable();
        } catch (InvocationTargetException e)
        {
            System.err.println("Error invoking Function "+callFunctionAction.functionName+" with arguments on Intern Class " + internClass.getClassName() + "!");
            return new Variable();
        }
    }

    @Override
    public Variable copy()
    {
        VariableJavaObject copy = new VariableJavaObject(internClass, object);
        copy.name = this.name;
        return copy;
    }

    @Override
    public String asString()
    {
        return object.toString();
    }

    @Override
    public boolean asBoolean()
    {
        return object != null;
    }
}
