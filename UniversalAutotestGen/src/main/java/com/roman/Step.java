package com.roman;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by roman on 27.11.2016.
 */
public class Step implements IEvaluative {
    private int number;
    private List<BaseOperation> operations;

    public Step(String s) throws NumberFormatException {
        number = Integer.parseInt(s);
        operations = new ArrayList<BaseOperation>();
    }

    public int getNumber() { return number; }

    public void addOperation(BaseOperation operation) {
        operations.add(operation);
    }

    public List<BaseOperation> getOperations() {
        return operations;
    }

    public String evaluate() {
        String result = "";
        for (BaseOperation operation : operations) {
            result += "\t\t" + operation.evaluate();
        }

        return result;
    }

    public boolean equals(Step step) {
        if (number == step.getNumber()) {
            return true;
        }

        return false;
    }

    public boolean equals(String str) {
        try {
            String trimmed = str.trim();
            String[] spaced = str.split(" ");
            Pattern pattern = Pattern.compile(".*\\/\\/\\d+ step");
            if (pattern.matcher(trimmed).matches()) {
                pattern = Pattern.compile("\\d+");
                Matcher matcher = pattern.matcher(str);
                if (matcher.find()) {
                    Logger.printLine(String.format("numberFromStr = %s", matcher.group()));
                    int numberFromStr = Integer.parseInt(matcher.group());
                    if (number == numberFromStr) {
                        return true;
                    }
                }

            }

        } catch (NumberFormatException nfe) {
        }


        return false;
    }

}
