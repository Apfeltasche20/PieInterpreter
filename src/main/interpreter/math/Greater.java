package main.interpreter.math;

import main.interpreter.variable.*;

public class Greater
{
    public static Variable greater(Variable left, Variable right)
    {
        VariableType leftType = left.getCurrentType();
        VariableType rightType = right.getCurrentType();

        if((leftType == VariableType.NUMBER) && (rightType == VariableType.NUMBER))
        {
            return greaterNumberNumber(left, right);
        }
        else
        {
            System.err.println("Operation " + leftType.toString() + " + " + rightType.toString() + " not allowed!");
            System.exit(-1);
        }
        return new Variable();
    }

    private static Variable greaterNumberNumber(Variable left, Variable right)
    {
        return new VariableBoolean(left.asNumber() > right.asNumber());
    }
}
