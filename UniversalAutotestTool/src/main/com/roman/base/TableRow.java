package com.roman.base;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Created by roman on 15.11.2016.
 */
public class TableRow extends AbstractElement {
    private List<WebElement> columns;

    public TableRow(WebElement webElement) {
        super(webElement);
        columns = webElement.findElements(By.tagName("td"));
    }

    public TableColumn column(int index) throws UniFrameworkException {
        if (index < 0 || index > columns.size()) {
            throw new UniFrameworkException("Invalid index at 'row()'");
        }
        return new TableColumn(columns.get(index));
    }
}
