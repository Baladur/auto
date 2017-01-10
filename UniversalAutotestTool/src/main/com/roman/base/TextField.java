package com.roman.base;

/**
 * Created by roman on 19.11.2016.
 */
public class TextField extends AbstractElement {
    protected TextField(UniDriver driver, BaseElement element) {
        super(driver.findOnPage(element));
    }

    public String getText() {
        return webElement.getText();
    }

    public void setText(String text) {
        clear();
        webElement.sendKeys(text);
    }

    public void clear() {
        webElement.clear();
    }
}
