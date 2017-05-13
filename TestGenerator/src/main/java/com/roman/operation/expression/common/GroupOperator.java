package com.roman.operation.expression.common;

import com.roman.base.Context;
import com.roman.interpretation.ExpressionInterpreter;
import com.roman.operation.ExpressionOperator;
import com.roman.operation.Operator;
import com.roman.util.ProcessException;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by roman on 10.09.2016.
 */
@Slf4j
public class GroupOperator extends OneOperandExpression implements ExpressionOperator {

    public GroupOperator(ExpressionInterpreter interpreter, String exp, Context context) throws ProcessException {
        super(interpreter, exp, context);
        log.info("Interpreted group.");
    }

    @Override
    public String evaluate(Context context) {
        return String.format("(%s)", operand.evaluate(context));
    }
}
