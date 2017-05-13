package com.roman.base;

import com.assertthat.selenium_shutterbug.core.Shutterbug;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebElement;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by roman on 07.03.2017.
 */
@Slf4j
public class ShutterbugScreenshotManager implements ScreenshotManager {
    @Override
    public File shoot(Driver driver, BaseElement element) {
        return shoot(driver, driver.find(element), element.getName());
    }

    @Override
    public File shoot(Driver driver, WebElement element, String elementName) {
        String filePath = "screenshots/" + driver.hashCode() + File.separator;
        Shutterbug.shootPage(driver.baseDriver())
                .highlightWithText(element, Color.red, 3, elementName, Color.red, new Font("SansSerif", Font.BOLD, 20))
                .save(filePath);
        File screenshotFile = null;
        try {
            screenshotFile = Files.list(Paths.get(filePath))
                    .map(path -> path.toFile())
                    .max((f1, f2) -> (int)(f1.lastModified() - f2.lastModified())).get();
        } catch (IOException e) {
            log.error("Shutterbug error", e);
        } finally {
            return screenshotFile;
        }
    }
}
