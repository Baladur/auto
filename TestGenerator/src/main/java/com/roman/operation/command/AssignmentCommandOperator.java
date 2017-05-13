package com.roman.operation.command;

import com.roman.base.Context;
import com.roman.base.Interpreters;
import com.roman.operation.CommandOperator;
import com.roman.operation.ExpressionOperator;
import com.roman.util.BaseTestWriter;

import java.io.IOException;

/**
 * Created by roman on 30.04.2017.
 */
public class AssignmentCommandOperator implements CommandOperator {
    private ExpressionOperator assignment;

    public AssignmentCommandOperator(ExpressionOperator assignment) {
        this.assignment = assignment;
    }

    @Override
    public void evaluate(BaseTestWriter writer, Context context) throws IOException {
        writer.newLine();
        writer.write(assignment.evaluate(context));
    }
}
