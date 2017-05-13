package com.roman.operation.command;

import com.roman.base.Context;
import com.roman.interpretation.ExpressionInterpreter;
import com.roman.operation.CommandOperator;
import com.roman.operation.Operator;
import com.roman.operation.expression.common.OneOperandExpression;
import com.roman.util.BaseTestWriter;
import com.roman.util.ProcessException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * Created by roman on 21.04.2017.
 */
@Slf4j
public class GoOperator extends OneOperandExpression implements CommandOperator {
    public GoOperator(ExpressionInterpreter interpreter, String exp, Context context) throws ProcessException {
        super(interpreter, exp, context);
        log.info("Interpreted go.");
    }

    @Override
    public void evaluate(BaseTestWriter writer, Context context) throws IOException {
        writer.writeGo(operand.evaluate(context));
    }
}
