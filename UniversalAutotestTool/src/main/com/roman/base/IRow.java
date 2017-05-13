package com.roman.base;

import java.util.List;
import java.util.Map;

/**
 * Created by roman on 25.02.2017.
 */
public interface IRow extends IAndColumn {
    public Map<String, String> rowAsMap();
    public List<String> rowAsList();
    public ICell cellByHeader(String columnName) throws UniFrameworkException;
    public ICell cellByHeader(int columnIndex) throws UniFrameworkException;
}
