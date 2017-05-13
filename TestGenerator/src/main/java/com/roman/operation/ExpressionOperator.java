package com.roman.operation;

import com.roman.base.Context;
import com.roman.util.ProcessException;

/**
 * Created by roman on 28.04.2017.
 */
public interface ExpressionOperator extends Operator {
    public String evaluate(Context context);
}
