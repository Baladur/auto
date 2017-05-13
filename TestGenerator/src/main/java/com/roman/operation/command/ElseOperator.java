package com.roman.operation.command;

import com.roman.base.Context;
import com.roman.operation.CommandOperator;
import com.roman.operation.Operator;
import com.roman.util.BaseTestWriter;
import com.roman.util.ProcessException;
import lombok.Setter;

import java.io.IOException;
import java.util.List;

/**
 * Created by roman on 27.04.2017.
 */
public class ElseOperator implements CommandOperator {
    private List<CommandOperator> instructions;

    public ElseOperator(List<CommandOperator> instructions) {
        this.instructions = instructions;
    }

    @Override
    public void evaluate(BaseTestWriter writer, Context context) throws IOException {
        writer.writeElse();
        writer.startBlock();
        for (CommandOperator instruction : instructions) {
            instruction.evaluate(writer, context);
        }
        writer.endBlock();
    }
}
