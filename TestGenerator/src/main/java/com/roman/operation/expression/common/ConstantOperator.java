package com.roman.operation.expression.common;

import com.roman.base.Context;
import com.roman.operation.ExpressionOperator;
import com.roman.operation.Operator;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by roman on 11.09.2016.
 */
@Slf4j
public class ConstantOperator implements ExpressionOperator {

    private String value;

    public ConstantOperator(String value) {
        this.value = value;
        log.info(String.format("Interpreted constant: %s.", value));
    }

    @Override
    public String evaluate(Context context) {
        return value;
    }
}
