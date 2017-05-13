package com.roman.operation.expression.bool;

import com.roman.base.Context;
import com.roman.interpretation.ExpressionInterpreter;
import com.roman.operation.ExpressionOperator;
import com.roman.operation.Operator;
import com.roman.operation.expression.common.TwoOperandExpression;
import com.roman.util.ProcessException;

/**
 * Created by roman on 09.04.2017.
 */
public class MoreOrEqualsOperator extends TwoOperandExpression implements ExpressionOperator {
    public MoreOrEqualsOperator(ExpressionInterpreter interpreter, String leftExp, String rightExp, Context context) throws ProcessException {
        super(interpreter, leftExp, rightExp, context);
    }

    @Override
    public String evaluate(Context context) {
        return String.format("%s >= %s", operand1.evaluate(context), operand2.evaluate(context));
    }
}
