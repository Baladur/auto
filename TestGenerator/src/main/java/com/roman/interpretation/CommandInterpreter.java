package com.roman.interpretation;

import com.roman.base.Context;
import com.roman.operation.CommandOperator;
import com.roman.operation.Operator;
import com.roman.util.InputTestReader;
import com.roman.util.ProcessException;

import java.io.IOException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by roman on 10.09.2016.
 */
public interface CommandInterpreter extends Interpreter {
    public CommandOperator interpret(InputTestReader reader, Context context) throws ProcessException, IOException;
    default boolean canBeInterpreted(Pattern pattern, InputTestReader reader) throws IOException {
        Optional<String> line = Optional.ofNullable(reader.nextLine());
        return line.isPresent() && pattern.matcher(line.get()).matches();
    }
}
