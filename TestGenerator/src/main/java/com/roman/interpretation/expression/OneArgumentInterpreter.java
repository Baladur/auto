package com.roman.interpretation.expression;

import com.roman.base.Context;
import com.roman.base.Interpreters;
import com.roman.interpretation.ExpressionInterpreter;
import com.roman.operation.ExpressionOperator;
import com.roman.operation.Operator;
import com.roman.operation.expression.ParameterOperator;
import com.roman.util.ProcessException;

import java.util.Arrays;

/**
 * Created by roman on 18.04.2017.
 */
public class OneArgumentInterpreter implements ExpressionInterpreter {
    @Override
    public ExpressionOperator interpret(String input, Context context) throws ProcessException {
        ExpressionOperator param = Interpreters.VALUE.interpret(input, context);
        return new ParameterOperator(Arrays.asList(param));
    }
}
