package com.roman.interpretation.expression;

import com.roman.base.Context;
import com.roman.interpretation.ExpressionInterpreter;
import com.roman.operation.ExpressionOperator;
import com.roman.operation.Operator;
import com.roman.util.ProcessException;
import com.roman.interpretation.ChainedInterpreter;
import com.roman.base.Interpreters;
import com.roman.operation.expression.common.GroupOperator;

/**
 * Created by roman on 10.09.2016.
 */
public class GroupInterpreter extends ChainedInterpreter<ExpressionInterpreter> implements ExpressionInterpreter {
    private ExpressionType type;

    public GroupInterpreter(ExpressionType type) {
        this.type = type;
    }

    @Override
    public ExpressionOperator interpret(String input, Context context) throws ProcessException {
        if (input.charAt(0) == '(' && input.charAt(input.length() - 1) == ')') {
            String group = input.substring(1, input.length() - 1);
            return new GroupOperator(getParentInterpreter(), group, context);
        } else {
            return getNextResponsible().interpret(input, context);
        }
    }

    private ExpressionInterpreter getParentInterpreter() throws ProcessException {
        switch (type) {
            case ARITHMETIC: return Interpreters.ARITHMETIC;
            case BOOL: return Interpreters.BOOL;
            case ANY: return Interpreters.VALUE;
            default: throw new ProcessException("Unexpected type of group interpreter.");
        }
    }
}
