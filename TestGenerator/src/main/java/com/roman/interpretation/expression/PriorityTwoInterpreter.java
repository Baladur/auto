package com.roman.interpretation.expression;

import com.roman.base.Context;
import com.roman.base.Keywords;
import com.roman.operation.ExpressionOperator;
import com.roman.operation.Operator;
import com.roman.operation.expression.arithmetic.MinusOperator;
import com.roman.operation.expression.common.PlusOperator;
import com.roman.util.ProcessException;
import com.roman.base.Interpreters;
import com.roman.operation.expression.arithmetic.DivideOperator;
import com.roman.operation.expression.arithmetic.MultiplyOperator;

/**
 * Created by roman on 11.09.2016.
 */
public class PriorityTwoInterpreter extends ArithmeticInterpreter {
    private static final String[] operators = new String[] {
            Keywords.MULTIPLY,
            Keywords.DIVIDE
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
            case Keywords.MULTIPLY:
                return new MultiplyOperator(Interpreters.ARITHMETIC, leftOperand, rightOperand, context);
            case Keywords.DIVIDE:
                return new DivideOperator(Interpreters.ARITHMETIC, leftOperand, rightOperand, context);
            default: return null;
        }
    }
}
