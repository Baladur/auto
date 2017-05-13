package com.roman.interpretation.expression;

import com.roman.base.Context;
import com.roman.base.Interpreters;
import com.roman.interpretation.ChainedInterpreter;
import com.roman.interpretation.ExpressionInterpreter;
import com.roman.operation.ExpressionOperator;
import com.roman.operation.Operator;
import com.roman.operation.expression.ParameterOperator;
import com.roman.util.ProcessException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by roman on 18.04.2017.
 */
public class ManyArgumentsInterpreter extends ChainedInterpreter<ExpressionInterpreter> implements ExpressionInterpreter {
    @Override
    public ExpressionOperator interpret(String input, Context context) throws ProcessException {
        //check clauses
        if (!(input.startsWith("(") && input.endsWith(")"))) {
            return getNextResponsible().interpret(input, context);
        }
        input = input.substring(1, input.length()-1);
        //check that ',' is not inside string
        boolean inString = false;
        List<Integer> paramSplitterIndexes = new ArrayList<>();
        for (Character ch : input.toCharArray()) {
            if (inString) {
                if (ch == '"') {
                    inString = false;
                }
            } else {
                if (ch == '"') {
                    inString = true;
                } else if (ch == ',') {
                    paramSplitterIndexes.add(input.indexOf(ch));
                }
            }
        }
        List<ExpressionOperator> params = new ArrayList<>();
        int startIndex = 0;
        for (int i = 0; i < paramSplitterIndexes.size(); i++) {
            String paramStr = input.substring(startIndex, paramSplitterIndexes.get(i));
            params.add(Interpreters.VALUE.interpret(paramStr, context));
            //new parameter start index is after ',' and ' '
            startIndex = paramSplitterIndexes.get(i) + 2;
        }
        //add last parameter
        params.add(Interpreters.VALUE.interpret(
                input.substring(paramSplitterIndexes.get(paramSplitterIndexes.size()-1) + 2, input.length()), context));

        return new ParameterOperator(params);
    }
}
