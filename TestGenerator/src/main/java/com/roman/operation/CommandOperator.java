package com.roman.operation;

import com.roman.base.Context;
import com.roman.util.BaseTestWriter;
import com.roman.util.OutputTestWriter;
import com.roman.util.ProcessException;

import java.io.IOException;

/**
 * Created by roman on 28.04.2017.
 */
public interface CommandOperator extends Operator {
    public void evaluate(BaseTestWriter writer, Context context) throws IOException;
}
