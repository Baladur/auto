package com.roman;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by roman on 27.11.2016.
 */
public class StepInterpretator extends BaseInterpretator {

    public StepInterpretator(BaseInterpretator baseInterpretator) {
        this.baseInterpretator = baseInterpretator;
    }

    public void interpret(String line) throws IOException {
        Logger.printLine(String.format("StepInt:::line = %s", line));
        String trimmedLine = line.trim();
        Pattern pattern = Pattern.compile("^\\d+\\..*");
        String forNext = "";
        if (pattern.matcher(trimmedLine).matches()) {
            Logger.printLine("Pattern matches");
            String[] dotted = trimmedLine.split("\\.");
            baseInterpretator.setLastStep(new Step(dotted[0]));
            for (int i = 1; i < dotted.length; i++) {
                forNext += dotted[i];
                if (i < dotted.length - 1) {
                    forNext += ".";
                }
            }
            if (forNext.startsWith(" ")) {
                forNext = forNext.substring(1, forNext.length());
            }

        } else {
            forNext = trimmedLine;
            Logger.printLine("Pattern does not match");
        }
        baseInterpretator.setCurrentLine(forNext);
        Logger.printLine(String.format("forNext = %s", forNext));
    }
}
