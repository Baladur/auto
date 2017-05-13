package com.roman.model;

import com.roman.base.Context;
import com.roman.operation.ExpressionOperator;
import com.roman.operation.Operator;
import com.roman.operation.expression.Value;
import com.roman.util.ProcessException;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by roman on 26.03.2017.
 */
@Getter
@Setter
public class Variable extends Value implements ExpressionOperator {
    private String name;
    private boolean withGetter = false;
    private boolean withSetter = false;

    public Variable(Type type, ExpressionOperator concreteValue) {
        super(type, concreteValue);
    }

    public Variable() {}

    public VariableScope getScope() {
        if (name.startsWith("@")) {
            return VariableScope.GLOBAL;
        }
        return VariableScope.LOCAL;
    }

    public String getFinalName() {
        if (name.startsWith("@")) {
            return name.substring(1, name.length());
        }
        return name;
    }

    public String toString() {
        return new StringBuilder()
                .append("name: ")
                .append(name)
                .append(", value: ")
                .append(", scope: ")
                .append(getScope().name())
                .append(", is input parameter: ")
                .append(withSetter)
                .append(", is output parameter: ")
                .append(withGetter)
                .toString();
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return hashCode() == o.hashCode();
    }

    @Override
    public String evaluate(Context context) {
        return getFinalName();
    }
}
