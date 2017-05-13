package com.roman.interpretation.expression;

import com.roman.base.Context;
import com.roman.base.Keywords;
import com.roman.operation.ExpressionOperator;
import com.roman.operation.Operator;
import com.roman.util.ProcessException;
import com.roman.base.Interpreters;
import com.roman.operation.expression.bool.NegationOperator;

/**
 * Created by roman on 09.04.2017.
 */
public class NegationInterpreter extends BooleanInterpreter {
    @Override
    public ExpressionOperator interpret(String input, Context context) throws ProcessException {
        if (!(input.startsWith(Keywords.NOT))) {
            return getNextResponsible().interpret(input, context);
        }
        return new NegationOperator(Interpreters.BOOL, input.substring(1, input.length()), context);
    }
}
