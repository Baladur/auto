package com.roman.operation.command;

import com.roman.base.Context;
import com.roman.operation.CommandOperator;
import com.roman.operation.ExpressionOperator;
import com.roman.operation.Operator;
import com.roman.util.BaseTestWriter;
import com.roman.util.ProcessException;
import lombok.Setter;

import java.io.IOException;

/**
 * Created by roman on 27.04.2017.
 */
@Setter
public class AssertOperator implements CommandOperator {
    private ExpressionOperator condition;
    private ExpressionOperator message;

    @Override
    public void evaluate(BaseTestWriter writer, Context context) throws IOException {
        writer.writeAssert(condition.evaluate(context), message.evaluate(context));
    }
}
