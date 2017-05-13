package com.roman.operation.command;

import com.roman.base.Context;
import com.roman.operation.CommandOperator;
import com.roman.operation.Operator;
import com.roman.util.BaseTestWriter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by roman on 26.03.2017.
 */
public class StepOperator implements CommandOperator {
    private int stepIndex;
    private String stepName;
    private String stepDescription;
    private List<CommandOperator> instructions;

    public StepOperator(int stepIndex, String stepName, String stepDescription, List<CommandOperator> instructions) {
        this.stepIndex = stepIndex;
        this.stepName = stepName;
        this.stepDescription = stepDescription;
        this.instructions = instructions;
    }

    @Override
    public void evaluate(BaseTestWriter writer, Context context) throws IOException {
        Map<String, String> annotationParams = new HashMap<>();
        annotationParams.put("name", stepName);
        writer.writeAnnotation("StepInfo", annotationParams);
        writer.writeStepDeclaration(stepIndex);
        writer.startBlock();

        //write instructions
        for (CommandOperator instruction : instructions) {
            instruction.evaluate(writer, context);
        }
        writer.endBlock();
    }
}
