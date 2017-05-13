package com.roman.base;

/**
 * Created by roman on 13.03.2017.
 */
public interface IClickMatch extends IWithTime {
    public IWithTime contains(String text);
    public IWithTime equals(String text);
    public IWithTime startsWith(String text);
    public IWithTime endsWith(String text);
}
