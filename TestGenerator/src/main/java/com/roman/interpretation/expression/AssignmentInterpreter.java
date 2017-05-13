package com.roman.interpretation.expression;

import com.roman.base.*;
import com.roman.interpretation.ExpressionInterpreter;
import com.roman.base.Interpreters;
import com.roman.model.Variable;
import com.roman.operation.ErrorOperator;
import com.roman.operation.ExpressionOperator;
import com.roman.operation.expression.AssignmentOperator;
import com.roman.operation.expression.Value;
import com.roman.util.ProcessException;
import lombok.Getter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by roman on 06.04.2017.
 */
@Getter
public class AssignmentInterpreter implements ExpressionInterpreter {
    private static final Pattern ASSIGN = Pattern.compile("^\\t*([A-z][^\\s]*) : (.+)$");

    @Override
    public ExpressionOperator interpret(String input, Context context) throws ProcessException {
        Matcher matcher = ASSIGN.matcher(input);
        if (!matcher.matches()) {
            //retrieve all possible errors
            boolean anyError = false;

            //id does not begin from letter
            anyError |= checkId(input, context);

            //there are spaces before assignment operator
            anyError |= checkSpaces(input, context);

            //there is nothing in the right part of assignment
            anyError |= checkRightPart(input, context);

            if (anyError) {
                return new ErrorOperator();
            }
        }
        Variable variable = new Variable();
        variable.setName(matcher.group(1));
        String valueString = matcher.group(2);
        context.skipColumns(input.indexOf(valueString));
        ExpressionOperator rightValue = Interpreters.VALUE.interpret(valueString, context);
        variable.setConcreteValue(rightValue);
        variable.setWithSetter(context.isInParameterDeclaration());
        context.addVariable(variable);
        return new AssignmentOperator(variable, (Value)rightValue);
    }

    private boolean checkId(String input, Context context) {
        char firstLetter = input.trim().toCharArray()[0];
        if (firstLetter < 'A' || firstLetter > 'z') {
            context.addError("Variable name must start from letter.");
            return false;
        }
        return true;
    }

    private boolean checkSpaces(String input, Context context) {
        int assignIndex = input.indexOf(" " + Keywords.ASSIGN + " ");
        int spaceIndex = input.indexOf(" ");
        if (spaceIndex > 0 && spaceIndex < assignIndex) {
            context.addError("There should not be spaces before assignment operator.");
            return false;
        }
        return true;
    }

    private boolean checkRightPart(String input, Context context) {
        String[] leftAndRight = input.split(Keywords.ASSIGN);
        if (leftAndRight.length == 1 || leftAndRight[1].trim().length() == 0) {
            context.addError("There is nothing right to assignment operator.", input.indexOf(Keywords.ASSIGN) + Keywords.ASSIGN.length());
            return false;
        }
        return true;
    }
}
