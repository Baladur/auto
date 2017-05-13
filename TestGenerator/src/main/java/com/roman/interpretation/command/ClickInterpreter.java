package com.roman.interpretation.command;

import com.roman.base.*;
import com.roman.base.Interpreters;
import com.roman.operation.CommandOperator;
import com.roman.operation.command.ClickOperator;
import com.roman.operation.Operator;
import com.roman.util.InputTestReader;
import com.roman.util.ProcessException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by roman on 26.03.2017.
 */
@Slf4j
public class ClickInterpreter extends InstructionInterpreter {

    private static final Pattern CLICK = Pattern.compile("^\\t*click #([A-z][^\\s]*)( during (\\d+) (seconds|minutes|hours))?$");

    @Override
    public CommandOperator interpret(InputTestReader reader, Context context) throws ProcessException, IOException {
        if (!canBeInterpreted(CLICK, reader)) {
            return getNextResponsible().interpret(reader, context);
        }
        reader.readLine();
        reader.readToken(Keywords.CLICK);
        reader.readPrefix(Keywords.ELEMENT_PREFIX);
        String elementName = reader.readID();
        ClickOperator clickOperator = new ClickOperator(elementName, context.getElementType(elementName));
        if (!reader.isEndOfLine()) {
            reader.readToken(Keywords.DURING);
            clickOperator.setTimeAmount(Optional.of(Interpreters.ARITHMETIC.interpret(reader.readUntilOneOf(Keywords.TIME_UNITS), context)));
            clickOperator.setTimeUnit(Optional.of(reader.readToken(Keywords.TIME_UNITS)));
        }
        reader.assertEndOfLine();
        if (Interpreters.OR_ELSE.canBeInterpreted(reader)) {
            clickOperator.setOrElseOperator(Optional.of(Interpreters.OR_ELSE.interpret(reader, context)));
        }
        return clickOperator;
    }
}
