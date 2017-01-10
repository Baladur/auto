package com.roman;

/**
 * Created by roman on 27.11.2016.
 */
public class SetOperation extends TrinaryOperation {
    public SetOperation(String elementClass, String elementName, String text) {
        operand1 = elementClass;
        operand2 = elementName;
        operand3 = text;
    }

    public String evaluate() {
        String result = "driver." + String.format(Action.set.getText(), operand1, operand2, operand3);
        return result;
    }
}
