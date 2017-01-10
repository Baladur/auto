package com.roman.base;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Created by roman on 15.11.2016.
 */
public class TableObject extends AbstractElement {
    private List<WebElement> rows;

    public TableObject(UniDriver driver, BaseElement baseElement) {
        super(driver.find(baseElement));
        rows = webElement.findElements(By.tagName("tr"));
    }

    public TableRow row(int index) throws UniFrameworkException {
        if (index < 0 || index > rows.size()) {
            throw new UniFrameworkException("Invalid index at 'row()'");
        }
        return new TableRow(rows.get(index));
    }

    public TableRow row(String text) throws UniFrameworkException {
        for (WebElement we : rows) {
            if (we.findElements(By.xpath(String.format("td[text()='%s']//*", text))).size() > 0) {
                return new TableRow(we);
            }
        }
        throw new UniFrameworkException(String.format("Rows with text '%s' were not found.", text));
    }
}
