package com.roman.base;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by roman on 22.02.2017.
 */
@Slf4j
@Getter
public class FillAction extends ElementAction implements IWithText {
    private String text;

    public FillAction(Driver driver, BaseElement element) {
        this(driver, element, 1);
    }

    public FillAction(Driver driver, BaseElement element, int elementIndex) {
        super(driver, element, elementIndex);
        orElseAction = () -> { throw new AssertionError(LogMessages.failFillAction(this)); };
    }

    @Override
    protected void execute() {
        log.info(LogMessages.fillAction(this));
        driver.report().log(LogMessages.fillAction(this));
        try {
            driver.find(element).sendKeys(text);
            driver.report().screenshot(element);
        } catch (Throwable t) {
            throw new AssertionError(t);
        }
    }

    @Override
    public IWithTime withText(String text) {
        this.text = text;
        return this;
    }
}
