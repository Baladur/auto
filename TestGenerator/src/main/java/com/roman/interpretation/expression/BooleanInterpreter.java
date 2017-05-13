package com.roman.interpretation.expression;

import com.roman.base.Context;
import com.roman.base.PrimitiveTypes;
import com.roman.interpretation.ExpressionInterpreter;
import com.roman.operation.ExpressionOperator;
import com.roman.operation.Operator;
import com.roman.operation.expression.Value;
import com.roman.util.ProcessException;
import com.roman.interpretation.ChainedInterpreter;

/**
 * Created by roman on 07.04.2017.
 */
public class BooleanInterpreter extends ChainedInterpreter<ExpressionInterpreter> implements ExpressionInterpreter {
    private static final ExpressionInterpreter VARIABLE = new VariableInterpreter(ExpressionType.BOOL);
    private static final ExpressionInterpreter FUNCTION = new FunctionInterpreter(ExpressionType.BOOL).withNextResponsible(VARIABLE);
    private static final ExpressionInterpreter CONSTANT = new BooleanConstantInterpreter().withNextResponsible(FUNCTION);
    private static final ExpressionInterpreter GROUP = new GroupInterpreter(ExpressionType.BOOL).withNextResponsible(CONSTANT);
    private static final ExpressionInterpreter AND_OR = new AndOrInterpreter().withNextResponsible(GROUP);
    private static final ExpressionInterpreter NEGATE = new NegationInterpreter().withNextResponsible(AND_OR);

    @Override
    public ExpressionOperator interpret(String input, Context context) throws ProcessException {
        ExpressionOperator res = NEGATE.interpret(input, context);
        if (res == null) {
            return getNextResponsible().interpret(input, context);
        }
        //TODO: understand why IDE found Operator here
        return (ExpressionOperator) Value.ofBool(res, context);
    }
}
