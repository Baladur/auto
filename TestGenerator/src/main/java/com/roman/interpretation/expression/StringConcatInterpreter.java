package com.roman.interpretation.expression;

import com.roman.base.*;
import com.roman.operation.ExpressionOperator;
import com.roman.operation.Operator;
import com.roman.operation.expression.Value;
import com.roman.operation.expression.common.PlusOperator;
import com.roman.util.ProcessException;

/**
 * Created by roman on 10.04.2017.
 */
public class StringConcatInterpreter extends StringInterpreter {
    @Override
    public ExpressionOperator interpret(String input, Context context) throws ProcessException {
        //TODO: solve problem with arithmetic plus
        String[] operands = splitOperands(input, Keywords.PLUS);
        if (operands == null) {
            return getNextResponsible().interpret(input, context);
        }
        String leftOperand = operands[0];
        String operator = operands[1];
        String rightOperand = operands[2];

        return new PlusOperator(Interpreters.STRING, leftOperand, rightOperand, context);
    }
}
