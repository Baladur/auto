package com.roman.operation.expression.common;

import com.roman.base.Context;
import com.roman.interpretation.ExpressionInterpreter;
import com.roman.operation.ExpressionOperator;
import com.roman.operation.Operator;
import com.roman.operation.expression.common.TwoOperandExpression;
import com.roman.util.ProcessException;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by roman on 10.09.2016.
 */
@Slf4j
public class PlusOperator extends TwoOperandExpression implements ExpressionOperator {

    public PlusOperator(ExpressionInterpreter interpreter, String leftExp, String rightExp, Context context) throws ProcessException {
        super(interpreter, leftExp, rightExp, context);
        log.info("Interpreted plus.");
    }

    @Override
    public String evaluate(Context context) {
        return String.format("%s + %s", operand1.evaluate(context), operand2.evaluate(context));
    }
}
