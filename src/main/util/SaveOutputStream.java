package main.util;

import main.interpreter.scope.Scope;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SaveOutputStream
{
    private OutputStream outputStream;
    private List<String> stringArray;
    private Map<Scope, Integer> mappedScopes;
    private int nextScopeID;

    public SaveOutputStream(OutputStream outputStream)
    {
        this.outputStream = outputStream;
        this.stringArray = new ArrayList<>();
        this.mappedScopes = new HashMap<>();
        this.nextScopeID = 1;
    }

    public int getScopeID(Scope scope)
    {
        if(mappedScopes.containsKey(scope))
            return mappedScopes.get(scope);
        else
            System.err.println("Scope not found!");
        return 0;
    }

    public int getNextScopeID(Scope scope)
    {
        mappedScopes.put(scope, nextScopeID);
        return nextScopeID++;
    }

    public int saveString(String string)
    {
        stringArray.add(string);
        return stringArray.size() - 1;
    }

    public void saveAndWriteString(String string)
    {
        writeInt(string.length());
        int paddedLength = ((string.length() / 8) + 1) * 8;
        writeStringZeroPadding(string, paddedLength);

        //writeInt(saveString(string));
    }

    public void writeByte(byte b)
    {
        try
        {
            outputStream.write(b);
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public void writeByte(int i)
    {
        writeByte((byte)(i & 0xFF));
    }

    public void writeByte(long i)
    {
        writeByte((byte)(i & 0xFF));
    }

    public void writeBytes(byte[] bytes)
    {
        try
        {
            outputStream.write(bytes);
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public void writeInt(int i)
    {
        writeByte(i);
        writeByte(i >> 8);
        writeByte(i >> 16);
        writeByte(i >> 24);
    }

    public void writeLong(long i)
    {
        writeByte(i);
        writeByte(i >> 8);
        writeByte(i >> 16);
        writeByte(i >> 24);
        writeByte(i >> 32);
        writeByte(i >> 40);
        writeByte(i >> 48);
        writeByte(i >> 56);
    }

    public void writeStringZeroPadding(String string, int length)
    {
        while(string.length() < length)
            string = string + "\0";

        writeBytes(string.getBytes(StandardCharsets.UTF_8));
    }
}
