package main.code;

import main.input.CodeStream;
import main.input.Preprocessor;
import main.interpreter.action.*;
import main.interpreter.clazz.Clazz;
import main.interpreter.function.Function;
import main.interpreter.logger.Logger;
import main.interpreter.scope.Scope;
import main.util.Util;

import java.util.ArrayList;
import java.util.List;

public class Code
{
    private String sourceCode;

    private Scope globalScope;

    private boolean initialized;

    public Code(String code)
    {
        this.sourceCode = code;
        this.globalScope = new Scope();
        this.initialized = false;

        parseSourceCode();

        Logger.debugLog("Finished Parsing");
    }

    public boolean isInitialized()
    {
        return initialized;
    }

    public void setInitialized(boolean initialized)
    {
        this.initialized = initialized;
    }

    private void parseSourceCode()
    {
        CodeStream codeStream = new CodeStream(Preprocessor.preprocess(sourceCode));

        while (!codeStream.isFinished())
        {
            CodeAction codeAction = parseCodeLine(codeStream);
            globalScope.addAction(codeAction);
        }

        /*
        String codeLine = codeStream.getNextLine();
        while(codeLine != null)
        {
            if(codeLine.endsWith(";"))
                codeLine = codeLine.substring(0, codeLine.length() - 1);

            parseCodeLine(codeLine);
            codeLine = codeStream.getNextLine();
        }
         */
    }

    private void parseCodeStreamIntoScope(CodeStream codeStream, Scope scope)
    {
        while (!codeStream.isFinished())
        {
            scope.addAction(parseCodeLine(codeStream));
        }
    }

    private CodeAction parseCodeLine(String codeLine)
    {
        return parseCodeLine(new CodeStream(codeLine));
    }

    private CodeAction parseCodeLine(CodeStream codeStream)
    {
        String token = codeStream.getNextToken();

        switch (token)
        {
            case "var" -> {
                CodeStream variableCodeStream = codeStream.getCodeStreamUntilTopLevelToken(";");
                //actionList.add(variableDeclaration);
                return parseVariableDeclaration(variableCodeStream);
            }
            case "function" -> {
                CodeStream functionCodeStream = codeStream.getCodeStreamUntilOpenAndClosingCurlyBrackets();
                return parseFunctionDeclaration(functionCodeStream);
            }
            case "class" -> {
                CodeStream classCodeStream = codeStream.getCodeStreamUntilOpenAndClosingCurlyBrackets();
                return parseClassDeclaration(classCodeStream);
            }
            case "import" -> {
                String fileName = codeStream.getNextToken();
                return new ImportAction(fileName);
            }
            default -> {
                return parseNonKeywordCodeLine(token, codeStream);
                //System.err.println("Unexpected token: \"" + token + "\" at");
                //return null;
                //System.err.println("\t" + codeLine);
            }
        }
    }

    private CodeAction replaceLeftCodeAction(CodeAction oldLeftCodeAction, CodeAction newLeftCodeAction)
    {
        if(oldLeftCodeAction == null)
            return newLeftCodeAction;
        if(oldLeftCodeAction instanceof ExecuteOnOtherScopeAction)
        {
            ((ExecuteOnOtherScopeAction) oldLeftCodeAction).setAction(newLeftCodeAction);
            return oldLeftCodeAction;
        }
        System.err.println("WARNING: OVERRIDING LEFT CODE ACTION!");
        return newLeftCodeAction;
    }

