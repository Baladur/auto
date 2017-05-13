package com.roman.interpretation.expression;

import com.roman.base.Context;
import com.roman.base.PrimitiveTypes;
import com.roman.model.Variable;
import com.roman.interpretation.ChainedInterpreter;
import com.roman.interpretation.ExpressionInterpreter;
import com.roman.operation.ExpressionOperator;
import com.roman.operation.Operator;
import com.roman.util.ProcessException;

/**
 * Created by roman on 10.04.2017.
 */
public class VariableInterpreter extends ChainedInterpreter<ExpressionInterpreter> implements ExpressionInterpreter {
    private ExpressionType type;

    public VariableInterpreter(ExpressionType type) {
        this.type = type;
    }

    @Override
    public ExpressionOperator interpret(String input, Context context) throws ProcessException {
        if (!context.isVariableDeclared(input)) {
            return null;
        }
        //check type of variable
        Variable variable = context.getVariableByName(input);
        switch (type) {
            case ARITHMETIC:
                if (!(variable.getType().scriptName.equals(PrimitiveTypes.DOUBLE) || variable.getType().scriptName.equals(PrimitiveTypes.INT))) {
                    return null;
                }
                break;
            case BOOL:
                if (!variable.getType().scriptName.equals(PrimitiveTypes.BOOL)) {
                    return null;
                }
                break;
            case STRING:
                if (!variable.getType().scriptName.equals(PrimitiveTypes.STRING)) {
                    return null;
                }
        }
        return variable;
    }
}
