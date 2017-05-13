package com.roman.interpretation.expression;

import com.roman.base.Context;
import com.roman.base.PrimitiveTypes;
import com.roman.interpretation.ExpressionInterpreter;
import com.roman.operation.ExpressionOperator;
import com.roman.operation.Operator;
import com.roman.operation.expression.Value;
import com.roman.operation.expression.common.ConstantOperator;
import com.roman.util.ProcessException;

/**
 * Created by roman on 10.04.2017.
 */
public class StringConstantInterpreter extends StringInterpreter {
    @Override
    public ExpressionOperator interpret(String input, Context context) throws ProcessException {
        if (!(input.startsWith("\"") && input.endsWith("\"") && input.indexOf("\"", 1) == input.length() - 1)) {
            return getNextResponsible().interpret(input, context);
        }

        return new Value(context.getType(PrimitiveTypes.STRING), new ConstantOperator(input));
    }
}