    private CodeAction parseNonKeywordCodeLine(String token, CodeStream codeStream)
    {
        CodeStream ownCodeStream = codeStream.getCodeStreamUntilTopLevelToken(";");
        Logger.debugLog(token + " " + ownCodeStream.getCode());

        String currentToken = token;

        CodeAction currentLeftAction = null;
        while (currentToken != null)
        {
            if(currentToken.isBlank())
            {

            }
            else if(Util.isNumber(currentToken))
            {
                Logger.debugLog("\tNumber");
                currentLeftAction = replaceLeftCodeAction(currentLeftAction, new NumberAction(currentToken));
            }
            else if(Util.isString(currentToken))
            {
                Logger.debugLog("\tString");
                currentLeftAction = replaceLeftCodeAction(currentLeftAction,new StringAction(currentToken.substring(1, currentToken.length() - 1)));
            }
            else if(Util.isMathSymbol(currentToken))
            {
                Logger.debugLog("\tMATH");
                currentLeftAction = new MathAction(currentToken, currentLeftAction, parseCodeLine(ownCodeStream));
            }
            else if(currentToken.equals("("))
            {
                Logger.debugLog("\tBrackets");
                currentLeftAction = replaceLeftCodeAction(currentLeftAction, parseCodeLine(ownCodeStream.getCodeStreamUntilClosingBrackets()));
            }
            else if(currentToken.equals("return"))
            {
                Logger.debugLog("\tReturn Value");
                currentLeftAction = new ReturnAction(parseCodeLine(ownCodeStream));
            }
            else if(currentToken.equals("run"))
            {
                Logger.debugLog("\tRun");
                currentLeftAction = new RunAction(parseCodeLine(ownCodeStream));
            }
            else if(currentToken.equals("intern"))
            {
                Logger.debugLog("\tIntern");
                currentLeftAction = replaceLeftCodeAction(currentLeftAction, new InternAction(parseCodeLine(ownCodeStream)));
            }
            else if(currentToken.equals("if"))
            {
                Logger.debugLog("\tIf");
                String openBrackets = ownCodeStream.getNextToken();
                if(!openBrackets.equals("("))
                {
                    System.err.println("Expected ( after if but got " + openBrackets);
                    return null;
                }
                CodeStream condition = ownCodeStream.getCodeStreamUntilClosingBrackets();
                openBrackets = ownCodeStream.getNextToken();
                if(!openBrackets.equals("{"))
                {
                    System.err.println("Expected { after if but got " + openBrackets);
                    return null;
                }
                CodeStream scopeStream = ownCodeStream.getCodeStreamUntilClosingCurlyBrackets();
                IfAction ifAction = new IfAction(parseCodeLine(condition));
                parseCodeStreamIntoScope(scopeStream, ifAction.scope);
                currentLeftAction = ifAction;
            }
            else if(currentToken.equals("else"))
            {
                Logger.debugLog("Else");
                if(!(currentLeftAction instanceof IfAction))
                {
                    System.err.println("Else without if");
                    return null;
                }
                String openBrackets = ownCodeStream.getNextToken();
                if(!openBrackets.equals("{"))
                {
                    System.err.println("Expected { after else but got " + openBrackets);
                    return null;
                }
                CodeStream scopeStream = ownCodeStream.getCodeStreamUntilClosingCurlyBrackets();
                ElseAction elseAction = new ElseAction();
                parseCodeStreamIntoScope(scopeStream, elseAction.scope);
                ((IfAction) currentLeftAction).elseAction = elseAction;
            }
            else if(currentToken.equals("false"))
            {
                Logger.debugLog("\tFalse");
                currentLeftAction = new BooleanAction(false);
            }
            else if(currentToken.equals("true"))
            {
                Logger.debugLog("\tTrue");
                currentLeftAction = new BooleanAction(true);
            }
            else if(currentToken.equals("new"))
            {
                Logger.debugLog("New Object");
                String className = ownCodeStream.getNextToken();
                String openBrackets = ownCodeStream.getNextToken();
                if(!openBrackets.equals("("))
                {
                    System.err.println("Expected ( after else but got " + openBrackets);
                    return null;
                }
                CodeStream argumentsStream = ownCodeStream.getCodeStreamUntilClosingBrackets();
                List<CodeStream> functionArguments = argumentsStream.splitOnTopLevelToken(",");
                List<CodeAction> arguments = new ArrayList<>();
                for(int i = 0;i<functionArguments.size();i++)
                    arguments.add(parseCodeLine(functionArguments.get(i)));

                currentLeftAction = replaceLeftCodeAction(currentLeftAction, new NewClassObjectAction(className, arguments));
            }
            else if(currentToken.equals("["))
            {
                CodeStream arrayLengthStream = ownCodeStream.getCodeStreamUntilTopLevelToken("]");

                currentLeftAction = replaceLeftCodeAction(currentLeftAction, new ArrayAction(parseCodeLine(arrayLengthStream)));
            }
            else
            {
                String nextToken = ownCodeStream.peakNextToken();
                if(nextToken == null)
                {
                    Logger.debugLog("\tGet Variable Value");
                    currentLeftAction = replaceLeftCodeAction(currentLeftAction, new GetVariableAction(currentToken));
                }
                else if(nextToken.equals("."))
                {
                    ownCodeStream.getNextToken();

                    Logger.debugLog("\tExecute on other Scope");

                    //String variableOrFunctionName = ownCodeStream.getNextToken();
                    //String peakToken = ownCodeStream.peakNextToken();
                    //if(peakToken.equals("("))
                    //{
                        // call function from other scope
                    //}
                    //else
                    //{
                        // get variable from scope
                    //}
                    currentLeftAction = replaceLeftCodeAction(currentLeftAction, new ExecuteOnOtherScopeAction(currentToken));
                }
                else if(nextToken.equals("="))
                {
                    ownCodeStream.getNextToken();

                    Logger.debugLog("\tSet Variable Value");
                    currentLeftAction = new SetVariableAction(currentToken, parseCodeLine(ownCodeStream));
                }
                else if(nextToken.equals("("))
                {
                    ownCodeStream.getNextToken();

                    Logger.debugLog("\tCall Function");
                    CodeStream allFunctionArguments = ownCodeStream.getCodeStreamUntilClosingBrackets();
                    List<CodeStream> functionArguments = allFunctionArguments.splitOnTopLevelToken(",");
                    List<CodeAction> arguments = new ArrayList<>();
                    for(int i = 0;i<functionArguments.size();i++)
                        arguments.add(parseCodeLine(functionArguments.get(i)));
                    currentLeftAction = replaceLeftCodeAction(currentLeftAction, new CallFunctionAction(currentToken, arguments));
                }
                else
                {
                    //Logger.debugLog("\t????");
                    Logger.debugLog("\tGet Variable Value");
                    currentLeftAction = replaceLeftCodeAction(currentLeftAction, new GetVariableAction(currentToken));
                }
            }

            currentToken = ownCodeStream.getNextToken();
        }

        return currentLeftAction;
    }

