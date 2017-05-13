package com.roman.operation.command;

import com.roman.base.Context;
import com.roman.model.Variable;
import com.roman.operation.CommandOperator;
import com.roman.operation.Operator;
import com.roman.util.BaseTestWriter;
import com.roman.util.OutputTestWriter;
import lombok.Getter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by roman on 26.03.2017.
 */
@Getter
public class TestOperator implements CommandOperator {
    private String testName;
    private String testLocalizedName;
    private String testLocalizedDescription;
    private List<CommandOperator> steps;

    public TestOperator(String testName, String testLocalizedName, String testLocalizedDescription, List<CommandOperator> steps) {
        this.testName = testName;
        this.testLocalizedName = testLocalizedName;
        this.testLocalizedDescription = testLocalizedDescription;
        this.steps = steps;
    }

    @Override
    public void evaluate(BaseTestWriter writer, Context context) throws IOException {
        Map<String, String> testAnnotationParams = new HashMap<>();
        testAnnotationParams.put("name", testLocalizedName);
        testAnnotationParams.put("description", testLocalizedDescription);
        writer.writeAnnotation("TestInfo", testAnnotationParams);
        writer.writeTestDeclaration(testName);
        writer.startBlock();
        List<Variable> testInputParams = context.getVariables().stream()
                .filter(var -> var.isWithSetter())
                .collect(Collectors.toList());
        List<Variable> testOutputParams = context.getVariables().stream()
                .filter(var -> var.isWithGetter())
                .collect(Collectors.toList());
        for (Variable param : testInputParams) {
            writer.writeField(param.getType().getFinalName(), param.getFinalName());
        }
        for (Variable param : testOutputParams) {
            writer.writeField(param.getType().getFinalName(), param.getFinalName());
        }

        //write setters
        for (Variable param : testInputParams) {
            writer.writeEmptyLine();
            writer.writeSetter(param.getType().getFinalName(), param.getFinalName(), testName);
        }

        //write getters
        for (Variable param : testOutputParams) {
            writer.writeEmptyLine();
            writer.writeGetter(param.getType().getFinalName(), param.getFinalName());
        }

        writer.writeEmptyLine();
        writer.writeMainTestMethod();

        //write steps
        for (CommandOperator step : steps) {
            writer.writeEmptyLine();
            step.evaluate(writer, context);
        }
        writer.endBlock();
    }
}
