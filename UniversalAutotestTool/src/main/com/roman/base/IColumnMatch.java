package com.roman.base;

/**
 * Created by roman on 26.02.2017.
 */
public interface IColumnMatch extends IAndColumn {
    public IRow equals(String columnValue) throws UniFrameworkException;
    public IRow contains(String columnValue) throws UniFrameworkException;
    public IRow startsWith(String columnValue) throws UniFrameworkException;
    public IRow endsWith(String columnValue) throws UniFrameworkException;
}
