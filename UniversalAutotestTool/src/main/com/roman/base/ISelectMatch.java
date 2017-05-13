package com.roman.base;

/**
 * Created by roman on 13.03.2017.
 */
public interface ISelectMatch extends IOption {
    public IOption contains(String text);
    public IOption equals(String text);
    public IOption startsWith(String text);
    public IOption endsWith(String text);
}
