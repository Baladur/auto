package com.roman.interpretation.command;

import com.roman.base.Context;
import com.roman.base.Interpreters;
import com.roman.base.Keywords;
import com.roman.interpretation.CommandInterpreter;
import com.roman.operation.CommandOperator;
import com.roman.operation.Operator;
import com.roman.operation.command.AssignmentCommandOperator;
import com.roman.util.InputTestReader;
import com.roman.util.ProcessException;

import java.io.IOException;
import java.util.regex.Pattern;

/**
 * Created by roman on 11.04.2017.
 */
public class AssignmentCommandInterpreter extends InstructionInterpreter {
    private static final Pattern ASSIGN = Pattern.compile("^\\t*([A-z][^\\s]*) : (.+)$");

    @Override
    public CommandOperator interpret(InputTestReader reader, Context context) throws ProcessException, IOException {
        if (!canBeInterpreted(ASSIGN, reader)) {
            return getNextResponsible().interpret(reader, context);
        }
        return new AssignmentCommandOperator(Interpreters.ASSIGNMENT.interpret(reader.readLine(), context));
    }
}
