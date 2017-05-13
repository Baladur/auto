package com.roman.interpretation.command;

import com.roman.base.*;
import com.roman.interpretation.CommandInterpreter;
import com.roman.base.Interpreters;
import com.roman.operation.CommandOperator;
import com.roman.operation.ErrorOperator;
import com.roman.operation.Operator;
import com.roman.operation.command.TestOperator;
import com.roman.util.InputTestReader;
import com.roman.util.ProcessException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by roman on 26.03.2017.
 */
@Slf4j
public class TestInterpreter implements CommandInterpreter {
    private static final CommandInterpreter STEP = new StepInterpreter();

    @Override
    public CommandOperator interpret(InputTestReader reader, Context context) throws ProcessException, IOException {
        reader.readLine();
        reader.assertIndents(0);
        reader.readToken(Keywords.TEST);
        String testName = reader.readID();
        reader.readToken(Keywords.WITH);
        reader.readToken(Keywords.NAME);
        reader.readToken(Keywords.ASSIGN);
        String testLocalizedName = reader.readStringConstant();
        reader.readLine();
        reader.assertIndents(1);
        reader.readToken(Keywords.WITH);
        reader.readToken(Keywords.DESCRIPTION);
        reader.readToken(Keywords.ASSIGN);
        String description = reader.readStringConstant();
        context.setInParameterDeclaration(true);
        while (reader.nextLine().trim().length() > 0) {
            reader.readLine();
            reader.assertIndents(1);
            reader.readToken(Keywords.WITH);
            Interpreters.ASSIGNMENT.interpret(reader.readAllTokensInLine(), context);
        }
        context.setInParameterDeclaration(false);
        List<CommandOperator> steps = new ArrayList<>();
        do {
            reader.readLine();
            steps.add(STEP.interpret(reader, context));
        } while (!reader.isEOF());
        log.info(context.toString());
        context.setTestName(testName);
        return new TestOperator(testName, testLocalizedName, description, steps);
    }
}
