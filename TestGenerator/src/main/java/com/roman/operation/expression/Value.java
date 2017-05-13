package com.roman.operation.expression;

import com.roman.base.*;
import com.roman.model.Type;
import com.roman.operation.ExpressionOperator;
import com.roman.operation.Operator;
import com.roman.util.ProcessException;
import com.sun.deploy.ref.AppModel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.stream.Stream;

/**
 * Created by roman on 07.04.2017.
 */
@Getter
@Setter
public class Value implements ExpressionOperator {
    protected Type type;
    @Setter(AccessLevel.NONE) protected ExpressionOperator concreteValue;

    public Value(Type type, ExpressionOperator concreteValue) {
        this.type = type;
        this.concreteValue = concreteValue;
    }

    public Value() {}

    public void setConcreteValue(ExpressionOperator concreteValue) {
        if (concreteValue instanceof Value) {
            Value value = (Value)concreteValue;
            this.concreteValue = value.concreteValue;
            this.type = value.type;
        } else {
            this.concreteValue = concreteValue;
        }
    }

    /**
     * Helper method for proper creation of Value objects.
     */
    public static ExpressionOperator of(ExpressionOperator operator, Type ... possibleTypes) throws ProcessException {
        if (operator instanceof Value) {
            Type paramType = ((Value)operator).type;
            if (!Stream.of(possibleTypes).anyMatch(t -> t.equals(paramType))) {
                if (!paramType.equals(Type.ERROR)) {
                    throw new ProcessException(String.format("Incompatible types. Expected: %s. Actual: %s.", possibleTypes.toString(), paramType.toString()));
                }
            }
            return operator;
        }
        return new Value(possibleTypes[0], operator);
    }

    public static Operator ofBool(ExpressionOperator operator, Context context) throws ProcessException {
        return of(operator, context.getType(PrimitiveTypes.BOOL));
    }

    public static Operator ofNumber(ExpressionOperator operator, Context context) throws ProcessException {
        return of(operator, context.getType(PrimitiveTypes.DOUBLE), context.getType(PrimitiveTypes.INT));
    }

    public static Operator ofString(ExpressionOperator operator, Context context) throws ProcessException {
        return of(operator, context.getType(PrimitiveTypes.STRING));
    }

    @Override
    public String evaluate(Context context) {
        return concreteValue.evaluate(context);
    }
}
