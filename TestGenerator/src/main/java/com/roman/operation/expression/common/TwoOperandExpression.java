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
public abstract class TwoOperandExpression {
    protected ExpressionOperator operand1;
    protected ExpressionOperator operand2;

    public TwoOperandExpression(ExpressionInterpreter interpreter, String leftExp, String rightExp, Context context) throws ProcessException {
        operand1 = interpreter.interpret(leftExp, context);
        log.info("Interpreted first operand.");
        operand2 = interpreter.interpret(rightExp, context);
        log.info("Interpreted second operand.");
    }
}
