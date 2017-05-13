package com.roman.operation.expression.common;

import com.roman.base.Context;
import com.roman.interpretation.ExpressionInterpreter;
import com.roman.operation.ExpressionOperator;
import com.roman.operation.Operator;
import com.roman.util.ProcessException;

/**
 * Created by roman on 10.09.2016.
 */
public abstract class OneOperandExpression {
    protected ExpressionOperator operand;

    public OneOperandExpression(ExpressionInterpreter interpreter, String exp, Context context) throws ProcessException {
        operand = interpreter.interpret(exp, context);
    }

}
