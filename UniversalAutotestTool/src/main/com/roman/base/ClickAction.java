package com.roman.base;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by roman on 22.02.2017.
 */
@Slf4j
public class ClickAction extends ElementAction implements IClickMatch, IWithTime {
    private Match match;
    private String text;

    public ClickAction(Driver driver, BaseElement element) {
        this(driver, element, 1);
    }

    public ClickAction(Driver driver, BaseElement element, int elementIndex) {
        super(driver, element, elementIndex);
        orElseAction = () -> { throw new AssertionError(LogMessages.failClickAction(this)); };
    }

    @Override
    protected void execute() {
        log.info(LogMessages.clickAction(this));
        driver.report().log(LogMessages.clickAction(this));
        try {
            driver.report().screenshot(element);
            if (match != null && text != null) {
                driver.find(element, text, match).click();
            } else {
                driver.find(element, elementIndex).click();
            }
        } catch (Throwable t) {
            throw new AssertionError(LogMessages.failClickAction(this));
        }
    }

    @Override
    public IWithTime contains(String text) {
        match = Match.Contain;
        this.text = text;
        return this;
    }

    @Override
    public IWithTime equals(String text) {
        match = Match.Equal;
        this.text = text;
        return this;
    }

    @Override
    public IWithTime startsWith(String text) {
        match = Match.StartWith;
        this.text = text;
        return this;
    }

    @Override
    public IWithTime endsWith(String text) {
        match = Match.EndWith;
        this.text = text;
        return this;
    }
}
