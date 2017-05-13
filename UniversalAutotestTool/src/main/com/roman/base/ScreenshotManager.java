package com.roman.base;

import org.openqa.selenium.WebElement;

import java.io.File;

/**
 * Created by roman on 07.03.2017.
 */
public interface ScreenshotManager {
    File shoot(Driver driver, BaseElement element);
    File shoot(Driver driver, WebElement element, String elementName);
}
