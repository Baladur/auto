package com.roman.base;

import org.openqa.selenium.WebElement;

/**
 * Created by roman on 15.11.2016.
 */
public class TableColumn extends AbstractElement {

    protected TableColumn(WebElement webElement) {
        super(webElement);
    }

    public String getText() {
        return webElement.getText();
    }
}
