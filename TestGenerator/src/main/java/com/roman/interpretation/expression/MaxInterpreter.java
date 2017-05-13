package com.roman.interpretation.expression;

import com.roman.base.Context;
import com.roman.operation.ExpressionOperator;
import com.roman.operation.Operator;
import com.roman.util.ProcessException;
import com.roman.base.Interpreters;
import com.roman.operation.expression.arithmetic.MaxOperator;

/**
 * Created by roman on 11.09.2016.
 */
public class MaxInterpreter extends ArithmeticInterpreter {


    @Override
    public ExpressionOperator interpret(String input, Context context) throws ProcessException {
        if (input.length() > 5) {
            String params = input.substring(4, input.length() - 1);
            if (input.startsWith("max(") && input.endsWith(")")) {
                int level = 0;
                int delimCounter = 0;
                int index = 0;
                for (int i = 0; i < params.length(); i++) {
                    if (params.charAt(i) == '(') {
                        level++;
                    } else if (params.charAt(i) == ')') {
                        level--;
                    }

                    if (level == 0 && params.charAt(i) == ',') {
                        index = i;
                        delimCounter++;
                    }
                }

                if (delimCounter > 1) {
                    throw new ProcessException("Incorrect use of 'max()' function: too many arguments");
                } else if (delimCounter == 1) {
                    return new MaxOperator(Interpreters.ARITHMETIC, params.substring(0, index), params.substring(index + 1, params.length()), context);
                }

                throw new ProcessException("Incorrect use of 'max()' function: too few arguments");

            }
        }


        return getNextResponsible().interpret(input, context);
    }
}
