package com.roman.interpretation.expression;

import com.roman.base.*;
import com.roman.interpretation.ChainedInterpreter;
import com.roman.interpretation.ExpressionInterpreter;
import com.roman.model.Method;
import com.roman.model.Variable;
import com.roman.operation.ErrorOperator;
import com.roman.operation.ExpressionOperator;
import com.roman.operation.Operator;
import com.roman.operation.expression.FunctionOperator;
import com.roman.operation.expression.ParameterOperator;
import com.roman.operation.expression.Value;
import com.roman.util.ProcessException;

/**
 * Created by roman on 07.04.2017.
 */
public class FunctionInterpreter extends ChainedInterpreter<ExpressionInterpreter> implements ExpressionInterpreter {
    private ExpressionType type;

    public FunctionInterpreter(ExpressionType type) {
        this.type = type;
    }

    public FunctionInterpreter() {
        this.type = ExpressionType.ANY;
    }

    @Override
    public ExpressionOperator interpret(String input, Context context) throws ProcessException {
        int accessorIndex = input.lastIndexOf(Keywords.ACCESS);
        if (accessorIndex < 0) {
            return getNextResponsible().interpret(input, context);
        }
        //object -> method
        String objectName = input.substring(0, accessorIndex);
        //divide method name and params
        String methodNameAndParams = input.substring(accessorIndex + Keywords.ACCESS.length(), input.length());
        int firstSpaceIndex = methodNameAndParams.indexOf(" ");
        String methodName = methodNameAndParams.substring(0, firstSpaceIndex > -1 ? firstSpaceIndex : methodNameAndParams.length());

        Value object = null;
        if (context.isVariableDeclared(objectName)) {
            //if variable is declared, get value type
            object = context.getVariableByName(objectName);
        } else {
            //object is some value expression
            object = (Value)Interpreters.VALUE.interpret(objectName, context);
        }

        String typeName = object.getType().scriptName;
        Method method = object.getType().methods.stream()
                .filter(m -> methodName.equals(m.scriptName))
                .findFirst()
                .orElse(null);
        if (method == null) {
            return new ErrorOperator();
        }
        if (!type.equals(ExpressionType.ANY)) {
            switch (type) {
                case ARITHMETIC:
                    if (!method.returnType.equals(PrimitiveTypes.DOUBLE)
                            && !method.returnType.equals(PrimitiveTypes.INT)) {
                        return getNextResponsible().interpret(input, context);
                    }
                    break;
                case STRING:
                    if (!method.returnType.equals(PrimitiveTypes.STRING)) {
                        return getNextResponsible().interpret(input, context);
                    }
                    break;
                case BOOL:
                    if (!method.returnType.equals(PrimitiveTypes.BOOL)) {
                        return getNextResponsible().interpret(input, context);
                    }
                    break;
            }
        }

        ParameterOperator parameterOperator = null;
        if (!methodName.equals(methodNameAndParams)) {
            //there are params
            String paramsStr = methodNameAndParams.substring(firstSpaceIndex + 1, methodNameAndParams.length());
            parameterOperator = (ParameterOperator)Interpreters.PARAMETER.interpret(paramsStr, context);
        } else {
            parameterOperator = new ParameterOperator();
        }

        return new Value(context.getType(method.returnType), new FunctionOperator(method, object, parameterOperator));
    }
}
