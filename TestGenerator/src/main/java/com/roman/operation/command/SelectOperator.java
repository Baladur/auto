package com.roman.operation.command;

import com.roman.base.Context;
import com.roman.operation.CommandOperator;
import com.roman.operation.ExpressionOperator;
import com.roman.operation.Operator;
import com.roman.util.BaseTestWriter;
import com.roman.util.ProcessException;
import lombok.Setter;

import java.io.IOException;
import java.util.Optional;

/**
 * Created by roman on 27.04.2017.
 */
@Setter
public class SelectOperator implements CommandOperator {
    private String elementName;
    private ExpressionOperator option;
    private Optional<ExpressionOperator> timeAmount = Optional.empty();
    private Optional<String> timeUnit = Optional.empty();
    private Optional<CommandOperator> orElseOperator = Optional.empty();

    @Override
    public void evaluate(BaseTestWriter writer, Context context) throws IOException {
        writer.writeSelect(context.getElementType(elementName), elementName,
                option.evaluate(context), timeAmount.map(ta -> ta.evaluate(context)), timeUnit);
        if (orElseOperator.isPresent()) {
            orElseOperator.get().evaluate(writer, context);
        } else {
            writer.writeActionEnd();
        }
    }
}
