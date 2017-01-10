package com.roman;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by roman on 27.11.2016.
 */
public class BaseInterpretator implements IInterpretator {
    protected List<Step> steps;
    protected Step currentStep;
    protected BaseInterpretator baseInterpretator;
    protected String currentLine;

    private StepInterpretator stepInterpretator;
    private OperationInterpretator operationInterpretator;

    public BaseInterpretator(int i) {
        steps = new ArrayList<Step>();
        stepInterpretator = new StepInterpretator(this);
        operationInterpretator = new OperationInterpretator(this);
    }

    protected BaseInterpretator() {}

    public void interpret(String line) throws IOException {

    }

    public void interpretLine(String line) {
        try {
            stepInterpretator.interpret(line);
        } catch (IOException e) {
            e.printStackTrace();
        }
        operationInterpretator.interpret(line);
    }

    public List<Step> getResultSteps() {
        return steps;
    }

    protected void setLastStep(Step step) {
        if (steps.size() > 0 && step.equals(steps.get(steps.size()-1))) {
            steps.remove(steps.size()-1);
        }
        steps.add(step);
    }

    protected Step getLastStep() {
        return steps.size() == 0 ? null : steps.get(steps.size()-1);
    }

    protected void setCurrentLine(String line) {
        currentLine = line;
    }

    protected String getCurrentLine() { return currentLine; }

}
