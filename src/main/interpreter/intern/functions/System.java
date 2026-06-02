package main.interpreter.intern.functions;

import main.interpreter.Interpreter;
import main.interpreter.variable.Variable;

import java.util.List;

public class System
{
    public static Variable print(Interpreter interpreter, List<Variable> args)
    {
        for(int i = 0;i<args.size();i++)
            java.lang.System.out.print(args.get(i).asString());
        java.lang.System.out.println();
        return new Variable();
    }

    public static Variable err(Interpreter interpreter, List<Variable> args)
    {
        for(int i = 0;i<args.size();i++)
            java.lang.System.err.print(args.get(i).asString());
        java.lang.System.err.println();
        return new Variable();
    }

    public static Variable wait(Interpreter interpreter, List<Variable> args)
    {
        if(args.isEmpty())
        {
            return new Variable();
        }

        try
        {
            Thread.sleep(args.getFirst().asNumber());
        } catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }
        return new Variable();
    }
}
