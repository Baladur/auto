package com.roman.interpretation.command;

import com.roman.base.Context;
import com.roman.base.Interpreters;
import com.roman.base.Keywords;
import com.roman.interpretation.ChainedInterpreter;
import com.roman.interpretation.CommandInterpreter;
import com.roman.operation.CommandOperator;
import com.roman.operation.Operator;
import com.roman.operation.command.IfOperator;
import com.roman.util.InputTestReader;
import com.roman.util.ProcessException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * @
 */
public class IfInterpreter extends ChainedInterpreter<CommandInterpreter> implements CommandInterpreter {
    private static final Pattern IF = Pattern.compile("^\\t*if .*$");

    @Override
    public CommandOperator interpret(InputTestReader reader, Context context) throws ProcessException, IOException {
        if (!canBeInterpreted(IF, reader)) {
            return getNextResponsible().interpret(reader, context);
        }
        reader.readLine();
        reader.readToken(Keywords.IF);
        String condition = reader.readAllTokensInLine();
        IfOperator ifOperator = new IfOperator();
        ifOperator.setCondition(Interpreters.BOOL.interpret(condition, context));
        List<CommandOperator> instructions = new ArrayList<>();
        reader.startBlock();
        do {
            instructions.add(Interpreters.INSTRUCTION.interpret(reader, context));
        } while (!reader.isEndOfBlock());
        ifOperator.setInstructions(instructions);
        if (Interpreters.ELSE.canBeInterpreted(reader)) {
            ifOperator.setElseOperator(Optional.of(Interpreters.ELSE.interpret(reader, context)));
        }
        return ifOperator;
    }
}
