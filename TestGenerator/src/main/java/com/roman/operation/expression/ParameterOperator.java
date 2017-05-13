package com.roman.operation.expression;

import com.roman.base.Context;
import com.roman.operation.ExpressionOperator;
import com.roman.operation.Operator;
import com.roman.util.ProcessException;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by roman on 18.04.2017.
 */
@Getter
public class ParameterOperator implements ExpressionOperator {
    private List<ExpressionOperator> params;

    public ParameterOperator(List<ExpressionOperator> params) {
        this.params = params;
    }
    public ParameterOperator() {
        this.params = new ArrayList<>();
    }

    @Override
    public String evaluate(Context context) {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i));
            if (i < params.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append(")");
        return sb.toString();
    }
}
