package com.roman.base;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebElement;

import java.util.Stack;
import java.util.stream.Stream;

/**
 * Created by roman on 09.03.2017.
 */
@Slf4j
public class ExtentReportLogger implements ReportLogger {
    private Driver driver;
    private Stack<ExtentTest> extentTests = new Stack<>();
    private ExtentTest currentExtentTest;
    private ExtentReports extentReports;
    private ScreenshotManager screenshotManager;
    private boolean isClosed = false;

    public ExtentReportLogger(Driver driver) {
        this.driver = driver;
        this.screenshotManager = new ShutterbugScreenshotManager();
    }

    @Override
    public void startTestReport(BaseTest test) {
        if (extentReports == null) {
            log.info("Начинаем запись HTML-отчёта.");
            extentReports = new ExtentReports(test.getClass().getSimpleName() + ".html", true);
        }
        TestInfo testInfo = Stream.of(test.getClass().getAnnotations())
                .filter(annotation -> annotation instanceof TestInfo)
                .map(annotation -> (TestInfo)annotation)
                .findFirst().get();
        if (currentExtentTest != null) {
            extentTests.push(currentExtentTest);
        }
        currentExtentTest = extentReports.startTest(testInfo.name(), testInfo.description());
    }

    @Override
    public void endTestReport() {
        if (!isClosed) {
            if (extentTests.size() == 0) {
                log.info("Завершаем главный тест.");
                extentReports.endTest(currentExtentTest);
                extentReports.flush();
                isClosed = true;
            } else {
                log.info("Завершаем вложенный тест.");
                ExtentTest parent = extentTests.pop();
                parent.appendChild(currentExtentTest);
                currentExtentTest = parent;
            }
        }
    }

    @Override
    public void close() {
        if (!isClosed) {
            log.info("Вынужденное завершение теста.");
            while (extentTests.size() > 0) {
                ExtentTest parent = extentTests.pop();
                parent.appendChild(currentExtentTest);
                currentExtentTest = parent;
            }
            extentReports.endTest(currentExtentTest);
            extentReports.flush();
            isClosed = true;
        }
    }

    @Override
    public void step(String message) {
        currentExtentTest.log(LogStatus.INFO, message, "");
    }

    @Override
    public void log(String message) {
        currentExtentTest.log(LogStatus.INFO, "", message);
    }

    @Override
    public void pass(String message) {
        currentExtentTest.log(LogStatus.PASS, "", message);
    }

    @Override
    public void fail(Throwable t) {
        currentExtentTest.log(LogStatus.FAIL, t);
    }

    @Override
    public void screenshot(BaseElement element) {
        String screenshotPath = screenshotManager.shoot(driver, element).getPath();
        log.info(String.format("Сохраняем скриншот в файле '%s'.", screenshotPath));
        currentExtentTest.log(LogStatus.INFO, "", currentExtentTest.addScreenCapture(screenshotPath));
    }

    @Override
    public void screenshot(WebElement element, String name) {
        String screenshotPath = screenshotManager.shoot(driver, element, name).getPath();
        log.info(String.format("Сохраняем скриншот в файле '%s'.", screenshotPath));
        currentExtentTest.log(LogStatus.INFO, "", currentExtentTest.addScreenCapture(screenshotPath));
    }
}
