package com.roman.operation.command;

import com.roman.base.Context;
import com.roman.interpretation.ExpressionInterpreter;
import com.roman.operation.CommandOperator;
import com.roman.operation.ExpressionOperator;
import com.roman.operation.Operator;
import com.roman.operation.expression.common.OneOperandExpression;
import com.roman.util.BaseTestWriter;
import com.roman.util.ProcessException;
import lombok.Setter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Created by roman on 26.04.2017.
 */
@Setter
public class IfOperator implements CommandOperator {
    private List<CommandOperator> instructions;
    private ExpressionOperator condition;
    private Optional<CommandOperator> elseOperator = Optional.empty();

    @Override
    public void evaluate(BaseTestWriter writer, Context context) throws IOException {
        writer.writeIf(condition.evaluate(context));
        writer.startBlock();
        for (CommandOperator instruction : instructions) {
            instruction.evaluate(writer, context);
        }
        writer.endBlock();
        if (elseOperator.isPresent()) {
            elseOperator.get().evaluate(writer, context);
        }
    }
}
