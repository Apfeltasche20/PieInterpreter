package main.interpreter;

import main.code.Code;
import main.code.Compiler;
import main.interpreter.action.*;
import main.interpreter.clazz.Clazz;
import main.interpreter.function.Function;
import main.interpreter.intern.FunctionCallback;
import main.interpreter.intern.functions.System;
import main.interpreter.intern.functions.ui.UI;
import main.interpreter.logger.Logger;
import main.interpreter.scope.Scope;
import main.interpreter.scope.ScopeEndReason;
import main.interpreter.scope.ScopeResult;
import main.interpreter.scope.ScopeType;
import main.interpreter.variable.Variable;
import main.interpreter.variable.VariableString;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Interpreter
{
    private List<String> includeDirs;

    private Map<String, FunctionCallback> internFunctions;
    private Map<String, Code> loadedFiles;

    public Interpreter(List<String> includeDirs)
    {
        this.internFunctions = new HashMap<>();

        this.internFunctions.put("print", System::print);
        this.internFunctions.put("err", System::err);

        this.internFunctions.put("readFile", main.interpreter.intern.functions.File::readFile);

        this.internFunctions.put("windowCreate", UI::windowCreate);
        this.internFunctions.put("windowSetVisible", UI::windowSetVisible);
        this.internFunctions.put("windowSetSize", UI::windowSetSize);
        this.internFunctions.put("windowSetName", UI::windowSetName);
        this.internFunctions.put("windowSetDrawHandler", UI::windowSetDrawHandler);
        this.internFunctions.put("windowCenterOnScreen", UI::windowCenterOnScreen);

        this.internFunctions.put("canvasDrawText", UI::canvasDrawText);

        this.loadedFiles = new HashMap<>();
        this.includeDirs = includeDirs;
    }

    public Variable executeInternFunction(String name, List<Variable> arguments)
    {
        FunctionCallback internalFunction = internFunctions.get(name);
        if(internalFunction != null)
        {
            return internalFunction.call(this, arguments);
        }
        else
        {
            java.lang.System.err.println("Internal Function " + name + " not found!");
            return new Variable();
        }
    }

    private Code loadAndCacheFile(String path)
    {
        try
        {
            Code code = new Code(Files.readString(Path.of(path + ".txt")));
            Compiler.saveCode(new File(path + ".bin"), code);
            //loadedFiles.put(path, code);
            return code;
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    private Code locateAndLoadFile(String name)
    {
        File file = new File(name + ".txt");
        if(file.exists())
            return loadAndCacheFile(name);

        for(int i = 0;i<includeDirs.size();i++)
        {
            String newPath = includeDirs.get(i) + "/" + name;
            File f = new File(newPath + ".txt");
            if(f.exists())
                return loadAndCacheFile(newPath);
        }

        java.lang.System.err.println("Could not find File " + name);
        java.lang.System.exit(-1);
        return null;
    }

    public Code getCodeFromCacheOrLoad(String name)
    {
        if(loadedFiles.containsKey(name))
            return loadedFiles.get(name);
        else
        {
            Code code = locateAndLoadFile(name);
            loadedFiles.put(name, code);
            return code;
        }
    }

    public void executeFile(String name)
    {
        Code code = getCodeFromCacheOrLoad(name);
        code.setInitialized(true);
        Logger.debugLog("\n------------------------------------\n");
        Variable returnValue = executeScope(code.getGlobalScope(), ScopeType.FUNCTION);
        System.print(this, List.of(new VariableString("Code returned with: "), returnValue));
    }

    public void execute(String sourceCode)
    {
        Code code = new Code(sourceCode);
        code.setInitialized(true);
        Logger.debugLog("\n------------------------------------\n");
        Variable returnValue = executeScope(code.getGlobalScope(), ScopeType.FUNCTION);
        System.print(this, List.of(new VariableString("Code returned with: "), returnValue));
    }

    public Variable executeScope(Scope scope, ScopeType type)
    {
        ScopeResult result = new ScopeResult();
        return executeScope(scope, type, result);
    }

    public void initializeScope(Scope scope)
    {
        List<CodeAction> actions = scope.getActionList();
        for(int i = 0;i<actions.size();i++)
        {
            CodeAction currentAction = actions.get(i);
            if(currentAction == null)
                continue;

            ScopeResult actionResult = new ScopeResult();
            if(currentAction instanceof Function)
            {
                currentAction.evaluate(this, scope, actionResult);
            }
            else if(currentAction instanceof Clazz)
            {
                currentAction.evaluate(this, scope, actionResult);
            }
        }
    }

    public Variable executeScope(Scope scope, ScopeType type, ScopeResult result)
    {
        List<CodeAction> actions = scope.getActionList();
        for(int i = 0;i<actions.size();i++)
        {
            CodeAction currentAction = actions.get(i);
            if(currentAction == null)
                continue;

            ScopeResult actionResult = new ScopeResult();
            //if(currentAction instanceof Function)
            //    continue;
            //if(currentAction instanceof Clazz)
            //    continue;
            Variable variable = currentAction.evaluate(this, scope, actionResult);
            if(actionResult.reason == ScopeEndReason.RETURN)
            {
                if(type == ScopeType.CONDITION)
                    result.reason = ScopeEndReason.RETURN;
                else
                    result.reason = ScopeEndReason.END_OF_CODE;

                return variable;
            }
        }

        result.reason = ScopeEndReason.END_OF_CODE;
        return new Variable();
    }
}
