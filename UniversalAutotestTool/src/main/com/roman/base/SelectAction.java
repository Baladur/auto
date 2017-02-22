package com.roman.base;

import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by roman on 21.02.2017.
 */
public class SelectAction implements Action {
    private UniDriver driver;
    private SelectElement element;
    private Match match;
    private String optionValue;
    private int time;

    public int getTime() {
        return time;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    private TimeUnit timeUnit;

    public SelectAction(UniDriver driver) {
        this.driver = driver;
        time = 1;
        timeUnit = TimeUnit.SECONDS;
    }

    @Override
    public void execute() throws UniFrameworkException {
        driver.click(element);
        List<WebElement> options = driver.findOptions(element);
        WebElement foundOption = options
                .stream()
                .filter(option -> {
                    switch (match) {
                        case Contain: return option.getText().contains(optionValue);
                        case Equal: return option.getText().equals(optionValue);
                        case StartWith: return option.getText().startsWith(optionValue);
                        case EndWith: return option.getText().endsWith(optionValue);
                        default: return false;
                    }
                })
                .findFirst()
                .orElseThrow(() -> new UniFrameworkException("option not found"));
        foundOption.click();
    }

    public SelectAction element(SelectElement element) {
        this.element = element;
        return this;
    }

    public SelectAction match(Match match) {
        this.match = match;
        return this;
    }

    public SelectAction optionValue(String optionValue) {
        this.optionValue = optionValue;
        return this;
    }

    public SelectAction withTime(int time, TimeUnit timeUnit) {
        this.time = time;
        this.timeUnit = timeUnit;
        return this;
    }
}
