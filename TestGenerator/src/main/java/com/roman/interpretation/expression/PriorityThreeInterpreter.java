package com.roman.interpretation.expression;

import com.roman.base.Context;
import com.roman.base.Keywords;
import com.roman.operation.ExpressionOperator;
import com.roman.operation.Operator;
import com.roman.operation.expression.arithmetic.DivideOperator;
import com.roman.operation.expression.arithmetic.MultiplyOperator;
import com.roman.util.ProcessException;
import com.roman.base.Interpreters;
import com.roman.operation.expression.arithmetic.PowerOperator;

/**
 * Created by roman on 11.09.2016.
 */
public class PriorityThreeInterpreter extends ArithmeticInterpreter {

    @Override
    public ExpressionOperator interpret(String input, Context context) throws ProcessException {
        String[] operands = splitOperands(input, Keywords.POWER);
        if (operands == null) {
            return getNextResponsible().interpret(input, context);
        }
        String leftOperand = operands[0];
        String operator = operands[1];
        String rightOperand = operands[2];
        return new PowerOperator(Interpreters.ARITHMETIC, leftOperand, rightOperand, context);
    }
}
