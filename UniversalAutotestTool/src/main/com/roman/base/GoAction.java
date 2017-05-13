package com.roman.base;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by roman on 11.03.2017.
 */
@Slf4j
@Getter
public class GoAction extends AbstractAction {
    private String url;

    public GoAction(Driver driver, String url) {
        super(driver);
        this.url = url;
    }

    @Override
    protected void execute() {
        driver.navigate().to(url);
        log.info(LogMessages.goAction(this));
        driver.report().log(LogMessages.goAction(this));
    }
}
