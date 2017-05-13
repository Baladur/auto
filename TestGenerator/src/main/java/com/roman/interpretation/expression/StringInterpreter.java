package com.roman.interpretation.expression;

import com.roman.base.*;
import com.roman.interpretation.ChainedInterpreter;
import com.roman.interpretation.ExpressionInterpreter;
import com.roman.operation.ExpressionOperator;
import com.roman.operation.expression.Value;
import com.roman.operation.Operator;
import com.roman.util.ProcessException;

/**
 * Created by roman on 07.04.2017.
 */
public class StringInterpreter extends ChainedInterpreter<ExpressionInterpreter> implements ExpressionInterpreter {
    private static final ExpressionInterpreter VARIABLE = new VariableInterpreter(ExpressionType.STRING);
    private static final ExpressionInterpreter FUNCTION = new FunctionInterpreter(ExpressionType.STRING).withNextResponsible(VARIABLE);
    private static final ExpressionInterpreter CONSTANT = new StringConstantInterpreter().withNextResponsible(FUNCTION);

    @Override
    public ExpressionOperator interpret(String input, Context context) throws ProcessException {
        ExpressionOperator res = CONSTANT.interpret(input, context);
        if (res == null) {
            return getNextResponsible().interpret(input, context);
        }
        return (ExpressionOperator) Value.ofString(res, context);
    }
}
