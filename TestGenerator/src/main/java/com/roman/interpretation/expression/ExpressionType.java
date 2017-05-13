package com.roman.interpretation.expression;

import com.roman.base.PrimitiveTypes;

/**
 * Created by roman on 10.04.2017.
 */
public enum  ExpressionType {
    ARITHMETIC("arithmetic"), BOOL(PrimitiveTypes.BOOL), STRING(PrimitiveTypes.BOOL), ANY("any");

    private final String str;

    ExpressionType(String str) {
        this.str = str;
    }

    @Override
    public String toString() {
        return str;
    }
}
