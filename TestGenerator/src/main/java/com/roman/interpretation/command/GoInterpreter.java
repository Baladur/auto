package com.roman.interpretation.command;

import com.roman.base.Context;
import com.roman.base.Interpreters;
import com.roman.base.Keywords;
import com.roman.interpretation.ChainedInterpreter;
import com.roman.interpretation.CommandInterpreter;
import com.roman.operation.CommandOperator;
import com.roman.operation.Operator;
import com.roman.operation.command.GoOperator;
import com.roman.util.InputTestReader;
import com.roman.util.ProcessException;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by roman on 21.04.2017.
 */
public class GoInterpreter extends ChainedInterpreter<CommandInterpreter> implements CommandInterpreter {
    private static final Pattern GO = Pattern.compile("\\t*go (.*)$");

    @Override
    public CommandOperator interpret(InputTestReader reader, Context context) throws ProcessException, IOException {
        if (!canBeInterpreted(GO, reader)) {
            return getNextResponsible().interpret(reader, context);
        }
        reader.readLine();
        reader.readToken(Keywords.GO);
        return new GoOperator(Interpreters.STRING, reader.readAllTokensInLine(), context);
    }
}
