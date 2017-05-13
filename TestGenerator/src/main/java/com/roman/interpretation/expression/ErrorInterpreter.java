package com.roman.interpretation.expression;

import com.roman.base.Context;
import com.roman.base.InterpretationError;
import com.roman.interpretation.CommandInterpreter;
import com.roman.interpretation.ExpressionInterpreter;
import com.roman.operation.CommandOperator;
import com.roman.operation.ErrorOperator;
import com.roman.operation.ExpressionOperator;
import com.roman.operation.Operator;
import com.roman.util.InputTestReader;
import com.roman.util.ProcessException;

import java.io.IOException;

/**
 * Created by roman on 23.04.2017.
 */
public class ErrorInterpreter implements ExpressionInterpreter, CommandInterpreter {
    @Override
    public ExpressionOperator interpret(String input, Context context) throws ProcessException {
        context.addError(String.format("Could not interpret expression '%s'.", input));
        return new ErrorOperator();
    }

    @Override
    public CommandOperator interpret(InputTestReader reader, Context context) throws ProcessException, IOException {
        context.addError(String.format("Could not interpret line : %s.", reader.nextLine()));
        return new ErrorOperator();
    }
}
