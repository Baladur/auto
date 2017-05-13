package com.roman.interpretation;

import com.roman.base.Context;
import com.roman.operation.ExpressionOperator;
import com.roman.operation.Operator;
import com.roman.util.ProcessException;

import java.util.List;
import java.util.stream.Stream;

/**
 * Created by roman on 30.03.2017.
 */
public interface ExpressionInterpreter extends Interpreter {
    public ExpressionOperator interpret(String input, Context context) throws ProcessException;

    /**
     * Helper method for splitting input string to array of left operand, operator and right operand.
     * If there are several possible operators in the input string, method will return the one with the largest index.
     *
     * @param input input string
     * @param operators possible operators for split
     * @return left operand, operator, right operand
     */
    default String[] splitOperands(String input, String... operators) {
        int level = 0;  //level - level of group
        int start = -1, end = -1;   //start and end - boundaries of substring containing operator
        for (int i = input.length() - 1; i >= 0; i--) {
            if (input.charAt(i) == '(') {
                level++;
            } else if (input.charAt(i) == ')') {
                level--;
            }

            if (level == 0) {
                //we are in the parent group
                end = i;
                int lastRightBrace = input.substring(0, end).lastIndexOf(')');
                start = lastRightBrace < 0 ? 0 : lastRightBrace + 1;
                String strWithOperator = input.substring(start, end + 1);
                int operatorIndex = Stream.of(operators)
                        .map(op -> strWithOperator.lastIndexOf(" " + op + " "))
                        .max((index1, index2) -> index1 - index2)
                        .get();
                if (operatorIndex < 0) {
                    //operator is not found, keep searching
                    //move to closest group barrier and reset state
                    i = start;
                    start = -1;
                    end = -1;
                } else {
                    //operator is found, let's take it
                    String operator = Stream.of(operators)
                            .filter(op -> strWithOperator.lastIndexOf(" " + op + " ") == operatorIndex)
                            .findFirst()
                            .get();
                    String leftOperand = input.substring(0, start + operatorIndex);
                    String rightOperand = input.substring(start + operatorIndex + operator.length() + 2, input.length());
                    return new String[] {leftOperand, operator, rightOperand};
                }
            }
        }

        //no operators of given variants could be found
        return null;
    }
}
