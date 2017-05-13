package com.roman.operation.command;

import com.roman.base.Context;
import com.roman.operation.CommandOperator;
import com.roman.operation.Operator;
import com.roman.util.BaseTestWriter;
import com.roman.util.ProcessException;

import java.io.IOException;
import java.util.List;

/**
 * Created by roman on 02.04.2017.
 */
public class OrElseOperator implements CommandOperator {
    private final List<CommandOperator> instructions;

    public OrElseOperator(List<CommandOperator> instructions) {
        this.instructions = instructions;
    }

    @Override
    public void evaluate(BaseTestWriter writer, Context context) throws IOException {
        writer.writeOrElse();
        writer.startBlock();
        for (CommandOperator instruction : instructions) {
            instruction.evaluate(writer, context);
        }
        writer.endBlock();
        writer.write(")");
        writer.writeActionEnd();
    }
}