    private Clazz parseClassDeclaration(CodeStream codeStream)
    {
        String functionName = codeStream.getNextToken();
        Scope functionScope = new Scope();
        String argumentsOpen = codeStream.getNextToken();
        if(!argumentsOpen.equals("{"))
        {
            System.err.println(codeStream.getLine());
            System.err.println("\tExpected '{' but got " + argumentsOpen);
            return null;
        }

        parseCodeStreamIntoScope(codeStream, functionScope);
        return new Clazz(functionName, functionScope);
    }

    private Function parseFunctionDeclaration(CodeStream codeStream)
    {
        String functionName = codeStream.getNextToken();
        String argumentsOpen = codeStream.getNextToken();
        if(!argumentsOpen.equals("("))
        {
            System.err.println(codeStream.getLine());
            System.err.println("\tExpected '(' but got " + argumentsOpen);
            return null;
        }

        List<CodeStream> arguments = codeStream.getCodeStreamUntilClosingBrackets().splitOnTopLevelToken(",");
        List<VariableDeclaration> parsedArguments = new ArrayList<>();

        for(int i = 0;i<arguments.size();i++)
        {
            VariableDeclaration variableDeclaration = parseVariableDeclaration(arguments.get(i));
            parsedArguments.add(variableDeclaration);
        }

        Function function = new Function(functionName, parsedArguments);

        String functionOpen = codeStream.getNextToken();
        if(!functionOpen.equals("{"))
        {
            System.err.println(codeStream.getLine());
            System.err.println("\tExpected '{' but got " + functionOpen);
            return null;
        }
        //CodeStream functionBody = codeStream.getCodeStreamUntilClosingCurlyBrackets();
        //System.out.println(codeStream.getCode());

        while(!codeStream.isFinished())
        {
            function.getFunctionScope().addAction(parseCodeLine(codeStream));
        }

        return function;
    }

    private VariableDeclaration parseVariableDeclaration(CodeStream codeStream)
    {
        String variableName = codeStream.getNextToken();
        VariableDeclaration variableDeclaration = new VariableDeclaration(variableName);
        String possibleValue = codeStream.getNextToken();
        if(possibleValue != null)
        {
            if(possibleValue.equals("="))
            {
                CodeAction codeAction = parseCodeLine(codeStream);
                if(codeAction != null)
                {
                    variableDeclaration.setValue(codeAction);
                }
                else
                {
                    System.err.println(codeStream.getLine());
                    System.err.println("\tExpected [value] but got nothing");
                }
            }
            else
            {
                System.err.println(codeStream.getLine());
                System.err.println("\tExpected \"=\" but got \"" + possibleValue + "\"");
            }
        }

        return variableDeclaration;
    }

    public Scope getGlobalScope()
    {
        return globalScope;
    }
}
