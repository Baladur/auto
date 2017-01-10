package com.roman.base;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * Created by roman on 17.11.2016.
 */
public class AbstractElement {
    protected WebElement webElement;

    protected AbstractElement(WebElement webElement) {
        this.webElement = webElement;
    }

    public void click() {
        webElement.click();
    }

    public WebElement findLink() {
        return webElement.findElements(By.cssSelector("a[href]")).get(0);
    }
}
