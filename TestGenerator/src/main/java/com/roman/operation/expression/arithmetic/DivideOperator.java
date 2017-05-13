package com.roman.operation.expression.arithmetic;

import com.roman.base.Context;
import com.roman.interpretation.ExpressionInterpreter;
import com.roman.operation.ExpressionOperator;
import com.roman.operation.Operator;
import com.roman.operation.expression.common.TwoOperandExpression;
import com.roman.util.ProcessException;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by roman on 11.09.2016.
 */
@Slf4j
public class DivideOperator extends TwoOperandExpression implements ExpressionOperator {

    public DivideOperator(ExpressionInterpreter interpreter, String leftExp, String rightExp, Context context) throws ProcessException {
        super(interpreter, leftExp, rightExp, context);
        log.info("Interpreted division.");
    }
    @Override
    public String evaluate(Context context) {
        return String.format("%s / %s", operand1.evaluate(context), operand2.evaluate(context));
    }
}
