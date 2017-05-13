package com.roman.interpretation.expression;

import com.roman.base.*;
import com.roman.base.Interpreters;
import com.roman.operation.*;
import com.roman.operation.expression.bool.*;
import com.roman.util.ProcessException;

import java.util.Arrays;
import java.util.List;

/**
 * Created by roman on 09.04.2017.
 */
public class AndOrInterpreter extends BooleanInterpreter {
    private static String[] operators = new String[]{
            Keywords.AND,
            Keywords.OR,
            Keywords.STARTS_WITH,
            Keywords.ENDS_WITH,
            Keywords.CONTAINS,
            Keywords.EQUALS,
            Keywords.MORE_OR_EQUALS,
            Keywords.LESS_OR_EQUALS,
            Keywords.MORE_THAN,
            Keywords.LESS_THAN
    };

    @Override
    public ExpressionOperator interpret(String input, Context context) throws ProcessException {
        String[] operands = splitOperands(input, operators);
        if (operands == null) {
            return getNextResponsible().interpret(input, context);
        }
        String leftOperand = operands[0];
        String operator = operands[1];
        String rightOperand = operands[2];
        switch (operator) {
            case Keywords.AND:
                return new AndOperator(Interpreters.BOOL, leftOperand, rightOperand, context);
            case Keywords.OR:
                return new OrOperator(Interpreters.BOOL, leftOperand, rightOperand, context);
            case Keywords.STARTS_WITH:
                return new StartsWithOperator(Interpreters.STRING, leftOperand, rightOperand, context);
            case Keywords.ENDS_WITH:
                return new EndsWithOperator(Interpreters.STRING, leftOperand, rightOperand, context);
            case Keywords.MORE_THAN:
                return new MoreThanOperator(Interpreters.ARITHMETIC, leftOperand, rightOperand, context);
            case Keywords.LESS_THAN:
                return new LessThanOperator(Interpreters.ARITHMETIC, leftOperand, rightOperand, context);
            case Keywords.EQUALS:
                return new EqualsOperator(Interpreters.VALUE, leftOperand, rightOperand, context);
            case Keywords.MORE_OR_EQUALS:
                return new MoreOrEqualsOperator(Interpreters.ARITHMETIC, leftOperand, rightOperand, context);
            case Keywords.LESS_OR_EQUALS:
                return new LessOrEqualsOperator(Interpreters.ARITHMETIC, leftOperand, rightOperand, context);
        }

        return getNextResponsible().interpret(input, context);
    }
}
