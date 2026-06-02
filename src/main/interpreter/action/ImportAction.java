package main.interpreter.action;

import main.code.Code;
import main.interpreter.Interpreter;
import main.interpreter.scope.Scope;
import main.interpreter.scope.ScopeResult;
import main.interpreter.scope.ScopeType;
import main.interpreter.variable.Variable;
import main.interpreter.variable.VariableImport;
import main.util.SaveOutputStream;

public class ImportAction extends CodeAction
{
    private String fileName;

    public ImportAction(String fileName)
    {
        this.fileName = fileName;
    }

    @Override
    public Variable evaluate(Interpreter interpreter, Scope scope, ScopeResult result)
    {
        Code importedFile = interpreter.getCodeFromCacheOrLoad(fileName);
        if(!importedFile.isInitialized())
        {
            interpreter.executeScope(importedFile.getGlobalScope(), ScopeType.FUNCTION);
            importedFile.setInitialized(true);
        }
        VariableImport variableImport = new VariableImport(fileName, importedFile.getGlobalScope());
        scope.addImportVariable(variableImport);
        return variableImport;
    }

    @Override
    public CodeAction copy()
    {
        return new ImportAction(fileName);
    }

    @Override
    public void toBinary(SaveOutputStream saveOutputStream)
    {
        saveOutputStream.writeInt(7);
        saveOutputStream.saveAndWriteString(fileName);
    }
}
