package main.interpreter.math;

import main.interpreter.variable.Variable;
import main.interpreter.variable.VariableBoolean;
import main.interpreter.variable.VariableType;

public class Equals
{
    public static Variable equals(Variable left, Variable right)
    {
        VariableType leftType = left.getCurrentType();
        VariableType rightType = right.getCurrentType();

        if((leftType == VariableType.NUMBER) && (rightType == VariableType.NUMBER))
        {
            return equalsNumberNumber(left, right);
        }
        else if((leftType == VariableType.STRING) && (rightType == VariableType.STRING))
        {
            return equalsStringString(left, right);
        }
        else
        {
            System.err.println("Operation " + leftType.toString() + " ~ " + rightType.toString() + " not allowed!");
            System.exit(-1);
        }
        return new Variable();
    }

    private static Variable equalsNumberNumber(Variable left, Variable right)
    {
        return new VariableBoolean(left.asNumber() < right.asNumber());
    }

    private static Variable equalsStringString(Variable left, Variable right)
    {
        return new VariableBoolean(left.asString().equals(right.asString()));
    }
}
