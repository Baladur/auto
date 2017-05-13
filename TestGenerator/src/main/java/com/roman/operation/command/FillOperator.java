package com.roman.operation.command;

import com.roman.base.Context;
import com.roman.operation.CommandOperator;
import com.roman.operation.ExpressionOperator;
import com.roman.operation.Operator;
import com.roman.operation.expression.Value;
import com.roman.util.BaseTestWriter;
import com.roman.util.ProcessException;
import lombok.Setter;

import java.io.IOException;
import java.util.Optional;

/**
 * Created by roman on 10.04.2017.
 */
@Setter
public class FillOperator implements CommandOperator {
    private final String elementName;
    private ExpressionOperator value;
    private Optional<String> timeUnit = Optional.empty();
    private Optional<ExpressionOperator> timeAmount = Optional.empty();
    private Optional<CommandOperator> orElseOperator = Optional.empty();

    public FillOperator(String elementName) {
        this.elementName = elementName;
    }

    @Override
    public void evaluate(BaseTestWriter writer, Context context) throws IOException {
        writer.writeFill(context.getElementType(elementName), elementName,
                value.evaluate(context), timeAmount.map(ta -> ta.evaluate(context)), timeUnit);
        if (orElseOperator.isPresent()) {
            orElseOperator.get().evaluate(writer, context);
        } else {
            writer.writeActionEnd();
        }
    }
}
