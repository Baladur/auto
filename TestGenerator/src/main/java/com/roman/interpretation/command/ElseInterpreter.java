package com.roman.interpretation.command;

import com.roman.base.Context;
import com.roman.base.Interpreters;
import com.roman.base.Keywords;
import com.roman.interpretation.CommandInterpreter;
import com.roman.operation.CommandOperator;
import com.roman.operation.Operator;
import com.roman.operation.command.ElseOperator;
import com.roman.util.InputTestReader;
import com.roman.util.ProcessException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by roman on 27.04.2017.
 */
public class ElseInterpreter implements CommandInterpreter {
    private static final Pattern ELSE = Pattern.compile("^\\t*else$");

    @Override
    public CommandOperator interpret(InputTestReader reader, Context context) throws ProcessException, IOException {
        reader.readLine();
        reader.readToken(Keywords.ELSE);
        reader.assertEndOfLine();
        reader.startBlock();
        List<CommandOperator> instructions = new ArrayList<>();
        do {
            instructions.add(Interpreters.INSTRUCTION.interpret(reader, context));
        } while (!reader.isEndOfBlock());
        return new ElseOperator(instructions);
    }

    public boolean canBeInterpreted(InputTestReader reader) throws IOException {
        return canBeInterpreted(ELSE, reader);
    }
}
