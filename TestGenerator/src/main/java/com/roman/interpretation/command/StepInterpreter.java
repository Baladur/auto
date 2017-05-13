package com.roman.interpretation.command;

import com.roman.base.*;
import com.roman.interpretation.CommandInterpreter;
import com.roman.base.Interpreters;
import com.roman.operation.CommandOperator;
import com.roman.operation.Operator;
import com.roman.operation.command.StepOperator;
import com.roman.util.InputTestReader;
import com.roman.util.LineHelper;
import com.roman.util.ProcessException;

import javax.xml.stream.events.Characters;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Created by roman on 29.03.2017.
 */
public class StepInterpreter implements CommandInterpreter {

    @Override
    public CommandOperator interpret(InputTestReader reader, Context context) throws ProcessException, IOException {
        //1 line of step declaration
        reader.readLine();
        reader.assertIndents(0);
        reader.readToken(Keywords.STEP);
        Integer stepNumber = reader.readInt();
        reader.readToken(Keywords.WITH);
        reader.readToken(Keywords.NAME);
        reader.readToken(Keywords.ASSIGN);
        String stepName = reader.readStringConstant();
        reader.readLine();
        reader.assertIndents(1);
        reader.readToken(Keywords.WITH);
        reader.readToken(Keywords.DESCRIPTION);
        reader.readToken(Keywords.ASSIGN);
        String description = reader.readStringConstant();
        reader.readEmptyLine();

        //step instructions
        List<CommandOperator> instructions = new ArrayList<>();
        do {
            instructions.add(Interpreters.INSTRUCTION.interpret(reader, context));
        } while (Optional.ofNullable(reader.nextLine()).orElseGet(String::new).trim().length() > 0);
        return new StepOperator(stepNumber, stepName, description, instructions);
    }
}
