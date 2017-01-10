package com.roman;

/**
 * Created by roman on 27.11.2016.
 */
public class GoOperation extends UnaryOperation {
    public GoOperation(String url) {
        operand1 = url;
    }

    public String evaluate() {
        String result = "driver." + String.format(Action.go.getText(), operand1);
        return result;
    }
}
