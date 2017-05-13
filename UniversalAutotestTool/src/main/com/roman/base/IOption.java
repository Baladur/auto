package com.roman.base;

/**
 * Created by roman on 21.02.2017.
 */
public interface IOption {
    public IWithTime option(String text);
    public IWithTime option(int index);
    public IWithTime optionContains(String text);
    public IWithTime optionEquals(String text);
    public IWithTime optionStartsWith(String text);
    public IWithTime optionEndsWith(String text);
}
