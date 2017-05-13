package com.roman.interpretation.expression;

import com.roman.base.Context;
import com.roman.base.Keywords;
import com.roman.operation.ExpressionOperator;
import com.roman.operation.Operator;
import com.roman.util.ProcessException;
import com.roman.base.Interpreters;
import com.roman.operation.expression.arithmetic.MinusOperator;
import com.roman.operation.expression.common.PlusOperator;

import java.util.List;

/**
 * Created by roman on 10.09.2016.
 */
public class PriorityOneInterpreter extends ArithmeticInterpreter {
    private static final String[] operators = new String[] {
            Keywords.PLUS,
            Keywords.MINUS
    };

    @Override
    public ExpressionOperator interpret(String input, Context context) throws ProcessException {
        String[] operands = splitOperands(input, operators);
        if (operands == null) {
            return getNextResponsible().interpret(input, context);
        }
        String leftOperand = operands[0];
        String operator = operands[1];
        String rightOperand = operands[2];
        switch (operator) {
            case Keywords.PLUS:
                return new PlusOperator(Interpreters.ARITHMETIC, leftOperand, rightOperand, context);
            case Keywords.MINUS:
                return new MinusOperator(Interpreters.ARITHMETIC, leftOperand, rightOperand, context);
            default: return null;
        }
    }
}
