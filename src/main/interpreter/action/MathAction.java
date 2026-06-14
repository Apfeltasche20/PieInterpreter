package main.interpreter.action;

import main.interpreter.Interpreter;
import main.interpreter.math.*;
import main.interpreter.scope.Scope;
import main.interpreter.scope.ScopeEndReason;
import main.interpreter.scope.ScopeResult;
import main.interpreter.variable.Variable;
import main.interpreter.variable.VariableType;
import main.util.SaveOutputStream;

public class MathAction extends CodeAction
{
    public String mathSymbol;
    public CodeAction left;
    public CodeAction right;

    public MathAction(String mathSymbol, CodeAction left, CodeAction right)
    {
        this.mathSymbol = mathSymbol;
        this.left = left;
        this.right = right;
    }

    @Override
    public Variable evaluate(Interpreter interpreter, Scope scope, ScopeResult result)
    {
        result.reason = ScopeEndReason.END_OF_CODE;

        Variable leftVariable = left.evaluate(interpreter, scope);
        Variable rightVariable = right.evaluate(interpreter, scope);

        switch (mathSymbol)
        {
            case "+" -> {
                return Plus.plus(leftVariable, rightVariable);
            }
            case "-" -> {
                return Minus.minus(leftVariable, rightVariable);
            }
            case "*" -> {
                return Multiplication.multiplication(leftVariable, rightVariable);
            }
            case "/" -> {
                return Division.division(leftVariable, rightVariable);
            }
            case ">" -> {
                return Greater.greater(leftVariable, rightVariable);
            }
            case "<" -> {
                return Smaller.smaller(leftVariable, rightVariable);
            }
            case "~" -> {
                return Equals.equals(leftVariable, rightVariable);
            }
            default -> {
                System.err.println("Unknown Math Symbol: " + mathSymbol);
                System.exit(-1);
            }
        }

        return null;
    }

    @Override
    public CodeAction copy()
    {
        return new MathAction(mathSymbol, left.copy(), right.copy());
    }

    @Override
    public void toBinary(SaveOutputStream saveOutputStream)
    {
        saveOutputStream.writeInt(9);
        saveOutputStream.writeStringZeroPadding(mathSymbol, 4);
        left.toBinary(saveOutputStream);
        right.toBinary(saveOutputStream);
    }
}
