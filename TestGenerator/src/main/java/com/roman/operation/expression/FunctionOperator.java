package com.roman.operation.expression;

import com.roman.base.Context;
import com.roman.model.Method;
import com.roman.operation.ExpressionOperator;
import com.roman.operation.Operator;
import com.roman.util.ProcessException;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by roman on 17.04.2017.
 */
@Slf4j
public class FunctionOperator implements ExpressionOperator {
    private Method method;
    private ExpressionOperator object;
    private ParameterOperator params;

    public FunctionOperator(Method method, ExpressionOperator operator, ParameterOperator params) {
        this.method = method;
        this.object = operator;
        this.params = params;
        log.info("Interpreted function: method = {}", method.scriptName);
    }

    @Override
    public String evaluate(Context context) {
        return String.format("%s.%s%s", object.evaluate(context), method.javaName, params.evaluate(context));
    }
}
