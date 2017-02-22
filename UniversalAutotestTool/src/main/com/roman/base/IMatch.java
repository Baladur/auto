package com.roman.base;

/**
 * Created by roman on 21.02.2017.
 */
public interface IMatch extends IEnd {
    public IWithTime contains(String text);
    public IWithTime equals(String text);
    public IWithTime startsWith(String text);
    public IWithTime endsWith(String text);
}
