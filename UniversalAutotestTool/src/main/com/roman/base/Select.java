package com.roman.base;

import org.openqa.selenium.WebElement;

/**
 * Created by roman on 20.02.2017.
 */
public class Select extends AbstractElement {
    protected Select(UniDriver driver, SelectElement element) {
        super(driver.findOnPage(element));
    }
}
