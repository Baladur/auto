package com.roman.interpretation.expression;

import com.roman.base.*;
import com.roman.interpretation.ChainedInterpreter;
import com.roman.interpretation.ExpressionInterpreter;
import com.roman.operation.ExpressionOperator;
import com.roman.operation.Operator;
import com.roman.operation.expression.Value;
import com.roman.util.ProcessException;


/**
 * Created by roman on 10.09.2016.
 */
public class ArithmeticInterpreter extends ChainedInterpreter<ExpressionInterpreter> implements ExpressionInterpreter {
    private static final ExpressionInterpreter VARIABLE = new VariableInterpreter(ExpressionType.ARITHMETIC);
    private static final ExpressionInterpreter FUNCTION = new FunctionInterpreter(ExpressionType.ARITHMETIC).withNextResponsible(VARIABLE);
    private static final ExpressionInterpreter CONSTANT = new NumberConstantInterpreter().withNextResponsible(FUNCTION);
    private static final ExpressionInterpreter GROUP = new GroupInterpreter(ExpressionType.ARITHMETIC).withNextResponsible(CONSTANT);
    private static final ExpressionInterpreter MAX = new MaxInterpreter().withNextResponsible(GROUP);
    private static final ExpressionInterpreter PRIORITY_THREE = new PriorityThreeInterpreter().withNextResponsible(MAX);
    private static final ExpressionInterpreter PRIORITY_TWO = new PriorityTwoInterpreter().withNextResponsible(PRIORITY_THREE);
    private static final ExpressionInterpreter PRIORITY_ONE = new PriorityOneInterpreter().withNextResponsible(PRIORITY_TWO);


    @Override
    public ExpressionOperator interpret(String input, Context context) throws ProcessException {
        ExpressionOperator res = PRIORITY_ONE.interpret(input, context);
        if (res == null) {
            return getNextResponsible().interpret(input, context);
        }
        return (ExpressionOperator) Value.ofNumber(res, context);
    }


}