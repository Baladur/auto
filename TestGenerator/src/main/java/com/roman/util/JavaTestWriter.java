package com.roman.util;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.IntStream;

/**
 * Created by roman on 29.04.2017.
 */
public class JavaTestWriter extends BaseTestWriter implements OutputTestWriter {
    public JavaTestWriter(BufferedWriter bw) {
        super(bw);
    }

    @Override
    public void startBlock() throws IOException {
        write(" {");
        logicIndents++;
    }

    @Override
    public void endBlock() throws IOException {
        logicIndents--;
        newLine();
        write("}");
    }

    @Override
    public void writePackage(String packageName) throws IOException {
        writeToken("package");
        write(packageName);
        write(";");
        writeEmptyLine();
    }

    @Override
    public void writeImport(String importValue) throws IOException {
        newLine();
        writeToken("import");
        write(importValue);
        write(";");
    }

    @Override
    public void writeAnnotation(String annotation, Map<String, String> params) throws IOException {
        newLine();
        write("@");
        write(annotation);
        if (params.size() > 0) {
            write("(");
            int counter = 0;
            int size = params.size();
            for (Map.Entry<String, String> paramPair : params.entrySet()) {
                writeToken(paramPair.getKey());
                writeToken("=");
                write(paramPair.getValue());
                if (counter++ < size - 1) {
                    //add ','
                    writeToken(",");
                }
            }
            write(")");
        }
    }

    @Override
    public void writeTestDeclaration(String testName) throws IOException {
        newLine();
        writeToken("public");
        writeToken("class");
        writeToken(testName);
        writeToken("extends");
        write("BaseTest");
    }

    @Override
    public void writeField(String type, String fieldName) throws IOException {
        newLine();
        writeToken("private");
        writeToken(type);
        write(fieldName);
        write(";");
    }

    @Override
    public void writeGetter(String type, String fieldName) throws IOException {
        newLine();
        writeToken("public");
        writeToken(type);
        write("get");
        write(fieldName.substring(0, 1).toUpperCase());
        write(fieldName.substring(1, fieldName.length()));
        write("() { return ");
        write(fieldName);
        writeToken(";");
        write("}");
    }

    @Override
    public void writeSetter(String type, String fieldName, String testName) throws IOException {
        newLine();
        writeToken("public");
        writeToken(testName);
        write("with");
        write(fieldName.substring(0, 1).toUpperCase());
        write(fieldName.substring(1, fieldName.length()));
        write("(");
        writeToken(type);
        write(fieldName);
        write(") { this.");
        writeToken(fieldName);
        writeToken("=");
        write(fieldName);
        write("; }");
    }

    @Override
    public void writeMainTestMethod() throws IOException {
        newLine();
        writeAnnotation("Test", new HashMap<>());
        newLine();
        writeToken("public");
        writeToken("void");
        write("test()");
        startBlock();
        newLine();
        write("execute();");
        newLine();
        write("driver.close();");
        endBlock();
    }

    @Override
    public void writeStepDeclaration(Integer stepIndex) throws IOException {
        newLine();
        writeToken("public");
        writeToken("void");
        write("step");
        write(stepIndex.toString());
        write("()");
    }

    @Override
    public void writeMethodCall(String object, String method, String... params) {
        write(object);
        write(".");
        write(method);
        write("(");
        for (int i = 0; i < params.length; i++) {
            write(params[i]);
            if (i < params.length - 1) {
                writeToken(",");
            }
        }
        write(")");
    }

    @Override
    public void writeAssert(String assertCondition, String assertMessage) throws IOException {
        newLine();
        write("assertThat(");
        write(assertCondition);
        write(").describedAs(");
        write(assertMessage);
        write(").isTrue();");

    }

    @Override
    public void writeActionEnd() throws IOException {
        write(".end();");
    }

    @Override
    public void writeClick(String elementType, String elementName, Optional<String> timeAmount, Optional<String> timeUnit) throws IOException {
        newLine();
        write(String.format("driver.click(%s.%s)", elementType, elementName));
        writeActionTime(timeAmount, timeUnit);
    }

    @Override
    public void writeFill(String elementType, String elementName, String text, Optional<String> timeAmount, Optional<String> timeUnit) throws IOException {
        newLine();
        write(String.format("driver.fill(%s.%s).withText(%s)", elementType, elementName, text));
        writeActionTime(timeAmount, timeUnit);
    }

    @Override
    public void writeSelect(String elementType, String elementName, String option, Optional<String> timeAmount, Optional<String> timeUnit) throws IOException {
        newLine();
        write(String.format("driver.select(%s.%s).option(%s)", elementType, elementName, option));
        writeActionTime(timeAmount, timeUnit);
    }

    @Override
    public void writeIf(String condition) throws IOException {
        newLine();
        writeToken("if");
        write("(");
        write(condition);
        write(")");
    }

    @Override
    public void writeElse() {
        write(" else");
    }

    @Override
    public void writeWhile(String condition) throws IOException {
        newLine();
        write("while (");
        write(condition);
        write(")");
    }

    @Override
    public void writeGo(String link) throws IOException {
        newLine();
        write(String.format("driver.go(%s);", link));
    }

    @Override
    public void writeOrElse() throws IOException {
        newLine();
        write(".orElse(() ->");
    }

    private void writeActionTime(Optional<String> timeAmount, Optional<String> timeUnit) {
        if (timeAmount.isPresent()) {
            write(".during");
            write(timeUnit.get().substring(0, 1).toUpperCase());
            write(timeUnit.get().substring(1, timeAmount.get().length()));
            write("(");
            write(timeAmount.get());
            write(")");
        }
    }
}
