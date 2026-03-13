package main.interpreter.math;

import main.interpreter.variable.Variable;
import main.interpreter.variable.VariableNumber;
import main.interpreter.variable.VariableString;
import main.interpreter.variable.VariableType;

public class Plus
{
    public static Variable plus(Variable left, Variable right)
    {
        VariableType leftType = left.getCurrentType();
        VariableType rightType = right.getCurrentType();

        if((leftType == VariableType.NUMBER) && (rightType == VariableType.NUMBER))
        {
            return plusNumberNumber(left, right);
        }
        else if((leftType == VariableType.STRING) || (rightType == VariableType.STRING))
        {
            return new VariableString(left.asString() + right.asString());
        }
        else
        {
            System.err.println("Operation " + leftType.toString() + " + " + rightType.toString() + " not allowed!");
            System.exit(-1);
        }
        return new Variable();
    }

    private static Variable plusNumberNumber(Variable left, Variable right)
    {
        return new VariableNumber(left.asNumber() + right.asNumber());
    }
}
