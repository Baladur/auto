package com.roman.interpretation.expression;

import com.roman.base.Context;
import com.roman.base.PrimitiveTypes;
import com.roman.interpretation.ExpressionInterpreter;
import com.roman.operation.ExpressionOperator;
import com.roman.operation.Operator;
import com.roman.operation.expression.Value;
import com.roman.util.ProcessException;
import com.roman.operation.expression.common.ConstantOperator;

/**
 * Created by roman on 11.09.2016.
 */
public class NumberConstantInterpreter extends ArithmeticInterpreter {

    @Override
    public ExpressionOperator interpret(String input, Context context) throws ProcessException {
        try {
            Integer.parseInt(input);
        } catch (NumberFormatException nfe) {
            try {
                Double.parseDouble(input);
            } catch (NumberFormatException nfe1) {
                return getNextResponsible().interpret(input, context);
            }
            return new Value(context.getType(PrimitiveTypes.DOUBLE), new ConstantOperator(input));
        }
        return new Value(context.getType(PrimitiveTypes.INT), new ConstantOperator(input));
    }
}
