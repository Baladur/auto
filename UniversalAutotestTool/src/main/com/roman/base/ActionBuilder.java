package com.roman.base;

/**
 * Created by roman on 21.02.2017.
 */
public class ActionBuilder implements IEnd, IMatch {

    @Override
    public void end() {

    }

    @Override
    public IEnd contains(String text) {
        return null;
    }

    @Override
    public IEnd equals(String text) {
        return null;
    }

    @Override
    public IEnd startsWith(String text) {
        return null;
    }

    @Override
    public IEnd endsWith(String text) {
        return null;
    }
}
