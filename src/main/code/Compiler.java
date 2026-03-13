package main.code;

import main.interpreter.action.CodeAction;
import main.interpreter.scope.Scope;
import main.util.SaveOutputStream;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;

public class Compiler
{
    public static void saveCode(File file, Code code)
    {
        try
        {
            SaveOutputStream saveOutputStream = new SaveOutputStream(new FileOutputStream(file));

            saveOutputStream.writeInt(1); // version
            saveOutputStream.writeInt(0); // reserved

            saveScope(saveOutputStream, code.getGlobalScope());
        } catch (FileNotFoundException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static void saveScope(SaveOutputStream saveOutputStream, Scope scope)
    {
        saveOutputStream.writeInt(saveOutputStream.getNextScopeID(scope));

        if(scope.getParent() != null)
            saveOutputStream.writeInt(saveOutputStream.getScopeID(scope.getParent()));
        else
            saveOutputStream.writeInt(0);

        saveOutputStream.writeInt(0);

        List<CodeAction> actionList = scope.getActionList();
        saveOutputStream.writeInt(actionList.size());

        for(int i = 0;i<actionList.size();i++)
        {
            actionList.get(i).toBinary(saveOutputStream);
        }
    }
}
