package com.roman.interpretation.expression;

import com.roman.base.Context;
import com.roman.interpretation.ChainedInterpreter;
import com.roman.interpretation.ExpressionInterpreter;
import com.roman.operation.ExpressionOperator;
import com.roman.operation.Operator;
import com.roman.util.ProcessException;

import java.util.Optional;

/**
 * Created by roman on 07.04.2017.
 */
public class ValueInterpreter extends ChainedInterpreter<ExpressionInterpreter> implements ExpressionInterpreter {
    private static final ExpressionInterpreter VARIABLE = new VariableInterpreter(ExpressionType.ANY);
    private static final ExpressionInterpreter FUNCTION = new FunctionInterpreter(ExpressionType.ANY).withNextResponsible(VARIABLE);
    private static final ExpressionInterpreter ARITHMETIC = new ArithmeticInterpreter().withNextResponsible(FUNCTION);
    private static final ExpressionInterpreter BOOLEAN = new BooleanInterpreter().withNextResponsible(ARITHMETIC);
    private static final ExpressionInterpreter STRING = new StringInterpreter().withNextResponsible(BOOLEAN);


    @Override
    public ExpressionOperator interpret(String input, Context context) throws ProcessException {
        ExpressionOperator res = STRING.interpret(input, context);
        if (res == null) {
            return getNextResponsible().interpret(input, context);
        }
        return res;
    }
}
