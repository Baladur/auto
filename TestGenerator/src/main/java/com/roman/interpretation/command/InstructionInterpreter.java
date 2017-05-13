package com.roman.interpretation.command;

import com.roman.base.*;
import com.roman.interpretation.ChainedInterpreter;
import com.roman.interpretation.CommandInterpreter;
import com.roman.operation.CommandOperator;
import com.roman.operation.Operator;
import com.roman.util.InputTestReader;
import com.roman.util.ProcessException;

import java.io.IOException;
import java.util.Optional;

/**
 * Created by roman on 10.04.2017.
 */
public class InstructionInterpreter extends ChainedInterpreter<CommandInterpreter> implements CommandInterpreter {
    private static final CommandInterpreter ASSIGN = new AssignmentCommandInterpreter();
    private static final CommandInterpreter WHILE = new WhileInterpreter().withNextResponsible(ASSIGN);
    private static final CommandInterpreter IF = new IfInterpreter().withNextResponsible(WHILE);
    private static final CommandInterpreter SELECT = new SelectInterpreter().withNextResponsible(IF);
    private static final CommandInterpreter FILL = new FillInterpreter().withNextResponsible(SELECT);
    private static final CommandInterpreter CLICK = new ClickInterpreter().withNextResponsible(FILL);
    private static final CommandInterpreter GO = new GoInterpreter().withNextResponsible(CLICK);
    private static final CommandInterpreter ASSERT = new AssertInterpreter().withNextResponsible(GO);

    @Override
    public CommandOperator interpret(InputTestReader reader, Context context) throws ProcessException, IOException {
        CommandOperator res = ASSERT.interpret(reader, context);
        if (res == null) {
            throw new ProcessException(reader, "No instructions could be interpreted.");
        }
        return res;
    }
}
