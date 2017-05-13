package com.roman.operation;

import com.roman.base.Context;
import com.roman.model.ErrorType;
import com.roman.operation.expression.Value;
import com.roman.util.BaseTestWriter;
import com.roman.util.ProcessException;

import java.io.IOException;

/**
 * Created by roman on 23.04.2017.
 */
public class ErrorOperator extends Value implements ExpressionOperator, CommandOperator {
    public ErrorOperator() {
        this.concreteValue = this;
        this.type = new ErrorType();
    }

    @Override
    public void evaluate(BaseTestWriter writer, Context context) throws IOException {

    }
}
