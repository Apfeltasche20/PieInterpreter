package main.interpreter.intern;

import main.interpreter.variable.Variable;

import java.util.List;

public interface FunctionCallback
{
    Variable call(List<Variable> args);
}
