package com.roman.base;

/**
 * Created by roman on 26.02.2017.
 */
public interface IColumn {
    public IColumnMatch column(String columnName) throws UniFrameworkException;
    public IColumnMatch column(int columnIndex) throws UniFrameworkException;
}
