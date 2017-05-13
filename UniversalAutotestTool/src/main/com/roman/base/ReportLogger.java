package com.roman.base;

import org.openqa.selenium.WebElement;

/**
 * Created by roman on 09.03.2017.
 */
public interface ReportLogger {
    public void startTestReport(BaseTest test);
    public void endTestReport();
    public void close();
    public void step(String message);
    public void log(String message);
    public void pass(String message);
    public void fail(Throwable t);
    public void screenshot(BaseElement element);
    public void screenshot(WebElement element, String name);
}
