package main.interpreter.math;

import main.interpreter.variable.Variable;
import main.interpreter.variable.VariableNumber;
import main.interpreter.variable.VariableType;

public class Division
{
    public static Variable division(Variable left, Variable right)
    {
        VariableType leftType = left.getCurrentType();
        VariableType rightType = right.getCurrentType();

        if((leftType == VariableType.NUMBER) && (rightType == VariableType.NUMBER))
        {
            return divisionNumberNumber(left, right);
        }
        else
        {
            System.err.println("Operation " + leftType.toString() + " / " + rightType.toString() + " not allowed!");
            System.exit(-1);
        }
        return new Variable();
    }

    private static Variable divisionNumberNumber(Variable left, Variable right)
    {
        return new VariableNumber(left.asNumber() / right.asNumber());
    }
}
