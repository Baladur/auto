package com.roman.operation.command;

import com.roman.base.Context;
import com.roman.operation.CommandOperator;
import com.roman.operation.ExpressionOperator;
import com.roman.operation.Operator;
import com.roman.util.BaseTestWriter;
import com.roman.util.ProcessException;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.Optional;

/**
 * Created by roman on 02.04.2017.
 */
@Setter
@Getter
public class ClickOperator implements CommandOperator {
    private final String elementName;
    private final String elementType;
    private Optional<String> timeUnit = Optional.empty();
    private Optional<ExpressionOperator> timeAmount = Optional.empty();
    private Optional<CommandOperator> orElseOperator = Optional.empty();

    public ClickOperator(String elementName, String elementType) {
        this.elementName = elementName;
        this.elementType = elementType;
    }

    @Override
    public void evaluate(BaseTestWriter writer, Context context) throws IOException {
        Optional<String> timeAmountStr = timeAmount.map(ta -> ta.evaluate(context));
        writer.writeClick(elementType, elementName, timeAmountStr, timeUnit);
        if (orElseOperator.isPresent()) {
            orElseOperator.get().evaluate(writer, context);
        } else {
            writer.writeActionEnd();
        }
    }
}
