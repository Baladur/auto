package com.roman.operation.expression.bool;

import com.roman.base.Context;
import com.roman.interpretation.ExpressionInterpreter;
import com.roman.operation.ExpressionOperator;
import com.roman.operation.Operator;
import com.roman.operation.expression.common.OneOperandExpression;
import com.roman.util.ProcessException;

/**
 * Created by roman on 09.04.2017.
 */
public class NegationOperator extends OneOperandExpression implements ExpressionOperator {
    public NegationOperator(ExpressionInterpreter interpreter, String exp, Context context) throws ProcessException {
        super(interpreter, exp, context);
    }

    @Override
    public String evaluate(Context context) {
        return String.format("!%s", operand.evaluate(context));
    }
}
