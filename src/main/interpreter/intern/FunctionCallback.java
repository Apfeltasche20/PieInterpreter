package main.interpreter.intern;

import main.interpreter.Interpreter;
import main.interpreter.variable.Variable;

import java.util.List;

public interface FunctionCallback
{
    Variable call(Interpreter interpreter, List<Variable> args);
}
