package com.roman;

/**
 * Created by roman on 27.11.2016.
 */
public class WaitOperation extends BinaryOperation {
    public WaitOperation(Time time, String amount) {
        operand1 = time.getMeasure();
        operand2 = amount;
    }
    public String evaluate() {
        String result = "driver." + String.format(Action.wait.getText(), operand1, operand2);
        return result;
    }
}
