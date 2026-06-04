package main.interpreter;

import main.code.Code;
import main.code.Compiler;
import main.interpreter.action.*;
import main.interpreter.clazz.Clazz;
import main.interpreter.function.Function;
import main.interpreter.intern.FunctionCallback;
import main.interpreter.intern.classes.ExposedClass;
import main.interpreter.intern.classes.InternString;
import main.interpreter.intern.functions.System;
import main.interpreter.intern.functions.ui.UI;
import main.interpreter.logger.Logger;
import main.interpreter.scope.Scope;
import main.interpreter.scope.ScopeEndReason;
import main.interpreter.scope.ScopeResult;
import main.interpreter.scope.ScopeType;
import main.interpreter.variable.Variable;
import main.interpreter.variable.VariableJavaObject;
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

    private Map<String, Class<?>> internClasses;
    private Map<String, FunctionCallback> internFunctions;
    private Map<String, Code> loadedFiles;

    public Interpreter(List<String> includeDirs)
    {
        this.internFunctions = new HashMap<>();
        this.internClasses = new HashMap<>();

        this.internFunctions.put("print", System::print);
        this.internFunctions.put("err", System::err);
        this.internFunctions.put("wait", System::wait);

        this.internFunctions.put("readFile", main.interpreter.intern.functions.File::readFile);
        this.internFunctions.put("readFileRaw", main.interpreter.intern.functions.File::readFileRaw);
        this.internFunctions.put("readImage", main.interpreter.intern.functions.File::readImage);

        this.internFunctions.put("windowCreate", UI::windowCreate);
        this.internFunctions.put("windowSetVisible", UI::windowSetVisible);
        this.internFunctions.put("windowSetSize", UI::windowSetSize);
        this.internFunctions.put("windowSetName", UI::windowSetName);
        this.internFunctions.put("windowSetDrawHandler", UI::windowSetDrawHandler);
        this.internFunctions.put("windowCenterOnScreen", UI::windowCenterOnScreen);

        this.internFunctions.put("canvasDrawText", UI::canvasDrawText);
        this.internFunctions.put("canvasDrawBytes", UI::canvasDrawBytes);
        this.internFunctions.put("canvasDrawImage", UI::canvasDrawImage);

        addInternClass(InternString.class);

        this.loadedFiles = new HashMap<>();
        this.includeDirs = includeDirs;
    }

    public void addInternClass(Class<?> clazz)
    {
        ExposedClass exposedClass = clazz.getAnnotation(ExposedClass.class);
        if(exposedClass == null)
        {
            java.lang.System.err.println("Intern Class needs a Exposed Class Annotation to auto add!");
            return;
        }

        internClasses.put(exposedClass.internName(), clazz);
    }

    public void addInternClass(String name, Class<?> clazz)
    {
        internClasses.put(name, clazz);
    }

    public void addInternFunction(String name, FunctionCallback callback)
    {
        internFunctions.put(name, callback);
    }

    public VariableJavaObject createInternObjectFromJavaObject(Object javaObject)
    {
        Class<?> clazz = javaObject.getClass();

        ExposedClass exposedClass = clazz.getAnnotation(ExposedClass.class);
        if(exposedClass == null)
        {
            java.lang.System.err.println("Intern Class needs a Exposed Class Annotation to auto wrap!");
            return null;
        }

        return createInternObjectFromJavaObject(exposedClass.internModule(), exposedClass.internName(), javaObject);
    }

    public VariableJavaObject createInternObjectFromJavaObject(String originPackage, String internName, Object javaObject)
    {
        Code code = getCodeFromCacheOrLoad(originPackage);
        Clazz clazz = code.getGlobalScope().getClassByName(internName);
        if(clazz == null)
        {
            java.lang.System.err.println("Class " + internName + " not found!");
            return null;
        }
        if(clazz instanceof InternClassAction)
        {
            return new VariableJavaObject((InternClassAction) clazz, javaObject);
        }
        else
        {
            java.lang.System.err.println("Class " + internName + " is not an intern Class!");
            return null;
        }
    }

    public Class<?> getInternClass(String name)
    {
        return internClasses.get(name);
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
            String suffix = path.contains(".") ? "" : ".txt";

            Code code = new Code(Files.readString(Path.of(path + suffix)));
            //Compiler.saveCode(new File(path + ".bin"), code);
            //loadedFiles.put(path, code);
            return code;
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    private Code locateAndLoadFile(String name)
    {
        String suffix = name.contains(".") ? "" : ".txt";

        File file = new File(name + suffix);
        if(file.exists())
            return loadAndCacheFile(name);

        for(int i = 0;i<includeDirs.size();i++)
        {
            String newPath = includeDirs.get(i) + "/" + name;
            File f = new File(newPath + suffix);
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

    public Variable executeFile(String name)
    {
        Code code = getCodeFromCacheOrLoad(name);
        code.setInitialized(true);
        Logger.debugLog("\n------------------------------------\n");
        Variable returnValue = executeScope(code.getGlobalScope(), ScopeType.FUNCTION);
        if(Logger.isDebugLogging())
            System.print(this, List.of(new VariableString("Code returned with: "), returnValue));
        return returnValue;
    }

    public Variable execute(String sourceCode)
    {
        Code code = new Code(sourceCode);
        code.setInitialized(true);
        Logger.debugLog("\n------------------------------------\n");
        Variable returnValue = executeScope(code.getGlobalScope(), ScopeType.FUNCTION);
        if(Logger.isDebugLogging())
            System.print(this, List.of(new VariableString("Code returned with: "), returnValue));
        return returnValue;
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
