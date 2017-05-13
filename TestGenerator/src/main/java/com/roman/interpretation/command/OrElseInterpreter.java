package com.roman.interpretation.command;

import com.roman.base.*;
import com.roman.interpretation.CommandInterpreter;
import com.roman.base.Interpreters;
import com.roman.operation.CommandOperator;
import com.roman.operation.Operator;
import com.roman.operation.command.OrElseOperator;
import com.roman.util.InputTestReader;
import com.roman.util.ProcessException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by roman on 02.04.2017.
 */
public class OrElseInterpreter implements CommandInterpreter {
    private static final Pattern OR_ELSE = Pattern.compile("^\\t*or else$");

    @Override
    public CommandOperator interpret(InputTestReader reader, Context context) throws ProcessException, IOException {
        reader.readLine();
        reader.readToken(Keywords.OR);
        reader.readToken(Keywords.ELSE);
        reader.assertEndOfLine();
        reader.startBlock();
        List<CommandOperator> instructions = new ArrayList<>();
        do {
            instructions.add(Interpreters.INSTRUCTION.interpret(reader, context));
        } while (!reader.isEndOfBlock());

        return new OrElseOperator(instructions);
    }

    public boolean canBeInterpreted(InputTestReader reader) throws IOException {
        return canBeInterpreted(OR_ELSE, reader);
    }
}
