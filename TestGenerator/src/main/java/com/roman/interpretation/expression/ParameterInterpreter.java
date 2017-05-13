package com.roman.interpretation.expression;

import com.roman.base.Context;
import com.roman.interpretation.ChainedInterpreter;
import com.roman.interpretation.ExpressionInterpreter;
import com.roman.operation.ExpressionOperator;
import com.roman.operation.Operator;
import com.roman.util.ProcessException;

import java.util.Optional;

/**
 * Created by roman on 17.04.2017.
 */
public class ParameterInterpreter extends ChainedInterpreter<ExpressionInterpreter> implements ExpressionInterpreter {
    private static final ExpressionInterpreter ONE_ARGUMENT = new OneArgumentInterpreter();
    private static final ExpressionInterpreter MANY_ARGUMENTS = new ManyArgumentsInterpreter().withNextResponsible(ONE_ARGUMENT);

    @Override
    public ExpressionOperator interpret(String input, Context context) throws ProcessException {
        ExpressionOperator res = MANY_ARGUMENTS.interpret(input, context);
        if (res == null) {
            return getNextResponsible().interpret(input, context);
        }
        return res;
    }
}
