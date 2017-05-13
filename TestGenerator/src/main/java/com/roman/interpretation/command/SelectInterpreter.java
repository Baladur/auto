package com.roman.interpretation.command;

import com.roman.base.Context;
import com.roman.base.Interpreters;
import com.roman.base.Keywords;
import com.roman.base.PrimitiveTypes;
import com.roman.interpretation.ChainedInterpreter;
import com.roman.interpretation.CommandInterpreter;
import com.roman.model.Type;
import com.roman.operation.CommandOperator;
import com.roman.operation.ExpressionOperator;
import com.roman.operation.Operator;
import com.roman.operation.command.SelectOperator;
import com.roman.operation.expression.Value;
import com.roman.util.InputTestReader;
import com.roman.util.ProcessException;

import java.io.IOException;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Created by roman on 27.04.2017.
 */
public class SelectInterpreter extends ChainedInterpreter<CommandInterpreter> implements CommandInterpreter {
    private static final Pattern SELECT = Pattern.compile("^\\t*select (.*)$");

    @Override
    public CommandOperator interpret(InputTestReader reader, Context context) throws ProcessException, IOException {
        if (!canBeInterpreted(SELECT, reader)) {
            return getNextResponsible().interpret(reader, context);
        }
        SelectOperator selectOperator = new SelectOperator();
        reader.readLine();
        reader.readToken(Keywords.SELECT);
        reader.readPrefix(Keywords.ELEMENT_PREFIX);
        selectOperator.setElementName(reader.readID());
        reader.readToken(Keywords.OPTION);
        String valueString = reader.readUntilOneOf(Keywords.DURING);
        ExpressionOperator value = Interpreters.VALUE.interpret(valueString, context);
        Type valueType = ((Value)value).getType();
        if (!valueType.equals(context.getType(PrimitiveTypes.STRING)) && !valueType.equals(context.getType(PrimitiveTypes.INT))) {
            context.addError("Expected string value of option or option index.");
        }
        selectOperator.setOption(value);
        if (!reader.isEndOfLine()) {
            reader.readToken(Keywords.DURING);
            ExpressionOperator timeAmount = Interpreters.ARITHMETIC.interpret(reader.readUntilOneOf(Keywords.TIME_UNITS), context);
            selectOperator.setTimeAmount(Optional.of(timeAmount));
            selectOperator.setTimeUnit(Optional.of(reader.readToken(Keywords.TIME_UNITS)));
        }
        reader.assertEndOfLine();
        if (Interpreters.OR_ELSE.canBeInterpreted(reader)) {
            selectOperator.setOrElseOperator(Optional.of(Interpreters.OR_ELSE.interpret(reader, context)));
        }

        return selectOperator;
    }
}
