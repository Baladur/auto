package com.roman.base;

/**
 * Created by roman on 21.02.2017.
 */
public interface IMatch extends IEnd {
    public IEnd contains(String text);
    public IEnd equals(String text);
    public IEnd startsWith(String text);
    public IEnd endsWith(String text);
}
