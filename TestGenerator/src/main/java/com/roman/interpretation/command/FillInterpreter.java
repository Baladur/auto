package com.roman.interpretation.command;

import com.roman.base.*;
import com.roman.interpretation.ExpressionInterpreter;
import com.roman.base.Interpreters;
import com.roman.interpretation.Interpreter;
import com.roman.interpretation.expression.ArithmeticInterpreter;
import com.roman.interpretation.expression.StringInterpreter;
import com.roman.operation.CommandOperator;
import com.roman.operation.command.FillOperator;
import com.roman.operation.Operator;
import com.roman.operation.expression.Value;
import com.roman.util.InputTestReader;
import com.roman.util.ProcessException;

import java.io.IOException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Interpreter of 'fill' operator.
 * Notes:
 * 1)
 */
public class FillInterpreter extends InstructionInterpreter {
    private static final Pattern FILL = Pattern.compile("\\t*fill #([A-z][^\\s]*) with (.*)");

    @Override
    public CommandOperator interpret(InputTestReader reader, Context context) throws ProcessException, IOException {
        if (!canBeInterpreted(FILL, reader)) {
            return getNextResponsible().interpret(reader, context);
        }
        reader.readLine();
        reader.readToken(Keywords.FILL);
        reader.readPrefix(Keywords.ELEMENT_PREFIX);
        String elementName = reader.readID();
        reader.readToken(Keywords.WITH);
        FillOperator fillOperator = new FillOperator(elementName);
        fillOperator.setValue(Interpreters.VALUE.interpret(reader.readUntilOneOf(Keywords.DURING), context));
        if (!reader.isEndOfLine()) {
            reader.readToken(Keywords.DURING);
            fillOperator.setTimeAmount(Optional.of(Interpreters.ARITHMETIC.interpret(reader.readUntilOneOf(Keywords.TIME_UNITS), context)));
            fillOperator.setTimeUnit(Optional.of(reader.readToken(Keywords.TIME_UNITS)));
        }
        reader.assertEndOfLine();

        if (Interpreters.OR_ELSE.canBeInterpreted(reader)) {
            fillOperator.setOrElseOperator(Optional.of(Interpreters.OR_ELSE.interpret(reader, context)));
        }
        return fillOperator;
    }
}
