package com.roman.util;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

/**
 * Created by roman on 30.04.2017.
 */
public interface OutputTestWriter {
    public void startBlock() throws IOException;
    public void endBlock() throws IOException;
    public void writePackage(String packageName) throws IOException;
    public void writeImport(String importValue) throws IOException;
    public void writeAnnotation(String annotation, Map<String, String> params) throws IOException;
    public void writeTestDeclaration(String testName) throws IOException;
    public void writeField(String type, String fieldName) throws IOException;
    public void writeGetter(String type, String fieldName) throws IOException;
    public void writeSetter(String type, String fieldName, String testName) throws IOException;
    public void writeMainTestMethod() throws IOException;
    public void writeStepDeclaration(Integer stepIndex) throws IOException;
    public void writeMethodCall(String object, String method, String... params);
    public void writeAssert(String assertCondition, String assertMessage) throws IOException;
    public void writeActionEnd() throws IOException;
    public void writeClick(String elementType, String elementName, Optional<String> timeAmount, Optional<String> timeUnit) throws IOException;
    public void writeFill(String elementType, String elementName, String text, Optional<String> timeAmount, Optional<String> timeUnit) throws IOException;
    public void writeSelect(String elementType, String elementName, String option, Optional<String> timeAmount, Optional<String> timeUnit) throws IOException;
    public void writeIf(String condition) throws IOException;
    public void writeElse();
    public void writeWhile(String condition) throws IOException;
    public void writeGo(String link) throws IOException;
    public void writeOrElse() throws IOException;


}
