package com.roman.interpretation.command;

import com.roman.base.Context;
import com.roman.base.Interpreters;
import com.roman.base.Keywords;
import com.roman.interpretation.ChainedInterpreter;
import com.roman.interpretation.CommandInterpreter;
import com.roman.operation.CommandOperator;
import com.roman.operation.Operator;
import com.roman.operation.command.AssertOperator;
import com.roman.util.InputTestReader;
import com.roman.util.ProcessException;

import java.io.IOException;
import java.util.regex.Pattern;

/**
 * Created by roman on 27.04.2017.
 */
public class AssertInterpreter extends ChainedInterpreter<CommandInterpreter> implements CommandInterpreter {
    private static final Pattern ASSERT = Pattern.compile("^\\t*assert (.*)$");

    @Override
    public CommandOperator interpret(InputTestReader reader, Context context) throws ProcessException, IOException {
        if (!canBeInterpreted(ASSERT, reader)) {
            return getNextResponsible().interpret(reader, context);
        }
        AssertOperator assertOperator = new AssertOperator();
        reader.readLine();
        reader.readToken(Keywords.ASSERT);
        assertOperator.setCondition(Interpreters.BOOL.interpret(reader.readUntilOneOf(Keywords.WITH), context));
        reader.readToken(Keywords.WITH);
        assertOperator.setMessage(Interpreters.STRING.interpret(reader.readAllTokensInLine(), context));
        return assertOperator;
    }
}
