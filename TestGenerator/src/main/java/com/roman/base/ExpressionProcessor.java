package com.roman.base;

import com.google.gson.Gson;
import com.roman.model.Elements;
import com.roman.model.Type;
import com.roman.operation.command.TestOperator;
import com.roman.util.*;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by roman on 11.09.2016.
 */
@Slf4j
public class ExpressionProcessor {

    private static final String STANDARD_TYPES_PATH = "standard.bind";

    private final File elementsFile;
    private final String testDir;
    private List<String> bindingPaths = new ArrayList<>();
    private static Gson gson = new Gson();

    public ExpressionProcessor(String elementsPath, String testDir, List<String> bindingPaths) {
        this.elementsFile = new File(elementsPath);
        this.testDir = testDir;
        this.bindingPaths = bindingPaths;
    }

    public Context preProcess() throws IOException, ProcessException {
        Context context = new Context();
        readStandardTypes(context);
        readBindingTypes(context);
        readElements(elementsFile, context);
        return context;
    }


    /**
     * This method opens input stream and passes it to interpreter.
     *
     * @param filePath path to test script file
     * @throws ProcessException
     */
    public void process(String filePath) throws ProcessException, IOException {
        try (InputTestReader reader = new InputTestReader(new BufferedReader(new FileReader(filePath)))) {
            Context context = preProcess();
            reader.addListener(new InputTestReaderListener() {
                @Override
                public void onNewLine() {
                    context.newLine();
                }

                @Override
                public void onColumnCountChanged(int newColumnCount) {
                    context.changeColumnCount(newColumnCount);
                }

                @Override
                public void onError(String errorMessage) {
                    context.addError(errorMessage);
                }
            });
            TestOperator test = (TestOperator) Interpreters.TEST.interpret(reader, context);
            if (context.getErrors().size() > 0) {
                //TODO: write errors to file for test editor
                context.getErrors().forEach(error -> log.error(error.toString()));
                return;
            }

            try (BaseTestWriter writer = new JavaTestWriter(new BufferedWriter(new FileWriter(testDir + File.separator + context.getTestName() + ".java")))) {
                test.evaluate(writer, context);
            }
            log.info("Result: testName = {}, testLocalizedName = {}, testLocalizedDescription = {}.",
                    test.getTestName(), test.getTestLocalizedName(), test.getTestLocalizedDescription());
        }
    }

    private void readStandardTypes(Context context) throws IOException {
        readBindingType(new File(STANDARD_TYPES_PATH), context);
    }

    private void readBindingTypes(Context context) throws IOException {
        for (String bindingPath : bindingPaths) {
            File bindingFile = new File(bindingPath);
            if (bindingFile.isDirectory()) {
                List<File> files = Files.list(Paths.get(bindingPath))
                        .filter(path -> path.endsWith("bind"))
                        .map(path -> path.toFile())
                        .collect(Collectors.toList());
                for (File file : files) {
                    readBindingType(file, context);
                }
            } else {
                readBindingType(bindingFile, context);
            }
        }
    }

    private void readBindingType(File bindingFile, Context context) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(bindingFile))) {
            context.addTypes(gson.fromJson(br, Type[].class));
        }
    }

    private void readElements(File elementsFile, Context context) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(elementsFile))) {
            context.setElements(gson.fromJson(br, Elements.class));
        }
    }

    private void checkClauses(String input) throws PreprocessException {
        int level = 0;
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == '(') {
                level++;
            } else if (input.charAt(i) == ')') {
                level--;
            }

            if (level < 0) {
                throw new PreprocessException("Wrong clause order!");
            }
        }

        if (level != 0) {
            throw new PreprocessException("Clauses are not balanced!");
        }
    }

    private void checkOrder(String input) throws PreprocessException {
        for (int i = 0; i < input.length() - 1; i++) {
            char current = input.charAt(i);
            char next = input.charAt(i + 1);
            if (current == '(' && next == '-') {
                continue;
            }
            if ((isSpecialSymbol(current) && (isSpecialSymbol(next) || next == ')')) ||
                    (current == '(' && (isSpecialSymbol(next) || next == ')'))) {
                throw new PreprocessException("Invalid symbol sequence: " + current + next);
            }
        }
    }

    private boolean isSpecialSymbol(char symbol) {
        if (symbol == '+' || symbol == '-' || symbol == '*' || symbol == '/' || symbol == '^' || symbol == ',') {
            return true;
        }

        return false;
    }

}
