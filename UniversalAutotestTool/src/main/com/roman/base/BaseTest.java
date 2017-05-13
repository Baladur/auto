package com.roman.base;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.assertj.core.internal.Failures;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by roman on 05.03.2017.
 */
@Slf4j
public class BaseTest {
    private List<Method> steps;
    protected Driver driver;

    public BaseTest() {
        driver = Driver.getDriver();
        steps = Stream.of(this.getClass().getDeclaredMethods())
                .filter(method ->
                        Stream.of(method.getAnnotations()).anyMatch(annotation -> annotation instanceof StepInfo))
                .sorted((m1, m2) -> m1.getName().compareTo(m2.getName()))
                .collect(Collectors.toList());
    }

    public void execute(int from, int to) {
        driver.report().startTestReport(this);
        try {
            if (from < 1 || from > to || to > steps.size()) {
                throw new UniFrameworkException(String.format("Unexpected range put steps: from %d to %d. Maximum step is %d", from, to, steps.size()));
            }
            log.info("Запускаем тест {} с {} по {} шаги включительно.", this.getClass().getSimpleName(), from, to);
            for (int i = from - 1; i < to; i++) {
                StepInfo stepInfo = Stream.of(steps.get(i).getAnnotations())
                        .filter(annotation -> annotation instanceof StepInfo)
                        .map(annotation -> (StepInfo)annotation)
                        .findFirst().get();
                driver.report().step(String.format("<b>Шаг %d. %s</b>", i + 1, stepInfo.name()));
                steps.get(i).invoke(this);
            }
            driver.report().endTestReport();
        } catch (Throwable t) {
            Throwable cause = ExceptionUtils.getRootCause(t);
            if (cause instanceof AssertionError) {
                StackTraceElement[] trace = cause.getStackTrace();
                List<StackTraceElement> filteredTrace = Stream.of(trace)
                        .filter(e -> e.toString().contains(this.getClass().getPackage().getName()))
                        .collect(Collectors.toList());
                cause.setStackTrace(filteredTrace.toArray(new StackTraceElement[filteredTrace.size()]));
                driver.report().fail(cause);
                driver.close();
                throw (AssertionError)cause;
            }
            driver.report().fail(cause);
            driver.close();
            AssertionError error = Failures.instance().failure("Test failed by exception.");
            error.initCause(cause);
            throw error;
        }
    }

    public void execute() {
        execute(1, steps.size());
    }
}
