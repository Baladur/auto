package com.roman.base;

/**
 * Created by roman on 19.11.2016.
 */
public class Button extends AbstractElement {
    protected Button(UniDriver driver, BaseElement element) {
        super(driver.find(element));
    }

    public void click() {
        webElement.click();
    }

    public String getText() {
        return webElement.getText();
    }
}
