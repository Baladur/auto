package com.roman.base;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.*;
import java.util.stream.Collectors;

/**
 * This class helps to access any row, cell or column put a table element passed to constructor.
 * Paths for tables are stored in following order: cells, headers.
 * Example:
 * driver.table(Table.RATING)
 * .rowWhere().column("фильм").contains("начало").cellByHeader(2).asElement().click();
 */
public class TableAccessor implements IRowWhere, IColumn, IColumnMatch, IRow, ICell {
    private final List<By> paths;
    private final List<String> headers;
    private final WebElement[][] cells;
    private final int rowCount;
    private final int columnCount;
    private int columnIndex;
    private List<List<WebElement>> rows;
    private WebElement cell;

    public TableAccessor(Driver driver, BaseElement element) {
        this.paths = driver.toPaths(element);
        driver.findOnPage(element);
        WebDriver webDriver = driver.baseDriver();
        this.headers = webDriver.findElements(paths.get(1))
                .stream()
                .map(e -> e.getText())
                .collect(Collectors.toList());
        Queue<WebElement> cellsAsQueue = webDriver.findElements(paths.get(0))
                .stream()
                .collect(Collectors.toCollection(LinkedList<WebElement>::new));
        this.columnCount = headers.size();
        this.rowCount = cellsAsQueue.size() / columnCount;
        this.cells = new WebElement[rowCount][columnCount];
        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                cells[i][j] = cellsAsQueue.poll();
            }
        }
    }

    private void rowsByMatch(String columnValue, Match match) throws UniFrameworkException {
        if (rows == null) {
            rows = new ArrayList<>();
            for (int i = 0; i < rowCount; i++) {
                if (match.compare(cells[i][columnIndex].getText(), columnValue)) {
                    rows.add(Arrays.asList(cells[i]));
                }
            }
            return;
        }
        rows = rows
                .stream()
                .filter(row -> match.compare(row.get(columnIndex).getText(), columnValue))
                .collect(Collectors.toList());
    }

    @Override
    public IColumn rowWhere() {
        return this;
    }

    @Override
    public Map<String, String> rowAsMap() {
        Map<String, String> rowMap = new HashMap<>();
        headers.forEach(h -> {
            rowMap.put(h, rows.get(0).get(headers.indexOf(h)).getText());
        });
        return rowMap;
    }

    @Override
    public List<String> rowAsList() {
        return rows.get(0)
                .stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    @Override
    public ICell cellByHeader(String columnName) throws UniFrameworkException {
        int columnIndex = headers.indexOf(columnName);
        if (columnIndex < 0) {
            throw new UniFrameworkException(String.format("Column with name '%s' is not found.", columnName));
        }
        cell = rows.get(0).get(columnIndex);
        return this;
    }

    @Override
    public ICell cellByHeader(int columnIndex) throws UniFrameworkException {
        if (columnIndex < 0) {
            throw new UniFrameworkException(String.format("Column with index '%d' is not found.", columnIndex));
        }
        cell = rows.get(0).get(columnIndex);
        return this;
    }

    @Override
    public String asString() {
        return cell.getText();
    }

    @Override
    public WebElement asElement() {
        return cell;
    }

    @Override
    public IColumn and() {
        return this;
    }

    @Override
    public IColumnMatch column(String columnName) throws UniFrameworkException {
        return column(headers.indexOf(columnName));
    }

    @Override
    public IColumnMatch column(int columnIndex) throws UniFrameworkException {
        if (columnIndex < 0 || columnIndex >= columnCount) {
            throw new UniFrameworkException("Invalid column");
        }
        this.columnIndex = columnIndex;
        return this;
    }

    @Override
    public IRow equals(String columnValue) throws UniFrameworkException {
        rowsByMatch(columnValue, Match.Equal);
        return this;
    }

    @Override
    public IRow contains(String columnValue) throws UniFrameworkException {
        rowsByMatch(columnValue, Match.Contain);
        return this;
    }

    @Override
    public IRow startsWith(String columnValue) throws UniFrameworkException {
        rowsByMatch(columnValue, Match.StartWith);
        return this;
    }

    @Override
    public IRow endsWith(String columnValue) throws UniFrameworkException {
        rowsByMatch(columnValue, Match.EndWith);
        return this;
    }
}
