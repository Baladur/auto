package com.roman;

/**
 * Created by roman on 27.11.2016.
 */
public class ClickOperation extends BinaryOperation {

    public ClickOperation(String elementClass, String elementName) {
        operand1 = elementClass;
        operand2 = elementName;
    }

    public String evaluate() {
        String result = "driver." + String.format(Action.click.getText(), operand1, operand2);
        return result;
    }
}
