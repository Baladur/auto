package com.roman.interpretation.expression;

import com.roman.base.Context;
import com.roman.interpretation.ExpressionInterpreter;
import com.roman.operation.ExpressionOperator;
import com.roman.operation.Operator;
import com.roman.util.ProcessException;
import com.roman.operation.expression.common.ConstantOperator;

/**
 * Created by roman on 09.04.2017.
 */
public class BooleanConstantInterpreter extends BooleanInterpreter {
    @Override
    public ExpressionOperator interpret(String input, Context context) throws ProcessException {
        if (!(input.equals("true") || input.equals("false"))) {
            return getNextResponsible().interpret(input, context);
        }
        return new ConstantOperator(input);
    }
}
