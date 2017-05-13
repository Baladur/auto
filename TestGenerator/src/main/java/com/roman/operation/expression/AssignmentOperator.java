package com.roman.operation.expression;

import com.roman.base.Context;
import com.roman.operation.ExpressionOperator;
import com.roman.operation.Operator;
import com.roman.util.ProcessException;
import com.roman.model.Variable;

/**
 * Created by roman on 07.04.2017.
 */
public class AssignmentOperator implements ExpressionOperator {
    private Variable variable;
    private Value value;

    public AssignmentOperator(Variable variable, Value value) {
        this.variable = variable;
        this.value = value;
    }

    @Override
    public String evaluate(Context context) {
        return String.format("%s %s = %s;", variable.getType().getFinalName(), variable.getFinalName(), value.evaluate(context));
    }
}
