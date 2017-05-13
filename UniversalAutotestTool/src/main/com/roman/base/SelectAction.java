package com.roman.base;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebElement;

import java.util.List;


/**
 * Created by roman on 21.02.2017.
 */
@Slf4j
@Getter
public class SelectAction extends ElementAction implements ISelectMatch, IOption {
    private Match selectMatch;
    private String selectText;
    private Match optionMatch;
    private String optionValue;
    private int optionIndex;

    public SelectAction(Driver driver, BaseElement element) {
        this(driver, element, 1);
    }

    public SelectAction(Driver driver, BaseElement element, int elementIndex) {
        super(driver, element, elementIndex);
        optionIndex = -1;
        orElseAction = () -> { throw new AssertionError(LogMessages.failSelectActionWithOptionValue(this)); };
    }

    @Override
    protected void execute() {
        log.info(LogMessages.selectAction(this));
        driver.report().log(LogMessages.selectAction(this));
        try {
            if (selectText != null && selectMatch != null) {
                driver.find(element, selectText, selectMatch).click();
            } else {
                driver.find(element, elementIndex).click();
            }
            driver.report().screenshot(element);
        } catch (Throwable t) {
            orElseAction = () -> { throw new AssertionError(LogMessages.failSelectActionMain(this)); };
            throw new AssertionError(t);
        }

        List<WebElement> options = null;
        try {
            options = driver.findOptions(element);
        } catch (Throwable t) {
            orElseAction = () -> { throw new AssertionError(LogMessages.failSelectActionOptions(this)); };
            throw new AssertionError(t);
        }

        if (optionIndex >= 0) {
            if (options.size() <= optionIndex) {
                orElseAction = () -> { throw new AssertionError(LogMessages.failSelectActionWithIndex(this)); };
                throw new AssertionError();
            }
            options.get(optionIndex).click();
            return;
        }

        WebElement foundOption = options
                .stream()
                .filter(option -> optionMatch.compare(option.getText(), optionValue))
                .findFirst()
                .orElseThrow(() -> new AssertionError());
        foundOption.click();
    }

    @Override
    public IWithTime option(String text) {
        this.optionValue = text;
        this.optionMatch = Match.Equal;
        return this;
    }

    @Override
    public IWithTime option(int index) {
        this.optionIndex = index - 1;
        return this;
    }

    @Override
    public IWithTime optionContains(String text) {
        optionMatch = Match.Contain;
        optionValue = text;
        return this;
    }

    @Override
    public IWithTime optionEquals(String text) {
        optionMatch = Match.Equal;
        optionValue = text;
        return this;
    }

    @Override
    public IWithTime optionStartsWith(String text) {
        optionMatch = Match.StartWith;
        optionValue = text;
        return this;
    }

    @Override
    public IWithTime optionEndsWith(String text) {
        optionMatch = Match.EndWith;
        optionValue = text;
        return this;
    }

    @Override
    public IOption contains(String text) {
        selectMatch = Match.Contain;
        selectText = text;
        return this;
    }

    @Override
    public IOption equals(String text) {
        selectMatch = Match.Equal;
        selectText = text;
        return this;
    }

    @Override
    public IOption startsWith(String text) {
        selectMatch = Match.StartWith;
        selectText = text;
        return this;
    }

    @Override
    public IOption endsWith(String text) {
        selectMatch = Match.EndWith;
        selectText = text;
        return this;
    }
}
