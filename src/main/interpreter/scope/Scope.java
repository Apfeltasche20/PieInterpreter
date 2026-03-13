package main.interpreter.scope;

import main.interpreter.action.CodeAction;
import main.interpreter.clazz.Clazz;
import main.interpreter.function.Function;
import main.interpreter.variable.Variable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scope
{
    private Scope parent;

    private List<CodeAction> actionList;
    private List<Function> currentFunctionsScope;
    private List<Clazz> currentClasses;
    private Map<String, Variable> currentVariables;

    public Scope(Scope parent)
    {
        this.parent = parent;
        this.actionList = new ArrayList<>();
        this.currentFunctionsScope = new ArrayList<>();
        this.currentVariables = new HashMap<>();
        this.currentClasses = new ArrayList<>();
    }

    public Scope()
    {
        this(null);
    }

    public void setParent(Scope parent)
    {
        this.parent = parent;
    }

    public Scope copy()
    {
        Scope copy = new Scope();

        for(int i = 0;i<actionList.size();i++)
        {
            CodeAction action = this.actionList.get(i);
            if(action != null)
                copy.actionList.add(action.copy());
        }

        for(int i = 0;i<currentFunctionsScope.size();i++)
            copy.currentFunctionsScope.add(currentFunctionsScope.get(i).copy());

        copy.currentClasses.addAll(currentClasses);

        for(int i = 0;i<currentVariables.size();i++)
            copy.currentVariables.putAll(this.currentVariables);

        return copy;
    }

    public void replaceVariable(Variable variable)
    {
        if(currentVariables.containsKey(variable.getName()))
            addVariable(variable);
        else if(parent != null)
            parent.replaceVariable(variable);
    }

    public void addVariable(Variable variable)
    {
        currentVariables.put(variable.getName(), variable);
    }

    public void addClass(Clazz clazz)
    {
        currentClasses.add(clazz);
    }

    public Variable getVariableByName(String name)
    {
        Variable variable = currentVariables.get(name);
        if(variable == null)
        {
            if(parent != null)
                return parent.getVariableByName(name);
            else
                return null;
        }
        return variable;
    }

    public void addFunction(Function function)
    {
        function.getFunctionScope().parent = this;
        currentFunctionsScope.add(function);
    }

    public void addAction(CodeAction codeAction)
    {
        if(codeAction != null)
            actionList.add(codeAction);
    }

    public List<CodeAction> getActionList()
    {
        return actionList;
    }

    public Function getFunctionByName(String functionName)
    {
        for(int i = 0;i<currentFunctionsScope.size();i++)
            if(currentFunctionsScope.get(i).getName().equals(functionName))
                return currentFunctionsScope.get(i);

        if(parent != null)
            return parent.getFunctionByName(functionName);
        else
            return null;
    }

    public Clazz getClassByName(String className)
    {
        for(int i = 0;i<currentClasses.size();i++)
            if(currentClasses.get(i).getClassName().equals(className))
                return currentClasses.get(i);

        if(parent != null)
            return parent.getClassByName(className);
        else
            return null;
    }

    public Scope getParent()
    {
        return parent;
    }
}
